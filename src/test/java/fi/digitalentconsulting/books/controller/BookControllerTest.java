package fi.digitalentconsulting.books.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.model.dto.Category;
import fi.digitalentconsulting.books.service.BookService;
import fi.digitalentconsulting.books.service.BookServiceMapImpl;
import fi.digitalentconsulting.books.service.DatamuseService;
import fi.digitalentconsulting.books.service.TestDatamuseService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
	@Autowired
	private BookService bookService;
	@Autowired
	private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private final static String BASE_URL = "/api/v1/books";
	public static final List<BookTO> initialBooks = Arrays.asList(
			new BookTO("Book 1", "Author Arthur", EnumSet.of(Category.BIOGRAPHY), null),
			new BookTO("Book 2", "Author Beate", EnumSet.of(Category.POETRY, Category.COMPUTERS), "0-201-10088-6"),
			new BookTO("Book 3", "Author Clark", EnumSet.of(Category.BIOGRAPHY), null),
			new BookTO("Book 4", "Author Delilah", EnumSet.of(Category.COMPUTERS), null)
    		);
    
    @Autowired
    DatamuseService datamuseService;
	//private static final List<String> mockedSynonyms = Arrays.asList("ONE", "TWO");         
	@BeforeEach
	public void setup() throws Exception {
		// If not autoconfiguring and injecting mockMvc we can use:
		//	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        bookService.addAll(initialBooks);
//        Mockito.doReturn(mockedSynonyms)
//        		.when(datamuseService).getSynonyms(anyString());
	}
	
	@Test
	public void smoketestBooks() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
	}

	@Test
	public void bookListAsExpected() throws Exception {
        MvcResult res = mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookTO> books = mapper.readValue(res.getResponse().getContentAsByteArray(),
        		new TypeReference<List<BookTO>>() {});
        assertThat(books.size()).isEqualTo(initialBooks.size());
	}

	@Test
	public void existingSingleBookIsFound() throws Exception {
		MvcResult res = mockMvc.perform(get(BASE_URL+"/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookTO returned = mapper.readValue(res.getResponse().getContentAsString(),
                BookTO.class);
		assertThat(returned.getName()).isEqualTo("Book 1");
	}

	@Test
	public void missingSingleBookIsNotFound() throws Exception {
		mockMvc.perform(get(BASE_URL+"/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void creatingProperBookSucceeds() throws Exception {
		long expectedId = ((BookServiceMapImpl)bookService).nextId();// initialBooks.size()+1;
		BookTO book = new BookTO("Test Title", "Test Author", EnumSet.of(Category.POETRY, Category.COMPUTERS), null);
		MvcResult res = mockMvc.perform(post(BASE_URL)
				.content(mapper.writeValueAsBytes(book))
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
		String location = res.getResponse().getHeader("location");
		assertThat(location).endsWith("/books/"+expectedId);
        BookTO returned = mapper.readValue(res.getResponse().getContentAsString(),
                BookTO.class);
		assertThat(returned.getName()).isEqualTo("Test Title");
		assertThat(returned.getId()).isEqualTo(expectedId);
		bookService.delete(returned);
	}
	
	@Test
	public void creatingMisconfiguredBookFails() throws Exception {
		BookTO book = new BookTO("Test Title", "Test Author", EnumSet.of(Category.POETRY, Category.COMPUTERS), null);
		book.setName(null);
		mockMvc.perform(post(BASE_URL)
				.content(mapper.writeValueAsBytes(book))
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void synonymsAreReturned() throws Exception {
		List<String> mockedSynonyms = ((TestDatamuseService)datamuseService).getSynonyms(null);
		MvcResult res = mockMvc.perform(get(BASE_URL+"/1/synonyms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<String> returned = mapper.readValue(res.getResponse().getContentAsString(),
        		new TypeReference<List<String>>() {});
		//assertThat(returned.size()).isEqualTo(mockedSynonyms.size());
        assertThat(returned.size()).isEqualTo(mockedSynonyms.size());
		returned.forEach(syn -> {
			assertThat(syn).isIn(mockedSynonyms);
		});
	}
}
