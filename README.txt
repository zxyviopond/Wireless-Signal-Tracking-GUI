	
	
	I. Content
	
	-A Simple Space Database Application
	
	-Display wifi hot point(all or in a selected section)
	
	-Finding nearset hot point based on users' location
	

	II. Design

	-AP table: store location and radius of wifi hot point

	-BUILDING table: building(as a polypon) location

	-PEOPLE table: people location(2-dimension)

	


	III. File

	-ap.xy people.xy input data file
	
	-CREATEDB DROPDB SQL file to create or drop database table
	
	-spaceDB.java 
 	
	-populate.java get input and write it in db

	
	
	V.Run

	-javac -classpath classes111.jar;sdoapi.zip;SWT.jar;. SpaceDB.java
	
	-java -classpath classes111.jar;sdoapi.zip;SWT.jar;. SpaceDB
