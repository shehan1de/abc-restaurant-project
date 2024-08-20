package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Feedback;
import com.abcRestaurant.abcRestaurant.Service.FeedbackService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return new ResponseEntity<>(feedbackService.allFeedbacks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Feedback>> getSingleFeedback(@PathVariable ObjectId id) {
        return new ResponseEntity<>(feedbackService.singleFeedback(id), HttpStatus.OK);
    }

    // Add a new feedback
    @PostMapping
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        Feedback newFeedback = feedbackService.addFeedback(feedback);
        return new ResponseEntity<>(newFeedback, HttpStatus.CREATED);
    }

    // Update an existing feedback by id
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable("id") ObjectId id, @RequestBody Feedback feedback) {
        Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
        return ResponseEntity.ok(updatedFeedback);
    }

    // Delete a feedback by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable("id") ObjectId id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
