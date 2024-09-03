package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Offer;
import com.abcRestaurant.abcRestaurant.Model.User;
import com.abcRestaurant.abcRestaurant.Repository.OfferRepository;
import com.abcRestaurant.abcRestaurant.Repository.UserRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all Offers
    public List<Offer> allOffer() {
        return offerRepository.findAll();
    }

    // Get a single offer by id
    public Optional<Offer> singleOffer(ObjectId id) {
        return offerRepository.findById(id);
    }

    // Get an offer by offerId
    public Offer getOfferByOfferId(String offerId) {
        return offerRepository.findByOfferId(offerId);
    }

    // Add a new offer
    public Offer addOffer(Offer offer) {
        offer.setOfferId(generateOfferId());
        return offerRepository.save(offer);
    }

    private String generateOfferId() {
        List<Offer> offers = offerRepository.findAll();
        int maxId = 0;
        for (Offer offer : offers) {
            String offerId = offer.getOfferId();
            try {
                int numericPart = Integer.parseInt(offerId.split("-")[1]);
                if (numericPart > maxId) {
                    maxId = numericPart;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing branchId: " + offerId + ". Skipping this entry.");
            }
        }
        int nextId = maxId + 1;
        return String.format("offer-%03d", nextId);
    }

    public Offer updateOffer(String offerId, Offer offer) {
        Offer existingOffer = offerRepository.findByOfferId(offerId);
        if (existingOffer == null) {
            throw new ResourceNotFoundException("Offer not found with id " + offerId);
        }

        existingOffer.setOfferName(offer.getOfferName());
        existingOffer.setOfferDescription(offer.getOfferDescription());
        existingOffer.setOfferValue(offer.getOfferValue());

        return offerRepository.save(existingOffer);
    }



    public void deleteOffer(String offerId) {
        Offer existingOffer = offerRepository.findByOfferId(offerId);
        if (existingOffer == null) {
            throw new ResourceNotFoundException("Offer not found with id " + offerId);
        }
        offerRepository.delete(existingOffer);
    }


}
