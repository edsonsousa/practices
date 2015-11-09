

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private UserRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


//	@Override
//	public void run(String... strings) throws Exception {
//		RestTemplate restTemplate = new RestTemplate();
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());
//		
//		repository.deleteAll();
//
//		// save a couple of CustomerMongos
//		repository.save(new CustomerMongo("Alice", "Smith"));
//		repository.save(new CustomerMongo("Bob", "Smith"));
//
//		// fetch all CustomerMongos
//		System.out.println("CustomerMongos found with findAll():");
//		System.out.println("-------------------------------");
//		for (CustomerMongo CustomerMongo : repository.findAll()) {
//			System.out.println(CustomerMongo);
//		}
//		System.out.println();
//
//		// fetch an individual CustomerMongo
//		System.out.println("CustomerMongo found with findByFirstName('Alice'):");
//		System.out.println("--------------------------------");
//		System.out.println(repository.findByFirstName("Alice"));
//
//		System.out.println("CustomerMongos found with findByLastName('Smith'):");
//		System.out.println("--------------------------------");
//		for (CustomerMongo CustomerMongo : repository.findByLastName("Smith")) {
//			System.out.println(CustomerMongo);
//		}

//	}

}