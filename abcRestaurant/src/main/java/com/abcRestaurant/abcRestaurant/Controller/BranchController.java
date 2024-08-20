package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Service.BranchService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return new ResponseEntity<>(branchService.allBranch(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Branch>> getSingleBranch(@PathVariable ObjectId id) {
        return new ResponseEntity<>(branchService.singleBranch(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(branch);
        return new ResponseEntity<>(newBranch, HttpStatus.CREATED);
    }

    // Update an existing branch by id
    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable("id") ObjectId id, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(id, branch);
        return ResponseEntity.ok(updatedBranch);
    }

    // Delete a branch by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") ObjectId id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}