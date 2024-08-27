package com.cooksys.team2database.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Credentials {

	private String username;
	
	private String password;
}
