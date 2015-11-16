package hello;

import java.util.List;

public interface CompanyService {

	public Company searchEmployees(String twitterUser);

	public void registerTwitter();

	public List<Company> findAll();

	public Company findByProfile(String profile);

}
