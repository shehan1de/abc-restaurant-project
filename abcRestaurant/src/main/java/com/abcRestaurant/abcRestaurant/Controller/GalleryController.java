package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Model.Gallery;
import com.abcRestaurant.abcRestaurant.Service.FileStorageService;
import com.abcRestaurant.abcRestaurant.Service.GalleryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<Gallery>> getAllGalleries(@RequestParam(required = false) String pictureType) {
        List<Gallery> galleries;
        if (pictureType == null || pictureType.isEmpty()) {
            galleries = galleryService.allGallery();
        } else {
            galleries = galleryService.findByPictureType(pictureType);
        }
        return new ResponseEntity<>(galleries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Gallery>> getSingleGallery(@PathVariable ObjectId id) {
        return new ResponseEntity<>(galleryService.singleGallery(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addGalley(

            @RequestParam("pictureType") String pictureType,
            @RequestParam("picturePath") MultipartFile picturePath) {

        try {
            // Generate a unique filename for the image
            String filename = generateUniqueFilename(picturePath.getOriginalFilename());

            // Save the file
            fileStorageService.saveFile(picturePath.getBytes(), filename);

            // Create gallery object
            Gallery gallery = new Gallery();
            gallery.setPictureType(pictureType);
            gallery.setPicturePath(filename);

            // Save the Gallery
            Gallery newGallery = galleryService.addGallery(gallery);
            return new ResponseEntity<>(newGallery, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "image-" + timestamp + extension;
    }

    @PutMapping("/{pictureId}")
    public ResponseEntity<Map<String, Object>> updateGallery(
            @PathVariable("pictureId") String pictureId,
            @RequestParam(required = false) String pictureType,
            @RequestParam(value = "picturePath", required = false) MultipartFile picturePath) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Update the gallery using the service method
            Gallery updatedGallery = galleryService.updateGallery(pictureId, pictureType, picturePath);

            response.put("status", "success");
            response.put("gallery", updatedGallery);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "File upload failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (ResourceNotFoundException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{pictureId}")
    public ResponseEntity<Map<String, String>> deleteGallery(@PathVariable("pictureId") String pictureId) {
        Map<String, String> response = new HashMap<>();
        try {
            // Delete the gallery using the service method
            galleryService.deleteGallery(pictureId);

            response.put("status", "success");
            response.put("message", "Gallery image deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
