package hello;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.social.twitter.api.TwitterProfile;


public class Company {

    @Id
    private String id;

    private String xx;
    private TwitterProfile companyProfile;
    private List<ProfileRelation> listEmployees;

    public Company(TwitterProfile userProfile) {
		this.companyProfile = userProfile;
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
                id, getCompanyProfile());
    }

	public TwitterProfile getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(TwitterProfile companyProfile) {
		this.companyProfile = companyProfile;
	}

	public List<ProfileRelation> getListEmployees() {
		return listEmployees;
	}

	public void setListEmployees(List<ProfileRelation> listEmployees) {
		this.listEmployees = listEmployees;
	}

	public String getXx() {
		return xx;
	}

	public void setXx(String xx) {
		this.xx = xx;
	}

}