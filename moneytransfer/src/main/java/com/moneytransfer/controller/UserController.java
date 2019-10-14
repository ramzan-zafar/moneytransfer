package com.moneytransfer.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.Objects;

import com.google.gson.Gson;
import com.moneytransfer.component.UserServiceComponent;
import com.moneytransfer.model.User;
import com.moneytransfer.response.StandardResponse;
import com.moneytransfer.response.StatusResponse;
import com.moneytransfer.service.UserService;

import com.moneytransfer.component.DaggerUserServiceComponent;


public class UserController {
    UserServiceComponent userServiceComponent = DaggerUserServiceComponent.create();
    
    UserService userService = userServiceComponent.buildUserService();
    
    public void registerUserApiRoutes() {
        //get list of all users
       get("/users", (req, res) -> {
           return new Gson().toJson(
                   new StandardResponse(StatusResponse.SUCCESS,new Gson()
                           .toJsonTree(userService.getAllUsers())));
       });

       //get user with given id
       get("/user/:id", (req, res) -> {
           String id = Objects.requireNonNull(req.params(":id"));
           return new Gson().toJson(
                   new StandardResponse(StatusResponse.SUCCESS,new Gson()
                           .toJsonTree(userService.getUserById(Long.valueOf(id)))));
       });

        //create a new user
        post("/user", (req, res) -> {
            String firstName = Objects.requireNonNull(req.queryParams("firstName"));
            String lastName = Objects.requireNonNull(req.queryParams("lastName"));
            String email = Objects.requireNonNull(req.queryParams("email"));
            User user = User.builder().firstName(firstName).lastName(lastName)
                            .email(email).build();
           long userId = userService.createUser(user);
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,"New user has been created with userId: "+ userId));
        });

        //edit a particular user
        put("/user/:id", (req, res) -> {
            String userId = Objects.requireNonNull(req.params(":id"));
            String firstName = Objects.requireNonNull(req.queryParams("firstName"));
            String lastName = Objects.requireNonNull(req.queryParams("lastName"));
            User user = User.builder().firstName(firstName).lastName(lastName)
                    .userId(Long.valueOf(userId)).build();
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,new Gson()
                            .toJsonTree(userService.updateUser(user))));
        });

       //delete a particular user
       delete("/user/:id", (req, res) -> {
           String userId = Objects.requireNonNull(req.params(":id"));
           userService.deleteUser(Long.valueOf(userId));
           return new Gson().toJson(
                   new StandardResponse(StatusResponse.SUCCESS,"User with %s id has been deleted", userId));
       });

    }
}
