# iot

配置文件外置：/conf/flyjava/resource.properties

需要环境：

redis

mysql

zookeeper 





ID命名建议 三位类别ID + 6为时间（年月日） +5位随机数
例如 土壤温度 10018121000001
   粮仓温湿度 10618121000001

# 使用步骤：

#### 1.设备管理，新建主机

#### 2.添加设备，输入产品ID 

#### 3.我的设备 找到对应设备，

#### 4.可以根据以下测试url 给数据库创造数据，这时候网页上应该有数据展示

#### 5.粮仓温湿度 需要绑定到主机里面，默认打开粮仓监控页面是制定ID 的数据，这个可以在配置文件里面配置。

#### 6.多个粮仓温湿度设备绑定后，在我的主机列表里面，点击后跳转到展示页面。


#####################################################################
## 粮仓温度采集

温湿度 值为-256 赋值为空

/save/granary/{productId}/{T0}/{H0}/{T1}/{H1}/{T2}/{H2}/{T3}/{H3}/{T4}/{H4}/{T5}/{H5}/{T6}/{H6}/{T7}/{H7}/{T8}/{H8}/{T9}/{H9}/{T10}/{H10}

/save/granary/{productId}/{T0}/{H0}/{T1}/{H1}/{T2}/{H2}/{T3}/{H3}/{T4}/{H4}/{T5}/{H5}/{T6}/{H6}/{T7}/{H7}/{T8}/{H8}/{T9}/{H9}/{T10}/{H10}/{T11}/{H11}/{T12}/{H12}/{T13}/{H13}/{T14}/{H14}/{T15}/{H15}/{T16}/{H16}/{T17}/{H17}/{T18}/{H18}/{T19}/{H19}


测试URL:
http://localhost:8086/save/granary/10618121000001/1/1/2/2/3/3/4/4/5/5/6/6/7/7/8/8/9/9/10/10/11/11/12/12/13/13/14/14/15/15/16/16/17/17/18/18/19/19/20/20
######################################################################
## 保存GPS信息

	 * 位置/ID号/信息头/纬度ddmm.mmmmm（度分）/纬度半球N（北半球）或S（南半球）/经度dddmm.mmmmm（度分）/经度半球E（东经）或W（西经）/UTC时间：hhmmss（时分秒）/定位状态，A=有效定位，V=无效定位
	 * /save/location/10018012500005/$GPGLL/4544.92444/N/12643.08448/E/085304.00/A
	/save/location/{productId}/{sign}/{latitude}/{latitudeSource}/{longitude}/{longitudeSource}/{UTCtime}/{status}
测试URL:
http://localhost:8086/save/location/10018121000001/$GPGLL/4544.92444/N/12643.08448/E/085304.00/A

#######################################################################
## 保存温度数据

​	 * @param productId 产品id 唯一
	 * @param temp1
	 * @param temp2
	 * @param temp3
	 * @param temp4
	 * @param temp5
/save/temp/{productId}/{temp1}/{temp2}/{temp3}/{temp4}/{temp5}

测试url 
http://localhost:8086/save/temp/10018121000001/11/22/33/44/55/

#######################################################################



# **农业物联网数据平台**部署手册**

**版本<V1>**

 



​        

# 1、部署准备

## 1.1 机器准备

数据中心系统至少需要一台服务器作为支撑，部署Mysql,redis,zookeeper以及tomcat等。本次采用4台服务器进行部署实施。

【保密】   安装mysql

【保密】   安装redis zookeeper tomcat

【保密】   安装两个tomcat

【保密】   安装两个tomcat 和nginx

## 1.2 目录规范

部署过程中所涉及到的目录统一规划如下：

l  /conf/flyjava/     配置文件目录

使用下述命令依次创建上述目录

\# mkdir -p /conf/flyjava/

\# chmod -R 775 /conf (授权)

## 1.3 操作系统准备

数据标注管理平台系统部署在Linux操作系统环境中，操作系统采用基础安装方式安装，系统可以随意选择，请注意，centos7以上版本指令会有所不同，例如防火墙的开启关闭都不一样。本次在centos6.8上部署。

## 1.4 软件准备

