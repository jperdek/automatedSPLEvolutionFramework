package codeConstructsEvaluation.transformation;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	public static String doPostRequest(String serviceUrl, String serviceUrlLargeFiles, String fileContent) throws IOException, InterruptedException {
		BodyPublisher fileContentToPost = HttpRequest.BodyPublishers.ofString(fileContent);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.version(HttpClient.Version.HTTP_2)
			  .uri(URI.create(serviceUrl))
			  .header("Content-Type", "text/plain")
			  .POST(fileContentToPost)
			  .build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch(Exception e) {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(serviceUrl);
	        post.setHeader("Content-Type", "text/plain");
	        post.setEntity(new StringEntity(fileContent));
	
	        // Execute the request and get the response
	        CloseableHttpResponse responseApacheClient = httpClient.execute(post);
	        return EntityUtils.toString(responseApacheClient.getEntity());
		}
		return (String) response.body();
	}
	
	
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
	public static String doPost(String serviceUrl, String convertionServiceUrl, String serviceUrlLargeFiles, String fileContent, boolean useFileTransfer) throws IOException, InterruptedException {
		serviceUrl = serviceUrl.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
		long objectSize = fileContent.getBytes().length;
		String largeFileLocationUrl;
		if (serviceUrlLargeFiles == null || (!useFileTransfer && ((objectSize / 1024) / 1024) < 15)) {
			return PostRequester.doPostRequest(serviceUrl, serviceUrlLargeFiles, fileContent);
		} 
		
		largeFileLocationUrl = PostRequester.getUrlToDownloadByPostRequest(convertionServiceUrl, serviceUrlLargeFiles, fileContent);
		String loadedFileResponse = "";
		if (System.getenv().getOrDefault("SHARE_FILES_USING_DISK", "true").equals("true")) {
			serviceUrlLargeFiles = largeFileLocationUrl.replace("://", ":--");
			if (largeFileLocationUrl.startsWith(".")) {
				largeFileLocationUrl = convertionServiceUrl.substring(0, convertionServiceUrl.substring(10).indexOf("/") + 11) + largeFileLocationUrl.substring(1);
			}
			largeFileLocationUrl = largeFileLocationUrl.replace("/public/", "");
			System.out.println(largeFileLocationUrl);
			BufferedInputStream in = new BufferedInputStream(new URL(largeFileLocationUrl).openStream());
			
			byte[] contents = new byte[1024];
		    int bytesRead = 0;
		    while((bytesRead = in.read(contents)) != -1) { 
		    	loadedFileResponse += new String(contents, 0, bytesRead);              
		    }
		} else {
			System.out.println("HEEEREE");
			loadedFileResponse = loadFileContent(largeFileLocationUrl);
		}
		return loadedFileResponse;
	}
	
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
	public static String doPost(String serviceUrl, String convertionServiceUrl, String serviceUrlLargeFiles, String fileContent) throws IOException, InterruptedException {
		serviceUrl = serviceUrl.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
		System.out.println("---------------------------->");
		System.out.println(fileContent);
		System.out.println("---------------------------->");
		System.out.println(serviceUrl);
		long objectSize = fileContent.getBytes().length;
		String largeFileLocationUrl;
		if (serviceUrlLargeFiles == null || ((objectSize / 1024) / 1024) > 15) {
			return PostRequester.doPostRequest(serviceUrl, serviceUrlLargeFiles, fileContent);
		} 
		
		
		largeFileLocationUrl = PostRequester.getUrlToDownloadByPostRequest(convertionServiceUrl, serviceUrlLargeFiles, fileContent);
		String loadedFileResponse = "";
		if (System.getenv().getOrDefault("SHARE_FILES_USING_DISK", "true").equals("true")) {
			serviceUrlLargeFiles = serviceUrlLargeFiles.replace("://", ":---");
			convertionServiceUrl = convertionServiceUrl.replace("./", serviceUrlLargeFiles.substring(0, serviceUrlLargeFiles.indexOf("/")).replace(":---", "://"));
			BufferedInputStream in = new BufferedInputStream(new URL(convertionServiceUrl).openStream());
			
			byte[] contents = new byte[1024];
		    int bytesRead = 0;
		    while((bytesRead = in.read(contents)) != -1) { 
		    	loadedFileResponse += new String(contents, 0, bytesRead);              
		    }
		} else {
			System.out.println("HEEEREE");
			loadedFileResponse = loadFileContent(largeFileLocationUrl);
		}
		
		System.out.println("DONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		return loadedFileResponse;
	}

	private static String getUrlToDownloadByPostRequest(String convertionServiceUrl, String serviceUrlLargeFiles, String fileContent) throws IOException, InterruptedException {
		JSONObject fileLocationConfig = null;
		String largeFileLocationUrl;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest requestTo;
		
		if (System.getenv().getOrDefault("SHARE_FILES_USING_DISK", "true").equals("true")) {
			UUID uuid = UUID.randomUUID();
			String urlOfSavedFilesOnDisk = System.getenv().getOrDefault("LARGE_FILES_TMP_DIR", "E:/aspects/typescriptConverter/public/tmp") + "/convertLarge/" + uuid.toString() + ".txt";
			String serviceUrlWithParameters = convertionServiceUrl;
			if (convertionServiceUrl.contains("?")) {
				serviceUrlWithParameters = serviceUrlWithParameters + "&url=" + urlOfSavedFilesOnDisk;
			} else {
				serviceUrlWithParameters = serviceUrlWithParameters + "?url=" + urlOfSavedFilesOnDisk;
			}
			System.out.println(urlOfSavedFilesOnDisk);
			//Files.write(Paths.get(urlOfSavedFilesOnDisk), fileContent.getBytes());
			try {
			      FileWriter myWriter = new FileWriter(urlOfSavedFilesOnDisk);
			      myWriter.write(fileContent);
			      myWriter.close();
			      System.out.println("Successfully wrote to the file.");
			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			
			System.out.println(serviceUrlWithParameters);
			requestTo = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
				  .uri(URI.create(serviceUrlWithParameters))
				  .GET()
				  .build();
		} else {
			requestTo = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
				  .uri(URI.create(serviceUrlLargeFiles))
				  .header("Content-Type", "text/plain")
				  .POST(HttpRequest.BodyPublishers.ofString(fileContent))
				  .build();
		}
		
		HttpResponse<String> response = client.send(requestTo, HttpResponse.BodyHandlers.ofString());
		JSONParser jsonParser = new JSONParser();
		try {
			fileLocationConfig = (JSONObject) jsonParser.parse((String) response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(fileLocationConfig.toString());
		largeFileLocationUrl = (String) fileLocationConfig.get("location");
		return largeFileLocationUrl;
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
