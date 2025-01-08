package codeConstructsEvaluation.transformation;

import java.io.BufferedInputStream;
import java.io.File;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import evolutionSimulation.productAssetsInitialization.SharedConfiguration;


/**
 * Performs POST request and loads file content
 * 
 * @author Jakub Perdek
 *
 */
public class PostRequester {

	/**
	 * Logger to track information from post requests
	 */
	private static final Logger logger = LoggerFactory.getLogger(PostRequester.class);
	
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
	public static String doPostRequest(String serviceUrl, String fileContent) throws IOException, InterruptedException {
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
	
	
	public static String doGetRequest(String serviceUrl) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.uri(URI.create(serviceUrl))
			.GET()
			.build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch(Exception e) {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet(serviceUrl);
	
	        // Execute the request and get the response
	        CloseableHttpResponse responseApacheClient = httpClient.execute(getRequest);
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
		//long objectSize = fileContent.getBytes().length;
		String largeFileLocationUrl;
		try {
			return PostRequester.doPostRequest(serviceUrl, fileContent);
		} catch(Exception e) { 
			logger.error("[doPost(4args)]: Bypassing POST with file exchange contract. Requesting: " + convertionServiceUrl, e); 
		}
		
		largeFileLocationUrl = PostRequester.getUrlToDownloadByPostRequest(convertionServiceUrl, convertionServiceUrl, fileContent);
		logger.debug("Location of large file after error handling: " + largeFileLocationUrl);
		String loadedFileResponse = "";
		if (largeFileLocationUrl.startsWith(".") || largeFileLocationUrl.contains("E://") ) {
			serviceUrlLargeFiles = largeFileLocationUrl.replace("://", ":--");
			if (largeFileLocationUrl.startsWith(".")) {
				largeFileLocationUrl = convertionServiceUrl.substring(0, convertionServiceUrl.substring(10).indexOf("/") + 11) + largeFileLocationUrl.substring(1);
			}
			largeFileLocationUrl = largeFileLocationUrl.replace("/public/", "");
			BufferedInputStream in = new BufferedInputStream(new URL(largeFileLocationUrl).openStream());
			
			byte[] contents = new byte[1024];
		    int bytesRead = 0;
		    while((bytesRead = in.read(contents)) != -1) { 
		    	loadedFileResponse += new String(contents, 0, bytesRead);              
		    }
		} else {
			loadedFileResponse = loadFileContent(largeFileLocationUrl);
			//if (largeFileLocationUrl != null) { (new File(largeFileLocationUrl)).delete(); }
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
		serviceUrl = serviceUrl.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "127.0.0.1"));
		//long objectSize = fileContent.getBytes().length;
		String largeFileLocationUrl;
		//if (serviceUrlLargeFiles == null) { // || ((objectSize / 1024) / 1024) < 15) {
			try {
				return PostRequester.doPostRequest(serviceUrl, fileContent);
			} catch(Exception e) { 
				logger.error("[doPost(4args)]: Bypassing POST with file exchange contract. Requesting: " + convertionServiceUrl, e); 
			}
		//} 
		
		largeFileLocationUrl = PostRequester.getUrlToDownloadByPostRequest(convertionServiceUrl, convertionServiceUrl, fileContent);
		String loadedFileResponse = "";
		//serverFiles: convertionServiceUrl.startsWith(".")
		//largeFileLocationUrl = largeFileLocationUrl.replace("\temp", "./public/temp").replace("/temp", "./public/temp");
		logger.debug("Location of large file after error handling: " + convertionServiceUrl);
		boolean isLokal = false;
		// SERVING FILES IN PLACE OF SERVER PUBLIC DIRECTORY
		if (largeFileLocationUrl.startsWith(".")) {
			largeFileLocationUrl = largeFileLocationUrl.replace("://", ":---");
			int localDirectoryIndex = (isLokal)? 0 : 1;
			convertionServiceUrl = convertionServiceUrl.replace("./", convertionServiceUrl.substring(0, convertionServiceUrl.indexOf("/")).replace(":---", "://"));
			if (convertionServiceUrl.indexOf("?") > 0) {
				convertionServiceUrl = convertionServiceUrl + "&url=" + largeFileLocationUrl.replace("/public", "").substring(localDirectoryIndex);
			} else {
				convertionServiceUrl = convertionServiceUrl + "?url=" + largeFileLocationUrl.replace("/public", "").substring(localDirectoryIndex);
			}
			convertionServiceUrl = convertionServiceUrl.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
			logger.debug("Data from: " + convertionServiceUrl);
			BufferedInputStream in = new BufferedInputStream(new URL(convertionServiceUrl).openStream());
			
			byte[] contents = new byte[1024];
		    int bytesRead = 0;
		    while((bytesRead = in.read(contents)) != -1) { 
		    	loadedFileResponse += new String(contents, 0, bytesRead);              
		    }
		 // SERVING FILES FROM 
		} else {
			loadedFileResponse = loadFileContent(largeFileLocationUrl);
			if (largeFileLocationUrl != null) { (new File(largeFileLocationUrl)).delete(); }
		}
		return loadedFileResponse;
	}