语音围栏系统部署过程中涉及的基础组件清单如下：

| **序号** | **软件名称** | **软件版本**              | **备注**             |
| -------- | ------------ | ------------------------- | -------------------- |
| 1        | jdk          | jdk-7u80-linux-x64.tar.gz | 1.8版本也行          |
| 2        | MySQL        | 5.6.35                    | 5.7版本也行          |
| 3        | zookeeper    | 3.4.5                     |                      |
| 4        | redis        | 3.2.0                     |                      |
| 5        | Tomcat       | 7.0.86                    | 其他版本也行         |
| 6        | nginx        | 1.8.0                     | Yum安装，需要有yum源 |

## 1.5 基础环境准备

### 关闭防火墙

\# systemctl stop firewalld.service       //停止firewall

\# systemctl disable firewalld.service   //禁止firewall开机启动

\# firewall-cmd --state                      //查看默认防火墙状态（关闭后显示notrunning，开启后显示running）

### 关闭selinux

\# vim /etc/selinux/config

SELINUX=disabled                -----关闭安全服务

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)

### 修改机器名称

\# vim /etc/sysconfig/network

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)

### 配置名称解析

\# vim /etc/hosts    -----------(机器名与本机IP对应)

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)


### 修改资源限制

增加最大打开文件数

在/etc/security/limits.conf配置文件中追加以下两行

*       soft    nofile  60000

*       hard    nofile  60000

### jdk环境配置(每台机器都必须装)

查看是否已安装java

 

随后检查jdk版本

\# java -version

 

 

# 2、部署实施

### Mysql

 

**卸载mariadb**

Centos7.3内部集成了mariadb，而安装mysql的话会和mariadb的文件冲突，所以需要先卸载掉mariadb。

​         

查看mariadb版本

\# rpm -qa | grep mariadb

mariadb-libs-5.5.52-1.el7.x86_64

 

根据查询的结果进行卸载

\# rpm -e --nodeps mariadb-libs-5.5.52-1.el7.x86_64

 

**安装mysql 5.7.16**

安装依赖包

\# rpm -ivh libaio-0.3.107-10.el6.x86_64.rpm

\# rpm -ivh net-tools-2.0-0.22.20131004git.el7.x86_64.rpm

 

\# tar -xvf mysql-5.7.16-1.el7.x86_64.rpm-bundle.tar 

// 依次执行（几个包有依赖关系，所以执行有先后）

\# rpm -ivh mysql-community-common-5.7.16-1.el7.x86_64.rpm

\# rpm -ivh mysql-community-libs-5.7.16-1.el7.x86_64.rpm

\# rpm -ivh mysql-community-client-5.7.16-1.el7.x86_64.rpm

\# rpm -ivh mysql-community-server-5.7.16-1.el7.x86_64.rpm

 

**数据库初始化**

为了保证数据库目录为与文件的所有者为 mysql 登陆用户，如果你是以 root 身份运行 mysql 服务，需要执行下面的命令初始化

\# mysqld --initialize --user=mysql

这里会生成一个 root 账户密码，密码在log文件里

\# cat /var/log/mysqld.log 

2018-08-04T11:21:44.416489Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).

2018-08-04T11:21:46.473532Z 0 [Warning] InnoDB: New log files created, LSN=45790

2018-08-04T11:21:46.789344Z 0 [Warning] InnoDB: Creating foreign key constraint system tables.

2018-08-04T11:21:46.863034Z 0 [Warning] No existing UUID has been found, so we assume that this is the first time that this server has been started. Generating a new UUID: 92ee6f62-97d8-11e8-aaf6-000c291f2bd2.

2018-08-04T11:21:46.864941Z 0 [Warning] Gtid table is not ready to be used. Table 'mysql.gtid_executed' cannot be opened.

2018-08-04T11:21:46.866926Z 1 [Note] A temporary password is generated for root@localhost: ON&Dea*5G4,g

 

此时的数据库密码为：) ON&Dea*5G4,g

kpQ;XkyMD57s

现在启动mysql数据库

\# systemctl start mysqld.service

登录数据库：

\# mysql -uroot -p

Enter password: 

mysql> show databases;

ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.

 

**修改用户密码**

mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'iflytek';

