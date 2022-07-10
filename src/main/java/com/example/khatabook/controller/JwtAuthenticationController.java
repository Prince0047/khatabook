package com.example.khatabook.controller;

import com.example.khatabook.config.JwtTokenUtil;
import com.example.khatabook.dto.CreateUserRequestDto;
import com.example.khatabook.dto.CreateUserResponseDto;
import com.example.khatabook.dto.JwtRequestDto;
import com.example.khatabook.dto.JwtResponseDto;
import com.example.khatabook.model.User;
import com.example.khatabook.service.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage() {
        return "Welcome";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = jwtUserDetailService.save(createUserRequestDto);
        return ResponseEntity.ok(new CreateUserResponseDto(user.getUsername()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto jwtRequestDto) throws Exception {
        authenticate(jwtRequestDto.getUsername(), jwtRequestDto.getPassword());

        final UserDetails userDetails = jwtUserDetailService.loadUserByUsername(jwtRequestDto.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("User not found...");
        }
        catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials...");
        }
    }
}
