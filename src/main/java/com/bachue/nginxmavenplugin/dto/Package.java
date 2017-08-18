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
 * Class with package data
 * @author Alejandro Vivas
 * @version 17/08/2017 0.0.1-SNAPSHOT
 * @since 17/08/2017 0.0.1-SNAPSHOT
 */
public class Package
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
	private Package[]	dependencies;
	/** True if is latest version */
	private boolean latest = false; 

	/**
	 * Empty constructor.
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 */
	public Package()
	{
	}

	/**
	 * Copy constructor
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param package1 Object to cpoy
	 */
	public Package(Package package1)
	{
		this.name = package1.name;
		this.version = package1.version;
		this.osType = package1.osType;
		this.downloadUrl = package1.downloadUrl;
		this.ascCheckfileUrl = package1.ascCheckfileUrl;
		this.sigCheckfileUrl = package1.sigCheckfileUrl;
		this.packageType = package1.packageType;
		this.latest = package1.latest;
		if (package1.dependencies != null)
		{
			this.dependencies = new Package[package1.dependencies.length];
			for (int i = 0; i < package1.dependencies.length; i++)
			{
				this.dependencies[i] = new Package(package1.dependencies[i]);
			}
		}
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
	public Package[] getDependencies()
	{
		return dependencies;
	}

	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(Package[] dependencies)
	{
		this.dependencies = dependencies;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param latest the latest to set
	 */
	public void setLatest(boolean latest)
	{
		this.latest = latest;
	}
	
	/**
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @return the latest
	 */
	public boolean isLatest()
	{
		return latest;
	}
	
	/* (non-Javadoc)
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Package:\n Name[");
		stringBuilder.append(this.name);
		stringBuilder.append("]\n Version:[");
		stringBuilder.append(this.version);
		stringBuilder.append("]\n OsType:[");
		stringBuilder.append(this.osType);
		stringBuilder.append("]\n Download URL:[");
		stringBuilder.append(this.downloadUrl);
		stringBuilder.append("]\n Asc check file:[");
		stringBuilder.append(this.ascCheckfileUrl);
		stringBuilder.append("]\n Sig check file:[");
		stringBuilder.append(this.sigCheckfileUrl);
		stringBuilder.append("]\n Package type:[");
		stringBuilder.append(this.packageType);
		stringBuilder.append("]\n Latest:[");
		stringBuilder.append(this.latest);
		stringBuilder.append("]");
		if (this.dependencies != null)
		{
			stringBuilder.append("\n   Start Dependencies\n");
			for (Package package1 : this.dependencies)
			{
				stringBuilder.append("\n");
				stringBuilder.append(package1.toString());
			}
			stringBuilder.append("\n   End Dependencies");
		}
		
		return stringBuilder.toString();
	}	
}
