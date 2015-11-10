import java.io.StringWriter;

import org.bson.Document;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class HelloWorldMongodbSparkFreemarkerStyle {

	public static void main(String[] args) {

		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(HelloWorldMongodbSparkFreemarkerStyle.class
				, "/");

		MongoClient client = new MongoClient();

		MongoDatabase db = client.getDatabase("course");

		final MongoCollection<Document> coll = db.getCollection("hello");

		coll.drop();

		coll.insertOne(new Document("name", "Mongodb"));

		Spark.get(new Route("/") {

			@Override
			public Object handle(Request arg0, Response arg1) {
				StringWriter writer = new StringWriter();
				try {
					Template helloTemplate =  configuration.getTemplate("hello.ftl");

					Document document = coll.find().first();

					helloTemplate.process(document, writer);

					//System.out.println(writer);

				} catch (Exception e) {
					halt(500);
					e.printStackTrace();
				}
				return writer;
			}
		});

	}
}
