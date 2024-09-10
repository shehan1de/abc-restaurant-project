package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Branch;
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

    @Autowired
    private FeedResEmailService feedResEmailService;

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
        List<Feedback> feedbacks = feedbackRepository.findAll();
        int maxId = 0;
        for (Feedback feedback : feedbacks) {
            String feedbackId = feedback.getFeedbackId();
            try {
                int numericPart = Integer.parseInt(feedbackId.split("-")[1]);
                if (numericPart > maxId) {
                    maxId = numericPart;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing feedbackId: " + feedbackId + ". Skipping this entry.");
            }
        }
        int nextId = maxId + 1;
        return String.format("feedback-%03d", nextId);
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
    public Feedback updateFeedbackResponseByFeedbackId(String feedbackId, String staffResponse) {
        Feedback feedback = feedbackRepository.findByFeedbackId(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with feedbackId " + feedbackId));

        String userMessage = feedback.getMessage(); // Retrieve the user's original message

        feedback.setStaffResponse(staffResponse);
        Feedback updatedFeedback = feedbackRepository.save(feedback);

        // Send email with the staff response and user's original message
        feedResEmailService.sendFeedbackResponseEmail(
                feedback.getEmail(),
                feedback.getName(),
                staffResponse,
                userMessage
        );

        return updatedFeedback;
    }

}
