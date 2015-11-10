package hello;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

//    public Company findByProfile(String firstName);
//    public List<Company> findByLastName(String lastName);

}