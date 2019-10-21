package com.jonathan.marsroverphotos.endpoint;

import com.jonathan.marsroverphotos.bean.Photo;
import com.jonathan.marsroverphotos.service.FileDateParseService;
import com.jonathan.marsroverphotos.service.ImageService;
import com.jonathan.marsroverphotos.service.MarsRoverService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/photos/")
@RestController
public class PhotoEndpoint {
    private FileDateParseService fdpService = new FileDateParseService();
    private MarsRoverService marsRoverService = new MarsRoverService(fdpService.getDefaultDateList());
    private ImageService imageService = new ImageService();

    /**
     * Gets all downloaded photos
     *
     * @return JSON for all photos
     */
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public List<Photo> getAllPhotos() {
        return imageService.getSyncedPhotos(marsRoverService.getPhotosList());
    }

    /**
     * Retrieves more random images for the given dates
     *
     * @return JSON for all Photos
     */
    @RequestMapping(value = "more", method = RequestMethod.GET)
    public List<Photo> getMorePhotos() {
        marsRoverService.populateImageURLs(fdpService.getDefaultDateList());
        imageService.savePhotos(marsRoverService.getPhotosList());
        return getAllPhotos();
    }

    /**
     * Initial download of all photos URL's retrieved
     *
     * @return JSON for all photos
     */
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public List<Photo> downloadPhotos() {
        imageService.savePhotos(marsRoverService.getPhotosList());
        return getAllPhotos();
    }

    /**
     * Deletes all retrieved photos
     *
     * @return true if all photos were removed
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public boolean deletePhotos() {
        boolean deleted = imageService.deleteAllPhotos();
        if (deleted) marsRoverService.purgeImages();
        return deleted;
    }
}
