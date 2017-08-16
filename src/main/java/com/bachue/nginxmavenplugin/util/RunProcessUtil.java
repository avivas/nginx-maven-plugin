package com.bachue.nginxmavenplugin.util;

import java.io.File;

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

import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

/**
 * Run a process
 * @author Alejandro Vivas
 * @version 14/08/2017 0.0.1-SNAPSHOT
 * @since 14/08/2017 0.0.1-SNAPSHOT
 */
public class RunProcessUtil
{
	/**
	 * Run a process
	 * @author Alejandro Vivas
	 * @version 14/08/2017 0.0.1-SNAPSHOT
	 * @since 14/08/2017 0.0.1-SNAPSHOT
	 * @param args Arguments to start a process
	 * @return Process created
	 * @throws IOException If IO error
	 */
	public static Process run(String[] args) throws IOException
	{
		return Runtime.getRuntime().exec(args);
	}
	
	public static Process run(String[] args,String pwd,boolean printInputStream,boolean printErrorStream, final Log logger) throws IOException, InterruptedException
	{
		logger.info("Exec:[" + StringUtils.join(args," ") + "][" + pwd + "]");
		
		Process process = Runtime.getRuntime().exec(args,null,new File(pwd));
		
		if(printInputStream)
		{
			logger.info("Input:" + args[0]);
			InputStream e = process.getInputStream();
			StringBuilder stringBuilder = new StringBuilder();
			int c;
			while( (c = e.read()) != -1 )
			{
				char caracter = (char)c;
				if(caracter == '\n')
				{
					logger.info(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				}
				else
				{
					stringBuilder.append(caracter);
				}
			}
		}
		
		if(printErrorStream)
		{
			logger.info("Error:" + args[0]);
			InputStream e = process.getErrorStream();
			int c;
			StringBuilder stringBuilder = new StringBuilder();
			while( (c = e.read()) != -1 )
			{
				char caracter = (char)c;
				if(caracter == '\n')
				{
					logger.info(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				}
				else
				{
					stringBuilder.append(caracter);
				}
			}
		}
		
		int exitValue = process.waitFor();
		logger.info("Exit value:" + args[0] + ":[" + exitValue + "]");
		
		return process;
	}
}
