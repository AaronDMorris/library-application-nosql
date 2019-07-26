db = db.getSiblingDB('dbLibrary');

print("You are now connected to database : " + db);

db.Collection_Book.drop();


var book1 = {
    "_id": 1,
    "Book_ISBN": "9781118051221",
    "Book_Name": "Beginning Programming All-In-One Desk Reference For Dummies"
};
		
var book2 = {
    "_id": 2,
    "Book_ISBN": "9781409599340",
    "Book_Name": "Coding for Beginners Using Python "
};
		
var book3 = {
    "_id": 3,
    "Book_ISBN": "9781259589317",
    "Book_Name": "Java: A Beginner's Guide, Seventh Edition"
};
		
var book4 = {
    "_id": 4,
    "Book_ISBN": "9781840786422",
    "Book_Name": "Coding for Beginners in easy steps - basic programming for all ages"
};
		
db.Collection_Book.insert(book1);

db.Collection_Book.insert(book2);

db.Collection_Book.insert(book3);

db.Collection_Book.insert(book4);

cursor = db.Collection_Book.find();
cursor.forEach(printjson);







