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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
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
		logger.info("Starting to uncompress:[" + pathZipFile + "]");

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
					logger.info("Uncompress: " + newFile.getAbsolutePath());
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

		logger.info("Finish to uncompress:[" + pathZipFile + "]");
	}

	/**
	 * Uncompress a tar.gz file in a folder
	 * @author Alejandro Vivas
	 * @version 15/08/2017 0.0.1-SNAPSHOT
	 * @since 15/08/2017 0.0.1-SNAPSHOT
	 * @param pathTarGz File to uncompress
	 * @param pathOutputFolder Output folder
	 * @param logger Maven logger
	 * @throws IOException If fail uncompress
	 */
	public static void untargz(String pathTarGz, String pathOutputFolder, final Log logger) throws IOException
	{
		logger.info("Starting to uncompress:[" + pathTarGz + "]");

		File tarGzFile = new File(pathTarGz);
		File outputFolder = new File(pathOutputFolder);
		outputFolder.mkdir();

		TarArchiveInputStream tarArchiveInputStream = null;
		try
		{
			tarArchiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(tarGzFile))));
			TarArchiveEntry tarArchiveEntry = tarArchiveInputStream.getNextTarEntry();
			byte[] buffer = new byte[1024];
			while (tarArchiveEntry != null)
			{
				File outputFile = new File(outputFolder, tarArchiveEntry.getName());
				logger.info("Uncompress: " + outputFile.getCanonicalPath());
				if (tarArchiveEntry.isDirectory())
				{
					outputFile.mkdirs();
				}
				else
				{
					new File(outputFile.getParent()).mkdirs();
					outputFile.createNewFile();

					BufferedOutputStream bufferedOutputStream = null;
					try
					{
						bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
						int length = 0;
						while ((length = tarArchiveInputStream.read(buffer)) != -1)
						{
							bufferedOutputStream.write(buffer, 0, length);
						}

					}
					finally
					{
						if (bufferedOutputStream != null)
						{
							try
							{
								bufferedOutputStream.close();
							}
							catch (IOException e)
							{
								throw e;
							}
						}
					}
				}
				tarArchiveEntry = tarArchiveInputStream.getNextTarEntry();
			}
		}
		finally
		{
			if (tarArchiveInputStream != null)
			{
				try
				{
					tarArchiveInputStream.close();
				}
				catch (IOException e)
				{
					throw e;
				}
			}
		}

		logger.info("Finish to uncompress:[" + pathTarGz + "]");
	}
}
