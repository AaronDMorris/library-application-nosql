db = db.getSiblingDB('dbLibrary');

print("You are now connected to database : " + db);

cursor = db.Collection_Loan.find();
cursor.forEach(printjson);

