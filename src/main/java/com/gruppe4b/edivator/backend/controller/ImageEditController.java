package com.gruppe4b.edivator.backend.controller;

import com.gruppe4b.edivator.backend.service.ImageEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageEditController {

    @Autowired
    ImageEditService imageEditService;

    @RequestMapping(path = "/image/{imageId}/flip", method = RequestMethod.PUT)
    public String flip(@PathVariable("imageId") String imageId,
            @RequestParam(value = "horizontal", defaultValue = "false") boolean horizontal) {
        return imageEditService.flip(imageId, horizontal);
    }

    @RequestMapping(path = "/image/{imageId}/rotate", method = RequestMethod.PUT)
    public String rotate(@PathVariable("imageId") String imageId,
            @RequestParam(value = "left", defaultValue = "false") boolean left) {
        String response = "Error";
        if (left) {
            response = imageEditService.turnLeft(imageId);
        } else {
            response = imageEditService.turnRight(imageId);
        }
        return response;
    }

    @RequestMapping(path = "/image/{imageId}/resize", method = RequestMethod.PUT)
    public String resize(@PathVariable("imageId") String imageId,
            @RequestParam(value = "wishedWidth", required = true) int wishedWidth,
                         @RequestParam(value="wishedHeight", required = true) int wishedHeight) {
        return imageEditService.resizeImage(imageId,wishedWidth,wishedHeight);
    }

    @RequestMapping(path = "/image/{imageId}/crop", method = RequestMethod.PUT)
    public String crop(@PathVariable("imageId") String imageId,
            @RequestParam(value = "cropHeight", defaultValue = "false") boolean height,
            @RequestParam(value = "crop", required = true) int crop) {
        return imageEditService.crop(imageId, height, crop);
    }

    @RequestMapping(path = "/image/{imageId}/filter/feelinglucky", method = RequestMethod.PUT)
    public String feelingLuckyFilter(@PathVariable("imageId") String imageId) {
        return imageEditService.applyLuckyFilter(imageId);
    }
}
