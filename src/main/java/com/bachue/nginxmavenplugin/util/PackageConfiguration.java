package com.bachue.nginxmavenplugin.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import com.bachue.nginxmavenplugin.dto.OsType;
import com.bachue.nginxmavenplugin.dto.Package;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to get download Urls
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class PackageConfiguration
{
	private final Log logger;
	/** Latest version for os */
	private String latestVersion;
	/** Packages available */
	private Package[] packages;
	/** Current type os */
	private OsType typeOs;
	
	/**
	 * Create object to get URLs
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @throws IOException
	 */
	public PackageConfiguration(final Log logger) throws IOException 
	{
		this.logger = logger;
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
		for(Package item : this.packages)
		{
			if( item.getOsType().equals(this.typeOs) && item.isLatest() )
			{
				this.latestVersion = item.getVersion();
			}
		}
		
		if(this.latestVersion == null)
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
		for(Package package1 : this.packages)
		{
			if( (package1.getOsType() == this.typeOs) && (package1.getVersion().equals(version)))
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
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return Packages using local file downloads.json
	 * @throws IOException If fail to read downloads.json file
	 */
	private static Package[] getPackagesDownloadUrlsOnLocalFile() throws IOException
	{
		InputStream urlDownloadFile = PackageConfiguration.class.getClassLoader().getResourceAsStream("downloads.json");
	
		byte[] jsonData =  IOUtils.toByteArray(urlDownloadFile);
		ObjectMapper mapper = new ObjectMapper();
		Package[] packages = mapper.readValue(jsonData, Package[].class);
	
		return packages;
	}
}
