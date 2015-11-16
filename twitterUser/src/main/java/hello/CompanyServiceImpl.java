package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService{

	@Autowired
	CompanyRepository repository;

	private Twitter twitter;

	private Connection<Twitter> connection;

	public List<Company> findAll() {
		return repository.findAll();
	}

	public Company searchEmployees(String twitterUser) {
		Company resultCompany = new Company(twitterUser);
		TwitterProfile profile = twitter.userOperations().getUserProfile(twitterUser);

		if(profile == null){
			return null;
		}

		resultCompany.setCompanyProfile(profile);
		//String location = resultCompany.getCompanyProfile().getLocation();
		//List<TwitterProfile> followers = twitter.friendOperations().getFollowers(company.getTwitterUser());


		List<ProfileRelation> relations = new ArrayList<ProfileRelation>();
		List<ProfileRelation> relationsFollowers = relationsFollowers(twitter.friendOperations().getFollowers(twitterUser), resultCompany);

		List<ProfileRelation> relationsTweets = relationsTweets(twitter.timelineOperations().getUserTimeline(twitterUser, 200),resultCompany);//returns relations from company tweets
		relations.addAll(relationsRetweets());

		//relations.addAll(relationsMentions());

		//List<Tweet> tweets = twitter.timelineOperations().getMentions();

		return resultCompany;
	}


	private Set<ProfileRelation> relationsRetweets() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ProfileRelation> relationsTweets(List<Tweet> list, Company resultCompany) {
		List<ProfileRelation> relations = new ArrayList<ProfileRelation>();
		for (Tweet tweet : list) {
		}
		return relations;
	}

	private List<ProfileRelation> relationsFollowers(
			CursoredList<TwitterProfile> followers, Company resultCompany) {
		List<ProfileRelation> relations = new ArrayList<ProfileRelation>();
		ProfileRelation relation = null;

		boolean anyRelation = false;
		for (TwitterProfile follower : followers) {
			relation = new ProfileRelation();

			if(follower.getDescription() != null && follower.getDescription().contains(resultCompany.getCompanyProfile().getName())){
				relation.setDescription(true);
				anyRelation = true;
			}
			else{
				relation.setDescription(false);
			}
			if(follower.getLocation() != null
					&& (follower.getLocation().contains(resultCompany.getCompanyProfile().getLocation())
							|| resultCompany.getCompanyProfile().getLocation().contains(follower.getLocation()))){
				relation.setLocation(true);
				anyRelation = true;
			}
			else{
				relation.setLocation(false);
			}
			if(follower.getName() != null && (follower.getName().contains(resultCompany.getCompanyProfile().getName())
					|| resultCompany.getCompanyProfile().getName().contains(follower.getName()))){
				anyRelation = true;
				relation.setSubsidiary(true);
			}
			else{
				relation.setSubsidiary(false);
			}
			if(anyRelation){
				relation.setCompany(resultCompany);
				relations.add(relation);
			}
		}
		return relations;
	}

	public void registerTwitter() {
		twitter = connection != null ?
				connection.getApi() :
					new TwitterTemplate(
							"hZmoB4O5afAAyboLvuQJovYil","kWmUEhW1wyBvTK3pztt49gxbgSSDYTAxoq8P8lHgkj9JdjYDWc");

	}

	public Company findByProfile(String profile) {
		return repository.findByTwitterUser(profile);
	}

}
