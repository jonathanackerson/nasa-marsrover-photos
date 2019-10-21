package com.jonathan.marsroverphotos.service;

import com.jonathan.marsroverphotos.bean.Photo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service to Call NASA API to retrieve random images for specific dates
 */
public class MarsRoverService {
    private static final String API_KEY = "LpC7lceFeB1D87wz5oc00BNqQTWfawv4gV2tRvq7";
    private static final String NASA_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%s&api_key=" + API_KEY;
    private List<Photo> photosList = new ArrayList<>();
    private List<URL> imageUrls;

    private static DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("Y-MM-dd", Locale.ENGLISH);

    /**
     * Service to call Mars Rover API and retrieve photos for given dates
     *
     * @param dates     - List of LocalDates to get photos for
     */
    public MarsRoverService(List<LocalDate> dates) {
        populateImageURLs(dates);
    }

    /**
     * @return List of Photos from NASA API
     */
    public List<Photo> getPhotosList() {
        return photosList;
    }

    /**
     * @return List of Image URLs from NASA API
     */
    public List<URL> getImageUrls() {
        return imageUrls;
    }

    /**
     * @param dates     - dates to pull images from
     */
    public void populateImageURLs(List<LocalDate> dates) {
        List<URL> urls = constructURLsForRoverAPI(dates);
        List<Photo> newPhotos = getRandomImagePerAPICall(urls);

        if (photosList.size() > 0) {
            // remove duplicates
            for (Photo nphoto : newPhotos) {
                boolean addphoto = true;
                for (Photo photo : photosList) {
                    if (Objects.equals(photo.getDate(), nphoto.getDate()) &&
                            (Objects.equals(photo.getLocalFilePath(), nphoto.getLocalFilePath()) ||
                                    Objects.equals(photo.getUrl(), nphoto.getUrl()))) {
                        addphoto = false;
                        break;
                    }
                }
                if (addphoto) photosList.add(nphoto);
            }
        } else {
            photosList.addAll(newPhotos);
        }
    }

    /**
     * Remove all photos from photoList
     */
    public void purgeImages() {
        photosList.clear();
    }

    private static List<URL> constructURLsForRoverAPI(List<LocalDate> dates) {
        List<URL> urls = new ArrayList<>();

        for (LocalDate date : dates) {
            try {
                urls.add(new URL(String.format(NASA_URL, date.format(myFormat))));
            } catch (MalformedURLException e) {
                System.out.println("Bad url...");
                e.printStackTrace();
            }
        }

        return urls;
    }

    private static List<Photo> getRandomImagePerAPICall(List<URL> urls) {
        List<Photo> photos = new ArrayList<>();

        // Get JSON for URL
        for (URL url : urls) {
            try {
                Photo photo = getRandomImageFromJSON(getJSONFromAPI(url));
                photo.setDate(url.toString().split("earth_date=|&")[1]);
                if (photo.getUrl() == null) System.out.println("No Image found for url - " + url);
                photos.add(photo);
            } catch (IOException e) {
                System.out.println("Could not get JSON from - " + url);
                e.printStackTrace();
            }
        }

        // print photos
        // photos.forEach(System.out::println);

        return photos;
    }

    private static JSONArray getJSONFromAPI(URL url) throws IOException {
        StringBuilder jsonInput = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Bad response code - " + responseCode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                jsonInput.append(sc.nextLine());
            }
            sc.close();
        }

        JSONParser jParser = new JSONParser();
        JSONObject jObj;
        JSONArray jArray = new JSONArray();
        try {
            jObj = (JSONObject)jParser.parse(jsonInput.toString());
        } catch (ParseException e) {
            System.out.println("Unable to parse JSON");
            e.printStackTrace();
            return jArray;
        }
        jArray = (JSONArray) jObj.get("photos");

//        display json
//        System.out.println("\nJSON data in string format\n" + jsonInput);

        return jArray;
    }

    private static Photo getRandomImageFromJSON(JSONArray jArr) {
        Photo photo = null;

        if (!jArr.isEmpty()) {
            Random rand = new Random();
            int randInt = rand.nextInt(jArr.size());
//            System.out.printf("Array size = %s, random = %s\n", jArr.size(), randInt);

            JSONObject jObj1 = (JSONObject) jArr.get(randInt);
            try {
                URL imageURL = new URL((String) jObj1.get("img_src"));
                JSONObject jObjCamera = (JSONObject) jObj1.get("camera");
                String camera = (String) jObjCamera.get("full_name");
                JSONObject jObjName = (JSONObject) jObj1.get("rover");
                String name = (String) jObjName.get("name");
                photo = new Photo(imageURL, camera, name);
            } catch (MalformedURLException e) {
                System.out.println("Bad URL found in JSON...");
                e.printStackTrace();
            }
        }

        return photo == null ? new Photo("") : photo;
    }

}
