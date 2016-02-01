package hello;


import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {

	private final ConnectionRepository connectionRepository;

	@Autowired
	private RelationService service;

	@Autowired
	private ProfileRelationCustom repositoryCustom;


	@Inject
	public AppController(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping(value={"/","/list"}, method=RequestMethod.GET)
	public String startForm(Model model) {
		model.addAttribute("relation", new ProfileRelation());

		List<ProfileRelation> relations = repositoryCustom.findLast10ByGroupByUser();
		if(relations != null && relations.size() > 0){
			model.addAttribute("relations", relations);
		}
		return "form";
	}

	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String checkPersonInfo(@ModelAttribute ProfileRelation relation, Model model) {
		relation = (ProfileRelation) model.asMap().get("profileRelation");
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			service.registerTwitter();
		}
		TwitterProfile twitterUser;
		
		try {
			twitterUser = service.findTwitterUser(relation.getUser());
		} catch (Exception e) {
			return "noResult";
		}

		Collection<ProfileRelation> result = service.searchRelations(twitterUser);

		if(result != null && result.size() > 0){
			model.addAttribute("user",relation.getUser());
			model.addAttribute("result", result);
		}else{
			return "noResult";
		}

		return "resultRelations";
	}
}