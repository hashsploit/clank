@echo off
cls

REM Change directory to the current script directory
cd %~dp0

REM Compile the jar file
echo Building ...
call mvn clean compile assembly:single

REM Copy final jar to root path
copy /D /Y "target\clank-*.jar" /B "clank.jar" /B > nul
echo ^> Build successful^! Press any key to exit.
pause > nul
