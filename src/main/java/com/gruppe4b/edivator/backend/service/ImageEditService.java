package com.gruppe4b.edivator.backend.service;

import java.io.IOException;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.Transform;

public interface ImageEditService {
    public Image performTransformation(Image image, Transform transform);
    public boolean uploadImageToCloud(Image image) throws Exception;
    public Image loadImageFromCloud(String imageFileName);
    public byte[] convertFileToByteArray(String imageFilePath) throws IOException;

    public String resizeImage(String imageId, int wishedWidth, int wishedHeight);
    public String flip(String imageId,boolean horizontal);
    public String turnLeft(String imageId);
    public String turnRight(String imageId);
    public String crop(String imageId, float leftBorder, float rightBorder, float topBorder, float bottomBorder);
    public String applyLuckyFilter(String imageId);

}