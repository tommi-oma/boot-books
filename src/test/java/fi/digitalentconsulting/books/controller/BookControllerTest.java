package fi.digitalentconsulting.books.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.digitalentconsulting.books.entity.Publisher;
import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.model.dto.Category;
import fi.digitalentconsulting.books.repository.PublisherRepository;
import fi.digitalentconsulting.books.service.BookService;
import fi.digitalentconsulting.books.service.DatamuseService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {
	@Autowired
	private BookService bookService;
	@Autowired
	private PublisherRepository publisherRepository;
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

	private static final List<String> mockedSynonyms = Arrays.asList("ONE", "TWO");

	@BeforeEach
	public void setup() throws Exception {
		// If not autoconfiguring and injecting mockMvc we can use:
		//	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        bookService.addAll(initialBooks);
		Publisher p = new Publisher();
		p.setName("Publisher 1");
		p.setWebpage(new URL("http://www.pub1.com"));
		publisherRepository.save(p);
        
        Mockito.doReturn(mockedSynonyms)
        		.when(datamuseService).getSynonyms(anyString());
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
		// New Book::id changes depending on how many books are created before running this method
		// we should be able to expect new id to be 2 greater than the current max id value
		long expectedId = bookService.findBooks().stream().mapToLong(BookTO::getId).max().getAsLong()+1;
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
	public void canModifyABook() throws Exception {
		BookTO book = bookService.findBooks().get(0);
		String name = book.getName() + " TEST";
		book.setName(name);
		mockMvc.perform(put(BASE_URL+"/"+book.getId())
						.content(mapper.writeValueAsBytes(book))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		String newName = bookService.findBook(book.getId()).get().getName();
		assertThat(name).isEqualTo(newName);
	}

	@Test
	public void canNotModifyABookWithEmptyName() throws Exception {
		BookTO book = bookService.findBooks().get(0);
		book.setName("");
		mockMvc.perform(put(BASE_URL+"/"+book.getId())
						.content(mapper.writeValueAsBytes(book))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void synonymsAreReturned() throws Exception {
		MvcResult res = mockMvc.perform(get(BASE_URL+"/1/synonyms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<String> returned = mapper.readValue(res.getResponse().getContentAsString(),
        		new TypeReference<List<String>>() {});
        assertThat(returned.size()).isEqualTo(mockedSynonyms.size());
		returned.forEach(syn -> {
			assertThat(syn).isIn(mockedSynonyms);
		});
	}
	
	@Test
	public void publisherIsAddedWhenCreatingTheBook() throws Exception {
		Publisher p = publisherRepository.findAll().iterator().next();
		BookTO book = new BookTO("Test Title", "Test Author", EnumSet.of(Category.POETRY, Category.COMPUTERS), null, p);
		MvcResult res = mockMvc.perform(post(BASE_URL)
				.content(mapper.writeValueAsBytes(book))
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookTO returned = mapper.readValue(res.getResponse().getContentAsString(),
                BookTO.class);
		BookTO saved = bookService.findBook(returned.getId()).orElse(null);
		assertNotNull(saved);
		assertNotNull(saved.getPublisher());
		assertThat(p.getId()).isEqualTo(saved.getPublisher().getId());
	}
}
