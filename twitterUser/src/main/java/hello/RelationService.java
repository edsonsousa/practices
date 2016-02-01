package hello;

import java.util.Collection;
import java.util.List;

import org.springframework.social.twitter.api.TwitterProfile;

public interface RelationService {

	public Collection<ProfileRelation> searchRelations(TwitterProfile profile);

	public void registerTwitter();

	public List<ProfileRelation> findAll();

	public TwitterProfile findTwitterUser(String user) throws Exception;

}
