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
	private static final Map<TypeOs, Map<String, String>>	nginxOsToVersion	= new TreeMap<TypeOs, Map<String, String>>();

	/** Map to get Urls by TypeOs and version */
	private static final Map<String, String>				pcreToVersion		= new TreeMap<String, String>();
	private static final Map<String, String>				zlibToVersion		= new TreeMap<String, String>();
	private static final Map<String, String>				opensslToVersion	= new TreeMap<String, String>();

	/** Load Urls */
	static
	{
		nginxOsToVersion.put(TypeOs.WIN, new TreeMap<String, String>());
		nginxOsToVersion.get(TypeOs.WIN).put("1.12.1", "https://nginx.org/download/nginx-1.12.1.zip");
		nginxOsToVersion.get(TypeOs.WIN).put("1.13.4", "https://nginx.org/download/nginx-1.13.4.zip");
		nginxOsToVersion.get(TypeOs.WIN).put("latest", "https://nginx.org/download/nginx-1.13.4.zip");

		nginxOsToVersion.put(TypeOs.UNIX, new TreeMap<String, String>());
		nginxOsToVersion.get(TypeOs.UNIX).put("1.12.1", "https://nginx.org/download/nginx-1.12.1.tar.gz");
		nginxOsToVersion.get(TypeOs.UNIX).put("1.13.4", "https://nginx.org/download/nginx-1.13.4.tar.gz");
		nginxOsToVersion.get(TypeOs.UNIX).put("latest", "https://nginx.org/download/nginx-1.13.4.tar.gz");

		pcreToVersion.put("1.13.4", "https://ftp.pcre.org/pub/pcre/pcre-8.41.tar.gz");
		zlibToVersion.put("1.13.4", "http://zlib.net/zlib-1.2.11.tar.gz");
		opensslToVersion.put("1.13.4", "https://www.openssl.org/source/openssl-1.0.2k.tar.gz");
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
