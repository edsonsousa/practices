package hello;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

	public Company findByTwitterUser(String twitterUser);

//    public Company findByProfile(String firstName);
//    public List<Company> findByLastName(String lastName);

}