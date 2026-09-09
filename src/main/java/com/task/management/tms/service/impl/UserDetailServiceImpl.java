package com.task.management.tms.service.impl;

import com.task.management.tms.entity.User;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.model.UserModel;
import com.task.management.tms.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDetailServiceImpl(UserRepository userRepository,
                                 BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel register(UserModel userModel) {

        User user = new User();

        BeanUtils.copyProperties(userModel, user);

        user.setPassword(this.passwordEncoder.encode(userModel.getPassword()));

        user = userRepository.save(user);

        BeanUtils.copyProperties(user, userModel);

        return userModel;
    }

    public Iterable<User> getList() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email));

        UserModel userModel = new UserModel();

        userModel.setId(user.getId());

        userModel.setUsername(user.getEmail());

        userModel.setPassword(user.getPassword());
        userModel.setRole(user.getRole());

        return userModel;
    }
}


