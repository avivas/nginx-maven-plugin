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

import java.util.Map;
import java.util.TreeMap;

import com.bachue.nginxmavenplugin.util.OS.TypeOs;

/**
 * Class to get download Urls
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class UrlsDownloadNginx
{
	/** Map to get Urls by TypeOs and version */
	private static final Map<TypeOs, Map<String,String>> osToVersion = new TreeMap<TypeOs, Map<String,String>>();
	
	/** Load Urls */
	static
	{
		osToVersion.put(TypeOs.WIN, new TreeMap<String, String>());
		osToVersion.get(TypeOs.WIN).put("1.12.1", "https://nginx.org/download/nginx-1.12.1.zip");
		osToVersion.get(TypeOs.WIN).put("1.13.4", "https://nginx.org/download/nginx-1.13.4.zip");
		osToVersion.get(TypeOs.WIN).put("latest", "https://nginx.org/download/nginx-1.13.4.zip");
		
		osToVersion.put(TypeOs.UNIX, new TreeMap<String, String>());
		osToVersion.get(TypeOs.UNIX).put("1.12.1", "https://nginx.org/download/nginx-1.12.1.tar.gz");
		osToVersion.get(TypeOs.UNIX).put("1.13.4", "https://nginx.org/download/nginx-1.13.4.tar.gz");
		osToVersion.get(TypeOs.UNIX).put("latest", "https://nginx.org/download/nginx-1.13.4.tar.gz");
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
	public static String url(OS os,String version) throws NginxDownloadNotFoundException
	{	
		Map<String,String> mapVersionUrl  = osToVersion.get(os.getTypeOs());
		if(mapVersionUrl == null)
		{
			throw new NginxDownloadNotFoundException("Operative system:" + os.getName() + " no supported");
		}
		String url = mapVersionUrl.get(version);
		if(url == null)
		{
			throw new NginxDownloadNotFoundException("Nginx version:" +version + " no supported");
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
