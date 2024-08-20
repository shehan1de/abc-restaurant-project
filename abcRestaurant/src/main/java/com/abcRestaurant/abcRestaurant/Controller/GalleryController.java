package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Model.Gallery;
import com.abcRestaurant.abcRestaurant.Service.GalleryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

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
    public ResponseEntity<Gallery> addGallery(@RequestBody Gallery gallery) {
        Gallery newGallery = galleryService.addGallery(gallery);
        return new ResponseEntity<>(newGallery, HttpStatus.CREATED);
    }

    // Update an existing image by id
    @PutMapping("/{id}")
    public ResponseEntity<Gallery> updateGallery(@PathVariable("id") ObjectId id, @RequestBody Gallery gallery) {
        Gallery updateGallery = galleryService.updateGallery(id, gallery);
        return ResponseEntity.ok(updateGallery);
    }

    // Delete a image by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable("id") ObjectId id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }
}
