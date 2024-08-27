package com.cooksys.team2database.dtos;

import com.cooksys.team2database.entities.Credentials;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
	
	private String content;
	
	private Credentials credentials;

}
