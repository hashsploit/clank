@echo off
cls

REM Change directory to the current script directory
cd %~dp0

REM Compile the jar file
echo Building ...
mvn clean compile assembly:single

REM Copy final jar to root path.
copy /B /Y target\clank-*.jar clank.jar

