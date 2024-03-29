
/* ********************************************************************************
 * Project: Create a Recipe Project Using Spring/Spring Boot
 * Assignment: 1 & 2
 * Author(s): Wynne Tran
 * Student Number: 101161665
 * Date: Nov 4 2021
 * Description:  this page user for user service, create, edit user account
 ******************************************************************************** */
package com.example.assignment.services;

import com.example.assignment.model.Roles;
import com.example.assignment.model.Users;
import com.example.assignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(Users user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Roles userRole = new Roles("USER");
        List<Roles> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void updateUser(Users user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void createAdmin(Users user) {
        BCryptPasswordEncoder  encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Roles userRole = new Roles("ADMIN");
        List<Roles> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Users findOne(String email) {
        return userRepository.findById(email).orElse(null);
    }

    public boolean isUserPresent(String email) {
        // TODO Auto-generated method stub
        Users u=userRepository.findById(email).orElse(null);
        return u != null;
    }

    public List<Users> findAll() {
        // TODO Auto-generated method stub
        return userRepository.findAll();
    }

    public List<Users> findByName(String name) {
        // TODO Auto-generated method stub
        return  userRepository.findByNameLike("%"+name+"%");
    }

}
