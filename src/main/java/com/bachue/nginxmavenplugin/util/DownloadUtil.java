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

import org.codehaus.plexus.util.FileUtils;

/**
 * Download Util
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class DownloadUtil
{
	/**
	 * Download a file
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param fileName Path to download
	 * @param fileUrl Url path
	 * @throws MalformedURLException If invalid url
	 * @throws IOException If IO error
	 */
	public static void dowloadFile(String fileName, String fileUrl) throws MalformedURLException, IOException
	{
		FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
	}
}