	private static String getUrlToDownloadByPostRequest(String convertionServiceUrl, String serviceUrlLargeFiles, String fileContent) throws IOException, InterruptedException {
		JSONObject fileLocationConfig = null;
		String largeFileLocationUrl;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest requestTo;
		String urlOfSavedFilesOnDisk = null;
		
		if (System.getenv().getOrDefault("SHARE_FILES_USING_DISK", "true").equals("true")) {
			UUID uuid = UUID.randomUUID();
			if (SharedConfiguration.USE_TMP_LOCATION_INSTEAD_OF_SERVER) {
				urlOfSavedFilesOnDisk = SharedConfiguration.PATH_TO_TEMP_DIRECTORY;
			} else {
				urlOfSavedFilesOnDisk = System.getenv().getOrDefault("LARGE_FILES_TMP_DIR", ".");
			}
			Files.createDirectories(Paths.get(urlOfSavedFilesOnDisk + "/convertLarge"));
			urlOfSavedFilesOnDisk = urlOfSavedFilesOnDisk + "/convertLarge/" + uuid.toString() + ".txt";
			
			String serviceUrlWithParameters = convertionServiceUrl;
			if (convertionServiceUrl.contains("?")) {
				serviceUrlWithParameters = serviceUrlWithParameters + "&url=" + urlOfSavedFilesOnDisk;
			} else {
				serviceUrlWithParameters = serviceUrlWithParameters + "?url=" + urlOfSavedFilesOnDisk;
			}
			//Files.write(Paths.get(urlOfSavedFilesOnDisk), fileContent.getBytes());
			try {
			      FileWriter myWriter = new FileWriter(urlOfSavedFilesOnDisk);
			      myWriter.write(fileContent);
			      myWriter.close();
			} catch (IOException e) {
			      e.printStackTrace();
			}
			
			serviceUrlWithParameters = serviceUrlWithParameters.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
			logger.debug("Sending data to: " + serviceUrlWithParameters);
			requestTo = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
				  .uri(URI.create(serviceUrlWithParameters))
				  .GET()
				  .build();
		} else {
			serviceUrlLargeFiles = serviceUrlLargeFiles.replace("localhost", System.getenv().getOrDefault("DOCKER_HOST", "localhost"));
			logger.debug("Sending data to: " + serviceUrlLargeFiles);
			requestTo = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
				  .uri(URI.create(serviceUrlLargeFiles))
				  .header("Content-Type", "text/plain")
				  .POST(HttpRequest.BodyPublishers.ofString(fileContent))
				  .build();
		}
		
		HttpResponse<String> response = client.send(requestTo, HttpResponse.BodyHandlers.ofString());
		//if (urlOfSavedFilesOnDisk != null) { (new File(urlOfSavedFilesOnDisk)).delete(); }
		
		JSONParser jsonParser = new JSONParser();
		try {
			fileLocationConfig = (JSONObject) jsonParser.parse((String) response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		largeFileLocationUrl = (String) fileLocationConfig.get("location");
		logger.debug("Response location: " + largeFileLocationUrl);
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
