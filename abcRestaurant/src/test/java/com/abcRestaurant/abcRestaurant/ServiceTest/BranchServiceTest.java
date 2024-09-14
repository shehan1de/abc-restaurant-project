package com.abcRestaurant.abcRestaurant.ServiceTest;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Repository.BranchRepository;
import com.abcRestaurant.abcRestaurant.Service.BranchService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch1;
    private Branch branch2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        branch1 = new Branch();
        branch1.setBranchId("branch-001");
        branch1.setBranchName("Branch 1");
        branch1.setBranchAddress("Address 1");

        branch2 = new Branch();
        branch2.setBranchId("branch-002");
        branch2.setBranchName("Branch 2");
        branch2.setBranchAddress("Address 2");
    }

    @Test
    void testAllBranch() {
        List<Branch> branches = Arrays.asList(branch1, branch2);
        when(branchRepository.findAll()).thenReturn(branches);

        List<Branch> result = branchService.allBranch();

        assertEquals(2, result.size());
        assertEquals("Branch 1", result.get(0).getBranchName());
        assertEquals("Branch 2", result.get(1).getBranchName());
        verify(branchRepository, times(1)).findAll();
    }

    @Test
    void testSingleBranch_Success() {
        ObjectId branchId = new ObjectId();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch1));

        Optional<Branch> result = branchService.singleBranch(branchId);

        assertTrue(result.isPresent());
        assertEquals("Branch 1", result.get().getBranchName());
        verify(branchRepository, times(1)).findById(branchId);
    }

    @Test
    void testSingleBranch_NotFound() {
        ObjectId branchId = new ObjectId();
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());
        Optional<Branch> result = branchService.singleBranch(branchId);

        assertFalse(result.isPresent());
        verify(branchRepository, times(1)).findById(branchId);
    }

    @Test
    void testAddBranch() {
        Branch newBranch = new Branch();
        newBranch.setBranchName("New Branch");
        newBranch.setBranchAddress("New Address");

        when(branchRepository.findAll()).thenReturn(Arrays.asList(branch1, branch2));
        when(branchRepository.save(any(Branch.class))).thenReturn(newBranch);

        Branch result = branchService.addBranch(newBranch);

        assertNotNull(result);
        assertEquals("branch-003", result.getBranchId()); // New branch should have ID branch-003
        verify(branchRepository, times(1)).save(newBranch);
    }

    @Test
    void testUpdateBranch_Success() {

        String branchId = "branch-001";
        Branch updatedBranch = new Branch();
        updatedBranch.setBranchName("Updated Branch 1");
        updatedBranch.setBranchAddress("Updated Address 1");

        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.of(branch1));
        when(branchRepository.save(any(Branch.class))).thenReturn(updatedBranch);

        Branch result = branchService.updateBranch(branchId, updatedBranch);

        assertNotNull(result);
        assertEquals("Updated Branch 1", result.getBranchName());
        assertEquals("Updated Address 1", result.getBranchAddress());
        verify(branchRepository, times(1)).findByBranchId(branchId);
        verify(branchRepository, times(1)).save(branch1);
    }

    @Test
    void testUpdateBranch_NotFound() {

        String branchId = "branch-999";
        Branch updatedBranch = new Branch();
        updatedBranch.setBranchName("Updated Branch");
        updatedBranch.setBranchAddress("Updated Address");

        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.updateBranch(branchId, updatedBranch);
        });
        verify(branchRepository, times(1)).findByBranchId(branchId);
        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    void testDeleteBranch_Success() {

        String branchId = "branch-001";
        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.of(branch1));

        branchService.deleteBranch(branchId);

        verify(branchRepository, times(1)).findByBranchId(branchId);
        verify(branchRepository, times(1)).delete(branch1);
    }

    @Test
    void testDeleteBranch_NotFound() {

        String branchId = "branch-999";
        when(branchRepository.findByBranchId(branchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.deleteBranch(branchId);
        });
        verify(branchRepository, times(1)).findByBranchId(branchId);
        verify(branchRepository, never()).delete(any(Branch.class));
    }
}
