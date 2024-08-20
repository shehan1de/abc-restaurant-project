package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Feedback;
import com.abcRestaurant.abcRestaurant.Repository.FeedbackRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    // Get all feedbacks
    public List<Feedback> allFeedbacks() {
        return feedbackRepository.findAll();
    }

    // Get a single feedback by id
    public Optional<Feedback> singleFeedback(ObjectId id) {
        return feedbackRepository.findById(id);
    }

    // Add a new feedback
    public Feedback addFeedback(Feedback feedback) {
        feedback.setFeedbackId(generateFeedbackId());
        return feedbackRepository.save(feedback);
    }

    // Generate a new feedback ID
    private String generateFeedbackId() {
        long count = feedbackRepository.count();
        return String.format("feedback-%03d", count + 1);
    }

    // Update an existing feedback by id
    public Feedback updateFeedback(ObjectId id, Feedback feedback) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        feedback.setId(id);

        // Preserve the original feedbackId
        Feedback existingFeedback = feedbackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Feedback not found with id " + id));
        feedback.setFeedbackId(existingFeedback.getFeedbackId());

        return feedbackRepository.save(feedback);
    }

    // Delete a feedback by id
    public void deleteFeedback(ObjectId id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id " + id);
        }
        feedbackRepository.deleteById(id);
    }
}
