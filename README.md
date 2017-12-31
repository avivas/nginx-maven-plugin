# Nginx Maven Plugin

[nginx](https://www.nginx.com/) maven plugin. 

## Features

* Install automatically nginx on windows and *nix
* Execute internal o external nginx
* Support different versions of nginx

## Installing

```
git clone https://github.com/avivas/nginx-maven-plugin.git
cd nginx-maven-plugin
mvn install
```

## Plugin goals

Goal | Description
------------ | -------------
start | Send start signal to nginx
reload | Send reload signal to nginx
reopen | Send reopen signal to nginx
stop | Send stop signal to nginx
quit | Send quit signal to nginx

## Usage

Add plugin to build section:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.bachue</groupId>
      <artifactId>nginx-maven-plugin</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <configuration>
         <!-- Put your configuration -->
      </configuration>
     </plugin>
   </plugins>
</build>
```
### Configuration

Goal | Description | Default value
------------ | -------------|-------------
nginxVersion | Define nginx version| latest
nginxConfigurationFile | Path to nginx configuration file (nginx.conf)|  
nginxPrefixPath | nginx prefix path | 
disableValidationCertificates | Disable validation certificates on https | true
urlDownloads | Url to downloads.js file | https://raw.githubusercontent.com/avivas/nginx-maven-plugin/master/src/main/resources/downloads.json 
pathDownloads | Local file download.js file |  internal download.js
nginxExecutablePath | Path to nginx executable | 
useLocalNginx | True if you want to use local nginx | false


## Examples

Run nginx version 1.13.4 using nginx.conf file.

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.bachue</groupId>
      <artifactId>nginx-maven-plugin</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <configuration>
        <nginxVersion>1.13.4</nginxVersion>
        <nginxConfigurationFile>nginx.conf</nginxConfigurationFile>
      </configuration>
    </plugin>
  </plugins>
</build>
```

## Issues

If you feel to add any feature you can open issue in https://github.com/avivas/nginx-maven-plugin/issues/ and we will try to address it as soon as possible

## Authors

* **Alejandro Vivas** - *Initial work* - [avivas](https://github.com/avivas)

See also the list of [contributors](https://github.com/avivas/nginx-maven-plugin/contributors) who participated in this project.

## License

This project is licensed under the Apache License Version 2.0 MIT License - see the [LICENSE.txt](LICENSE.txt) file for details
