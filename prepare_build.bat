@echo off
setlocal enabledelayedexpansion

REM Get command line argument for the processing base directory
set PROCESSING_DIR=%1

REM Validate the directory exists
if not exist "%PROCESSING_DIR%" (
    echo The provided directory does not exist: %PROCESSING_DIR%
    exit /b 1
)

REM Read version from the version.txt file
if exist "%PROCESSING_DIR%\lib\version.txt" (
    for /f "delims=" %%i in (%PROCESSING_DIR%\lib\version.txt) do set PROCESSING_VERSION=%%i
) else (
    echo version.txt not found in the provided directory: %PROCESSING_DIR%\lib\version.txt ?!
    exit /b 1
)
echo Processing version is: %PROCESSING_VERSION%

REM Split the version into its segments
for /f "tokens=1-3 delims=." %%a in ("%PROCESSING_VERSION%") do (
    set MAJOR_VERSION=%%a
    set MINOR_VERSION=%%b
    set PATCH_VERSION=%%c
)

if not defined PATCH_VERSION set PATCH_VERSION=0

REM Check the version segments
if %MAJOR_VERSION% lss 4 (
    echo The detected version is less than 4.2!
    exit /b 1
)
if %MAJOR_VERSION% equ 4 (
    if %MINOR_VERSION% lss 2 (
        echo The detected version is less than 4.2!
        exit /b 1
    )
)

echo Processing version version is valid: %MAJOR_VERSION%.%MINOR_VERSION%.%PATCH_VERSION%"

REM Check if lib/pde.jar exists in the given directory
if not exist "%PROCESSING_DIR%\lib\pde.jar" (
    echo pde.jar not found in the provided directory: %PROCESSING_DIR%\lib\pde.jar
    exit /b 1
)
echo found Processing pde.jar "

REM Check if core/library/core.jar exists in the given directory
if not exist "%PROCESSING_DIR%\core\library\core.jar" (
    echo core.jar not found in the provided directory: %PROCESSING_DIR%\core\library\core.jar
    exit /b 1
)
echo found Processing core.jar "

REM Install pde.jar to the local maven repository
pushd %TEMP%
call mvn -N install:install-file -Dfile="%PROCESSING_DIR%\lib\pde.jar" -DgroupId=processing.org -DartifactId=pde-jar -Dversion=%PROCESSING_VERSION% -Dpackaging=jar
if %ERRORLEVEL% neq 0 (
	echo Error during mvn install:install-file!
	exit /b 1
)    
REM Install core.jar to the local maven repository
call mvn -N install:install-file -Dfile="%PROCESSING_DIR%\core\library\core.jar" -DgroupId=processing.org -DartifactId=core-jar -Dversion=%PROCESSING_VERSION% -Dpackaging=jar
if %ERRORLEVEL% neq 0 (
	echo Error during mvn install:install-file!
	exit /b 1
)    
popd
echo Both JAR files installed successfully!

echo try to update pom version from build.properties!

SET PROP_FILE=path_to_your_file.properties

:: Variable to store the version
SET VERSION_VALUE=

REM Read properties file and extract version
FOR /F "tokens=* delims=" %%A IN (build.properties) DO (
    SET line=%%A
    IF NOT "!line:tool.prettyVersion=!"=="!line!" (
        FOR /F "tokens=2 delims==" %%B IN ("!line!") DO SET VERSION_VALUE=%%B
    )
)

REM Check if version is set
IF NOT "%VERSION_VALUE%"=="" (
	echo set version %VERSION_VALUE% to pom.xml 
    call mvn versions:set -DnewVersion=%VERSION_VALUE%
	if %ERRORLEVEL% neq 0 (
    	echo Error during mvn install:install-file!
    	exit /b 1
	)    
) ELSE (
    echo "Version not found in properties file?!"
)

endlocal
