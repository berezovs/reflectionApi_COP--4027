del *.class
del *.dat
javac Main.java 
java -classpath derby.jar;. Main database.properties
