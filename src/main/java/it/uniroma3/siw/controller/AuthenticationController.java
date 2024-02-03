package it.uniroma3.siw.controller;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthenticationController {
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;
    
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("user", new User());
		return "formRegister";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}
	
	@GetMapping(value="/admin/indexAdmin") 
	public String showIndexAdmin (Model model) {
		return "indexAdmin";
	}
	
	@GetMapping(value="/index") 
	public String showIndex (Model model) {
		return "index";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		else {	
			Authentication userDetails =  SecurityContextHolder.getContext().getAuthentication();
			String loggedUser = userDetails.getName();
			Credentials credentials = credentialsService.getCredentials(loggedUser);
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
		}
        return "index.html";
	}
		
	

    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
    	Authentication userDetails =  SecurityContextHolder.getContext().getAuthentication();
		String loggedUser = userDetails.getName();
    	Credentials credentials = credentialsService.getCredentials(loggedUser);
    	if (credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "index.html";
    }
     
    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model) {
    	
    	this.userValidator.validate(user, userBindingResult);
    	if (this.credentialsValidator != null) {
            this.credentialsValidator.validate(credentials, credentialsBindingResult);
        }
    	
    	//this.credentialsValidator.validate(credentials,credentialsBindingResult);

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            this.userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "formRegister";
    }


}


