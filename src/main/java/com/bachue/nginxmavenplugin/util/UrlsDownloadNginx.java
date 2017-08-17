package com.bachue.nginxmavenplugin.util;

import java.io.InputStream;

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

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;

import com.bachue.nginxmavenplugin.dto.OsType;
import com.bachue.nginxmavenplugin.dto.Software;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to get download Urls
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class UrlsDownloadNginx
{
	/** Map to get Urls by TypeOs and version */
	private static final Map<OsType, Map<String, String>>	nginxOsToVersion	= new TreeMap<OsType, Map<String, String>>();

	/** Map to get Urls by TypeOs and version */
	private static final Map<String, String>				pcreToVersion		= new TreeMap<String, String>();
	private static final Map<String, String>				zlibToVersion		= new TreeMap<String, String>();
	private static final Map<String, String>				opensslToVersion	= new TreeMap<String, String>();

	static
	{
		try
		{
			nginxOsToVersion.put(OsType.WIN, new TreeMap<String, String>());
			nginxOsToVersion.put(OsType.UNIX, new TreeMap<String, String>());
			
			InputStream urlDownloadFile = UrlsDownloadNginx.class.getClassLoader().getResourceAsStream("downloads.json");
		
			byte[] jsonData =  IOUtils.toByteArray(urlDownloadFile);
			System.out.println("Data:" + new String(jsonData));
			ObjectMapper mapper = new ObjectMapper();
			Software[] softwares = mapper.readValue(jsonData, Software[].class);
			for(Software nginx : softwares)
			{
				nginxOsToVersion.get(nginx.getOsType()).put(nginx.getVersion(), nginx.getDownloadUrl());
				
				if( nginx.getDependencies() != null )
				{
					for(Software dependency :  nginx.getDependencies())
					{
						switch (dependency.getName())
						{
							case "pcre":
								pcreToVersion.put(nginx.getVersion(),dependency.getDownloadUrl());								
							break;
							
							case "zlib":
								zlibToVersion.put(nginx.getVersion(),dependency.getDownloadUrl());
							break;
							
							case "openssl":
								opensslToVersion.put(nginx.getVersion(),dependency.getDownloadUrl());
							break;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal error to try download files:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Return a URL by OS and version
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param os OS to filter download url
	 * @param version Nginx version
	 * @return URL to download
	 * @throws NginxDownloadNotFoundException If URL not found
	 */
	public static String urlNginx(OS os, String version) throws NginxDownloadNotFoundException
	{
		Map<String, String> mapVersionUrl = nginxOsToVersion.get(os.getTypeOs());
		if (mapVersionUrl == null)
		{
			throw new NginxDownloadNotFoundException("Operative system:" + os.getName() + " no supported");
		}
		String url = mapVersionUrl.get(version);
		if (url == null)
		{
			throw new NginxDownloadNotFoundException("Nginx version:" + version + " no supported");
		}

		return url;
	}

	/**
	 * 
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @param version
	 * @return
	 * @throws NginxDownloadNotFoundException
	 */
	public static String urlPcre(String version) throws NginxDownloadNotFoundException
	{
		String url = pcreToVersion.get(version);
		if (url == null)
		{
			throw new NginxDownloadNotFoundException("Pcre->nginx version:" + version + " no supported");
		}
		return url;
	}

	/**
	 * 
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @param version
	 * @return
	 * @throws NginxDownloadNotFoundException
	 */
	public static String urlZlib(String version) throws NginxDownloadNotFoundException
	{
		String url = zlibToVersion.get(version);
		if (url == null)
		{
			throw new NginxDownloadNotFoundException("Zlib->nginx version:" + version + " no supported");
		}
		return url;
	}

	/**
	 * Return a URL to download 
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @param version
	 * @return
	 * @throws NginxDownloadNotFoundException
	 */
	public static String urlOpenssl(String version) throws NginxDownloadNotFoundException
	{
		String url = opensslToVersion.get(version);
		if (url == null)
		{
			throw new NginxDownloadNotFoundException("Openssl->nginx version:" + version + " no supported");
		}
		return url;
	}

	/**
	 * Get latest URL version
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return Get latest URL version
	 */
	public static String getLatest()
	{
		return "1.13.4";
	}
	
	
}
