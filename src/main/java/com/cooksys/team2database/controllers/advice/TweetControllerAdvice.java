package com.cooksys.team2database.controllers.advice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.cooksys.quiz_api.dtos.ErrorDto;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.exceptions.NotFoundException;


@ControllerAdvice(basePackages = {"com.cooksys.team2database.controllers"})
@ResponseBody
public class TweetControllerAdvice {
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(Unauthorized.class)
	public ErrorDto handleNotAuthorizedException (BadRequestException badRequestException) {
		return new ErrorDto(badRequestException.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorDto handleBadRequestException (BadRequestException badRequestException) {
		return new ErrorDto(badRequestException.getMessage());
	}
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }
    
}
