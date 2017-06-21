package com.gruppe4b.edivator.backend.service;

import com.google.appengine.api.images.Image;
import org.springframework.stereotype.Service;

public interface MailService {
    public void sendImage(Image image, String recipient);
}
