@echo off  

rem This file provides MongoDb collection import
rem Expected parameters are: 1. Mongo database installation directory which should contain mongoimport.exe file
rem                          2. Host name and port number e.g. localhost:8060
rem                          3. The name of the Mongo database
rem                          4. The name of the Mongo database collection
  
if exist %1/mongoimport.exe goto CHECK_PAR_1  
echo ERROR: The import file "mongoimport.exe" in the directory "%1" not found!  
echo Please check a validity of the Mongo database installation directory.
goto ENDE  

rem This is a path to Mongo database installation directory  
:CHECK_PAR_1	
if %1=="" goto ERR_MESSAGE1  
set mongo_dir=%1  
echo mongo_dir=%mongo_dir%  
goto CHECK_PAR_2

:ERR_MESSAGE2 
echo ERROR: Missing mongo database path parameter 
goto ENDE

rem This is a host name with port number  
:CHECK_PAR_2
if "%2"=="" goto ERR_MESSAGE2  
set hostvar=%2  
echo hostvar=%hostvar%  
goto CHECK_PAR_3

:ERR_MESSAGE2 
echo ERROR: Missing host parameter 
goto ENDE

rem This is a mongo database name  
:CHECK_PAR_3
if "%3"=="" goto ERR_MESSAGE3  
set dbvar=%3  
echo dbvar=%dbvar%  
goto CHECK_PAR_4

:ERR_MESSAGE3 
echo ERROR: Missing database name parameter 
goto ENDE

rem This is a collection name  
:CHECK_PAR_4
if "%4"=="" goto ERR_MESSAGE4  
set collectionvar=%4  
echo collectionvar=%collectionvar%  
goto CHECK_FIELDS

:ERR_MESSAGE4 
echo ERROR: Missing collection name parameter 
goto ENDE
  
:CHECK_FIELDS
if exist %4_fields.txt goto EXECUTE  
echo ERROR: File "%4_fields.txt" not found!  
goto ENDE    

:EXECUTE
rem %1\mongoimport --host %2 --db %3 --collection %4 --drop
%1\mongoimport --host %2 --db %3 --collection %4  --fieldFile %4_fields.txt --file empty_json --headerline --drop

:ENDE  