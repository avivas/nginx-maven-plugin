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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.logging.Log;

import com.bachue.nginxmavenplugin.dto.OsType;
import com.bachue.nginxmavenplugin.dto.Package;
import com.bachue.nginxmavenplugin.dto.PackageResultInstall;
import com.bachue.nginxmavenplugin.dto.PackageType;

/**
 * Util download nginx
 * @author Alejandro Vivas
 * @version 18/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public final class PackageInstall
{
	/** Maven local repository */
	private final ArtifactRepository localRepository;
	/** Maven logger */
	private final Log logger;
	/** Url to downloads.json file */
	private String urlDownloads;	
	/** Path to downloads.json file */
	private String pathDownloads;
	
	/**
	 * Private constructor to avoid instances
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	public PackageInstall(final ArtifactRepository localRepository,final Log logger,String urlDownloads, String pathDownloads)
	{
		this.localRepository = localRepository;
		this.logger = logger;
		this.urlDownloads = urlDownloads;
		this.pathDownloads = pathDownloads;
	}

	/**
	 * Install nginx
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param localRepository Object to get repository
	 * @param version Nginx version
	 * @param logger Maven logger
	 * @param disableValidationCertificates Disable validation certificates
	 * @return Object with status to install
	 */
	public PackageResultInstall install(final String version, boolean disableValidationCertificates) throws PackageInstallException
	{
		try
		{
			URL url = new URL(localRepository.getUrl());
			File fileRepository = new File(url.getFile());

			PackageConfiguration urlsDownloadNginx = new PackageConfiguration(logger,this.urlDownloads,this.pathDownloads);
			String versionToDownload = version;
			if (version.equalsIgnoreCase("latest"))
			{
				versionToDownload = urlsDownloadNginx.getLatestVersion();
			}

			logger.info("Nginx version:[" + versionToDownload + "]");

			Package osPackageVersion = urlsDownloadNginx.getPackage(versionToDownload);
			logger.info(osPackageVersion.toString());

			String downloadNginxUrl = osPackageVersion.getDownloadUrl();

			String installPath = fileRepository.toPath() + File.separator + "com" + File.separator + "bachue" + File.separator + "nginx-maven-plugin" + File.separator;
			String nginxDirectory = installPath + "cache" + File.separator;
			String nginxExecutablePath = nginxDirectory;
			if (downloadNginxUrl.endsWith(".zip"))
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf("."));
			}
			else if (downloadNginxUrl.endsWith(".tar.gz"))
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf(".tar.gz"));
			}
			else
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf(".tgz"));
			}

			nginxExecutablePath += File.separator;

			String nginxHome = nginxExecutablePath;
			if (urlsDownloadNginx.getTypeOs().equals(OsType.WIN))
			{
				nginxExecutablePath += "nginx.exe";
			}
			else
			{
				nginxExecutablePath += "sbin" + File.separator + "nginx";
			}

			// Verified before download nginx
			if (new File(nginxExecutablePath).exists())
			{
				return new PackageResultInstall(nginxHome, nginxExecutablePath, true, null,null);
			}

			// Install package
			install(osPackageVersion, disableValidationCertificates);

			PackageResultInstall nginxInstall = new PackageResultInstall(nginxHome, nginxExecutablePath, true, null,null);
			return nginxInstall;
		}
		catch (Exception exception)
		{
			throw new PackageInstallException("Error to install package", exception);
		}
	}

	/**
	 * Install a package
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param package1 package to install
	 * @param localRepository Maven repository
	 * @param disableValidationCertificates Disable validation certificates on https
	 * @param logger Maven logger
	 * @return Package install
	 * @throws KeyManagementException If fail to download a package on https
	 * @throws NoSuchAlgorithmException If fail to download a package on https
	 * @throws IOException If fail to download
	 * @throws DownloadPackageException IF fail to download
	 * @throws UnsupportedCompressFileException If file format is unsupported
	 * @throws InterruptedException If fail to run nginx
	 */
	private PackageResultInstall install(Package package1, boolean disableValidationCertificates)
	        throws KeyManagementException, NoSuchAlgorithmException, IOException, DownloadPackageException, UnsupportedCompressFileException, InterruptedException
	{
		URL urlLocalRepository = new URL(this.localRepository.getUrl());
		File fileRepository = new File(urlLocalRepository.getFile());
		String pluginHome = fileRepository.toPath() + File.separator + "com" + File.separator + "bachue" + File.separator + "nginx-maven-plugin" + File.separator;
		String cacheHome = pluginHome + "cache" + File.separator;
		String sourceDirectory = cacheHome + File.separator + "src" + File.separator;

		// Download package and checksum
		String downloadPackageUrl = package1.getDownloadUrl();
		String packageLocalFileName = cacheHome + downloadPackageUrl.substring(downloadPackageUrl.lastIndexOf("/") + 1);
		
		// Uncompress package
		String installHome = cacheHome + File.separator +  getSimpleNameWithoutExtension(downloadPackageUrl);
				
		if(package1.getPackageType().equals(PackageType.BINARY))
		{
			uncompressFile(packageLocalFileName, cacheHome, logger);
			return new PackageResultInstall(installHome, null, true, null,package1.getName());
		}
		
		String installScriptPath = cacheHome + "install-nginx.sh";
		File targetFile = new File(installScriptPath);
		if(!targetFile.exists())
		{
			InputStream installScriptInputStream = PackageConfiguration.class.getClassLoader().getResourceAsStream("install-nginx.sh");
	    	FileUtils.copyInputStreamToFile(installScriptInputStream, targetFile);
	    	RunProcessUtil.run(new String[] {"chmod","u+x",installScriptPath});
		}		
		
		StringBuilder stringOptionsBuilder = new StringBuilder();
		stringOptionsBuilder.append(installScriptPath);//("/home/oracle/git/nginx-maven-plugin/src/main/resources/install-nginx.sh");
		stringOptionsBuilder.append(" ");
		
		// TODO util to define simple name
		String sourceHome =  sourceDirectory + getSimpleNameWithoutExtension(downloadPackageUrl);
		
		for (Package dependency : package1.getDependencies())
		{
			switch( dependency.getName() )
			{	
				case "pcre":
					stringOptionsBuilder.append("--url-pcre=");
				break;
				case "zlib":
					stringOptionsBuilder.append("--url-zlib=");
				break;
				case "openssl":
					stringOptionsBuilder.append("--url-openssl=");
				break;
				case "brotli":
					stringOptionsBuilder.append("--url-brotli=");
				break;
				
			}
			stringOptionsBuilder.append(dependency.getDownloadUrl());
			stringOptionsBuilder.append(" ");			
		}
		
		stringOptionsBuilder.append("--url-nginx=");
		stringOptionsBuilder.append(package1.getDownloadUrl());
		stringOptionsBuilder.append(" ");
		
		stringOptionsBuilder.append("--prefix=");
		stringOptionsBuilder.append(installHome);
		stringOptionsBuilder.append(" ");
		
		stringOptionsBuilder.append("--source-home=");
		stringOptionsBuilder.append(sourceDirectory);
		stringOptionsBuilder.append(" ");
		
		RunProcessUtil.run(stringOptionsBuilder.toString().split(" "), cacheHome, logger);
			
		return  new PackageResultInstall(installHome, null, true, null,package1.getName());		
	}

	/**
	 * Uncompress a file
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param compressFilePath Path to compress file
	 * @param directoryTarget Directory where uncompress file
	 * @param logger Maven logger
	 * @throws IOException If fail uncompress
	 * @throws UnsupportedCompressFileException If compress format is invalid
	 */
	private static void uncompressFile(String compressFilePath, String directoryTarget, final Log logger) throws IOException, UnsupportedCompressFileException
	{
		if (compressFilePath.endsWith(".zip"))
		{
			ZipUtil.unzip(compressFilePath, directoryTarget, logger);
		}
		else if (compressFilePath.endsWith(".tar.gz") || compressFilePath.endsWith(".tgz"))
		{
			ZipUtil.untargz(compressFilePath, directoryTarget, logger);
		}
		else
		{
			throw new UnsupportedCompressFileException("Compress format not supported. File:[" + compressFilePath + "]");
		}
	}

	/**
	 * Return a simple name of download package
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param downloadUrl Download url
	 * @return Simple name of download package
	 * @throws UnsupportedCompressFileException If compress format is invalid
	 */
	private static String getSimpleNameWithoutExtension(String downloadUrl) throws UnsupportedCompressFileException
	{
		if (downloadUrl.endsWith(".zip"))
		{
			return downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.lastIndexOf(".zip"));
		}
		else if (downloadUrl.endsWith(".tar.gz"))
		{
			return downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.lastIndexOf(".tar.gz"));
		}
		else if (downloadUrl.endsWith(".tgz"))
		{
			return downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.lastIndexOf(".tgz"));
		}
		else
		{
			throw new UnsupportedCompressFileException("Compress format not supported. File:[" + downloadUrl + "]");
		}
	}
}