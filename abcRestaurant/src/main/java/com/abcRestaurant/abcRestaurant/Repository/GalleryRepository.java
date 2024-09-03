package com.abcRestaurant.abcRestaurant.Repository;

import com.abcRestaurant.abcRestaurant.Model.Gallery;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends MongoRepository<Gallery, ObjectId> {
    List<Gallery> findByPictureType(String pictureType);
    Optional<Gallery> findByPictureId(String pictureId);
}
