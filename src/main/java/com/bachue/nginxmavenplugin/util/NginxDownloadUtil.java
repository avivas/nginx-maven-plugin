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
import org.codehaus.plexus.util.StringUtils;

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
		private String		home;
		/** If install successful */
		private boolean		success	= true;
		/** Error */
		private Throwable	throwable;
		/** Nginx executable path */
		private String		nginxExecutablePath;

		/**
		 * Create object
		 * @author Alejandro Vivas
		 * @version 14/08/2017 0.0.1-SNAPSHOT
		 * @since 14/08/2017 0.0.1-SNAPSHOT
		 * @param home Nginx home
		 * @param nginxExecutablePath Nginx executable path
		 * @param success true if success install, false otherwise
		 * @param throwable Exception error
		 */
		public NginxInstall(String home, String nginxExecutablePath, boolean success, Throwable throwable)
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
	public static NginxInstall install(final ArtifactRepository localRepository, final String version, boolean disableValidationCertificates, final Log logger)
	{
		try
		{
			URL url = new URL(localRepository.getUrl());
			File file = new File(url.getFile());

			String versionToDownload = version;
			if (version.equalsIgnoreCase("latest"))
			{
				versionToDownload = UrlsDownloadNginx.getLatest();
			}

			logger.info("Nginx version:[" + versionToDownload + "]");

			String downloadNginxUrl = UrlsDownloadNginx.urlNginx(OS.CURRENT_OS, versionToDownload);

			String installPath = file.toPath() + File.separator + "com" + File.separator + "bachue" + File.separator + "nginx-maven-plugin" + File.separator;
			String nginxDirectory = installPath + "cache" + File.separator + "nginx-" + versionToDownload + File.separator;
			String nginxCompressPath = nginxDirectory + downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1);

			String nginxExecutablePath = nginxDirectory;
			if(downloadNginxUrl.endsWith(".zip"))
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf("."));
			}
			else if(downloadNginxUrl.endsWith(".tar.gz"))
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf(".tar.gz"));
			}
			else
			{
				nginxExecutablePath += downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf("/") + 1, downloadNginxUrl.lastIndexOf(".tgz"));
			}
			
			nginxExecutablePath += File.separator;
			
			String nginxHome = nginxExecutablePath;
			OS currentOs = OS.CURRENT_OS;
			if (currentOs.getTypeOs().equals(TypeOs.WIN))
			{
				nginxExecutablePath += "nginx.exe";
			}
			else
			{
				nginxExecutablePath += "sbin" + File.separator + "nginx";
			}

			if (new File(nginxExecutablePath).exists())
			{
				return new NginxInstall(nginxHome, nginxExecutablePath, true, null);
			}
						
			// Verified if exist download file
			if (!new File(nginxCompressPath).exists())
			{
				logger.info("Download:[" + downloadNginxUrl + "] to [" + nginxCompressPath + "]");
				DownloadUtil.dowloadFile(nginxCompressPath, downloadNginxUrl, disableValidationCertificates);
			}

			String downloadUrlPcre = UrlsDownloadNginx.urlPcre(versionToDownload);
			String downloadUrlZlib = UrlsDownloadNginx.urlZlib(versionToDownload);
			String downloadUrlOpenssl = UrlsDownloadNginx.urlOpenssl(versionToDownload);
			String pcreCompressPath = null;
			String zlibCompressPath = null;
			String openSslCompressPath = null;
			if (currentOs.getTypeOs().equals(TypeOs.UNIX))
			{
				pcreCompressPath = nginxDirectory + downloadUrlPcre.substring(downloadUrlPcre.lastIndexOf("/") + 1);
				zlibCompressPath = nginxDirectory + downloadUrlZlib.substring(downloadUrlZlib.lastIndexOf("/") + 1);
				openSslCompressPath = nginxDirectory + downloadUrlOpenssl.substring(downloadUrlOpenssl.lastIndexOf("/") + 1);

				logger.info("Download:[" + downloadUrlPcre + "] to :[" + pcreCompressPath + "]");
				DownloadUtil.dowloadFile(pcreCompressPath, downloadUrlPcre, disableValidationCertificates);

				logger.info("Download:[" + downloadUrlZlib + "] to :[" + zlibCompressPath + "]");
				DownloadUtil.dowloadFile(zlibCompressPath, downloadUrlZlib, disableValidationCertificates);

				logger.info("Download:[" + downloadUrlOpenssl + "] to :[" + openSslCompressPath + "]");
				DownloadUtil.dowloadFile(openSslCompressPath, downloadUrlOpenssl, disableValidationCertificates);
			}

			String sourceDirectory = nginxDirectory + File.separator + "src" + File.separator;
			// Verified if executable nginx file exist
			if (!new File(nginxExecutablePath).exists())
			{
				if (nginxCompressPath.endsWith(".zip"))
				{
					ZipUtil.unzip(nginxCompressPath, nginxDirectory, logger);
				}
				else if (nginxCompressPath.endsWith(".tar.gz"))
				{
					ZipUtil.untargz(nginxCompressPath, sourceDirectory, logger);
				}
				else
				{
					return new NginxInstall(null, null, false, new Throwable("Unsupported file type"));
				}

				if (currentOs.getTypeOs().equals(TypeOs.UNIX))
				{
					ZipUtil.untargz(pcreCompressPath, sourceDirectory, logger);
					ZipUtil.untargz(zlibCompressPath, sourceDirectory, logger);
					ZipUtil.untargz(openSslCompressPath, sourceDirectory, logger);
				}
			}

			// Compile nginx
			if (currentOs.getTypeOs().equals(TypeOs.UNIX) && !new File(nginxExecutablePath).exists())
			{
				String homeNginx = execPermision(sourceDirectory, downloadNginxUrl, logger);
				String homePcre = execPermision(sourceDirectory, downloadUrlPcre, logger);
				String homeZlib = execPermision(sourceDirectory, downloadUrlZlib, logger);
				String homeOpenssl = execPermision(sourceDirectory, downloadUrlOpenssl, logger);

				String nginxFileName;
				if (downloadNginxUrl.endsWith(".tar.gz"))
				{
					nginxFileName = downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf('/') + 1, downloadNginxUrl.lastIndexOf(".tar.gz"));
				}
				else
				{
					nginxFileName = downloadNginxUrl.substring(downloadNginxUrl.lastIndexOf('/') + 1, downloadNginxUrl.lastIndexOf(".tgz"));
				}

				String[] execConfigureNginx = new String[] { "." + File.separator + "configure", "--prefix=" + nginxDirectory + nginxFileName, "--with-pcre=" + homePcre, "--with-zlib=" + homeZlib,
				        "--with-openssl=" + homeOpenssl };

				RunProcessUtil.run(execConfigureNginx, homeNginx, true, true, logger);
				RunProcessUtil.run(new String[] { "make", "install" ,"-d"}, homeNginx, true, true, logger);
			}

			NginxInstall nginxInstall = new NginxInstall(nginxHome, nginxExecutablePath, true, null);

			return nginxInstall;
		}
		catch (MalformedURLException malformedURLException)
		{
			return new NginxInstall(null, null, false, malformedURLException);
		}
		catch (IOException ioException)
		{
			return new NginxInstall(null, null, false, ioException);
		}
		catch (NginxDownloadNotFoundException e)
		{
			return new NginxInstall(null, null, false, e);
		}
		catch (KeyManagementException e)
		{
			return new NginxInstall(null, null, false, e);
		}
		catch (NoSuchAlgorithmException e)
		{
			return new NginxInstall(null, null, false, e);
		}
		catch (InterruptedException e)
		{
			return new NginxInstall(null, null, false, e);
		}
	}

	private static String execPermision(String sourceDirectory, String downloadUrl, final Log logger) throws IOException
	{
		String pathConfigureFile = sourceDirectory;
		if (downloadUrl.endsWith(".tar.gz"))
		{
			pathConfigureFile += downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1, downloadUrl.lastIndexOf(".tar.gz"));
		}
		else
		{
			pathConfigureFile += downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1, downloadUrl.lastIndexOf(".tgz"));
		}

		String homeApp = pathConfigureFile;

		pathConfigureFile += File.separator + "configure";

		String[] execPermissionConfigureNginx = new String[] { "chmod", "u+x", pathConfigureFile };
		logger.info("Exec:[" + StringUtils.join(execPermissionConfigureNginx, " ") + "]");
		RunProcessUtil.run(execPermissionConfigureNginx);

		return homeApp;
	}
}
