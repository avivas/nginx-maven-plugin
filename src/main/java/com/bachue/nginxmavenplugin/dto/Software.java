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
 * Class witd software data
 * @author Alejandro Vivas
 * @version 17/08/2017 0.0.1-SNAPSHOT
 * @since 17/08/2017 0.0.1-SNAPSHOT
 */
public class Software
{
	/** Software name */
	private String		name;
	/** Software version */
	private String		version;
	/** Os type */
	private OsType		osType;
	/** Download url */
	private String		downloadUrl;
	/** URL of asc check file */
	private String		ascCheckfileUrl;
	/** URL of sif check file */
	private String		sigCheckfileUrl;
	/** Package type */
	private PackageType	packageType;
	/** Dependencies */
	private Software[]	dependencies;

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

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the osType
	 */
	public OsType getOsType()
	{
		return osType;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param osType the osType to set
	 */
	public void setOsType(OsType osType)
	{
		this.osType = osType;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the downloadUrl
	 */
	public String getDownloadUrl()
	{
		return downloadUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param downloadUrl the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the ascCheckfileUrl
	 */
	public String getAscCheckfileUrl()
	{
		return ascCheckfileUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param ascCheckfileUrl the ascCheckfileUrl to set
	 */
	public void setAscCheckfileUrl(String ascCheckfileUrl)
	{
		this.ascCheckfileUrl = ascCheckfileUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the sigCheckfileUrl
	 */
	public String getSigCheckfileUrl()
	{
		return sigCheckfileUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param sigCheckfileUrl the sigCheckfileUrl to set
	 */
	public void setSigCheckfileUrl(String sigCheckfileUrl)
	{
		this.sigCheckfileUrl = sigCheckfileUrl;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the packageType
	 */
	public PackageType getPackageType()
	{
		return packageType;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param packageType the packageType to set
	 */
	public void setPackageType(PackageType packageType)
	{
		this.packageType = packageType;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the dependencies
	 */
	public Software[] getDependencies()
	{
		return dependencies;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(Software[] dependencies)
	{
		this.dependencies = dependencies;
	}
}
