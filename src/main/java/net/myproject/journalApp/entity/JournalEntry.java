package net.myproject.journalApp.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.myproject.journalApp.controller.ObjectIdSerializer;
import net.myproject.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;

}

//A POJO (Plain Old Java Object) class is a simple, standard Java class that holds data and has no special restrictions or dependencies on any framework, library, or external specification.

//@Data is Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
//This annotation helps Generates getters for all fields, a useful toString method, and hashCode and equals implementations that check all non-transient fields. Will also generate setters for all non-final fields, as well as a constructor (except that no constructor will be generated if any explicitly written constructors already exist).