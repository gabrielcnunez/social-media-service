package com.cooksys.team2database.dtos;

import com.cooksys.team2database.entities.Credentials;
import com.cooksys.team2database.entities.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;
//might needs an AllArgu annotation possibly in one of the Dtos
@NoArgsConstructor
@Data
public class UserRequestDto {
	
	private Credentials credentials;
	
	private Profile profile;

}
