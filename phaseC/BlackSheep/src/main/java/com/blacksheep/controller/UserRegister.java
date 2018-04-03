package com.blacksheep.controller;

import com.blacksheep.ErrorCodes;
import com.blacksheep.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * UserRegister class consists of controllers, functions and endpoints for user login
 */
@RestController

public class UserRegister {
    private static final  Logger logger = Logger.getLogger(User.class);
    Integer DEFAULT_PLAGIARISM_PERCENTAGE =0;

    //Added by Athul
    @RequestMapping(
            value = "/userRegister",
            method = RequestMethod.POST)
    public ResponseEntity<Object> userRegister(@RequestBody Map<String,Object> userDetails) throws SQLException, IOException {

        String userId               = (String) userDetails.get("email");
        String password             = (String) userDetails.get("password");
        String confirmedPassword    = (String) userDetails.get("confirmedPassword");
        String role                 = (String) userDetails.get("role");
        logger.info("User Registration endpoint reached");
        logger.info(userDetails);

        User user = new User(userId,password,confirmedPassword,
                role,DEFAULT_PLAGIARISM_PERCENTAGE);

        int result = User.createUser(user);
         if (result == ErrorCodes.SUCCESS)
         {
             logger.info("user account successfully created!");
             return ResponseEntity.status(HttpStatus.OK).build();
         }
         else{
             logger.info("Cannot create user account ! ");
             logger.info("Error code : " + result);

             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }
    }


    //Added by Athul
    @RequestMapping(value="/isEmailTaken", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Object>  isEmailTaken
    (@RequestParam("email") String email) throws SQLException, IOException {
        logger.info("email: " + email);
        if(User.isEmailTaken(email)){
            logger.info("Another user registered under the same email Id");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            logger.info("Avaliable!");
            return ResponseEntity.status(HttpStatus.OK).build();

        }
    }

}
