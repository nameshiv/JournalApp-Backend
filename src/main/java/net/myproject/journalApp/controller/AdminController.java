package net.myproject.journalApp.controller;

import net.myproject.journalApp.cache.AppCache;
import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(all, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/make-admin/{userId}")
    public void makeAdmin(@PathVariable ObjectId userId) {
        userService.makeAdmin(userId);
    }

    @DeleteMapping("/remove-admin/{userId}")
    public void removeAdmin(@PathVariable ObjectId userId) {
        userService.removeAdmin(userId);
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }

}
