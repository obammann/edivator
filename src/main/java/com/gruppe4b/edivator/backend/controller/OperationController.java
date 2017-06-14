package com.gruppe4b.edivator.backend.controller;

import com.google.appengine.api.images.Image;
import com.google.appengine.repackaged.com.google.gson.Gson;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.gruppe4b.edivator.backend.service.DefaultImageStoreService;
import com.gruppe4b.edivator.backend.service.ImageEditService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class OperationController {

    @Autowired
    ImageEditService imageEditService;

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
        String img = "" + imageId;
        imageEditService.resizeImage(img,10);
                String x = "afsdas";
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

    @RequestMapping(path = "/imagetest")
    public String testImage() {

        System.out.println("Upload requested.");
        // byte[] payload = requestEntity.getBody();

        DefaultImageStoreService imageStore = new DefaultImageStoreService("edivator_image_store_europe"); // TODO: use DI


        Resource resource = new ClassPathResource("garfield.jpg");
        try {
            InputStream resourceInputStream = resource.getInputStream();

            byte[] bytes = IOUtils.toByteArray(resourceInputStream);
            System.out.println("Image: " + bytes);
            imageStore.writeImageToCloudStorage(imageStore.getImageFromByteArray(bytes), "Uploaded_Garfield.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Imagetest:" + DateTime.now();

    }


    // https://stackoverflow.com/questions/14615692/how-do-i-upload-stream-large-images-using-spring-3-2-spring-mvc-in-a-restful-way
    @RequestMapping(path = "/image", method = RequestMethod.POST)
    public String uploadImage(HttpEntity<byte[]> requestEntity) {

        System.out.println("Request: Upload Image");
        byte[] payload = requestEntity.getBody();
        DefaultImageStoreService imageStore = new DefaultImageStoreService("edivator_image_store_europe"); // TODO: use DI

        String new_image_id = new Integer( Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

        String url = "No serving url...";
        try {
            url = imageStore.writeImageToCloudStorage(imageStore.getImageFromByteArray(payload), new_image_id);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle Exception properly

        }
        // TODO: Send JSON-Response with the new id or redircect link (get_serving_url())
        // https://cloud.google.com/appengine/docs/standard/python/refdocs/google.appengine.api.images#Image_get_serving_url
        System.out.println("URL: " + url);
        Gson gson = new Gson();
        return gson.toJson(url);
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