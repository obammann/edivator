package com.gruppe4b.edivator.backend.service;

import java.io.IOException;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.Transform;

public interface ImageEditService {
    public Image performTransformation(Image image, Transform transform);
    public boolean uploadImageToCloud(Image image) throws Exception;
    public Image loadImageFromCloud(String imageFileName);
    public byte[] convertFileToByteArray(String imageFile) throws IOException;
}