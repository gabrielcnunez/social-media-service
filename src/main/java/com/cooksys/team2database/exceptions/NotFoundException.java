package com.cooksys.team2database.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -7559363960346831762L;
	
	private String message;

}
