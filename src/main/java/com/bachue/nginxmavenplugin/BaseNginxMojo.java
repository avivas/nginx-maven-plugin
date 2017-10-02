package com.bachue.nginxmavenplugin;

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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import com.bachue.nginxmavenplugin.dto.PackageResultInstall;
import com.bachue.nginxmavenplugin.util.PackageInstallException;
import com.bachue.nginxmavenplugin.util.PackageInstall;
import com.bachue.nginxmavenplugin.util.RunProcessUtil;

/**
 * Base Mojo class
 * @author Alejandro Vivas
 * @version 2/10/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public abstract class BaseNginxMojo extends AbstractMojo
{		
	/** Object to get repository path */
	@Parameter(defaultValue="${localRepository}", readonly = true, required = true)
	private org.apache.maven.artifact.repository.ArtifactRepository localRepository;	
	/** Object to get project path */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;
	
	/** Nginx version to use  */
	@Parameter(property = "nginxVersion",defaultValue = "latest")
    private String nginxVersion;
	
	/** Nginx configuration file */
	@Parameter(property = "nginxConfigurationFile")
    private String nginxConfigurationFile;
	
	/** Nginx prefix path */
	@Parameter(property = "nginxPrefixPath")
    private String nginxPrefixPath;
	
	/** Disable validation certificates in https downloads*/
	@Parameter(property = "disableValidationCertificates", defaultValue = "true")
	private boolean disableValidationCertificates;
	
	/** Url to downloads.json file */
	@Parameter(property = "urlDownloads", defaultValue = "https://raw.githubusercontent.com/avivas/nginx-maven-plugin/master/src/main/resources/downloads.json")
	private String urlDownloads;
	
	/** Path to downloads.json file */
	@Parameter(property = "pathDownloads")
	private String pathDownloads;
	
	/**
	 * Execute nginx
	 * @author Alejandro Vivas
	 * @version 12/09/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	public void execute() throws MojoExecutionException
	{
		normalizePaths();
		// Install nginx
		PackageResultInstall nginxInstall;
		try
		{
			PackageInstall packageInstall = new PackageInstall(localRepository, getLog(),getUrlDownloads(),getPathDownloads());
			nginxInstall = packageInstall.install(getNginxVersion(),isDisableValidationCertificates());
		} 
		catch (PackageInstallException e1)
		{
			getLog().error("Error to install nginx",e1);
			throw new MojoExecutionException("", e1);
		}
		
		// Set default values
		if( getNginxPrefixPath() == null)
		{
			setNginxPrefixPath(nginxInstall.getHome());
		}
		getLog().info("Nginx Prefix:[" + getNginxPrefixPath() + "]");
		
		// Create nginx prefix folder if there is no exist 
		createNginxPrefixDirectory();		
		
		if(	getNginxConfigurationFile() == null	)
		{
			setNginxConfigurationFile(getNginxPrefixPath() + File.separator + "conf" + File.separator + "nginx.conf"); 
		}
		if( !new File(getNginxConfigurationFile()).isAbsolute() )
		{
			setNginxConfigurationFile( project.getBasedir() + File.separator  + getNginxConfigurationFile());
		}		
		getLog().info("Nginx Configuration file:[" + getNginxConfigurationFile() + "]");
		
		try
		{
			String [] args;
			String signal = getNginxSignal();
			if(signal == null)
			{
				args = new String[]{nginxInstall.getExecutablePath(),"-p",getNginxPrefixPath(),"-c",getNginxConfigurationFile()};
			}
			else
			{
				args = new String[]{nginxInstall.getExecutablePath(),"-p",getNginxPrefixPath(),"-c",getNginxConfigurationFile(),"-s",signal};
			}
			
			getLog().info("Nginx Command:[" +  StringUtils.join(args," ") + "]");
			
			RunProcessUtil.run(args);
		}
		catch (IOException e)
		{
			getLog().error("Error to run nginx", e);
		}		
	}
	
	/**
	 * Normalize paths (use OS file separator) to avoid problems to run nginx
	 * @author Alejandro Vivas
	 * @version 2/10/2017 0.0.1-SNAPSHOT
	 * @since 2/10/2017 0.0.1-SNAPSHOT
	 */
	private void normalizePaths()
	{
		if( getNginxPrefixPath() != null)
		{
			setNginxPrefixPath( getNginxPrefixPath().replace('/', File.separatorChar)  );
		}
		if( (getNginxConfigurationFile() != null) )
		{
			setNginxConfigurationFile(getNginxConfigurationFile().replace('/', File.separatorChar));
		}
	}
	
	/**
	 * Create nginx prefix directory
	 * @author Alejandro Vivas
	 * @version 12/09/2017 0.0.1-SNAPSHOT
	 * @since 12/09/2017 0.0.1-SNAPSHOT
	 * @throws MojoExecutionException If fail to create nginx prefix directory
	 */
	private void createNginxPrefixDirectory() throws MojoExecutionException
	{
		// Create nginx prefix directory
		File fileNginxPrefixPath = new File(getNginxPrefixPath());
		if( !fileNginxPrefixPath.exists() )
		{
			getLog().warn("nginx prefix path [" + getNginxPrefixPath()  + "] doesn't exist");
			try
			{
				fileNginxPrefixPath.mkdirs();
			}
			catch(SecurityException securityException)
			{
				String message = "Error to create nginx prefix path:[" + getNginxPrefixPath() + "]";
				getLog().error(message,securityException);
				throw new MojoExecutionException("Error to install nginx", securityException);
			}
		}
		
		// Create folder to logs in nginx prefix directory
		File fileNginxPrefixPathLogs = new File(getNginxPrefixPath() + File.separator + "logs");
		if( !fileNginxPrefixPathLogs.exists() )
		{
			getLog().warn("nginx prefix logs path [" +fileNginxPrefixPathLogs.getAbsolutePath()  + "] doesn't exist");
			try
			{
				fileNginxPrefixPathLogs.mkdirs();
			}
			catch(SecurityException securityException)
			{
				String message = "Error to create nginx logs prefix path:[" + getNginxPrefixPath() + "]";
				getLog().error(message,securityException);
				throw new MojoExecutionException(message, securityException);
			}
		}
	}
	
	/**
	 * Return nginx signal, can be: stop, quit, reload, reopen o null (start nginx) 
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return  Return nginx signal
	 */
	public abstract String getNginxSignal();

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return the nginxVersion
	 */
	public String getNginxVersion()
	{
		return nginxVersion;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param nginxVersion the nginxVersion to set
	 */
	public void setNginxVersion(String nginxVersion)
	{
		this.nginxVersion = nginxVersion;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return the nginxConfigurationFile
	 */
	public String getNginxConfigurationFile()
	{
		return nginxConfigurationFile;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param nginxConfigurationFile the nginxConfigurationFile to set
	 */
	public void setNginxConfigurationFile(String nginxConfigurationFile)
	{
		this.nginxConfigurationFile = nginxConfigurationFile;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return the nginxPrefixPath
	 */
	public String getNginxPrefixPath()
	{
		return nginxPrefixPath;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param nginxPrefixPath the nginxPrefixPath to set
	 */
	public void setNginxPrefixPath(String nginxPrefixPath)
	{
		this.nginxPrefixPath = nginxPrefixPath;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @param disableValidationCertificates the disableValidationCertificates to set
	 */
	public void setDisableValidationCertificates(boolean disableValidationCertificates)
	{
		this.disableValidationCertificates = disableValidationCertificates;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @return the disableValidationCertificates
	 */
	public boolean isDisableValidationCertificates()
	{
		return disableValidationCertificates;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @param urlDownloads the urlDownloads to set
	 */
	public void setUrlDownloads(String urlDownloads)
	{
		this.urlDownloads = urlDownloads;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @return the urlDownloads
	 */
	public String getUrlDownloads()
	{
		return urlDownloads;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @param pathDownloads the pathDownloads to set
	 */
	public void setPathDownloads(String pathDownloads)
	{
		this.pathDownloads = pathDownloads;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 18/08/2017 0.0.1-SNAPSHOT
	 * @since 18/08/2017 0.0.1-SNAPSHOT
	 * @return the pathDownloads
	 */
	public String getPathDownloads()
	{
		return pathDownloads;
	}
}
