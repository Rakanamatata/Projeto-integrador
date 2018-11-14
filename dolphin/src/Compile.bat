@ECHO OFF
SET buildpath=%~dp0build
ECHO Compiling source code
javac -d %buildpath% dolphin/*.java
ECHO Done