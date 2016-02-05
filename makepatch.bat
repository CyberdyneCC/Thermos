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
	call :pr "Diff of "
	call :red "%CLEAN%"
	call :pr " and "
	call :green "%CAULD%"
	call :pr " written to " 
	call :yellow "%PATCH%"
)
goto :eof

:blue
powershell -Command Write-Host -NoNewline "%*" -foreground "blue" -background "black"
goto :eof

:yellow
powershell  -Command Write-Host -NoNewline "%*" -foreground "yellow" -background "black"
goto :eof

:red
powershell -Command Write-Host -NoNewline "%*" -foreground "red" -background "black"
goto :eof

:green
powershell -Command Write-Host -NoNewline "%*" -foreground "green" -background "black"
goto :eof

:pr
<nul set /p=%*
goto :eof

:eof