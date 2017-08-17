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

/**
 * OS information
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class OSUtil
{
	/**
	 * True if a win OS, false otherwise
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return True if a windows os, false otherwise
	 */
	public static boolean isWindows()
	{
		String	OS_STRING	= System.getProperty("os.name").toLowerCase();
		return (OS_STRING.indexOf("win") >= 0);
	}

	/**
	 * True if a Unix OS, false otherwise
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @return True if a windows OS, false otherwise
	 */
	public static boolean isUnix()
	{
		String	OS_STRING	= System.getProperty("os.name").toLowerCase();
		return (OS_STRING.indexOf("nix") >= 0 || OS_STRING.indexOf("nux") >= 0 || OS_STRING.indexOf("aix") > 0);
	}
}
