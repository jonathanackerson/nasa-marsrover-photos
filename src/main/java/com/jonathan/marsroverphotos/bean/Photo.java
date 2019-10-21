package com.jonathan.marsroverphotos.bean;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class Photo {
    static final String APP_FILE_PATH = "rover/";

    private String localFilePath; // Local file path
    private File file;          // file
    private URL url;            // img_src
    private String camera;      // camera.full_name
    private String date;        // earth_date
    private String roverName;   // rover.name

    public Photo(URL url, String camera, String roverName) {
        this.url = url;
        this.camera = camera;
        this.roverName = roverName;
    }

    public Photo(File file) {
        configureFile(file);
    }

    public Photo(String filename) {
        configureFile(filename);
    }

    public void configureFile(String filename) {
        String filePath = !Objects.equals(filename, "") ? createRelativeFilename(filename) : "";
        File file = new File(filePath);
        configureFile(file);
    }

    public void configureFile(File file) {
        if (file.exists() && file.isFile()) {
            this.file = file;
            this.localFilePath = createRelativeFilename(file.getName());
        } else {
            this.localFilePath = "";
        }
    }

    private String createRelativeFilename(String fname) {
        return APP_FILE_PATH + fname;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public File getFile() {
        return file;
    }

    public URL getUrl() {
        return url;
    }

    public String getCamera() {
        return camera;
    }

    public String getDate() {
        return date;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
