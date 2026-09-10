package net.myproject.journalApp.service;

import net.myproject.journalApp.entity.JournalEntry;
import net.myproject.journalApp.entity.User;
import net.myproject.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import net.myproject.journalApp.enums.Sentiment;
import java.util.HashMap;
import java.util.Map;


//@Component -> generic, @Service -> specialized component
@Service
public class JournalEntryService {

    //Dependency Injection
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional
//    public void saveEntry(JournalEntry journalEntry, String username){
//        try {
//            User user = userService.findByUsername(username);
//            if (user == null) {
//                throw new RuntimeException("User not found.");
//            }
//            journalEntry.setDate(LocalDateTime.now());
//            if (!user.isSentimentAnalysis()) {
//                journalEntry.setSentiment(null);
//            }
//            JournalEntry saved = journalEntryRepository.save(journalEntry);
//            user.getJournalEntries().add(saved);
//            userService.saveUser(user);
//        }catch (Exception e){
//            throw new RuntimeException("A error occurred while saving the entry.", e);
//        }
//    }
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new RuntimeException("User not found.");
            }

            journalEntry.setDate(LocalDateTime.now());

            if (!user.isSentimentAnalysis()) {
                journalEntry.setSentiment(null);
            }

            JournalEntry saved =
                    journalEntryRepository.save(journalEntry);

            user.getJournalEntries().add(saved);

            // Recalculate weekly sentiment immediately
            if (user.isSentimentAnalysis()) {
                calculateWeeklySentiment(user);
            } else {
                user.setWeeklySentiment(null);
                user.setWeeklySentimentCounts(new HashMap<>());
            }

            userService.saveUser(user);

        } catch (Exception e) {
            throw new RuntimeException(
                    "A error occurred while saving the entry.",
                    e
            );
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }
    private void calculateWeeklySentiment(User user) {

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

        user.setWeeklySentiment(mostFrequentSentiment);
        user.setWeeklySentimentCounts(sentimentCounts);
    }

    public List<JournalEntry> getAll(){

        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){

        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean removed = false;

        try {
            User user = userService.findByUsername(username);

            removed = user.getJournalEntries()
                    .removeIf(x -> x.getId().equals(id));

            if (removed) {

                journalEntryRepository.deleteById(id);

                if (user.isSentimentAnalysis()) {
                    userService.calculateWeeklySentiment(user);
                } else {
                    user.setWeeklySentiment(null);
                    user.setWeeklySentimentCounts(new HashMap<>());
                }

                userService.saveUser(user);
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(
                    "An error occurred while deleting the entry.",
                    e
            );
        }

        return removed;
    }

}
