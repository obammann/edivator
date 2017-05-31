package com.gruppe4b.edivator.backend.service;

import com.google.appengine.api.images.Image;

import java.io.IOException;

public interface ImageStoreService {

    public void writeImageToCloudStorage(Image image, String name) throws IOException;

    public Image getImageFromCloudStorage(String name);

    public Image getImageFromByteArray(byte[] image);

}
