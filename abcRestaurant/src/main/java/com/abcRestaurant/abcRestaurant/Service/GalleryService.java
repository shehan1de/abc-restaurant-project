package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Branch;
import com.abcRestaurant.abcRestaurant.Model.Category;
import com.abcRestaurant.abcRestaurant.Model.Gallery;
import com.abcRestaurant.abcRestaurant.Repository.GalleryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private FileStorageService fileStorageService;

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
        gallery.setPictureId(generatePictureId());
        return galleryRepository.save(gallery);
    }

    private String generatePictureId() {
        List<Gallery> galleries = galleryRepository.findAll();
        int maxId = 0;
        for (Gallery gallery : galleries) {
            String pictureId = gallery.getPictureId();
            try {
                int numericPart = Integer.parseInt(pictureId.split("-")[1]);
                if (numericPart > maxId) {
                    maxId = numericPart;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing branchId: " + pictureId + ". Skipping this entry.");
            }
        }
        int nextId = maxId + 1;
        return String.format("image-%03d", nextId);
    }






    public Gallery updateGallery(String pictureId, String pictureType, MultipartFile picturePath) throws IOException {
        Gallery existingGallery = galleryRepository.findByPictureId(pictureId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + pictureId));

        String galleryImageFilename = handleGalleryImageUpload(picturePath);

        if (pictureType != null && !pictureType.isEmpty()) {
            existingGallery.setPictureType(pictureType);
        }

        if (galleryImageFilename != null) {
            existingGallery.setPicturePath(galleryImageFilename);
        }

        return galleryRepository.save(existingGallery);
    }

    private String handleGalleryImageUpload(MultipartFile picturePath) throws IOException {
        if (picturePath != null && !picturePath.isEmpty()) {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String originalFilename = picturePath.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // Construct the new image file name with a timestamp for uniqueness
            String galleryImageFilename = "image-" + timestamp + fileExtension;
            fileStorageService.saveFile(picturePath.getBytes(), galleryImageFilename);
            return galleryImageFilename;
        }
        return null;
    }

    public void deleteGallery(String pictureId) {
        Gallery existingGallery = galleryRepository.findByPictureId(pictureId)
                .orElseThrow(() -> new ResourceNotFoundException("Picture not found with id " + pictureId));

        galleryRepository.delete(existingGallery);
    }




    public List<Gallery> findByPictureType(String pictureType) {
        return galleryRepository.findByPictureType(pictureType);
    }
}