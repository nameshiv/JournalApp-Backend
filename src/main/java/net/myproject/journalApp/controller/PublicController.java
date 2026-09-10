package net.myproject.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.myproject.journalApp.dtos.LoginRequest;
import net.myproject.journalApp.dtos.SignupRequest;
import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.repository.UserRepository;
import net.myproject.journalApp.service.UserDetailsServiceImpl;
import net.myproject.journalApp.service.UserService;
import net.myproject.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }


    @PostMapping("/signup")
    public ResponseEntity<User> signup (@RequestBody SignupRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(
                            request.getUsername()
                    );

            String jwt =
                    jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {

            log.error("Exception occurred while createAuthenticationToken", e);

            return new ResponseEntity<>(
                    "Incorrect username or password",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
