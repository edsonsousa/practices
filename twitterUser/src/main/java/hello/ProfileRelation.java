package hello;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.social.twitter.api.TwitterProfile;


public class ProfileRelation {

	@Id
    private String id;

	private String user;
    private String profileRelated;
    private int favoritedCount;
    private int mentionCount;
    private int retweetCount;
    private int mentionedCount;
	private Date dateQuery;
   

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFavoritedCount() {
		return favoritedCount;
	}
	public void setFavoritedCount(int favoritedCount) {
		this.favoritedCount = favoritedCount;
	}
	public int getMentionCount() {
		return mentionCount;
	}
	public void setMentionCount(int mentionCount) {
		this.mentionCount = mentionCount;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	public int getMentionedCount() {
		return mentionedCount;
	}
	public void setMentionedCount(int mentionedCount) {
		this.mentionedCount = mentionedCount;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	public Date getDateQuery() {
		return dateQuery;
	}
	public void setDateQuery(Date dateQuery) {
		this.dateQuery = dateQuery;
	}
	public String getProfileRelated() {
		return profileRelated;
	}
	public void setProfileRelated(String profileRelated) {
		this.profileRelated = profileRelated;
	}

}
