package com.cooksys.team2database.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = 7820424981850222340L;
	
	private String message;

}
