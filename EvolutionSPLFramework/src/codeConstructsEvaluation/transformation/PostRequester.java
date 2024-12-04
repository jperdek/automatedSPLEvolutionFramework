package codeConstructsEvaluation.transformation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import evolutionSimulation.productAssetsInitialization.SharedConfiguration;

/**
 * Performs POST request and loads file content
 * 
 * @author Jakub Perdek
 *
 */
public class PostRequester {

	/**
	 * Performs POST request to complexity service (NodeJS Express API)
	 * 
	 * @param serviceUrl - the url of the service
	 * @param fileContent - the content of the file
	 * @return string output from the measurement
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String doPost(String serviceUrl, String fileContent) throws IOException, InterruptedException {
		serviceUrl = serviceUrl.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
		System.out.println("---------------------------->");
		System.out.println(fileContent);
		System.out.println("---------------------------->");
		System.out.println(serviceUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
				  .uri(URI.create(serviceUrl))
				  .header("Content-Type", "text/plain")
				  .POST(HttpRequest.BodyPublishers.ofString(fileContent))
				  .build();
		HttpResponse<?> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch(Exception e) {
			Thread.sleep(10000);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(serviceUrl);
            post.setHeader("Content-Type", "text/plain");
            post.setEntity(new StringEntity(fileContent));

            // Execute the request and get the response
            CloseableHttpResponse responseApacheClient = httpClient.execute(post);
            return EntityUtils.toString(responseApacheClient.getEntity());
		}
		System.out.println("DONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		return (String) response.body();
	}

	/**
	 * Loads file content from the path of the file
	 * 
	 * @param fileName - path to the file the file
	 * @return the loaded content of the file
	 * @throws IOException
	 */
	public static String loadFileContent(String fileName) throws IOException {
		if (SharedConfiguration.IS_LINUX) { fileName = fileName.replace("\\", "/"); }
	    String fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
	    return fileContent;
	}
}
