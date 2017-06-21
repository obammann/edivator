package com.gruppe4b.edivator.backend.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.gruppe4b.edivator.backend.domain.ImageResponse;
import com.gruppe4b.edivator.backend.service.ImageStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class ImageUploadController {

    @Autowired
    ImageStoreService imageStoreService;

    // https://stackoverflow.com/questions/14615692/how-do-i-upload-stream-large-images-using-spring-3-2-spring-mvc-in-a-restful-way
    @RequestMapping(path = "/imageUpload", method = RequestMethod.POST)
    public String uploadImage(HttpEntity<byte[]> requestEntity) {

        System.out.println("Request: Upload Image");
        byte[] payload = requestEntity.getBody();

        String new_image_id = new Integer(Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

        String url = "No serving url...";
        try {
            url = imageStoreService.writeImageToCloudStorage(imageStoreService.getImageFromByteArray(payload), new_image_id);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle Exception properly

        }
        // TODO: Send JSON-Response with the new id or redircect link (get_serving_url())
        // https://cloud.google.com/appengine/docs/standard/python/refdocs/google.appengine.api.images#Image_get_serving_url
        ImageResponse response = new ImageResponse(url, new_image_id);
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    @RequestMapping(path = "/imageUpload/form", consumes = { "multipart/form-data" }, method = RequestMethod.POST)
    public void UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());

        String fileName = file.getOriginalFilename();
        //        System.out.println(fileName);

        byte[] payload = new byte[0];
        try {
            payload = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String new_image_id = new Integer(Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

        String url = "No serving url...";
        try {
            url = imageStoreService.writeImageToCloudStorage(imageStoreService.getImageFromByteArray(payload), new_image_id);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle Exception properly
        }

        System.out.println(url);
    }

}
