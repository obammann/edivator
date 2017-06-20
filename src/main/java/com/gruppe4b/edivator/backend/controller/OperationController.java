package com.gruppe4b.edivator.backend.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.gruppe4b.edivator.backend.domain.ImageResponse;
import com.gruppe4b.edivator.backend.service.ImageEditService;
import com.gruppe4b.edivator.backend.service.ImageStoreService;
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

@RestController
public class OperationController {

    @Autowired
    ImageEditService imageEditService;

    @Autowired
    ImageStoreService imageStoreService;

    //    @RequestMapping (path="/test/operationsrunning", method = RequestMethod.GET)
    //    public String testAnswer(){
    //        return  "Route kommt auch hier an.";
    //    }


    @RequestMapping(path = "/image/{imageId}/flip", method = RequestMethod.PUT)
    public String flip(@PathVariable("imageId") String imageId,
                     @RequestParam(value = "horizontal", defaultValue = "false") boolean horizontal) {
        return imageEditService.flip(imageId,horizontal);
    }

    @RequestMapping(path = "/image/{imageId}/rotate", method = RequestMethod.PUT)
    public String rotate(@PathVariable("imageId") String imageId,
                         @RequestParam(value = "left", defaultValue = "false") boolean left) {
        String response = "Error";
        if(left) {
            response = imageEditService.turnLeft(imageId);
        } else {
            response = imageEditService.turnRight(imageId);
        }
        return response;
    }

    @RequestMapping(path = "/image/{imageId}/resize", method = RequestMethod.PUT)
    public String resize(@PathVariable("imageId") String imageId,
                       @RequestParam(value = "percentage", required = true) int percantage) {
        return imageEditService.resizeImage(imageId,percantage);
    }

    @RequestMapping(path = "/image/{imageId}/crop", method = RequestMethod.PUT)
    public String crop(@PathVariable("imageId") String imageId,
                     @RequestParam(value = "cropHeight", defaultValue = "false") boolean height) {
        return imageEditService.crop(imageId,height);
    }

    @RequestMapping(path = "/image/{imageId}/filter/feelinglucky", method = RequestMethod.PUT)
    public String feelingLuckyFilter(@PathVariable("imageId") String imageId) {
        return imageEditService.applyLuckyFilter(imageId);
    }

    @RequestMapping(path = "/imagetest")
    public String testImage() {

        System.out.println("Upload requested.");
        // byte[] payload = requestEntity.getBody();

        Resource resource = new ClassPathResource("garfield.jpg");
        try {
            InputStream resourceInputStream = resource.getInputStream();

            byte[] bytes = IOUtils.toByteArray(resourceInputStream);
            System.out.println("Image: " + bytes);
            imageStoreService.writeImageToCloudStorage(imageStoreService.getImageFromByteArray(bytes), "Uploaded_Garfield.jpg");

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

        String new_image_id = new Integer( Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

        String url = "No serving url...";
        try {
            url = imageStoreService.writeImageToCloudStorage(imageStoreService.getImageFromByteArray(payload), new_image_id);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle Exception properly

        }
        // TODO: Send JSON-Response with the new id or redircect link (get_serving_url())
        // https://cloud.google.com/appengine/docs/standard/python/refdocs/google.appengine.api.images#Image_get_serving_url
        ImageResponse response = new ImageResponse(url,new_image_id);
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    @RequestMapping(path = "/imageForm", consumes = { "multipart/form-data" }, method = RequestMethod.POST)
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

        String new_image_id = new Integer( Math.abs(new Integer(payload.hashCode() + DateTime.now().hashCode()).hashCode())).toString();

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