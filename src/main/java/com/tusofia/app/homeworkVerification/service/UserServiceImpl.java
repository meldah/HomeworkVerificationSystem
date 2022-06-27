package com.tusofia.app.homeworkVerification.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tusofia.app.homeworkVerification.domain.entities.Role;
import com.tusofia.app.homeworkVerification.domain.entities.User;
import com.tusofia.app.homeworkVerification.domain.models.service.UserBaseServiceModel;
import com.tusofia.app.homeworkVerification.error.UserNotFoundException;
import com.tusofia.app.homeworkVerification.repository.RoleRepository;
import com.tusofia.app.homeworkVerification.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	private static final String USER_NOT_FOUND_MESSAGE = "No user with such username found!";
	private final static int NAME_INITIAL_IDX = 0;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	private final static int LAST_NAME_SUBSTR_SIZE = 6;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return this.userRepository
                .findByEmail(email)
        		.orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
	}


	@Override
	public String generateUsername(String firstName, String lastName) {
		boolean firstNameInvalid = (firstName == null) || firstName.isEmpty();
		boolean lastNameInvalid = (lastName == null) || lastName.isEmpty();
		
		if(firstNameInvalid || lastNameInvalid)
		{
			throw new InvalidParameterException("First or last name is invalid");
		}
		
		//If the last name is smaller than six characters, get the whole of it
		int lastNameSize = lastName.length() < LAST_NAME_SUBSTR_SIZE ? 
				lastName.length() : LAST_NAME_SUBSTR_SIZE;
		
		StringBuilder usernameBuilder = new StringBuilder()
				.append(firstName.toLowerCase().charAt(NAME_INITIAL_IDX))
				.append(lastName.toLowerCase().substring(NAME_INITIAL_IDX, lastNameSize));
		
		// Find count of users with same first and last name as username is based on them
		long namesakeUsersCount = this.userRepository
				.countByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
		if (namesakeUsersCount > 0) {
			usernameBuilder.append(namesakeUsersCount);
		}

		return usernameBuilder.toString();
	}

	@Override
	public void setUserEnabled(String id, boolean isEnabled) {
		User user = this.userRepository
        .findById(id)
		.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
		
		user.setEnabled(isEnabled);
		this.userRepository.saveAndFlush(user);
	}

	@Override
	public void deleteUser(String id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public void assignUserRole(String id, String role) {
		User user = this.userRepository
				.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
		
		Role roleObj = this.roleRepository
				.findByAuthority(role);
		if(roleObj == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
		}
		Set<Role> userRoles = user.getAuthorities();
		userRoles.add(roleObj);
		this.userRepository.saveAndFlush(user);
	}

	@Override
	public void changeUserPassword(String email, String password) {
		User user = this.userRepository.findByEmail(email)
				.orElse(null);
		if(user != null) {
			user.setPassword(bCryptPasswordEncoder.encode(password));
			this.userRepository.saveAndFlush(user);
		}
	}
}