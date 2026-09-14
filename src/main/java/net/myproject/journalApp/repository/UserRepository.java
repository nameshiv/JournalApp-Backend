package net.myproject.journalApp.repository;

import net.myproject.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);
    void deleteByUsername(String username);
    List<User> findAllByOrderByCreatedAtDesc();
}
