package net.myproject.journalApp.Scheduler;

import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.repository.UserRepositoryImpl;
import net.myproject.journalApp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserService userService;


    @Scheduled(
            cron = "0 0 9 * * SUN",
            zone = "Asia/Kolkata"
    )
    public void calculateWeeklySentiment() {
        System.out.println("WEEKLY SENTIMENT SCHEDULER RUNNING");

        /*
         * Only users who have enabled sentiment analysis
         * are processed.
         */
        List<User> users = userRepository.getUsersForSA();

        for (User user : users) {

            userService.calculateWeeklySentiment(user);

            userService.saveUser(user);
        }
    }
}