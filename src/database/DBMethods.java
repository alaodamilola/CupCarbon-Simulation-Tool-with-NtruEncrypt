package database;

import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * help methods for database operations
 * @author Yiwei Yao
 *
 */

public class DBMethods {

	static MongoClient  mongo = ConnectToDB.mongoClient;

	/** get Database
	 * @param dbName
	 * @return
	 */
	public static MongoDatabase getDB(String dbName) {
		return mongo.getDatabase(dbName);
	}

	/**
	 * get a list of collections in database
	 * @param db
	 * @return
	 */
	public static MongoIterable<String> getCollections(MongoDatabase db) {
		return db.listCollectionNames();
	}


	/** 
	 * get the number of results in a collection
	 * @param collection
	 * @return
	 */
	public static long collectionsCount(MongoCollection<Document> collection) {
		return collection.count();
	}

	/**
	 * select a collection base on name and return it
	 * @param db
	 * @param collection
	 * @return
	 */
	public static MongoCollection<Document> getCollection(MongoDatabase db, String collection) {
		return db.getCollection(collection);
	}

	/**
	 * read collections document base on collection name and return it.
	 * @param collection
	 * @return
	 */
	public static FindIterable<Document> find(MongoCollection<Document> collection) {
		return collection.find();
	}

	/**
	 * Find document with certain prefix with certain collection name, prefix name
	 * @param collection
	 * @param prefix
	 * @return
	 */
	public static FindIterable<Document> findWithPrefix(MongoCollection<Document> collection, String prefix) {
		BasicDBObject query = new BasicDBObject();
		query.put("prefix", prefix);
		FindIterable<Document> data = collection.find(query);
		return data;
	}

	/**
	 * drop collection
	 * @param collection
	 */
	public static void dropCollection(MongoCollection<Document> collection) {
		collection.drop();
	}


	/**
	 * Empty collection
	 * @author Bang Tran
	 */
	// empty collection
	public static void emptyCollection(MongoCollection<Document> collection) {
		collection.deleteMany(new BasicDBObject());
	}
	//==== Bang Tran End


	// write operation
//	public static void insert(Document document, MongoCollection<Document> collection) {
//		collection.insertOne(document);
//	}
}
