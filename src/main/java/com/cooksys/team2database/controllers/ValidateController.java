package com.cooksys.team2database.controllers;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.team2database.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService;
	
	//Checks if hashtag exists
	
	@GetMapping("/tag/exists/{label}")
    public boolean checkHashtagExists(@PathVariable String label) {
        return validateService.hashtagExists(label);
    }
	
	//Checks if username exists

    @GetMapping("/username/exists/@{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return validateService.usernameExists(username);
    }
    
    //Checks is a username is available

    @GetMapping("/username/available/@{username}")
    public boolean checkUsernameAvailable(@PathVariable String username) {
        return validateService.usernameAvailable(username);
    }
	


}
