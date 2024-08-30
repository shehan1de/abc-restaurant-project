package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.abcRestaurant.abcRestaurant.Model.Branch;

import java.util.Optional;

public interface BranchRepository extends MongoRepository<Branch, ObjectId> {
    Optional<Branch> findByBranchId(String branchId);
}