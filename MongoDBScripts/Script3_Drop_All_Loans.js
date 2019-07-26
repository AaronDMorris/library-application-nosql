db = db.getSiblingDB('dbLibrary');

print("You are now connected to database : " + db);

db.Collection_Loan.drop();

cursor = db.Collection_Loan.find();
cursor.forEach(printjson);





