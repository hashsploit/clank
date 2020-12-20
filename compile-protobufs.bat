@echo off
cls

REM Change directory to the current script directory
cd %~dp0

REM Compile the jar file
echo Compiling protobuf sources ...
mvn clean protobuf:compile

