package com.gruppe4b.edivator.backend.controller;

import com.google.appengine.api.images.Image;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.gruppe4b.edivator.backend.service.DefaultImageStoreService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OperationController {


    @RequestMapping(path = "/image/{imageId}/flip", method = RequestMethod.PUT)
    public String flip(@PathVariable("imageId") int imageId,
                     @RequestParam(value = "horizontal", defaultValue = "false") boolean horizontal) {
        return "Imagine you flipped the image with id " + imageId + ".";
    }

    @RequestMapping(path = "/image/{imageId}/resize", method = RequestMethod.PUT)
    public String resize(@PathVariable("imageId") int imageId,
                       @RequestParam(value = "width", required = true) int width,
                       @RequestParam(value = "height", required = true) int height) {
        return "Imagine you resized the image with id " + imageId + ".";
    }

    @RequestMapping(path = "/image/{imageId}/crop", method = RequestMethod.PUT)
    public void crop(@PathVariable("imageId") int imageId,
                     @RequestParam(value = "top", defaultValue = "0") int top,
                     @RequestParam(value = "bottom", defaultValue = "0") int bottom,
                     @RequestParam(value = "left", defaultValue =  "0") int left,
                     @RequestParam(value = "right", defaultValue = "0") int right) {

    }

    @RequestMapping(path = "/image/{imageId}/filter/feelinglucky", method = RequestMethod.PUT)
    public void feelingLuckyFilter(@PathVariable("imageId") int imageId) {

    }

    // https://stackoverflow.com/questions/14615692/how-do-i-upload-stream-large-images-using-spring-3-2-spring-mvc-in-a-restful-way

    @RequestMapping(path = "/image/", method = RequestMethod.POST)
    public void uploadImage(HttpEntity<byte[]> requestEntity) {

        byte[] payload = requestEntity.getBody();

        DefaultImageStoreService imageStore = new DefaultImageStoreService("gruppe4b"); // TODO: use DI

        String new_image_id = new Integer(payload.hashCode() + DateTime.now().hashCode()).toString();

        imageStore.writeImageToCloudStorage(imageStore.getImageFromByteArray(payload), new_image_id);

        // TODO: Send JSON-Response with the new id or redircect link

    }



}
