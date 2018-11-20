@ECHO OFF
SET buildpath=%~dp0build
ECHO Compiling source code
mkdir build
javac -d %buildpath% dolphin/*.java
java -cp %buildpath% dolphin/Teacher