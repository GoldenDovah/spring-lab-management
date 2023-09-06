package com.example.LabManagementApplication.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.LabManagementApplication.model.Users;
import com.example.LabManagementApplication.service.UserService;
import com.example.LabManagementApplication.web.UserRequest;

@RestController
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method=RequestMethod.POST, value="/create-user")
    public UserRequest createUser(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        userService.createUser(
            userRequest.getFirstName(),
            userRequest.getLastName(),
            userRequest.getEmail(),
            userRequest.getRole(),
            userRequest.getPassword()
        );
        authWithHttpServletRequest(request, userRequest.getEmail(), userRequest.getPassword());
        return userRequest;
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
        }
    }

    @GetMapping("/current-user")
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

}
