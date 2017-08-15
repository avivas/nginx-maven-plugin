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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.maven.plugin.logging.Log;

/**
 * Zip util
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class ZipUtil
{
	/**
	 * Unzip a zip file
	 * @param pathZipFile path to zip file
	 * @param outputFolder Output folder to unzip
	 * @param logger Maven logger
	 * @throws IOException If fail to unzip a file
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 */
	public static void unzip(String pathZipFile, String outputFolder, final Log logger) throws IOException
	{
		logger.info("Starting to unzip:[" + pathZipFile + "]");

		byte[] buffer = new byte[1024];
		// Create output folder is not exists
		File folder = new File(outputFolder);
		if (!folder.exists())
		{
			folder.mkdir();
		}

		// Get the zip file
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(pathZipFile));
		// Get the first entry
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		while (zipEntry != null)
		{
			String fileName = zipEntry.getName();
			File newFile = new File(outputFolder + File.separator + fileName);
			if (zipEntry.isDirectory())
			{
				newFile.mkdirs();
			}
			else
			{
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fileOutputStream = null;
				try
				{
					fileOutputStream = new FileOutputStream(newFile);
					int length;
					while ((length = zipInputStream.read(buffer)) > 0)
					{
						fileOutputStream.write(buffer, 0, length);
					}
				}
				finally
				{
					if (fileOutputStream != null)
					{
						fileOutputStream.close();
					}
				}
			}
			zipEntry = zipInputStream.getNextEntry();
		}

		zipInputStream.closeEntry();
		zipInputStream.close();

		logger.info("Finish to unzip:[" + pathZipFile + "]");
	}
}
