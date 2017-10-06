#!/bin/bash

for i in "$@"
do
case $i in

    -s=*|--source-home=*)
    SOURCE_HOME="${i#*=}"
    shift # past argument=value
    ;;
    -p=*|--prefix=*)
    PREFIX_NGINX="${i#*=}"
    shift # past argument=value
    ;;
  	-uo=*|--url-openssl=*)
    URL_OPENSSL="${i#*=}"
    shift # past argument=value
    ;;
    -up=*|--url-pcre=*)
    URL_PCRE="${i#*=}"
    shift # past argument=value
    ;;
    -uz=*|--url-zlib=*)
    URL_ZLIB="${i#*=}"
    shift # past argument=value
    ;;
    -ub=*|--url-brotli=*)
    URL_BROTLI="${i#*=}"
    shift # past argument=value
    ;;
    -un=*|--url-nginx=*)
    URL_NGINX="${i#*=}"
    shift # past argument=value
    ;;
esac
done
echo "SOURCE_HOME  = ${SOURCE_HOME}"
echo "PREFIX_NGINX = ${PREFIX_NGINX}"
echo "URL_OPENSSL = ${URL_OPENSSL}"
echo "URL_PCRE = ${URL_PCRE}"
echo "URL_ZLIB = ${URL_ZLIB}"
echo "URL_BROTLI = ${URL_BROTLI}"
echo "URL_NGINX = ${URL_NGINX}"

mkdir -p $SOURCE_HOME

# Download openssl
cd $SOURCE_HOME
wget -c $URL_OPENSSL
tar -zxf ${URL_OPENSSL##*/}
#openssl-1.0.2k.tar.gz

# Download pcre
wget -c $URL_PCRE
tar -zxf ${URL_PCRE##*/}

# Download zlib
wget -c $URL_ZLIB
tar -zxf ${URL_ZLIB##*/}

# Download brotli module
git clone $URL_BROTLI
cd ngx_brotli
git submodule update --init --recursive
cd $SOURCE_HOME

# Download nginx
wget -c $URL_NGINX
tar zxf ${URL_NGINX##*/}
NGINX_PATH_DIRECTORY_=${URL_NGINX%.*}
NGINX_PATH_DIRECTORY__=${NGINX_PATH_DIRECTORY_%.*}
NGINX_PATH_DIRECTORY=${NGINX_PATH_DIRECTORY__##*/}
cd $NGINX_PATH_DIRECTORY

./configure --prefix=$PREFIX_NGINX \
	--with-openssl=$SOURCE_HOME/openssl-1.0.2k \
	--with-zlib=$SOURCE_HOME/zlib-1.2.11 \
	--with-pcre=$SOURCE_HOME/pcre-8.41 \
	--add-module=$SOURCE_HOME/ngx_brotli/ \
	--with-stream \
	--with-stream_ssl_module \
	--with-stream_realip_module \
	--with-stream_geoip_module \
	--with-stream_ssl_preread_module \
	--with-select_module \
  	--with-poll_module \
  	--with-threads \
  	--with-file-aio \
  	--with-http_ssl_module \
  	--with-http_v2_module \
  	--with-http_realip_module \
  	--with-http_addition_module \
  	--with-http_geoip_module \
  	--with-http_sub_module \
  	--with-http_dav_module \
  	--with-http_gunzip_module \
  	--with-http_gzip_static_module \
  	--with-http_auth_request_module \
  	--with-http_random_index_module \
  	--with-http_secure_link_module \
  	--with-http_degradation_module \
  	--with-http_slice_module \
  	--with-http_stub_status_module
  	
# --with-google_perftools_module 
# --with-google_perftools_module \  	
# --with-libatomic=$SOURCE_HOME/libatomic_ops-7.4.6

make
make install
