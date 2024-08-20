package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Gallery;
import com.abcRestaurant.abcRestaurant.Repository.GalleryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    // Get all Images
    public List<Gallery> allGallery() {
        return galleryRepository.findAll();
    }

    // Get a single image by id
    public Optional<Gallery> singleGallery(ObjectId id) {
        return galleryRepository.findById(id);
    }

    // Add a new image
    public Gallery addGallery(Gallery gallery) {
        gallery.setPictureId(generateGalleryId());
        return galleryRepository.save(gallery);
    }

    // Generate a new product ID
    private String generateGalleryId() {
        long count = galleryRepository.count();
        return String.format("image-%03d", count + 1);
    }


    // Update an existing image by id
    public Gallery updateGallery(ObjectId id, Gallery gallery) {
        if (!galleryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image not found with id " + id);
        }
        // Ensure the ID in the request body matches the ID in the URL
        gallery.setId(id);
        return galleryRepository.save(gallery);
    }

    // Delete a image by id
    public void deleteGallery(ObjectId id) {
        if (!galleryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image not found with id " + id);
        }
        galleryRepository.deleteById(id);
    }

    public List<Gallery> findByPictureType(String pictureType) {
        return galleryRepository.findByPictureType(pictureType);
    }
}