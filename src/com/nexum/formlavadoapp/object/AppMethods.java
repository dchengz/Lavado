package com.nexum.formlavadoapp.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AppMethods {
	public static final int NOTIFICATION_ID = 1;
	private static final String SENDurl = "http://contacto123.sytes.net/david/lavadoapp/newdata.php";
	public static final String Download_ID = "DOWNLOAD_ID";

	public static String getTicket(String matricula, int tipo, String total) throws IOException, IllegalStateException {
		JSONArray jsona = new JSONArray();
		JSONObject jsono = new JSONObject();
		try {
			jsono.put("matri", matricula);
			jsono.put("tipo", tipo);
			jsono.put("total", total);
			jsona.put(jsono);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		HttpResponse response = null;
		HttpClient client = MySSLSocketFactory.getNewHttpClient();
		HttpPost post = new HttpPost(SENDurl);
		post.addHeader("Content-Type", "application/json");
		post.setHeader("User-Agent", "add");
		post.setEntity(new ByteArrayEntity(jsona.toString().getBytes("UTF8")));
		response = client.execute(post);
		StringBuilder sb = new StringBuilder();
		if (response != null) {
			StatusLine sl = response.getStatusLine();
			int statusCode = sl.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("MessageHTTP", sb.toString());				
			}
		}
		return sb.toString();
	}
	public static String check(String matricula) throws IOException, IllegalStateException {
		JSONArray jsona = new JSONArray();
		JSONObject jsono = new JSONObject();
		try {
			jsono.put("matri", matricula);
			jsona.put(jsono);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		HttpResponse response = null;
		HttpClient client = MySSLSocketFactory.getNewHttpClient();
		HttpPost post = new HttpPost(SENDurl);
		post.addHeader("Content-Type", "application/json");
		post.setHeader("User-Agent", "check");
		post.setEntity(new ByteArrayEntity(jsona.toString().getBytes("UTF8")));
		response = client.execute(post);
		Log.d("matri", matricula);
		StringBuilder sb = new StringBuilder();
		if (response != null) {
			StatusLine sl = response.getStatusLine();
			int statusCode = sl.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.d("MessageHTTP", sb.toString());
			}
		}
		return sb.toString();
	}
}
