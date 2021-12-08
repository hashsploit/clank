@echo off
setlocal

REM Change directory to the current script directory
set DIR=%~dp0
cd %DIR%

set SCRIPT=%~0

IF "%~1" == "clean" GOTO clean
IF "%~1" == "proto" GOTO compile_protobufs
IF "%~1" == "pt" GOTO compile_protobufs
IF "%~1" == "build" GOTO build
IF "%~1" == "jar" GOTO build
IF "%~1" == "run" GOTO exec
goto menu

:check_installed
	where %~1 2>nul
	if %ERRORLEVEL% NEQ 0 (
		set /A "%~2 = 1"
	) else (
		set /A "%~2 = 0"
	)
exit /B 0

:require_installed
	call :check_installed %~1, status
	if %status% == "0" (
		echo %~1 ^(%~2^) is not installed, please install it!
		exit /B 100
	)
exit /B 0

:clean
	call :require_installed "java", "Java"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	call :require_installed "mvn", "Maven"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	
	echo Cleaning ...
	call mvn clean
	rmdir target
exit /B 0

:compile_protobufs
	call :require_installed "java", "Java"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	call :require_installed "mvn", "Maven"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	
	echo Building protocol-buffers ...
	call mvn clean protobuf:compile
exit /B 0

:build
	call :require_installed "java", "Java"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	call :require_installed "mvn", "Maven"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	
	echo Building ...
	call mvn clean compile assembly:single
	
	REM Copy final jar to root path
	copy /D /Y "target\clank-*.jar" /B "clank.jar" /B > nul
exit /B 0

:exec
	call :require_installed "java", "Java"
	if %ERRORLEVEL% NEQ 0 exit /B %ERRORLEVEL%
	
	REM Execute server
	set INIT_MEM=256m
	set MAX_MEM=1024m
	set JAR=clank.jar
	
	REM Check if the jar file exists
	if not exist %DIR%\%JAR% (
		echo The jar file %JAR% does not exist! Run '%SCRIPT% build' first.
		goto :eof
	)
	
	REM Remove 1st parameter from %*
	set _tail=%*
	call set _tail=%%_tail:*%1=%%
	
	REM Run server
	java -server -XX:+UseG1GC -Xms%INIT_MEM% -Xmx%MAX_MEM% -jar %DIR%\%JAR% %_tail%
exit /B 0





REM Menu
:menu
	echo =====================
	echo Clank Dev Script Tool
	echo =====================
	echo.
	echo  Commands:
	echo   * clean ................ Clean-up the development environment
	echo   * proto, pt ............ Compile the protocol-buffer sources
	echo   * build, jar ........... Compile/build clank into an executable jar file
	echo   * run ^<JSON_CONFIG^> .... Run clank with the JSON_CONFIG provided
goto :eof

