package hello;
import org.springframework.data.annotation.Id;
import org.springframework.social.twitter.api.TwitterProfile;


public class ProfileRelation {

	@Id
    private String id;

    private TwitterProfile profile;
    private Company company;
    private int favoritedCount;
    private int mentionCount;
    private int retweetCount;
    private int mentionedCount;
    private boolean description;
    private boolean location;
    private boolean subsidiary;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TwitterProfile getProfile() {
		return profile;
	}
	public void setProfile(TwitterProfile profile) {
		this.profile = profile;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
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
	public boolean isDescription() {
		return description;
	}
	public void setDescription(boolean description) {
		this.description = description;
	}
	public boolean isLocation() {
		return location;
	}
	public void setLocation(boolean location) {
		this.location = location;
	}
	public boolean isSubsidiary() {
		return subsidiary;
	}
	public void setSubsidiary(boolean subsidiary) {
		this.subsidiary = subsidiary;
	}

}
