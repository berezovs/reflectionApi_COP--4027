del *.class
del *.dat
cls
javac Main.java 
java -classpath derby.jar;. Main database.properties
