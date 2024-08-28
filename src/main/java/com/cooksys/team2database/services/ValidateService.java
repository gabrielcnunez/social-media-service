package com.cooksys.team2database.services;

public interface ValidateService {
	boolean hashtagExists(String label);
    boolean usernameExists(String username);
    boolean usernameAvailable(String username);

}
