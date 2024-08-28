package com.cooksys.team2database.dtos;

import com.cooksys.team2database.entities.Tweet;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

	private Long id;

	private UserResponseDto author;

	private Long posted;

	private String content;

	private Tweet inReplyTo;

	private Tweet repostOf;

}
