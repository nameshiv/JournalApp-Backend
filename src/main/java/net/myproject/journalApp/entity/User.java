package net.myproject.journalApp.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.myproject.journalApp.config.ObjectIdSerializer;
import net.myproject.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;

    private boolean sentimentAnalysis = false;
    private Sentiment weeklySentiment;
    private Map<Sentiment, Integer> weeklySentimentCounts =
            new HashMap<>();

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
    private List<String> roles;

}
