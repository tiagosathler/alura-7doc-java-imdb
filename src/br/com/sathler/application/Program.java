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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Program {

	private static List<String> objects = new ArrayList<>();
	private static List<String> titles = new ArrayList<>();
	private static List<String> urlImages = new ArrayList<>();
	private static List<Integer> years = new ArrayList<>();
	private static List<Double> ratings = new ArrayList<>();
	
	private static final String ATTRIBUTE_REGEX = "\\\"\\,\\\"";
	private static final String VALUE_REGEX = "\\\"\\:\\\"";

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
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		
		if (response.statusCode() == 200) {
			getObjects(response.body());
			getAttributes();
		}

		for (int i = 0; i < titles.size(); i++) {
			System.out.println(
					"Title: " + titles.get(i)
					+ ", URLImage: " + urlImages.get(i)
					+ ", Year: " + years.get(i)
					+ ", Rating: " + ratings.get(i));
		}

	}

	private static void getAttributes() {
		for (String obj : objects) {
			String[] attributes = obj.split(ATTRIBUTE_REGEX);
			for (String attribute : attributes) {
				if (attribute.contains("title")) {
					titles.add(attribute.split(VALUE_REGEX)[1]);
				} else if (attribute.contains("image")) {
					urlImages.add(attribute.split(VALUE_REGEX)[1]);
				} else if (attribute.contains("year")) {
					years.add(Integer.parseInt(attribute.split(VALUE_REGEX)[1]));
				} else if (attribute.contains("imDbRating") && !attribute.contains("imDbRatingCount")) {
					ratings.add(Double.parseDouble(attribute.split(VALUE_REGEX)[1]));
				}
			}
		}

	}

	private static void getObjects(String body) {
		int startArray = body.indexOf("[") + 1;
		int endArray = body.indexOf("]");

		String arrayStr = body.substring(startArray, endArray);

		String[] rawObjects = arrayStr.split("[\\{-\\}]");

		for (String obj : rawObjects) {
			if (!obj.equals(",")) {
				objects.add(obj);
			}
		}
	}

	private static URI createURI(Properties props) {
		String apiBaseURI = props.getProperty("api_base_uri", "https://imdb-api.com/en/API");
		String apiBaseSearch = props.getProperty("api_base_search", "Top250Movies");
		String apiKey = props.getProperty("api_key", "");

		String uriStr = new StringBuilder(apiBaseURI)
				.append("/").append(apiBaseSearch)
				.append("/").append(apiKey)
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
