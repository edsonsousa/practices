package hello;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.social.twitter.api.TwitterProfile;


public class Company {

    @Id
    private String id;

    private String twitterUser;
    private TwitterProfile companyProfile;
    private HashMap<String, ProfileRelation> mapEmployees;
    private List<ProfileRelation> listEmployees;

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

	public TwitterProfile getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(TwitterProfile companyProfile) {
		this.companyProfile = companyProfile;
	}

	public HashMap<String, ProfileRelation> getMapEmployees() {
		return mapEmployees;
	}

	public void setMapEmployees(HashMap<String, ProfileRelation> mapEmployees) {
		this.mapEmployees = mapEmployees;
	}

	public List<ProfileRelation> getListEmployees() {
		return new ArrayList<ProfileRelation>(getMapEmployees().values());
	}

	public void setListEmployees(List<ProfileRelation> listEmployees) {
		this.listEmployees = listEmployees;
	}

}