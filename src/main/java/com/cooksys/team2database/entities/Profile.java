package com.cooksys.team2database.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {
	
	// removed id do to embeddable annotation
	/*@Id
	@GeneratedValue
	private Long id;*/
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;
	

}
