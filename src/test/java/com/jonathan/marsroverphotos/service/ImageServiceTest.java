package com.jonathan.marsroverphotos.service;

import com.jonathan.marsroverphotos.bean.Photo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageServiceTest {
//    private static File testFile = new File("frontend/public/images/RLB_541484975EDR_F0611140RHAZ00341M_.JPG");
    private static File testFile = new File("photos/rover/RLB_541484975EDR_F0611140RHAZ00341M_.JPG");
    private ImageService imageService = new ImageService();

    @BeforeAll
    static void setUp() {
        if (testFile.exists()) testFile.delete();
    }

    @AfterAll
    static void tearDown() {
        if (testFile.exists()) testFile.delete();
    }

    @Test
    void savePhotos() {
        List<Photo> photoList = new ArrayList<>();

        try {
            URL url = new URL("http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01622/opgs/edr/rcam/RLB_541484975EDR_F0611140RHAZ00341M_.JPG");
            Photo photo = new Photo(url, "", "");
            photoList.add(photo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        imageService.savePhotos(photoList);

        assertTrue(testFile.exists() && testFile.isFile());
    }

    @Test
    void getAllPhotos() {
//        List<Photo> photos = imageService.getAllPhotos();

    }

    @Test
    void delteAllPhotos() {
//        imageService.delteAllPhotos(); // This will delete all photos...
    }
}