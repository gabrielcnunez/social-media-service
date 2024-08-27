package com.cooksys.team2database.dtos;

import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

	private Long id;

	private User author;

	private Long posted;

	private String content;

	private Tweet inReplyTo;

	private Tweet repostOf;

}
