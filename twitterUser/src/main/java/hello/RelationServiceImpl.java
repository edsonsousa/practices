package hello;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("relationService")
@Transactional
public class RelationServiceImpl implements RelationService{

	@Autowired
	ProfileRelationRepository repository;

	private Twitter twitter;

	private Connection<Twitter> connection;

	public List<ProfileRelation> findAll() {
		return repository.findAll();
	}


	@Override
	public Collection<ProfileRelation> searchRelations(String twitterUser) {
		TwitterProfile profile = null;
		try{
			profile = twitter.userOperations().getUserProfile(twitterUser);	
		}
		catch(Exception e){
			e.printStackTrace();
		}


		if(profile == null){
			return null;
		}
		HashMap<String, ProfileRelation> h = new HashMap<String, ProfileRelation>();
		List<Tweet> tweets = twitter.timelineOperations().getUserTimeline(twitterUser,100);
		List<String> citations;
		ProfileRelation p;
		for (Tweet tweet : tweets) {
			if(tweet.getText().contains("@")){
				citations = returnUsers(tweet.getText());
				if(citations != null && citations.size() > 0){
					for (String user : citations) {
						//System.out.println(user);
						if(!h.containsKey(user)){
							p = new ProfileRelation();
							p.setUser(twitterUser);
							p.setProfileRelated(user);
							h.put(user, p);
						}
						p = h.get(user);
						if(!tweet.isRetweet()){
							p.setMentionCount(p.getMentionCount()+1);
						}
						else{
							p.setRetweetCount(p.getRetweetCount()+1);
						}
						h.put(user, p);
						p.setDateQuery(new Date());
						repository.save(p);
					}
				}
			}
		}
		return h.values();
	}


	private List<String> returnUsers(String text) {
		int begin = 0;
		int end = 0;
		List<String> r = new ArrayList<String>();
		for (int i = 0; i < text.length(); i++) {
			if(text.charAt(i) == '@' || begin > 0){
				//i++;
				if(begin == 0){
					begin = i+1;
					end = i+1;
				}
				if(!(text.charAt(i) == ' ') && !(text.charAt(i) == ':')){
					end++;
				}
				else{
					r.add(text.substring(begin, end-1));
					begin = 0;
					end = 0;
				}
			}
		}
		return r;
	}
	
	public void registerTwitter() {
		twitter = connection != null ?
				connection.getApi() :
					new TwitterTemplate(
							"hZmoB4O5afAAyboLvuQJovYil","kWmUEhW1wyBvTK3pztt49gxbgSSDYTAxoq8P8lHgkj9JdjYDWc");

	}

}
