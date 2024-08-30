package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
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

    @PutMapping("/{branchId}")
    public ResponseEntity<Branch> updateBranch(@PathVariable("branchId") String branchId, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(branchId, branch);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("branchId") String branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}