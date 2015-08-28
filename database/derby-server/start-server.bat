set CLASSPATH=derby.jar;derbynet.jar
java -cp %CLASSPATH% org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527
pause