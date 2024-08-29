package com.cooksys.team2database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.team2database.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByCredentialsUsername(String username); //checks if a user with the given username exists

	Optional<User> findByCredentialsUsername(String username); // finds a user by their username
	
}
