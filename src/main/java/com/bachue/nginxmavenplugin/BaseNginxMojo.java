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

import com.bachue.nginxmavenplugin.util.NginxDownloadUtil;
import com.bachue.nginxmavenplugin.util.RunProcessUtil;
import com.bachue.nginxmavenplugin.util.NginxDownloadUtil.NginxInstall;

/**
 * Base Mojo class
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
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
	
	/** Nginx pprefix path */
	@Parameter(property = "nginxPrefixPath")
    private String nginxPrefixPath;	
	
	/**
	 * Execute nginx
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	public void execute() throws MojoExecutionException
	{
		// Install nginx
		NginxInstall nginxInstall = NginxDownloadUtil.install(this.localRepository,getNginxVersion(),getLog());		
		if(!nginxInstall.isSuccess())
		{
			return;
		}
		
		// Set default values
		if( getNginxPrefixPath() == null)
		{
			setNginxPrefixPath(nginxInstall.getHome());
		}
		getLog().info("Nginx Prefix:[" + getNginxPrefixPath() + "]");
		
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
				args = new String[]{nginxInstall.getNginxExecutablePath(),"-p",getNginxPrefixPath(),"-c",getNginxConfigurationFile()};
			}
			else
			{
				args = new String[]{nginxInstall.getNginxExecutablePath(),"-p",getNginxPrefixPath(),"-c",getNginxConfigurationFile(),"-s",signal};
			}
			
			getLog().info("Nginx Command:[" +  StringUtils.join(args," ") + "]");
			
			RunProcessUtil.run(args);
		}
		catch (IOException e)
		{
			getLog().error("Error to start nginx", e);
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
}
