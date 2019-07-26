db = db.getSiblingDB('dbLibrary');

print("You are now connected to database : " + db);

db.Collection_Student.drop();

student1 = 	
		{ 
			"Student_Num"	: "S001",
			"First_Name"	: "Ronald",			 
			"Last_Name"		: "DeDonald",
			"Addr_Line1"    : "25 Orchard Road",			 
			"City"     	 	: "London",			 
			"Postcode"      : "E8 4PQ",
			"DOB"  			: new Date("03/09/1985"),
			"email"      	: "rondald.dedonald@glos.ac.uk"
		};

           
student2 = 
		{
            "Student_Num"   : "S002",
			"First_Name"    : "Steve",
			"Last_Name"     : "Davies",
            "Addr_Line1"    : "154 Yew Tree Way",
			"City"     	 	: "Cheltenham",
			"Postcode"      : "GL51 4PQ",
            "DOB"  			: new Date("06/10/1995"),
			"email"      	: "steven.davies@glos.ac.uk"
		};
		
student3 = 	
		{ 
			"Student_Num"	: "S003",
			"First_Name"	: "George",			 
			"Last_Name"		: "Porge",
			"Addr_Line1"    : "25 Brunswick Road",			 
			"City"     	 	: "Leeds",			 
			"Postcode"      : "LD3 7RU",
			"DOB"  			: new Date("17/10/1988"),
			"email"      	: "george.porge@glos.ac.uk"
		};

           
student4 = 
		{
            "Student_Num"   : "S004",
			"First_Name"    : "Terry",
			"Last_Name"     : "Walsh",
            "Addr_Line1"    : "12 Oak Road",
			"City"     	 	: "Newport",
			"Postcode"      : "NP10 9EU",
            "DOB"  			: new Date("01/01/1990"),
			"email"      	: "terry.walsh@glos.ac.uk"
		};		

           

db.Collection_Student.insert(student1);
db.Collection_Student.insert(student2);
db.Collection_Student.insert(student3);
db.Collection_Student.insert(student4);

cursor = db.Collection_Student.find();
cursor.forEach(printjson);







