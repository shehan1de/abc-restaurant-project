package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.abcRestaurant.abcRestaurant.Model.Branch;

public interface BranchRepository extends MongoRepository <Branch, ObjectId> {
}