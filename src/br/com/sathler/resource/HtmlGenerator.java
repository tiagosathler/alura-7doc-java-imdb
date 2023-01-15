package br.com.sathler.resource;

import java.io.PrintWriter;
import java.util.List;

import br.com.sathler.model.records.Movie;

public class HtmlGenerator {
	private final PrintWriter writer;

	public HtmlGenerator(PrintWriter writer) {
		this.writer = writer;
	}

	public void generate(List<Movie> movies) {
		writer.println("""
				<html>
					<head>
						<meta charset=\"utf-8\">
						<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">
						<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\"
									+ "integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">

					</head>
					<body>
				""");

		int i = 0;
		
		for (Movie movie : movies) {
			i++;
			String div = """
						<div class=\"card text-white text-center align-middle bg-dark mb-3\" style=\"max-width: 18rem;\">
							<h4 class=\"card-header\">%s</h4>
						    <h6 class=\"card-subtitle mb-2\">Position: %d</h6>
							<div class=\"card-body\">
								<img class=\"card-img\" src=\"%s\" alt=\"%s\">
								<p class=\"card-text mt-2\">Nota: %.2f - Ano: %d</p>
							</div>
						</div>
						""";
			writer.println(
					String.format(div, movie.title(), i, movie.urlImage(), movie.title(), movie.rating(), movie.year()));
		}

		writer.println("""
					</body>
				</html>
				""");
	}
}
