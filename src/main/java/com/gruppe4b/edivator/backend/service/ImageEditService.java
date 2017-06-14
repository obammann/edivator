package com.gruppe4b.edivator.backend.service;

import java.io.IOException;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.Transform;

public interface ImageEditService {
    public Image performTransformation(Image image, Transform transform);
    public boolean uploadImageToCloud(Image image) throws Exception;
    public Image loadImageFromCloud(String imageFileName);
    public byte[] convertFileToByteArray(String imageFilePath) throws IOException;

    public int resizeImage(String imageId, int percentage);
    public int flip(String imageId,boolean horizontal);
    public int turnLeft(String imageId);
    public int turnRight(String imageId);
    public int cropHeight(String imageId);
    public int cropWidth(String imageId);
    public int applyLuckyFilter(String imageId);

}