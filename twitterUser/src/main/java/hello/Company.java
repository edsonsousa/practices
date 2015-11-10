package hello;


import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.social.twitter.api.TwitterProfile;


public class Company {

    @Id
    private String id;

    private String twitterUser;
    private TwitterProfile companyProfile;
    private Set<ProfileRelation> listEmployees;

    public Company(String twitterUser) {
    	setTwitterUser(twitterUser);
	}

	public Company() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return String.format(
                "Company[id=%s, profile='%s']",
                id, getTwitterUser());
    }

	public String getTwitterUser() {
		return twitterUser;
	}

	public void setTwitterUser(String twitterUser) {
		this.twitterUser = twitterUser;
	}

	public Set<ProfileRelation> getListEmployees() {
		return listEmployees;
	}

	public void setListEmployees(Set<ProfileRelation> listEmployees) {
		this.listEmployees = listEmployees;
	}

	public TwitterProfile getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(TwitterProfile companyProfile) {
		this.companyProfile = companyProfile;
	}

}