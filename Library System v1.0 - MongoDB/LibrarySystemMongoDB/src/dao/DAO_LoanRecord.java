/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;

/**
 *
 * @author arron
 */
public class DAO_LoanRecord {
    
    /**
     *  This method returns a book document object, that is specified by the ISBN parameter.
     * 
     * @param isbn The unique identifier of the book object
     * @return A book document object associated with the ISBN parameter
     */
    public Document getBook(String isbn) {
       
       MongoCollection<Document> collection = getMongoCollection("Collection_Book");
        
        // Return book object from an ISBN input String 
        Document book = collection.find(eq("Book_ISBN", isbn)).first();
        
        return book;
    }
    
    /**
     *  This method accepts a parameter of a student number, and returns the student document object associated with said student number.
     * 
     * @param studentNumber A unique student number 
     * @return A student document object related to the student number parameter
     */
    public Document getStudent(String studentNumber) {
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Student");
        
        // get a specific student details 
        Document student = collection.find(eq("Student_Num", studentNumber)).first();
        
        return student;
    }
    
    /**
     *  This method creates a new loan document, within the Loan Collection of MongoDB.
     * 
     * @param loan The loan that is wished to be created with the Loan Collection
     */
    public void createLoan(Document loan) {
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Loan");

        collection.insertOne(loan);
        
    }
    
    
    /**
     * This method returns the entire loan history of any given student, for the time-frame specified from the date parameters parsed.
     * 
     * @param studentNumber A unique student number to identify the student
     * @param fromDate The date that will determine the day at which the returned history will start at.
     * @param toDate The date that will determine the day at which the returned history will end at.
     * @return The loan history, as a FindIterable array of Documents.
     */
    public FindIterable<Document> getLoanHistory(String studentNumber, Date fromDate, Date toDate ){
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Loan");
        
        String formattedFromDate = new SimpleDateFormat("MM/dd/yyyy").format(fromDate);
        String formattedToDate = new SimpleDateFormat("MM/dd/yyyy").format(toDate);
        
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("Student_Num", studentNumber);
        searchQuery.append("Date_Issued", new BasicDBObject("$gte", formattedFromDate));
        searchQuery.append("Date_Issued", new BasicDBObject("$lte", formattedToDate));
        
        BasicDBObject query = new BasicDBObject();
        query.put("Date_Issued", BasicDBObjectBuilder.start("$gte", formattedFromDate).add("$lte", formattedToDate).get());
        query.put("Student_Num", BasicDBObjectBuilder.start("$eq", studentNumber).get());
        
        FindIterable<Document> loanHistory = collection.find(query).sort(new BasicDBObject("Date_Issued", -1));
        
        return loanHistory;
    }
    
    /**
     *  This static final String, is a hard-coded JavaScript function, stored as a static final String. It is the "Map" function part of the MapReduce function. Its job is to Map all of the fines together, and Map all of the amounts paid together also.
     */
    public static final String mapfunction = "function () {var finesPaidTotal = 0;if(this.Fines_Paid != null && this.Fines_Paid != 'undefined'){for(var i = 0; i < this.Fines_Paid.length; i++ ){if(this.Fines_Paid.indexOf(this.Fines_Paid[i]) === i) {finesPaidTotal = finesPaidTotal + this.Fines_Paid[i].Amount_Paid;}} }emit(this.Fines_Paid, finesPaidTotal);}";

    /**
     * This static final String, is a hard-coded JavaScript function, stored as a static final String. It is the "Reduce" function part of the MapReduce function. Its job is to Reduce all of the amounts paid together, and return the total amount.
     */
    public static final String reducefunction = "function(key, values) { return Array.sum(values); }";
 
    /**
     * This method returns the entire fine history of any given student, for the time-frame specified from the date parameters parsed.
     * 
     * @param studentNumber A unique student number to identify the student
     * @param fromDate The date that will determine the day at which the returned history will start at.
     * @param toDate The date that will determine the day at which the returned history will end at.
     * @return The fine history, as a MongoCursor.
     */
    public MongoCursor getFineHistory(String studentNumber, Date fromDate, Date toDate ){
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Loan");
        
        String formattedFromDate = new SimpleDateFormat("MM/dd/yyyy").format(fromDate);
        String formattedToDate = new SimpleDateFormat("MM/dd/yyyy").format(toDate);
        
        BasicDBObject query = new BasicDBObject();
        query.put("Date_Issued", BasicDBObjectBuilder.start("$gte", formattedFromDate).add("$lte", formattedToDate).get());
        query.put("Student_Num", BasicDBObjectBuilder.start("$eq", studentNumber).get());
        
        MapReduceIterable iterable = collection.mapReduce(mapfunction, reducefunction)
                .filter(query)
                .sort(new BasicDBObject("Date_Issued", -1));
        
        MongoCursor cursor = iterable.iterator();
        
        return cursor;
        
    }
    
    /**
     *  This method inserts a book as a returned book, into the loan record specified at the user input.
     *
     * @param loanRecord The loan record that the book being returned is associated with.
     * @param bookIsbn The ISBN of the book that is being returned.
     */
    public void returnBook(String loanRecord, String bookIsbn) {
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Loan");
        
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("Loan_Record_Num", loanRecord);
        searchQuery.append("Loan_Books.Book_ISBN", bookIsbn);
        
        String formattedDateReturned = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        
        collection.updateOne(new Document(searchQuery),
                new Document("$addToSet", 
                new Document("Returned_Books", 
                new Document("Book_ISBN", bookIsbn)
                .append("Returned_Date", formattedDateReturned))));
    }
    
    /**
     *  This method inserts a fine document, associated with an already loaned loan record and book.
     * 
     * @param loanRecord The loan record associated with the fine
     * @param isbn The ISBN of the book associated with the fine
     * @param amountPaid The amount of fine that is being paid
     */
    public void payFine(String loanRecord, String isbn,  double amountPaid) {
        
        MongoCollection<Document> collection = getMongoCollection("Collection_Loan");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("Loan_Record_Num", loanRecord);
        searchQuery.append("Loan_Books.Book_ISBN", isbn);
        
        String formattedDatePaid = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        collection.updateOne(new Document(searchQuery),
                new Document("$addToSet", 
                new Document("Fines_Paid", 
                new Document("Book_Loaned", isbn)
                .append("Amount_Paid", amountPaid)
                .append("Date_Paid", formattedDatePaid))));

    }
    
    /**
     * This method creates a connection to MongoDB, and returns the collection requested with the input parameter
     * @param collectionName The name of the collection wished to be returned
     * @return The MongoCollection requested
     */
    public MongoCollection<Document> getMongoCollection(String collectionName){
        
        //Instansiate new Mongo Client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );   

        // Accessing the database 
        MongoDatabase database = mongo.getDatabase("dbLibrary"); 

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection(collectionName);
        
        return collection;
        
    }



}
