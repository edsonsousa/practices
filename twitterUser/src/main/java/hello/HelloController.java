package hello;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RequestMapping("/")
public class HelloController {

	private final Twitter twitter;

	private final ConnectionRepository connectionRepository;

	@Inject
	public HelloController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String checkPersonInfo(@Valid Company company, BindingResult bindingResult) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}
		if (bindingResult.hasErrors()) {
			return "redirect:/connect/twitter";
		}
		System.out.println(company.getTwitterUser());

		Company resultCompany = new Company(company.getTwitterUser());
		resultCompany.setCompanyProfile(twitter.userOperations().getUserProfile(company.getTwitterUser()));
		//String location = resultCompany.getCompanyProfile().getLocation();
		//List<TwitterProfile> followers = twitter.friendOperations().getFollowers(company.getTwitterUser());


		List<ProfileRelation> relations = new ArrayList<ProfileRelation>();
		relations.addAll(relationsFollowers(twitter.friendOperations().getFollowers(company.getTwitterUser()), resultCompany));
		relations.addAll(relationsCitations());
		relations.addAll(relationsRetweets());



		return "hello";
	}

	private Collection<? extends ProfileRelation> relationsRetweets() {
		// TODO Auto-generated method stub
		return null;
	}

	private Collection<? extends ProfileRelation> relationsCitations() {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<ProfileRelation> relationsFollowers(
			CursoredList<TwitterProfile> followers, Company resultCompany) {
		Set<ProfileRelation> relations = new HashSet<ProfileRelation>();
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

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String startForm(Model model) {
		model.addAttribute("company", new Company());
		return "form";
	}

	@RequestMapping(method=RequestMethod.GET)
	public String helloTwitter(Model model) {
		//
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		//        SearchParameters params = new SearchParameters("#FechadoComVozão").count(100);
		//        //params.setLang("nl");
		//
		//        SearchResults results = twitter.searchOperations().search(params);


		//        for (Tweet t : results.getTweets()) {
		//        	if(t.getUser().getLocation() == null){
		//        		continue;
		//        	}
		//			System.out.println(t.getUser().getLocation());
		//		}
		//        model.addAttribute(twitter.userOperations().getUserProfile());
		//        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
		//        model.addAttribute("friends", friends);
		model.addAttribute("company", new Company());
		return "form";
	}

}