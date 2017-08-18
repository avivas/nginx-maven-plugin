package com.bachue.nginxmavenplugin.dto;

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

/**
 * Package data to install
 * @author Alejandro Vivas
 * @version 18/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class PackageResultInstall
{
	/** Nginx home */
	private String		home;
	/** If install successful */
	private boolean		success	= true;
	/** Error */
	private Throwable	throwable;
	/** Executable path */
	private String		executablePath;
	/** Package name */
	private String		name;

	/**
	 * Create object
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param home Nginx home
	 * @param executablePath Executable path
	 * @param success true if success install, false otherwise
	 * @param throwable Exception error
	 */
	public PackageResultInstall(String home, String executablePath, boolean success, Throwable throwable,String name)
	{
		this.home = home;
		this.success = success;
		this.throwable = throwable;
		this.executablePath = executablePath;
		this.name = name;
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
	public String getExecutablePath()
	{
		return executablePath;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
}
