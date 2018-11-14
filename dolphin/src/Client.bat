@ECHO OFF
SET buildpath=%~dp0build
ECHO Compiling source code
javac -d %buildpath% dolphin/*.java
ECHO NAME:
SET /P name=
ECHO IP:
SET /P IP=
java -cp %buildpath% dolphin/Student %name% %ip%