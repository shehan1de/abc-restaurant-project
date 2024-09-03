package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Offer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, ObjectId> {
    Offer findByOfferId(String offerId);
}
