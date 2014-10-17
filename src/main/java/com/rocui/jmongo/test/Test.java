package test;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Test {

    public static void main(String[] args) {
        try {
            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("company");
            DBCollection collection = db.getCollection("employees");
            BasicDBObject employee = new BasicDBObject();
            employee.put("name", "Hannah");
            employee.put("no", 2);
            collection.insert(employee);
            BasicDBObject searchEmployee = new BasicDBObject();
            searchEmployee.put("no", 2);
            DBCursor cursor = collection.find(searchEmployee);
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
            System.out.println("The Search Query has Executed!");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
