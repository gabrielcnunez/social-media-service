package com.cooksys.team2database.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.repositories.HashtagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl {
	
	private final HashtagRepository hashtagRepository;

}
