package com.task.management.tms.controllers;

import com.task.management.tms.entity.User;
import com.task.management.tms.model.JwtRequest;
import com.task.management.tms.model.JwtResponse;
import com.task.management.tms.model.UserModel;
import com.task.management.tms.service.impl.UserDetailServiceImpl;
import com.task.management.tms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PATCH,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class JwtController {

    public static final String REGISTER_URI = "/register";
    public static final String LOGIN_URI = "/login";
    public static final String USER_URI = "/currentUser";
    public static final String USER_LIST = "/users";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping(REGISTER_URI)
    public ResponseEntity<UserModel> register(
            @RequestBody UserModel userModel) {

        UserModel userModel1 =
                userDetailService.register(userModel);

        return new ResponseEntity<>(
                userModel1,
                HttpStatus.CREATED
        );
    }


    @PostMapping(LOGIN_URI)
    public ResponseEntity<JwtResponse> generateToken(
            @RequestBody JwtRequest jwtRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                );

        authenticationManager.authenticate(authenticationToken);

        UserDetails userDetails =
                userDetailService.loadUserByUsername(
                        jwtRequest.getEmail()
                );

        String jwtToken = jwtUtil.generateToken(userDetails);

        JwtResponse jwtResponse =
                new JwtResponse(jwtToken);

        return new ResponseEntity<>(
                jwtResponse,
                HttpStatus.OK
        );
    }


    @GetMapping(USER_URI)
    public UserModel getCurrentUser(Principal principal) {

        UserDetails userDetails =
                userDetailService.loadUserByUsername(
                        principal.getName()
                );

        return (UserModel) userDetails;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(USER_LIST)
    public ResponseEntity<?> getAll() {

        Iterable<User> list =
                userDetailService.getList();

        return new ResponseEntity<>(
                list,
                HttpStatus.OK
        );
    }
}

