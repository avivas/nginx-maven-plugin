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
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.logging.Log;

import com.bachue.nginxmavenplugin.util.OS.TypeOs;

/**
 * Util download nginx
 * @author Alejandro Vivas
 * @version 15/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public final class NginxDownloadUtil
{
	/**
	 * Nginx data to install
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	public static class NginxInstall
	{
		/** Nginx home */
		private String home;
		/** If install successful */
		private boolean success = true;
		/** Error */
		private Throwable throwable;
		/** Nginx executable path */
		private String nginxExecutablePath;
		
		/**
		 * Create object
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @param home Nginx home
		 * @param nginxExecutablePath  Nginx executable path
		 * @param success true if success install, false otherwise
		 * @param throwable Exception error
		 */
		public NginxInstall(String home,String nginxExecutablePath,boolean success,Throwable throwable)
		{
			this.home = home;
			this.success = success;
			this.throwable = throwable;
			this.nginxExecutablePath = nginxExecutablePath;
		}

		/**
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @return the home
		 */
		public String getHome()
		{
			return home;
		}

		/**
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @return the success
		 */
		public boolean isSuccess()
		{
			return success;
		}

		/**
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @return the throwable
		 */
		public Throwable getThrowable()
		{
			return throwable;
		}

		/**
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @return the nginxExecutablePath
		 */
		public String getNginxExecutablePath()
		{
			return nginxExecutablePath;
		}
	}
	
	/**
	 * Private constructor to avoid instances
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	private NginxDownloadUtil()
	{
	}
	
	/**
	 * Install nginx
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param localRepository Object to get repository
	 * @param version Nginx version
	 * @param logger maven logger
	 * @param disableValidationCertificates Disable validation certificates
	 * @return Object with status to install
	 */
	public static NginxInstall install(final ArtifactRepository localRepository,final String version,boolean disableValidationCertificates,final Log logger)
	{
		try
		{
			URL url = new URL(localRepository.getUrl());
			File file = new File( url.getFile() );			
			
			String versionToDownload = version;
			if(version.equalsIgnoreCase("latest"))
			{
				versionToDownload = UrlsDownloadNginx.getLatest();
			}
			
			logger.info("Nginx version:[" + versionToDownload +"]");
			
			String downloadNginxUrl = UrlsDownloadNginx.url(OS.CURRENT_OS,versionToDownload);
						
			String installPath = file.toPath() + File.separator + "com" + File.separator +  "bachue" + File.separator + "nginx-maven-plugin" + File.separator;
			String nginxDirectory =  installPath + "cache" + File.separator + "nginx-" + versionToDownload + File.separator;			
			String zipPath = nginxDirectory + downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/")+1);
			
			String nginxExecutablePath = nginxDirectory + downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/")+1, downloadNginxUrl.lastIndexOf(".")) + File.separator;
			String nginxHome = nginxExecutablePath;
			OS currentOs = OS.CURRENT_OS;
			if(currentOs.getTypeOs().equals(TypeOs.WIN))
			{
				nginxExecutablePath +=  "nginx.exe";
			}
			else
			{
				nginxExecutablePath +=  "sbin" + File.separator + "nginx";
			}
			logger.info("TOS:" + currentOs.getTypeOs() + ":" + currentOs.getTypeOs().equals(TypeOs.UNIX));
			
			// Verified if exist download file
			if( !new File(zipPath).exists() )
			{
				logger.info("Download:[" + downloadNginxUrl + "] to [" + zipPath +"]");
				DownloadUtil.dowloadFile(zipPath, downloadNginxUrl,disableValidationCertificates);
			}
			
			if(currentOs.getTypeOs().equals(TypeOs.UNIX))
			{
				String downloadUrlPcre = "ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/pcre-8.41.tar.gz";
				logger.info("Download:[" + downloadUrlPcre + "] to :[" + zipPath +"]");
				DownloadUtil.dowloadFile(zipPath, downloadUrlPcre,disableValidationCertificates);
				
				String downloadUrlZlib = "http://zlib.net/zlib-1.2.11.tar.gz";
				logger.info("Download:[" + downloadUrlZlib + "] to :[" + zipPath +"]");
				DownloadUtil.dowloadFile(zipPath, downloadUrlZlib,disableValidationCertificates);
				
				String downloadUrlOpenssl = "http://www.openssl.org/source/openssl-1.0.2k.tar.gz";
				logger.info("Download:[" + downloadUrlOpenssl + "] to :[" + zipPath +"]");
				DownloadUtil.dowloadFile(zipPath, downloadUrlOpenssl,disableValidationCertificates);
			}
			
			// Verified if unzip (win) files exist 
			if(OS.CURRENT_OS.getTypeOs().equals(TypeOs.WIN))
			{
				if( !new File(nginxExecutablePath).exists() )
				{
					ZipUtil.unzip(zipPath, nginxDirectory,logger);
				}
			}
			else
			{
				
			}
			
			NginxInstall nginxInstall = new NginxInstall(nginxHome,nginxExecutablePath, true, null);

			return nginxInstall;
		}
		catch(MalformedURLException malformedURLException)
		{
			return new NginxInstall(null,null, false, malformedURLException);
		}
		catch(IOException ioException)
		{	
			return new NginxInstall(null,null, false, ioException);
		}
		catch (NginxDownloadNotFoundException e)
		{
			return new NginxInstall(null,null, false, e);
		}
		catch (KeyManagementException e)
		{
			return new NginxInstall(null,null, false, e);
		}
		catch (NoSuchAlgorithmException e)
		{
			return new NginxInstall(null,null, false, e);
		}		
	}
}
