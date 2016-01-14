package hello;


import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRelationRepository extends MongoRepository<ProfileRelation, String> {

	public ProfileRelation findByProfileRelated(String profileRelated);
	
	public Collection<ProfileRelation> findByUser(String user);
	
	public List<ProfileRelation> findLast10ByOrderByDateQueryDesc();
	
	public List<ProfileRelation> findAll();

	public List<ProfileRelation> findLast10ByGroupByUser();

}