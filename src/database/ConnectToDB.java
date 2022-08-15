package database;

import com.mongodb.MongoClient;

/**
 * Connect Database on 127.0.0.1
 * @author Yiwei Yao
 *
 */
public class ConnectToDB {

	private ConnectToDB() {};

	public static MongoClient mongoClient = new MongoClient( "127.0.0.1" );

	//added by Bang Tran
	protected void finalize(){
		mongoClient.close();
	}

}
