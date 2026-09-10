package net.myproject.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.myproject.journalApp.entity.JournalEntry;
import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.enums.Sentiment;
import net.myproject.journalApp.repository.UserRepository;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Transactional
    public boolean saveNewUser(User user) {
        try {
            user.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );

            user.setRoles(
                    new ArrayList<>(Arrays.asList("USER"))
            );

            userRepository.save(user);

            return true;

        } catch (Exception e) {
            log.error(
                    "Error occurred for {} :",
                    user.getUsername(),
                    e
            );

            return false;
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void makeAdmin(ObjectId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        List<String> roles = user.getRoles();

        if (roles == null) {
            roles = new ArrayList<>();
            user.setRoles(roles);
        } else {
            // Make sure the list is mutable.
            roles = new ArrayList<>(roles);
            user.setRoles(roles);
        }

        if (!roles.contains("ADMIN")) {
            roles.add("ADMIN");
        }

        // Make sure the user remains a normal USER as well.
        if (!roles.contains("USER")) {
            roles.add("USER");
        }

        userRepository.save(user);
    }

    @Transactional
    public void removeAdmin(ObjectId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        List<String> roles = user.getRoles();

        if (roles == null || !roles.contains("ADMIN")) {
            throw new RuntimeException("User is not an admin");
        }

        // Create a mutable copy before removing ADMIN.
        roles = new ArrayList<>(roles);

        roles.removeIf(
                role -> role != null &&
                        role.equalsIgnoreCase("ADMIN")
        );

        // The account remains a normal user.
        if (!roles.contains("USER")) {
            roles.add("USER");
        }

        user.setRoles(roles);

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void calculateWeeklySentiment(User user) {

        LocalDateTime sevenDaysAgo =
                LocalDateTime.now().minusDays(7);

        Map<Sentiment, Integer> sentimentCounts =
                new HashMap<>();

        List<JournalEntry> journalEntries =
                user.getJournalEntries();

        if (journalEntries != null) {
            for (JournalEntry entry : journalEntries) {

                if (entry.getDate() != null &&
                        entry.getDate().isAfter(sevenDaysAgo)) {

                    Sentiment sentiment =
                            entry.getSentiment();

                    if (sentiment != null) {
                        sentimentCounts.put(
                                sentiment,
                                sentimentCounts.getOrDefault(
                                        sentiment,
                                        0
                                ) + 1
                        );
                    }
                }
            }
        }

        Sentiment mostFrequentSentiment = null;
        int maxCount = 0;

        for (Map.Entry<Sentiment, Integer> entry :
                sentimentCounts.entrySet()) {

            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentSentiment = entry.getKey();
            }
        }

        user.setWeeklySentiment(
                mostFrequentSentiment
        );

        user.setWeeklySentimentCounts(
                sentimentCounts
        );
    }
}