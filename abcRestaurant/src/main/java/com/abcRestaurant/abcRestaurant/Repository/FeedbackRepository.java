package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Feedback;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback, ObjectId> {
}
