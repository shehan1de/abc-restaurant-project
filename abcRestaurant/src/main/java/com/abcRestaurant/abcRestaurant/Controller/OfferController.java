package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Offer;
import com.abcRestaurant.abcRestaurant.Service.OfferService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        return new ResponseEntity<>(offerService.allOffer(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getSingleOffer(@PathVariable ObjectId id) {
        Optional<Offer> offer = offerService.singleOffer(id);
        return offer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer) {
        Offer newOffer = offerService.addOffer(offer);
        return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<Offer> updateOffer(@PathVariable("offerId") String offerId, @RequestBody Offer offer) {
        Offer updatedOffer = offerService.updateOffer(offerId, offer);
        return ResponseEntity.ok(updatedOffer);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable("offerId") String offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }


}
