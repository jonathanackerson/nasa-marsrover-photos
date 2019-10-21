package com.jonathan.marsroverphotos;

import com.jonathan.marsroverphotos.service.FileDateParseService;
import com.jonathan.marsroverphotos.service.ImageService;
import com.jonathan.marsroverphotos.service.MarsRoverService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarsroverphotosApplication {

    public static void main(String[] args) {
//        runApp();
        SpringApplication.run(MarsroverphotosApplication.class, args);
    }

    public static void runApp() {
        FileDateParseService fdpService = new FileDateParseService();
        MarsRoverService marsRoverService = new MarsRoverService(fdpService.getDefaultDateList());
        ImageService imageService = new ImageService();
        imageService.savePhotos(marsRoverService.getPhotosList());
    }

}
