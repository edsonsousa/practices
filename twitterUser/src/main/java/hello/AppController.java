package hello;


import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RequestMapping("/")
public class AppController {

	private final ConnectionRepository connectionRepository;


	@Autowired
	private CompanyService service;

	@Inject
	public AppController(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}


	@RequestMapping(value={"/","/list"}, method=RequestMethod.GET)
	public String startForm(Model model) {
		model.addAttribute("company", new Company());

		List<Company> companies = service.findAll();

		model.addAttribute("companies", companies);
		return "form";
	}

	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String checkPersonInfo(@Valid Company company, BindingResult bindingResult, Model model) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			service.registerTwitter();
		}
		if (bindingResult.hasErrors()) {
			return "redirect:/connect/twitter";
		}

		System.out.println(company.getTwitterUser());

		Company result = service.searchEmployees(company.getTwitterUser());

		if(result != null){
			model.addAttribute("result", result);
		}

		return "companyDetail";
	}

	  @RequestMapping(value = { "/view-company-{profile}" }, method = RequestMethod.GET)
	    public String editUser(@PathVariable String profile, ModelMap model) {
	        Company company = service.findByProfile(profile);
	        model.addAttribute("result", company);
	        return "companyDetail";
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