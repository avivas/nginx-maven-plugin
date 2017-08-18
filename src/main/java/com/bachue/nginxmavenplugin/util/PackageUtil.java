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

import com.bachue.nginxmavenplugin.dto.PackageInstall;
import com.bachue.nginxmavenplugin.dto.PackageType;
import com.bachue.nginxmavenplugin.dto.OsType;
import com.bachue.nginxmavenplugin.dto.Package;

/**
 * Util download nginx
 * @author Alejandro Vivas
 * @version 17/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public final class PackageUtil
{
	/**
	 * Private constructor to avoid instances
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	private PackageUtil()
	{
	}

	/**
	 * Install nginx
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param localRepository Object to get repository
	 * @param version Nginx version
	 * @param logger maven logger
	 * @param disableValidationCertificates Disable validation certificates
	 * @return Object with status to install
	 */
	public static PackageInstall install(final ArtifactRepository localRepository, final String version, boolean disableValidationCertificates, final Log logger)
	{
		try
		{
			URL url = new URL(localRepository.getUrl());
			File fileRepository = new File(url.getFile());

			PackageConfiguration urlsDownloadNginx = new PackageConfiguration(logger);
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
				return new PackageInstall(nginxHome, nginxExecutablePath, true, null,null);
			}

			// Install TODO
			install(osPackageVersion, localRepository, disableValidationCertificates, logger);
			// Install TODO

			PackageInstall nginxInstall = new PackageInstall(nginxHome, nginxExecutablePath, true, null,null);
			return nginxInstall;
		}
		catch (MalformedURLException malformedURLException)
		{
			return new PackageInstall(null, null, false, malformedURLException,null);
		}
		catch (IOException ioException)
		{
			return new PackageInstall(null, null, false, ioException,null);
		}
		catch (PackageException e)
		{
			return new PackageInstall(null, null, false, e,null);
		}
		catch (KeyManagementException e)
		{
			return new PackageInstall(null, null, false, e,null);
		}
		catch (NoSuchAlgorithmException e)
		{
			return new PackageInstall(null, null, false, e,null);
		}
		catch (InterruptedException e)
		{
			return new PackageInstall(null, null, false, e,null);
		}
		catch (DownloadPackageException e)
		{
			return new PackageInstall(null, null, false, e,null);
		}
		catch (UnsupportedCompressFileException e)
		{
			return new PackageInstall(null, null, false, e,null);
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
	private static PackageInstall install(Package package1, final ArtifactRepository localRepository, boolean disableValidationCertificates, final Log logger)
	        throws KeyManagementException, NoSuchAlgorithmException, IOException, DownloadPackageException, UnsupportedCompressFileException, InterruptedException
	{
		PackageInstall[] packageInstallDependencies = null;
		if (package1.getDependencies() != null)
		{
			packageInstallDependencies = new PackageInstall[package1.getDependencies().length];
			for (int i = 0; i < package1.getDependencies().length; i++)
			{
				Package dependency = package1.getDependencies()[i];
				packageInstallDependencies[i] = install(dependency, localRepository, disableValidationCertificates, logger);
			}
		}

		URL urlLocalRepository = new URL(localRepository.getUrl());
		File fileRepository = new File(urlLocalRepository.getFile());
		String pluginHome = fileRepository.toPath() + File.separator + "com" + File.separator + "bachue" + File.separator + "nginx-maven-plugin" + File.separator;
		String cacheHome = pluginHome + "cache" + File.separator;
		String sourceDirectory = cacheHome + File.separator + "src" + File.separator;

		// Download package and checksum
		String downloadPackageUrl = package1.getDownloadUrl();
		String downloadCheckfileUrl = package1.getAscCheckfileUrl() == null ? package1.getSigCheckfileUrl() : package1.getAscCheckfileUrl();

		String packageLocalFileName = cacheHome + downloadPackageUrl.substring(downloadPackageUrl.lastIndexOf("/") + 1);
		String checkPackageLocalFileName = null;
		if (downloadCheckfileUrl != null)
		{
			checkPackageLocalFileName = cacheHome + downloadCheckfileUrl.substring(downloadCheckfileUrl.lastIndexOf("/") + 1);
		}

		if (!new File(packageLocalFileName).exists() || !new File(checkPackageLocalFileName).exists() || !checkSum(packageLocalFileName, checkPackageLocalFileName))
		{
			DownloadUtil.dowloadFile(packageLocalFileName, downloadPackageUrl, disableValidationCertificates);
			if (downloadCheckfileUrl != null)
			{
				DownloadUtil.dowloadFile(checkPackageLocalFileName, downloadCheckfileUrl, disableValidationCertificates);
			}

			if (!checkSum(packageLocalFileName, checkPackageLocalFileName))
			{
				throw new DownloadPackageException("Checksum[" + checkPackageLocalFileName + "] or file [" + packageLocalFileName + "] invalid");
			}
		}

		// Uncompress package
		String installHome = cacheHome + File.separator +  getSimpleNameWithoutExtension(downloadPackageUrl);
		String sourceHome =  sourceDirectory + getSimpleNameWithoutExtension(downloadPackageUrl);
		switch (package1.getPackageType())
		{
			case BINARY:
				uncompressFile(packageLocalFileName, cacheHome, logger);
				return new PackageInstall(installHome, null, true, null,package1.getName());

			case SOURCE:
				uncompressFile(packageLocalFileName, sourceDirectory, logger);
				grantExecPermision(sourceDirectory, downloadPackageUrl, logger);
			break;
		}

		if (package1.getName().equals("nginx") && package1.getPackageType().equals(PackageType.SOURCE))
		{
			StringBuilder stringOptionsBuilder = new StringBuilder();
			for (PackageInstall dependency : packageInstallDependencies)
			{
				stringOptionsBuilder.append("--with-");
				stringOptionsBuilder.append(dependency.getName());
				stringOptionsBuilder.append("=");
				stringOptionsBuilder.append(dependency.getHome());
				stringOptionsBuilder.append(" ");
			}

			String[] execConfigureNginx = new String[] { "." + File.separator + "configure", "--prefix=" + installHome };
			String[] options = stringOptionsBuilder.toString().split(" ");
			execConfigureNginx = ArrayUtil.concat(execConfigureNginx, options);

			RunProcessUtil.run(execConfigureNginx, sourceHome, logger);
			RunProcessUtil.run(new String[] { "make", "install", "-d" }, sourceHome, logger);
			
			return  new PackageInstall(installHome, null, true, null,package1.getName());
		}
		else
		{
			PackageInstall packageInstall = new PackageInstall(sourceHome, null, true, null,package1.getName());
			return packageInstall;
		}
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

	private static boolean checkSum(String file, String fileChecksum)
	{
		// TODO
		return true;
	}

	/**
	 * Grant exec permission a configure file
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param sourceDirectory Source directorory
	 * @param downloadUrl download url file
	 * @param logger Logger
	 * @return String with home app
	 * @throws IOException If fail to grant permission
	 */
	private static String grantExecPermision(String sourceDirectory, String downloadUrl, final Log logger) throws IOException
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