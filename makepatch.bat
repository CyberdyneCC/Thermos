@echo off

IF [%1]==[] (
    call :red "Please provide a file to generate."
	) ELSE (
	call :blue "CREATING" %PATCH%
	echo "%1"
	FOR /F "delims=" %%i IN ('java makepatch chop %1') DO set FILE=%%i
	echo "%FILE%"
	set CLEAN="eclipse\Clean\src\main\java\%FILE%"
	set CAULD="eclipse\cauldron\src\main\java\%FILE%"
	set PATCH="patches/%FILE%.patch"
    git diff --minimal --no-prefix --ignore-space-at-eol --ignore-blank-lines --no-index %CLEAN% %CAULD% > %PATCH%
	java makepatch %PATCH%
	REM call :green "Diff of ${redf}%CLEAN%${reset} and ${gref}%CAULD%${reset} written to ${yelf}%PATCH%${reset}"
)
goto :eof

:blue
powershell -Command Write-Host "%*" -foreground "blue" -background "black"
goto :eof

:yellow
powershell -Command Write-Host "%*" -foreground "yellow" -background "black"
goto :eof

:red
powershell -Command Write-Host "%*" -foreground "red" -background "black"
goto :eof

:green
powershell -Command Write-Host "%*" -foreground "green" -background "black"
goto :eof

:eof