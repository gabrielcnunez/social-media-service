package com.cooksys.team2database.dtos;

import com.cooksys.team2database.entities.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private Long id;
	
	private String username;
	
	private Profile profile;
	
	private Long joined;
	

}
