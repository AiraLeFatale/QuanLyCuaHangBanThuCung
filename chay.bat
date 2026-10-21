@echo off
chcp 65001 >nul
if not exist out mkdir out
javac -encoding UTF-8 -d out src\inter\*.java src\entity\*.java src\manager\*.java src\main\*.java
if errorlevel 1 pause & exit /b
java -cp out main.Main
pause
