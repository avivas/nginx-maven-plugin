package com.bachue.nginxmavenplugin.util;

import java.io.FileInputStream;

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

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import com.bachue.nginxmavenplugin.dto.OsType;
import com.bachue.nginxmavenplugin.dto.Package;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to get download Urls
 * @author Alejandro Vivas
 * @version 18/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class PackageConfiguration
{
	private final Log	logger;
	/** Latest version for os */
	private String		latestVersion;
	/** Packages available */
	private Package[]	packages;
	/** Current type os */
	private OsType		typeOs;
	/** Url to downloads.json file */
	private String		urlDownloads;
	/** Path to downloads.json file */
	private String		pathDownloads;

	/**
	 * Create object to get URLs
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @throws IOException
	 */
	public PackageConfiguration(final Log logger, String urlDownloads, String pathDownloads) throws IOException
	{
		this.logger = logger;
		this.urlDownloads = urlDownloads;
		this.pathDownloads = pathDownloads;

		// Get packages configuration
		this.packages = getPackagesDownloadUrlsOnLocalFile();

		// Check type os
		if (OSUtil.isWindows())
		{
			this.typeOs = OsType.WIN;
		}
		else if (OSUtil.isUnix())
		{
			this.typeOs = OsType.UNIX;
		}
		else
		{
			this.logger.warn("Unsupported OS:[" + System.getProperty("os.name") + "], using *nix configuration");
			this.typeOs = OsType.UNIX;
		}

		// Search latest version by TypeOS
		for (Package item : this.packages)
		{
			if (item.getOsType().equals(this.typeOs) && item.isLatest())
			{
				this.latestVersion = item.getVersion();
			}
		}

		if (this.latestVersion == null)
		{
			this.logger.warn("No latest version defined");
		}
	}

	/**
	 * Get package by version
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param version Nginx version
	 * @return
	 */
	public Package getPackage(String version) throws PackageException
	{
		for (Package package1 : this.packages)
		{
			if ((package1.getOsType() == this.typeOs) && (package1.getVersion().equals(version)))
			{
				return package1;
			}
		}
		throw new PackageException("Nginx:" + version + " no supported");
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the latestVersion
	 */
	public String getLatestVersion()
	{
		return latestVersion;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the typeOs
	 */
	public OsType getTypeOs()
	{
		return typeOs;
	}

	/**
	 * Get packages using local file downloads.json
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return Packages using local file downloads.json
	 * @throws IOException If fail to read downloads.json file
	 */
	private Package[] getPackagesDownloadUrlsOnLocalFile() throws IOException
	{
		byte[] jsonData = null;
		if (this.pathDownloads != null)
		{
			logger.info("Using file downloads:" + this.pathDownloads);
			FileInputStream fileInputStream = new FileInputStream(this.pathDownloads);
			jsonData = IOUtils.toByteArray(fileInputStream);
		}
		else
		{
			try
			{
				logger.info("Download file downloads:" + this.urlDownloads);
				jsonData = DownloadUtil.downloadFile(this.urlDownloads, true);
			}
			catch (KeyManagementException | NoSuchAlgorithmException e)
			{
				logger.info("Error to download file downloads.json, using internal downloads.json file");
				InputStream urlDownloadFile = PackageConfiguration.class.getClassLoader().getResourceAsStream("downloads.json");
				try
				{
					jsonData = IOUtils.toByteArray(urlDownloadFile);
				}
				finally
				{
					urlDownloadFile.close();
				}
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		Package[] packages = mapper.readValue(jsonData, Package[].class);

		return packages;
	}
}
