package hello;

import java.util.Collection;

public interface RelationService {

	public Collection<ProfileRelation> searchRelations(String twitterUser);

	public void registerTwitter();

}
