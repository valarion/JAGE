@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  JAGEcore startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and JAG_ECORE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Djava.library.path=./natives"

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\JAGEcore.jar;%APP_HOME%\lib\JAGEPluginSystem.jar;%APP_HOME%\lib\slick2d-core-1.0.1.jar;%APP_HOME%\lib\slf4j-api-1.7.12.jar;%APP_HOME%\lib\lwjgl-2.9.1.jar;%APP_HOME%\lib\jorbis-0.0.17.jar;%APP_HOME%\lib\lwjgl-platform-2.9.1-natives-windows.jar;%APP_HOME%\lib\lwjgl-platform-2.9.1-natives-linux.jar;%APP_HOME%\lib\lwjgl-platform-2.9.1-natives-osx.jar;%APP_HOME%\lib\jinput-2.0.5.jar;%APP_HOME%\lib\jutils-1.0.0.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-linux.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-windows.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-osx.jar

@rem Execute JAGEcore
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %JAG_ECORE_OPTS%  -classpath "%CLASSPATH%" com.valarion.gameengine.core.GameCore %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable JAG_ECORE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%JAG_ECORE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
