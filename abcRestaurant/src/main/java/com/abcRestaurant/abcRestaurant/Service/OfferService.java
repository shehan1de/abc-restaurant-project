package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Offer;
import com.abcRestaurant.abcRestaurant.Repository.OfferRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;

    // Get all Offers
    public List<Offer> allOffer() {
        return offerRepository.findAll();
    }

    // Get a single offer by id
    public Optional<Offer> singleOffer(ObjectId id) {
        return offerRepository.findById(id);
    }

    // Add a new offer
    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    // Update an existing offer by id
    public Offer updateOffer(ObjectId id, Offer offer) {
        if (!offerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offer not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        offer.setId(id);
        return offerRepository.save(offer);
    }

    // Delete a offer by id
    public void deleteOffer(ObjectId id) {
        if (!offerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offer not found with id " + id);
        }
        offerRepository.deleteById(id);
    }
}
