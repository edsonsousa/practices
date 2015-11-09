package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private CustomerRepositoryMongo repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		log.info(quote.toString());
		
		repository.deleteAll();

		// save a couple of CustomerMongos
		repository.save(new CustomerMongo("Alice", "Smith"));
		repository.save(new CustomerMongo("Bob", "Smith"));

		// fetch all CustomerMongos
		System.out.println("CustomerMongos found with findAll():");
		System.out.println("-------------------------------");
		for (CustomerMongo CustomerMongo : repository.findAll()) {
			System.out.println(CustomerMongo);
		}
		System.out.println();

		// fetch an individual CustomerMongo
		System.out.println("CustomerMongo found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("CustomerMongos found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (CustomerMongo CustomerMongo : repository.findByLastName("Smith")) {
			System.out.println(CustomerMongo);
		}

	}
	
	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {
			// save a couple of CustomerMongos
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));

			// fetch all CustomerMongos
			log.info("CustomerMongos found with findAll():");
			log.info("-------------------------------");
			for (Customer Customer : repository.findAll()) {
				log.info(Customer.toString());
			}
            log.info("");

			// fetch an individual CustomerMongo by ID
			Customer Customer = repository.findOne(1L);
			log.info("CustomerMongo found with findOne(1L):");
			log.info("--------------------------------");
			log.info(Customer.toString());
            log.info("");

			// fetch CustomerMongos by last name
			log.info("CustomerMongo found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Customer bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
            log.info("");
		};
	}

}