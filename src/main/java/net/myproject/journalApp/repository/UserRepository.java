package net.myproject.journalApp.repository;

import net.myproject.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);
    void deleteByUsername(String username);

    //What is happening here ?
//    you're telling Spring Data:
//
//            "I want a User found by username."
//
//    Spring Data handles the underlying database work.

// Spring Data MongoDB automatically provides the implementation of   repository methods and can derive database queries from method names such as findByUsername().
}
