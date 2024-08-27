package com.cooksys.team2database.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UnauthorizedException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -7863107368381243206L;
	
	private String message;

}
