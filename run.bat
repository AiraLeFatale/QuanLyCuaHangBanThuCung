@echo off
REM ============================================================
REM  Bien dich va chay ung dung Quan Ly Cua Hang Ban Thu Cung
REM  Yeu cau: JDK 17 tro len (khuyen nghi JDK 21)
REM ============================================================
chcp 65001 >nul
setlocal

if not exist out mkdir out
dir /s /b src\*.java > out\sources.txt

echo [1/2] Dang bien dich...
javac -encoding UTF-8 -d out @out\sources.txt
if errorlevel 1 (
    echo Bien dich THAT BAI.
    exit /b 1
)

echo [2/2] Dang chay...
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8 -cp out petshop.app.Main %*

endlocal
