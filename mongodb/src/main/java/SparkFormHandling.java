import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;


public class SparkFormHandling {

	public static void main(String[] args) {

		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(SparkFormHandling.class
				, "/");
		Spark.get(new Route("/") {

			@Override
			public Object handle(Request arg0, Response arg1) {

				try {
					Template fruitPickerTemplate =  configuration.getTemplate("fruitPicker.ftl");

					Map<String, Object> fruitsMap = new HashMap<String, Object>();
					fruitsMap.put("fruits", Arrays.asList("maça", "laranja", "banana", "pessego"));
					StringWriter writer = new StringWriter();
					fruitPickerTemplate.process(fruitsMap, writer);

					return writer;

				} catch (Exception e) {
					halt(500);
					e.printStackTrace();
					return  null;
				}
			}
		});

		Spark.post(new Route("/favorite_fruit") {

			@Override
			public Object handle(Request request, Response response) {
				final String fruit = request.queryParams("fruit");
				if(fruit == null){
					return "Seleciona uma fruta baitinga";
				}
				else {
					return "Sua fruta mais preferida é "+fruit;
				}
			}
		});

	}
}
