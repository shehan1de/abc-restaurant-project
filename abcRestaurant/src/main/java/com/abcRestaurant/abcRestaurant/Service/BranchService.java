package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Repository.BranchRepository;
import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    // Get all branches
    public List<Branch> allBranch() {
        return branchRepository.findAll();
    }

    // Get a single branch by id
    public Optional<Branch> singleBranch(ObjectId id) {
        return branchRepository.findById(id);
    }

    // Add a new branch
    public Branch addBranch(Branch branch) {
        branch.setBranchId(generateBranchId());
        return branchRepository.save(branch);
    }

    // Generate a new branch ID
    private String generateBranchId() {
        long count = branchRepository.count();
        return String.format("branch-%03d", count + 1);
    }

    // Update an existing branch by id
    public Branch updateBranch(ObjectId id, Branch branch) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        branch.setId(id);
        return branchRepository.save(branch);
    }

    // Delete a branch by id
    public void deleteBranch(ObjectId id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id " + id);
        }
        branchRepository.deleteById(id);
    }
}
