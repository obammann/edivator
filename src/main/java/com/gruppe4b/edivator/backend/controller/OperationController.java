package com.gruppe4b.edivator.backend.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.gruppe4b.edivator.backend.service.DefaultImageStoreService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class OperationController {

    @RequestMapping (path="/test/operationsrunning", method = RequestMethod.GET)
    public String testAnswer(){
        return  "Route kommt auch hier an.";
    }


    @RequestMapping(path = "/image/{imageId}/flip", method = RequestMethod.PUT)
    public String flip(@PathVariable("imageId") int imageId,
                     @RequestParam(value = "horizontal", defaultValue = "false") boolean horizontal) {
        return "Imagine you flipped the image with id " + imageId + " " + (horizontal ? "horizontally" : "vertically") + ".";
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

      //  imageStore.writeImageToCloudStorage(imageStore.getImageFromByteArray(payload), new_image_id);

        // TODO: Send JSON-Response with the new id or redircect link

    }


    @RequestMapping(path="/newDocument", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public void UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr=request.getFileNames();

        MultipartFile file=request.getFile(itr.next());


        String fileName=file.getOriginalFilename();
        System.out.println(fileName);

        byte[] payload = new byte[0];
        try {
            payload = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DefaultImageStoreService imageStore = new DefaultImageStoreService("edivator_image_store_europe"); // TODO: use DI

        String new_image_id = new Integer( Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

        String url = "No serving url...";
        try {
            url = imageStore.writeImageToCloudStorage(imageStore.getImageFromByteArray(payload), new_image_id);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle Exception properly

        }

        System.out.println(url);
    }

}
