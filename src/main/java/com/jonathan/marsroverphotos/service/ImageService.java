package com.jonathan.marsroverphotos.service;

import com.jonathan.marsroverphotos.bean.Photo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Save images locally
 */
public class ImageService {
//    private static final String PHOTO_DIR = "frontend/public/images/";
    private static final String PHOTO_DIR = "photos/rover/";

    public ImageService() {
    }

    /**
     * Allows passing a list into the service to save the photos
     *
     * @param photos list of URLs to save
     */
    public void savePhotos (List<Photo> photos) {
        photos.forEach(ImageService::downloadImageFromPhoto);
    }

    /**
     * Sync JSON photos retrieved and photos that might already be downloaded
     *
     * @param photos gets synced photo list
     * @return List of Photos
     */
    public List<Photo> getSyncedPhotos(List<Photo> photos) {
        List<Photo> downloadedPhotos = getDownloadedPhotos();
        String localfname;
        for (Photo loadedPhoto : downloadedPhotos) {
            localfname = loadedPhoto.getLocalFilePath().replaceAll(".*/","");
            boolean addPhoto = true;
            for (Photo photo : photos) {
                // No need to repeat photos with the same url
                if (photo.getUrl() != null && localfname.equals(generateFileName(photo.getUrl()))) {
                    addPhoto = false;
                    photo.configureFile(loadedPhoto.getFile());
                }
                if (!addPhoto || loadedPhoto.getLocalFilePath().equals(photo.getLocalFilePath())) {
                    addPhoto = false;
                    break;
                }
            }
            if (addPhoto) photos.add(loadedPhoto);
        }

        return photos;
    }

    /**
     * Gets all photos from the photo directory
     *
     * @return List of Photos in the photo directory
     */
    public List<Photo> getDownloadedPhotos() {
        List<Photo> photos = new ArrayList<>();

        File photoDir = new File(PHOTO_DIR);
        File[] files = photoDir.listFiles();
        if (files != null && files.length < 1) return photos;

        for (File file : files) {
            System.out.println("files: " + file);
            photos.add(new Photo(file));
        }

        return photos;
    }

    /**
     * Deletes all photos from the photo directory
     *
     * @return true if all photos have been deleted
     */
    public boolean deleteAllPhotos() {
        List<Photo> photos = getDownloadedPhotos();
        photos.forEach(p -> p.getFile().delete());
        return getDownloadedPhotos().size() <= 0;
    }


    /**
     * Download image from URL
     *
     * @param photo Takes a Photo from JSON
     */
    private static void downloadImageFromPhoto(Photo photo) {
        if (photo.getUrl() == null) return;
        try {
            InputStream in = photo.getUrl().openStream();
            String filename = generateFileName(photo.getUrl());

            //Photo Dir needs to be configurable

            // No need to get file if we already have it
            if (!checkForFile(filename)) Files.copy(in, Paths.get(PHOTO_DIR + filename));
            if (checkForFile(filename)) photo.configureFile(filename);
            in.close();
        } catch (IOException e) {
            System.out.println("Unable to get or save file from - " + photo.getUrl());
            e.printStackTrace();
        }
    }

    private static String generateFileName(URL url) {
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private static boolean checkForFile(String filename) {
        File file = new File(PHOTO_DIR + filename);
        return file.exists();
    }


}
