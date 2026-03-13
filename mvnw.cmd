@echo off
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with a '@' or write a 'if "%MAVEN_BATCH_ECHO%" == "on" echo on'
@if "%MAVEN_BATCH_ECHO%" == "on" echo on
@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible wrapper scripts, use a different variable name.
set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@if "%MAVEN_PROJECTBASEDIR%" == "" set MAVEN_PROJECTBASEDIR=%~dp0

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@REM check for pre script, once with legacy .bat file and once with .cmd file
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat"
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd"
:skipRcPre

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.
goto error

@REM ==== END VALIDATION ====

:init

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.

set MAVEN_PROJECTBASEDIR=%~dp0
:findBaseDir
if exist "%MAVEN_PROJECTBASEDIR%\.mvn" goto baseDirFound
set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%..\"
if "%MAVEN_PROJECTBASEDIR%" == "..\" goto baseDirNotFound
goto findBaseDir

:baseDirFound
set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%
goto initMaven

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%~dp0
goto initMaven

:initMaven
set MAVEN_JAR_PATH=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
if exist "%MAVEN_JAR_PATH%" goto runMaven

echo.
echo Error: Could not find or load main class org.apache.maven.wrapper.MavenWrapperMain
echo.
goto error

:runMaven
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
set WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

set CLASSPATH=%MAVEN_JAR_PATH%

"%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% -classpath "%CLASSPATH%" %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_BATCH_PAUSE%" == "on" goto mainEnd
pause
:mainEnd

if not "%MAVEN_TERMINATE_CMD%" == "" exit %ERROR_CODE%

exit /b %ERROR_CODE%
