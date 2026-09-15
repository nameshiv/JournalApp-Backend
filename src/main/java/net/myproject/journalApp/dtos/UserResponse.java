package net.myproject.journalApp.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.myproject.journalApp.config.ObjectIdSerializer;
import net.myproject.journalApp.entity.JournalEntry;
import net.myproject.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String username;
    private List<String> roles;
    private boolean sentimentAnalysis;
    private Sentiment weeklySentiment;
    private Map<Sentiment, Integer> weeklySentimentCounts;
    private List<JournalEntry> journalEntries;

    //getters and setters
}