// 以前的 password()函数将会被抛弃，官方建议使用上面的命令来修改密码

 

**调整mysql****配置文件my.cnf**

\# vim /etc/my.cnf

 

在 [mysqld] 配置节下追加以下内容：        

default-storage-engine=INNODB

character-set-server=utf8

collation-server=utf8_general_ci

lower_case_table_names=1

max_connections=10000

sql-mode="ALLOW_INVALID_DATES"

max_allowed_packet=16M

bind-address=0.0.0.0

// bind-address也可以绑定真实的ip地址

 

重启mysql服务

\# systemctl restart mysqld.service

 

**赋予远程访问权限**

\# mysql -u root -p

 

mysql>  GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'iflytek' WITH GRANT OPTION;

mysql>  flush privileges;

 

若重启失败，检查数据库日志

\# tail -100f  /var/log/mysqld.log

 

☆  验证mysql主机访问密码是否过期

mysql> use mysql;

mysql> select host,user,password_expired from user;

 

password_expired ：y说明密码已经过期，可以改成N,就是未过期

如果过期，使用以下命令进行更改：

mysql> update mysql.user set password_expired='N';

mysql> flush privileges;

 

 

### ZooKeeper

 

Zookkkper设置超级管理员权限446：Neau2018.--

zookeeper授权

 

如果以上配置完成后zookeeper仍然无法启动，则查看/etc/hosts文件，检查主机名与主机ip是否正确映射了，若没有则加上映射，如下图中最后一行所示：

 

![4](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image008.png)

 

//格式为IP 主机名

 

### Tomcat

 

#### 自己上传业务包到tomcat(建议动手能力强的操作)

我们有4个工程 可以放在一个服务器下，也可以放在多台机器上，这里我们分别放在两台服务器上。

 

Flyjava-sso     端口8083  单点登录系统 服务层 (能力实现，操作数据库)

Flyjava-sso-web 端口8084  单点登录系统 web层 (展示登录页面，与用户交互)

 

Flyjava-data     端口8085  数据中心 服务层  

Flyjaba-data-web 端口8086  数据中心 web层

 

这里我将按照以下方式安装

【保密】  服务层（首先启动）

​      Flyjava-sso     端口【保密】

Flyjava-data     端口【保密】 

192.168.25.114  表现层（在服务层启动后，在启动）

Flyjava-sso-web 端口【保密】

Flyjaba-data-web 端口【保密】

 

#### 使用修改后端口的tomcat工程（不需要安装tomcat以及上传工程）

如果直接我给的文件，那么tomcat都不要安装了，我那个就是将工程放在tomcat里面并修改了端口，并给出了启动多个tomcat的脚本。

1.将表现层的tomcat-flyjava文件夹复制到/usr/local下

