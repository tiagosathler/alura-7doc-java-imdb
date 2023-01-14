package br.com.sathler.application;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Properties;

public class Program {

	public static void main(String[] args) throws IOException, InterruptedException {
		Properties props = loadProperties();

		URI uri = createURI(props);
		
		HttpClient client = HttpClient
				.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.proxy(ProxySelector.getDefault())
				.build();
		
		HttpRequest request = HttpRequest
				.newBuilder()
				.uri(uri)
				.timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		
		System.out.println(response.statusCode());
		System.out.println(response.body());
		
	}

	private static URI createURI(Properties props) {
		String apiBaseURI = props.getProperty("api_base_uri", "https://imdb-api.com/en/API");
		String apiBaseSearch = props.getProperty("api_base_search", "Top250Movies");
		String apiKey = props.getProperty("api_key", "");
		
		String uriStr = new StringBuilder(apiBaseURI)
				.append("/")
				.append(apiBaseSearch)
				.append("/")
				.append(apiKey)
				.toString();
		
		return URI.create(uriStr);
		
	}

	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
		try (FileInputStream fs = new FileInputStream("imdb.properties")) {
			props.load(fs);			
		}
		return props;		
	}

}
