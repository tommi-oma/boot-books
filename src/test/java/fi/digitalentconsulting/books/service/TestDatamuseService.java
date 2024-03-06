package fi.digitalentconsulting.books.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service // Not Primary
public class TestDatamuseService extends DatamuseService {

	private List<String> synonyms = Arrays.asList("Red", "Green", "Blue", "White");
	public TestDatamuseService() throws MalformedURLException {
		super("http://localhost/", null);
	}

	@Override
	public List<String> getSynonyms(String word) throws UnsupportedEncodingException {
		return synonyms;
	}
}
