*Extract all files from the Zip, prior to following these steps*

MongoDB

1. Serve a version of the MongoDB database with the MongoDB server command. 
(either create a new with the MongoDB scripts, or connect to the DATA (Library System v1.0 - MongoDB\DATA) folder for a pre-built DB)

2. Ensure Student data and Book data is being served from MongoDB
(insert data at: \MongoDBScripts)

3. Open "LibrarySystemMongoDB" as a project within the NetBeans IDE

4. Run the project file, and the application will default to the HomeMenu default class



Oracle

1. Ensure a database is pre-built. Either keep default connection settings to connect to S1408926 or build
a new DB with the script "SQL Library DB Build Scripts\Library_Build_Script_With_Sequences_And_Triggers.sql" and
insert data with the INSERT_*.sql files in the same directory. (CHANGE CONNECTION SETTINGS IF CREATING NEW DB, SEE THE getConnection() method in Oracle JavaDocs)

2. Open "LibrarySystemOracle" as a project within the NetBeans IDE

3. Run the project file, and the application will default to the HomeMenu default class
(dist's directory exe does not connect to external Oracle DB, please ensure project is ran within NetBeans)





*SEE JavaDocs Documentation for detailed Documentation on the methods used within these projects*

