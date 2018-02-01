package com.trukker.trukkershipperuae.httpsrequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class RestClient {
	private static final String HTTPS_STRING = "https";

	private RestClient()
	{
	}
	private static RestClient instance = null;
	public static RestClient getInstance()
	{
		if(instance == null) {
			instance = new RestClient();
		}
		return instance;
	}

	public String postRequest(String url, String json)
    {
		String responseString = "";
		HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
		HttpResponse response = null;
		InputStream in;
		URI newURI = URI.create(url);
		HttpPost postMethod = new HttpPost(newURI);
		try
        {

			postMethod.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
			postMethod.setHeader("Content-Type", "application/json");
			response = httpClient.execute(postMethod);
			in = response.getEntity().getContent();
			responseString = convertStreamToString(in);
		} catch (Exception e) {}
		return responseString;
	}

	public static String convertStreamToString(InputStream is) throws Exception
    {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
        {
			sb.append(line);
		}
		is.close();
		return sb.toString();
	}

	public String getRequest(String url)
	{
		String responseString = "";
		HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
		HttpResponse response = null;
		InputStream in;
		URI newURI = URI.create(url);
		HttpGet getMethod = new HttpGet(newURI);
		try {
			response = httpClient.execute(getMethod);
			in = response.getEntity().getContent();
			responseString = convertStreamToString(in);
		} catch (Exception e) {}
		return responseString;
	}
}
