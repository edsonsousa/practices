package hello;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;

public class ProfileRelationRepositoryImpl implements ProfileRelationCustom {

	@Autowired 
	private MongoOperations operations;
	
	@Autowired 
	private MongoTemplate template;

	@Override
	public List<ProfileRelation> findLast10ByGroupByUser() {
		List<ProfileRelation> r = new ArrayList<ProfileRelation>();
		GroupByResults<ProfileRelation> results = 
				template.group("profileRelation", GroupBy.key("user")
						.initialDocument("{ count: 0 }")
				        .reduceFunction("function(doc, prev) { prev.count += 1 }"),ProfileRelation.class);
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			r.add((ProfileRelation) iterator.next());
			
		}
		return r;
	}

}
