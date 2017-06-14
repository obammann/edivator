package com.gruppe4b.edivator.backend.service;

import java.io.IOException;

import com.google.appengine.api.images.Image;

public interface ImageStoreService {

    public String writeImageToCloudStorage(Image image, String name) throws IOException;

    public Image getImageFromCloudStorage(String name);

    public Image getImageFromByteArray(byte[] image);

}
