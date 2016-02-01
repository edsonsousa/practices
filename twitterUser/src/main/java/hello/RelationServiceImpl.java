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

	private static final String SPRING_SOCIAL_TWITTER_APPSECRET = "";

	private static final String SPRING_SOCIAL_TWITTER_APPID = "";

	private static final String AT = "@";

	private static final int QTD_RETRIEVE_TWEETS = 100;

	@Autowired
	ProfileRelationRepository repository;

	private Twitter twitter;

	private Connection<Twitter> connection;

	@Override
	public List<ProfileRelation> findAll() {
		return repository.findAll();
	}


	@Override
	public Collection<ProfileRelation> searchRelations(TwitterProfile profile) {
		HashMap<String, ProfileRelation> hashRelations = new HashMap<String, ProfileRelation>();
		List<Tweet> tweets = null;
		List<String> citations = null;
		ProfileRelation profileRelation;
		try{
						
			tweets = twitter.timelineOperations().getUserTimeline(profile.getName(),QTD_RETRIEVE_TWEETS);
			
			for (Tweet tweet : tweets) {
				if(tweet.getText().contains(AT)){
					citations = returnUsers(tweet.getText());
					if(citations != null && citations.size() > 0){
						for (String user : citations) {
							if(!hashRelations.containsKey(user)){
								profileRelation = new ProfileRelation();
								profileRelation.setUser(profile.getName());
								profileRelation.setProfileRelated(user);
								hashRelations.put(user, profileRelation);
							}
							profileRelation = hashRelations.get(user);
							if(!tweet.isRetweet()){
								profileRelation.setMentionCount(profileRelation.getMentionCount()+1);
							}
							else{
								profileRelation.setRetweetCount(profileRelation.getRetweetCount()+1);
							}
							hashRelations.put(user, profileRelation);
							profileRelation.setDateQuery(new Date());
							repository.save(profileRelation);
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return hashRelations.values();
	}


	private List<String> returnUsers(String text) {
		int begin = 0;
		int end = 0;
		List<String> r = new ArrayList<String>();
		for (int i = 0; i < text.length(); i++) {
			if(text.charAt(i) == '@' || begin > 0){
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

	@Override
	public void registerTwitter() {
		twitter = connection != null ?
				connection.getApi() :
					new TwitterTemplate(
							SPRING_SOCIAL_TWITTER_APPID,SPRING_SOCIAL_TWITTER_APPSECRET);

	}


	@Override
	public TwitterProfile findTwitterUser(String user) throws Exception{
		return twitter.userOperations().getUserProfile(user);	
	}

}
