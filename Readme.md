File Format Metadata Aggregator - FFMA
======================================

*The LOD metadata aggregation tool*

Installation Guide
------------------

### Requirements

To install you need:

* Java 7.0
* Maven 2.0
* MongoDB
* Git
* Eclipse (Version: Mars Service Release 1) can support you by project configuration

### Download

| Version | Size   | SHA1                                                    |                      |
|---------|--------|---------------------------------------------------------|----------------------|
| v1.0    | 23.6 MB| <small>1b34d11f66fe4b847f271a5aaaabb02c7917690c</small> |[download](https://github.com/ait-ngcms/ffma/archive/master.zip)            |

### Installing Fingerdet

#### Use Eclipse to create Java Maven project from github sources.
1. Check out project sources from github e.g. using TortoiseGit
2. Build Maven project using Ecliplse menu: File -> Import -> Maven -> Existing Maven Projects

#### Use Eclipse to create WAR file. 

1. First compile your project
2. Right click on your PROJECT -> Export -> WAR


### VM management

iptables prerouting was necessary to resolve ffma domain name

#### Deployment
{domain-name}/manager.html -> possible upload new WAR file for preservation-riskmanager.war or directly using e.g. WinScp
/usr/local/tomcat/webapps/preservation-riskmanagement.war

#### Mongo start: run_mongo.sh -> install/mogodb_linux/bin
rm mongod.lock

#### Tomcat start: etc/init.d/tomcat stop/start
see /usr/local/tomcat/logs
e.g. cat tail catalina.out
Tomcat users are defined in server.xml -> tomcat.users.xml -> pwd user


Using FFMA
--------------

### Rest API

http://localhost:8983/ffma/component
http://localhost:8983/ffma/services/preservation-riskmanagement
http://{domain-name}/preservation-riskmanagement/
http://{domain-name}/common/rest/collection/list

a. http://{domain-name}/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/AIT/true/true 
                (As a response you will get created collections names like: AitFileFormat;LODFormat;LODSoftware;DipFormatId;DipSoftwareId;LODVendor;DipVendorId;)

b. http://{domain-name}/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/Pronom/true/true 
(Response: PronomFileFormat;LODSoftware;DipFormatId;DipSoftwareId;)

c. http://{domain-name}/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/Freebase/true/true 
(Response: FreebaseFileFormat;LODFormat;LODSoftware;DipFormatId;DipSoftwareId;LODVendor;DipVendorId;)

d. http://{domain-name}/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/DBPedia/true/true 
(Response: LODFormat;LODSoftware;DipFormatId;DipSoftwareId;LODVendor;DipVendorId;)

### Sample aggregation results (after loading data the following collections were created in mongodb):
AitFileFormat (14)
DBPediaFileFormat (106)
DipFormatId (576)
DipSoftwareId (169)
DipVendorId (18)
FreebaseFileFormat (77)
LODFormat (45)
LODSoftware (317)
LODVendor (19)
PronomFileFormat (977)

CollectionAnalysisReport
MetadataAnalysisResult


Check resulting collections existence using service http://{domain-name}/preservation-riskmanagement/rest/loddataanalysis/checkdataexist. 
   As a response you should get a list of stored collections like (PronomFileFormat;DBPediaFileFormat;FreebaseFileFormat;LODFormat;LODSoftware;LODVendor;DipFormatId;DipSoftwareId;DipVendorId;).

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/AIT/true/true

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/Freebase/true/true

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/Pronom/true/true

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/storeallextensions/DBPedia/true/true

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/checkdataexist

http://localhost:8983/ffma/preservation-riskmanagement/admin.html

http://localhost:8983/ffma/preservation-riskmanagement/

http://localhost:8983/ffma/preservation-riskmanagement/rest/loddataanalysis/checkdataexist


## Features and roadmap

### Version 1.0

* LOD metadata aggregation and presentation