2.进入tomcat-flyjava目录下，给*.sh文件授权

 Chmod 775 ./*.sh

3.进入tomcat-flyjava里面的两个tomcat 的bin路径下，给里面的*.sh文件授权（tomcat我改成端口名字了。 8082 8088 这两个tomcat里面放的默认的文件，没有使用）

 

4.同样的步骤，将表现层的tomcat-flyjava文件夹复制到 另一台机器上，并给里面所有的.sh文件授权。

5.在tomcat-flyjava路径下 启动多个tomcat 

./start-flyjava.sh   启动脚本

./stop-flyjava.sh   关闭脚本（注意，一旦tomcat发生错误，都会导致脚本不能完全关闭tomcat 需要用 ps –ef|grep tomcat 查询还在运行的tomcat，接着使用kill -9 PID进程号 杀掉进程）

#### 注意：

我们的代码的配置外置了，每一台机器都会在 /conf/flyjava路径下读取resource.properties这个配置文件。建议修改后，复制到相应的机器上。

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image013.jpg)

### Dubbo监控中心

我们需要将dubbo-admin.war 放在一个tomcat下，建议解压后放在tomcat/webapps/ROOT下，这样就可以IP:端口访问了，不需要加文件名了。

 

如果这台机器8080被使用了，那就改端口，这里我们改成9090端口

我们需要一个tomcat启动dubbo的监控中心，dubbo监控中心需要连接我们的zookeeper,所以为了使其能连接上，我们需要更改监控中心的配置。

 

 

#### 注意

如果直接复制我给的tomcat.zip 那就直接将解压的tomcat 文件夹复制到装有zookeeper的机器上/usr/local下，检查tomcat/bin的.sh文件是否权限，没有权限执行不了，

添加权限

cd /usr/local/tomcat/bin

 chmod 775 ./*.sh

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image014.png)

### Redis

 

### Nginx

参考nginx.conf 修改策略

| upstream [sso.flyjava.com](http://sso.flyjava.com/) {          server 【保密】;       }       upstream   [data.flyjava.com](http://data.flyjava.com/) {          server 【保密】;       }           upstream   [www.flyjava.com](http://www.flyjava.com/) {          server 【保密】;       }       server   {           listen       80;           server_name  [sso.flyjava.com](http://sso.flyjava.com/);            location = / {                   index   index.html;           }           location   / {               proxy_pass   [http://sso.flyjava.com](http://sso.flyjava.com/);                      }       }           server   {           listen       80;           server_name  [data.flyjava.com](http://data.flyjava.com/);           location   = / {                   index   index.html;           }           location   / {               proxy_pass   [http://data.flyjava.com](http://data.flyjava.com/);                      }       }       server   {           listen       80;           server_name  [www.flyjava.com](http://www.flyjava.com/) [flyjava.com](http://flyjava.com/);           location   = / {                   index   index.html;           }           location   / {               proxy_pass   [http://www.flyjava.com](http://www.flyjava.com/);               index  index.html   index.htm;           }       } |
| ------------------------------------------------------------ |
|                                                              |

 

### 数据库初始化

l  使用连接工具Navicat for MySQL  登陆mysql

l  新建数据库flyjava

l  导入数据库初始脚本（flyjava.sql.sql）

 

# 启动之前检查一下：

1.  确保mysql开启远程访问权限，创建了数据库，并执行了初始化脚本

2.  Redis数据库启动了，并设置了密码

3.  Zookeeper 启动了，并且设置/dubbo目录，并增加了446用户和密码

4.  启动dubbo监控中心的tomcat,确保可以用446和密码登录上去。，后期我们需要在该网页观看服务层工程和表现层启动是否成功。

# 第一步：172.16.74.209-学校-监控缓存  服务器

1.1. 启动Zookeeper

/usr/local/zookeeper/zookeeper-3.4.12/bin/zkServer.sh start

查看Zookeeper启动状态

/usr/local/zookeeper/zookeeper-3.4.12//bin/zkServer.sh status

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image015.png)

​      1.2启动Redis数据库

​       /usr/local/redis/bin/redis-server /usr/local/redis/bin/redis.conf

​       查看Redis是否启动

​      ps -ef |grep redis

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image016.png)

​      1.3启动tomcat 

​      /usr/local/tomcat/bin/startup.sh

​      查看启动状态

打开浏览器输入 <http://【保密】:9090>    用户名密码 【保密】 【保密】

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image018.jpg)

# 第二步：【保密】-学校-mysql 服务器 重启自动运行mysql数据库

# 第三步：【保密】-学校-web-服务层  服务器（需要切换到普通用户权限登录446）

/usr/local/tomcat-flyjava/start-flyjava.sh

查看，服务层是否启动成功，在<http://【保密】9090>  查看

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image020.jpg)

# 第四步：【保密】-学校-Nginx-web表现层 服务器（需要切换到普通用户权限登录446）

/usr/local/tomcat-flyjava/start-flyjava.sh

查看，服务层是否启动成功，在<http://【保密】:9090>  查看

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image022.jpg)

启动Nginx （普通用户启动Nginx需要用sudo命令获取root权限）

sudo /usr/local/nginx/sbin/nginx

输入密码

查看是否启动成功

 ps -ef|grep nginx

![img](file:///C:/Users/彭童鞋/AppData/Local/Temp/msohtmlclip1/01/clip_image024.jpg)

 

完成所有软件的启动。

访问ck.neau.edu.cn

## 注意：

如果没有域名 没有安装nginx的话，可以用IP:8086/index.html进行访问数据中心。

 

 
