package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Model.User;
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

    private String generateBranchId() {
        List<Branch> branches = branchRepository.findAll();
        int maxId = 0;
        for (Branch branch : branches) {
            String branchId = branch.getBranchId();
            try {
                int numericPart = Integer.parseInt(branchId.split("-")[1]);
                if (numericPart > maxId) {
                    maxId = numericPart;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing userId: " + branchId + ". Skipping this entry.");
            }
        }
        int nextId = maxId + 1;
        return String.format("branch-%03d", nextId);
    }


    public Branch updateBranch(String branchId, Branch branch) {
        Branch existingBranch = branchRepository.findByBranchId(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + branchId));

        existingBranch.setBranchName(branch.getBranchName());
        existingBranch.setBranchAddress(branch.getBranchAddress());

        return branchRepository.save(existingBranch);
    }


    public void deleteBranch(String branchId) {
        Branch existingBranch = branchRepository.findByBranchId(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + branchId));
        branchRepository.delete(existingBranch);
    }
}
