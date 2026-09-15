package net.myproject.journalApp.controller;

import net.myproject.journalApp.api.response.WeatherResponse;
import net.myproject.journalApp.dtos.SentimentAnalysisRequest;
import net.myproject.journalApp.dtos.UserResponse;
import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.repository.UserRepository;
import net.myproject.journalApp.service.UserService;
import net.myproject.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    //Update user
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUsername(userName);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User userInDb = userService.findByUsername(userName);

        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserResponse response = new UserResponse();

        response.setId(userInDb.getId());
        response.setUsername(userInDb.getUsername());
        response.setRoles(userInDb.getRoles());
        response.setSentimentAnalysis(userInDb.isSentimentAnalysis());
        response.setWeeklySentiment(userInDb.getWeeklySentiment());
        response.setWeeklySentimentCounts(userInDb.getWeeklySentimentCounts());
        response.setJournalEntries(userInDb.getJournalEntries());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/sentiment-analysis")
    public ResponseEntity<?> updateSentimentAnalysis(
            @RequestBody SentimentAnalysisRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User userInDb = userService.findByUsername(userName);

        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // First update the setting
        userInDb.setSentimentAnalysis(request.isSentimentAnalysis());

        if (request.isSentimentAnalysis()) {

            // Calculate weekly sentiment immediately
            userService.calculateWeeklySentiment(userInDb);

        } else {

            // Clear weekly sentiment when disabled
            userInDb.setWeeklySentiment(null);
            userInDb.setWeeklySentimentCounts(new HashMap<>());
        }

        // Save updated user
        userService.saveUser(userInDb);

        // Return the updated user
        return new ResponseEntity<>(userInDb, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //Weather App (Separate)
    @GetMapping("weather")
    public ResponseEntity<?> greeting(@RequestParam String city){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greeting = "";
        if(weatherResponse != null){
            greeting = "Temperature in " + weatherResponse.getName() + " is " + weatherResponse.getMain().getTemp()
                    + " and Weather feels like " + weatherResponse.getMain().getFeels_like();
        }
        return new ResponseEntity<>( greeting,  HttpStatus.OK);
    }
}
