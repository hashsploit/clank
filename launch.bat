@echo off
cls

REM Set program arguments
set INIT_MEM=512m
set MAX_MEM=1024m
set JAR=clank.jar

REM Change directory to the current script directory
set DIR=%~dp0
cd %DIR%

REM Check if the jar file exists
if not exist %DIR%\%JAR% (
	echo The jar file %JAR% does not exist! Run build.bat first.
	goto :eof
)

REM Execute server
java -server -XX:+UseG1GC -Xms%INIT_MEM% -Xmx%MAX_MEM% -jar %DIR%\%JAR% %*

