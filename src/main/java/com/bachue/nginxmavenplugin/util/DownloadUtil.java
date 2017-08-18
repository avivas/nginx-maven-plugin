package com.bachue.nginxmavenplugin.util;

/*-
 * #%L
 * nginx-maven-plugin Maven Plugin
 * %%
 * Copyright (C) 2017 Bachue
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Download Util
 * @author Alejandro Vivas
 * @version 18/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class DownloadUtil
{
	/**
	 * Download a file
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param fileName Path to download
	 * @param fileUrl Url path
	 * @param disableValidationCertificates True if disable validation certificates, false otherwise
	 * @throws MalformedURLException If invalid url
	 * @throws IOException If IO error
	 * @throws KeyManagementException If fail to disable validation certificates
	 * @throws NoSuchAlgorithmException	 If fail to disable validation certificates
	 */
	public static void dowloadFile(String fileName, String fileUrl, boolean disableValidationCertificates) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		File f = new File(fileName);
		
		// Create parent folders
		new File(f.getParent()).mkdirs();
		
		// Write file
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		dowloadFile(fileOutputStream,fileUrl,disableValidationCertificates);		
	}
	
	/**
	 * Download a file a return content
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @param fileUrl URL to file
	 * @param disableValidationCertificates Disable validation certificates
	 * @return Content of file
	 * @throws IOException If fail to download
	 * @throws KeyManagementException If fail to download on https
	 * @throws NoSuchAlgorithmException If fail to download on https
	 */
	public static byte [] downloadFile(String fileUrl,boolean disableValidationCertificates) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		final ArrayList<Byte> bytes = new ArrayList<>();
		dowloadFile(new OutputStream()
		{			
			@Override
			public void write(int b) throws IOException
			{
				bytes.add((byte)b);
			}
		},fileUrl, disableValidationCertificates);
		
		byte [] result = new byte[bytes.size()];
		for(int i = 0; i < bytes.size();i++)
		{
			result[i] = bytes.get(i);
		}
		
		return result;
	}

	/**
	 * Download a file on OutputStream
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @param outputStream OutputStream, this method close de OutputStream
	 * @param fileUrl Url to download
	 * @param disableValidationCertificates Disable validation certificates on https
	 * @throws IOException If fail to download
	 * @throws KeyManagementException If fail to download on https
	 * @throws NoSuchAlgorithmException If fail to download on https
	 */
	private static void dowloadFile(OutputStream outputStream, String fileUrl, boolean disableValidationCertificates) throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		SSLSocketFactory sslSocketFactory = null;
		if (disableValidationCertificates)
		{
			sslSocketFactory = disableValidationCertificates();
		}

		InputStream inputStream = null;
		try
		{
			// Connection to download file
			URL url = new URL(fileUrl);
			URLConnection connection = url.openConnection();
			inputStream = connection.getInputStream();
			
			// Write file
			int length = -1;
			byte[] buffer = new byte[1024];
			while ((length = inputStream.read(buffer)) > -1)
			{
				outputStream.write(buffer, 0, length);
			}			
		}
		finally
		{
			if(disableValidationCertificates && (sslSocketFactory != null))
			{
				HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
			}
			
			try
			{
				if(outputStream != null)
				{
					outputStream.close();
				}
			}
			finally
			{
				if(inputStream != null)
				{
					inputStream.close();
				}
			}
		}
	}
	
	/**
	 * Disable validation certificates on https
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @return Current SSLSocketFactory
	 * @throws KeyManagementException If fail to disable certificates
	 * @throws NoSuchAlgorithmException If fail to disable certificates
	 */
	private static SSLSocketFactory disableValidationCertificates() throws KeyManagementException, NoSuchAlgorithmException
	{
		// Trust manager that trust all certificates
		TrustManager[] trustManagerAllCerts = new TrustManager[] { new X509TrustManager()
		{
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
			{
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
			{
			}
		} };

		// Activate trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustManagerAllCerts, new java.security.SecureRandom());
		SSLSocketFactory currentSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		return currentSSLSocketFactory;
	}
}
