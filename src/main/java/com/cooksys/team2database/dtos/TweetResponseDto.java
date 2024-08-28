package com.cooksys.team2database.dtos;

import java.sql.Timestamp;

import com.cooksys.team2database.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

	private Long id;

	private UserResponseDto author;

	private Timestamp posted;

	private String content;

	private TweetResponseDto inReplyTo;

	private TweetResponseDto repostOf;

}
