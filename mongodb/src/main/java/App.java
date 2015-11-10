import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class App {

	public static void main(String[] args) {
		//MongoClientOptions options = new MongoClientOptions().builder().build();

		App a = new App();
//		a.insertTest();
//		a.findTest();
		a.findWithFilterTest();

	}

	private void findWithFilterTest() {
		MongoClient client = new MongoClient();

		MongoDatabase db = client.getDatabase("course");

		MongoCollection<Document> coll = db.getCollection("findWithFilterTest");

		coll.drop();

		for (int i = 0; i < 10; i++) {
			coll.insertOne(new Document()
						.append("x", new Random().nextInt(2))
						.append("y", new Random().nextInt(100)));
		}
//		Bson filter = new Document("x", 0)
//			.append("y", new Document("$gt", 10)
//			.append("$lt", 90));

		Bson filter = Filters.and(Filters.eq("x", 0), Filters.gt("y",10));

		List<Document> all = coll.find(filter).into(new ArrayList<Document>());



		for (Document document : all) {
			printJson(document);
		}

		long count = coll.count(filter);
		System.out.println();
		System.out.println(count);
	}

	private void insertTest() {
		MongoClient client = new MongoClient();

		MongoDatabase db = client.getDatabase("course");

		MongoCollection<Document> coll = db.getCollection("insertTest");

		coll.drop();

		Document smith = new Document("name", "Smith")
						.append("age", 30)
						.append("profession", "programmer");

		coll.insertOne(smith);

	}

	private void findTest() {
		MongoClient client = new MongoClient();

		MongoDatabase db = client.getDatabase("course");


		MongoCollection<Document> coll = db.getCollection("findTest");

		coll.drop();

		for (int i = 0; i < 10; i++) {
			coll.insertOne(new Document("x" , i));

		}
		System.out.println("Find One:");
		Document first = coll.find().first();

		printJson(first);
		System.out.println("Find all with into:");

		List<Document> all = coll.find().into(new ArrayList<Document>());

		for (Document document : all) {
			printJson(document);
		}
		System.out.println("Find all with iteration");

		MongoCursor<Document> cursor = coll.find().iterator();
		try {
			while(cursor.hasNext()){
				Document cur = cursor.next();
				printJson(cur);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally{
			cursor.close();
		}

		System.out.println("Count");
		System.out.println(coll.count());

	}
	private static void printJson(Document document) {
		JsonWriter j = new JsonWriter(new StringWriter(),
				new JsonWriterSettings(JsonMode.SHELL));
		new DocumentCodec().encode(j, document,
				EncoderContext.builder()
				.isEncodingCollectibleDocument(true).build());

		System.out.println(j.getWriter());
		System.out.flush();
	}
}
