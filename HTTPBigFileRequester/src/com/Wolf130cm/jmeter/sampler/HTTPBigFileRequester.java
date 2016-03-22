
package com.Wolf130cm.jmeter.sampler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class HTTPBigFileRequester extends AbstractSampler {

	private static final long serialVersionUID = -5054224428382720281L;

	private String DA_WebServerHost;
	private String DA_WebServerPort;
	private String DA_WebServerPath;
	private String DA_WebServerProtocol;
	private String DA_WebServerMethod;
	private String DA_XAuthToken;
	private String DA_PUTFile;
	private String DA_GETFile;
	private int DA_HTTPSendCache;
	private Boolean DA_Abort;
	private int DA_AbortSize;

	private static final Logger log = LoggingManager.getLoggerForClass();

	private void getDAProperties() {
		DA_WebServerHost = getProperty("DA_WebServerHost").toString();
		DA_WebServerPort = getProperty("DA_WebServerPort").toString();
		DA_WebServerPath = getProperty("DA_WebServerPath").toString();
		DA_WebServerProtocol = getProperty("DA_WebServerProtocol").toString();
		DA_WebServerMethod = getProperty("DA_WebServerMethod").toString();
		DA_XAuthToken = getProperty("DA_XAuthToken").toString();
		DA_PUTFile = getProperty("DA_PUTFile").toString();
		DA_GETFile = getProperty("DA_GETFile").toString();

		String temp = getProperty("DA_HTTPSendCache").toString();
		if (temp.isEmpty())
			temp = "2";
		DA_HTTPSendCache = Integer.parseInt(temp);
		if (DA_HTTPSendCache == 0)
			DA_HTTPSendCache = 2;

		temp = getProperty("DA_AbortSize").toString();
		if (temp.isEmpty())
			temp = "50";
		DA_AbortSize = Integer.parseInt(temp);
		if (DA_AbortSize == 0)
			DA_AbortSize = 50;

		DA_Abort = (Boolean) getProperty("DA_Abort").getObjectValue();

	}

	@Override
	public SampleResult sample(Entry entry) {
		getDAProperties();

		HTTPSampleResult result = new HTTPSampleResult();
		int maxBufferSize = DA_HTTPSendCache * 1024 * 1024;

		result.sampleStart();
		result.setSampleLabel(super.getName());
		int HttpCode;

		try {
			URL url = new URL(DA_WebServerProtocol, DA_WebServerHost, Integer.parseInt(DA_WebServerPort),
					DA_WebServerPath);
			result.setURL(url);
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
				// Header
				if (HTTPConstants.PUT.equals(DA_WebServerMethod)) {
					connection.setDoOutput(true);
					connection.setChunkedStreamingMode(1 * 1024 * 1024);
					connection.setRequestProperty("Accept", "");
					connection.setRequestProperty("Content-Type", "application/octet-stream");
				} else if (HTTPConstants.GET.equals(DA_WebServerMethod)) {
					connection.setRequestProperty("Accept", "*/*");
					connection.setDoInput(true);
				}
				connection.setRequestProperty("Connection", "keep-alive");
				connection.setRequestProperty("X-Auth-Token", DA_XAuthToken);
				connection.setRequestProperty("User-Agent", "Apache-HttpClient/4.2.6 (java 1.5)");
				connection.setRequestMethod(DA_WebServerMethod);
				result.setHTTPMethod(DA_WebServerMethod);

				// SamplerResult
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, List<String>> e : connection.getRequestProperties().entrySet()) {
					for (String s : e.getValue()) {
						sb.append(e.getKey() + " : " + s + "\n");
					}
				}
				result.setRequestHeaders(sb.toString());

				// Send
				if (HTTPConstants.PUT.equals(DA_WebServerMethod)) {

					// PUT Request
					log.info("PUT Request Start");
					long sendedsize = 0;
					File file = new File(DA_PUTFile);
					FileInputStream fileInputStream = new FileInputStream(file);
					DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

					int bytesRead, bytesAvailable, bufferSize;

					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					byte[] buffer = new byte[bufferSize];

					int count = 0;
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						outputStream.write(buffer, 0, bufferSize);
						sendedsize += bufferSize;

						// Log
						if (++count % (100 / DA_HTTPSendCache) == 0)
							log.info("Sended file " + Long.toString(sendedsize / (1024 * 1024)) + "MBytes...");

						// Abort
						if (DA_Abort) {
							if (sendedsize > DA_AbortSize * 1024 * 1024) {
								return result;
							}
						}

						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					buffer = null;
					outputStream.flush();
					outputStream.close();
					fileInputStream.close();

					result.setBodySize((int) sendedsize);
					log.info("PUT Request End");
				}

				connection.connect();

				// ------------- Response -------------
				if (HTTPConstants.GET.equals(DA_WebServerMethod)) {
					// GET Response

					File file = null;
					FileOutputStream fileOutputStream = null;
					if (!DA_GETFile.isEmpty()) {
						file = new File(DA_GETFile);
						log.info("GET Receive Start -> " + DA_GETFile);
						fileOutputStream = new FileOutputStream(file, false);
					} else {
						log.info("GET Receive Start");
					}

					long sendedsize = 0;
					long file_size = connection.getContentLength();
					InputStream inp = connection.getInputStream();

					int bytesRead = 0;

					byte[] buffer = new byte[maxBufferSize];
					bytesRead = inp.read(buffer, 0, maxBufferSize);

					long NextGred = 0;
					while (bytesRead > -1) {
						if (fileOutputStream != null)
							fileOutputStream.write(buffer, 0, bytesRead);
						sendedsize += bytesRead;

						// Log
						if (sendedsize > NextGred) {
							log.info("Received file " + Long.toString(sendedsize / (1024L * 1024L)) + "MBytes...");
							NextGred = sendedsize + 100L * 1024L * 1024L;
						}

						// Abort
						if (DA_Abort) {
							if (sendedsize > DA_AbortSize * 1024 * 1024) {
								return result;
							}
						}
						bytesRead = inp.read(buffer, 0, maxBufferSize);
					}
					inp.close();
					result.setBodySize((int) sendedsize);
					if (fileOutputStream != null)
						fileOutputStream.close();

					inp.close();

					log.info("GET Receive End");

				} else {

					InputStream inp = connection.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(inp));
					StringBuffer bufStr = new StringBuffer();
					String temp = null;
					while ((temp = br.readLine()) != null) {
						bufStr.append(temp);
					}
					result.setResponseMessage(bufStr.toString());
					inp.close();
				}

				// ResponseHeader
				sb = new StringBuilder();
				for (Map.Entry<String, List<String>> e : connection.getHeaderFields().entrySet()) {
					for (String s : e.getValue()) {
						if (e.getKey() == null)
							sb.append(s + "\n");
						else
							sb.append(e.getKey() + " : " + s + "\n");
					}
				}
				result.setResponseHeaders(sb.toString());

				HttpCode = connection.getResponseCode();
				log.info("ResponseCode: " + Integer.toString(HttpCode));
				result.setResponseCode(Integer.toString(HttpCode));

				// 200-299
				result.setSuccessful((200 <= HttpCode && HttpCode < 300));

			} catch (IOException e) {

				log.error(e.getMessage());
				result.setSuccessful(false);

				InputStream inp = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inp));
				StringBuffer bufStr = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					bufStr.append(temp);
				}
				result.setResponseMessage(bufStr.toString());
				inp.close();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			result.setSuccessful(false);
		}

		result.sampleEnd();
		return result;
	}

}
