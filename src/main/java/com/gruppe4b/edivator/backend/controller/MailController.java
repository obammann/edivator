package com.gruppe4b.edivator.backend.controller;

import com.google.appengine.api.images.Image;
import com.gruppe4b.edivator.backend.service.ImageStoreService;
import com.gruppe4b.edivator.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MailController {

    @Autowired
    MailService mailService;

    @Autowired
    ImageStoreService imageStoreService;

    @RequestMapping(path = "/image/{imageId}/sendMail", method = RequestMethod.PUT)
    public String sendMail(@PathVariable("imageId") String imageId,
                       @RequestParam(value = "mail", required = true) String mailAddress) {
        Image img = imageStoreService.getImageFromCloudStorage(imageId);
        if(img == null) {
            return "Image not found...";
        } else {
            mailService.sendImage(img.getImageData(), mailAddress);
            return "Sended Image.";
        }
    }

}
