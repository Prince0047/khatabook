package com.example.khatabook.service;

import com.example.khatabook.dto.CreateUserRequestDto;
import com.example.khatabook.dto.CreateUserResponseDto;
import com.example.khatabook.model.User;
import com.example.khatabook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder bCryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with this username: " + username);
        }
        System.out.println("Returning Something >>>>>>>>");
        org.springframework.security.core.userdetails.User _user = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        return _user;
    }

    public User save(CreateUserRequestDto createUserRequestDto) {
        User user = new User();
        user.setUsername(createUserRequestDto.getUsername());
        user.setPassword(bCryptEncoder.encode(createUserRequestDto.getPassword()));

        return userRepository.save(user);

    }
}
