package com.gruppe4b.edivator.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.*;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import org.springframework.stereotype.Service;

@Service
public class ImageEditServiceImpl implements ImageEditService {

    private final String bucket = "gruppe-4b.appspot.com";

    private final ImagesService imagesService;

    public ImageEditServiceImpl() {
        imagesService = ImagesServiceFactory.getImagesService();
    }

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    @Override
    public Image performTransformation(Image image, Transform transform) {
        return imagesService.applyTransform(transform, image);
    }

    @Override
    public boolean uploadImageToCloud(Image image) {
        try {
            String imageFormat = image.getFormat().toString();
            gcsService.createOrReplace(
                    new GcsFilename(bucket, "image." + imageFormat),
                    new GcsFileOptions.Builder().mimeType("image/" + imageFormat).build(),
                    ByteBuffer.wrap(convertFileToByteArray("image.png")));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public Image loadImageFromCloud(String imageFileName) {
        return null;
    }

    @Override
    public byte[] convertFileToByteArray(String imageFilePath) throws IOException {

        FileChannel fileChannel;
        ByteBuffer byteBuffer;
        try (FileInputStream fileInputStream = new FileInputStream(new File("img/image.png"))) {
            fileChannel = fileInputStream.getChannel();
        }
        byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(byteBuffer);

        return byteBuffer.array();
    }

    public int resizeImage(String imageId, int percentage) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image resizeImage = ImagesServiceFactory.makeImageFromBlob(key);

        int origWidth = resizeImage.getWidth();
        int origHeight = resizeImage.getHeight();
        Transform transform = ImagesServiceFactory.makeResize(origWidth * percentage, origHeight * percentage);
        Image resizedImage = imagesService.applyTransform(transform, resizeImage);

        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int flip(String imageId,boolean horizontal) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image flippingImage = ImagesServiceFactory.makeImageFromBlob(key);

        Transform transform = ImagesServiceFactory.makeHorizontalFlip();
        if (horizontal == true) {
            transform = ImagesServiceFactory.makeHorizontalFlip();
        } else if (horizontal == false) {
            transform = ImagesServiceFactory.makeVerticalFlip();
        }
        Image flippedImage = imagesService.applyTransform(transform, flippingImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int turnLeft(String imageId) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image turnImage = ImagesServiceFactory.makeImageFromBlob(key);

        Transform transform = ImagesServiceFactory.makeRotate(-90);
        Image turnedImage = imagesService.applyTransform(transform, turnImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int turnRight(String imageId) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image turnImage = ImagesServiceFactory.makeImageFromBlob(key);

        Transform transform = ImagesServiceFactory.makeRotate(90);
        Image turnedImage = imagesService.applyTransform(transform, turnImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int cropHeight(String imageId) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image croppingImage = ImagesServiceFactory.makeImageFromBlob(key);

        int width = croppingImage.getWidth();
        int height = croppingImage.getHeight();
        float crop = height * 10 / 100;

        Transform transform = ImagesServiceFactory.makeCrop(0,crop,0,crop);
        Image croppedImage = imagesService.applyTransform(transform, croppingImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int cropWidth(String imageId) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image croppingImage = ImagesServiceFactory.makeImageFromBlob(key);

        int width = croppingImage.getWidth();
        int height = croppingImage.getHeight();
        float crop = width * 10 / 100;

        Transform transform = ImagesServiceFactory.makeCrop(crop,0,crop,0);
        Image croppedImage = imagesService.applyTransform(transform, croppingImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }

    public int applyLuckyFilter(String imageId) {
        //TODO: richtiges Image holen
        BlobKey key = new BlobKey("xx");
        Image filterImage = ImagesServiceFactory.makeImageFromBlob(key);

        Transform transform = ImagesServiceFactory.makeImFeelingLucky();
        Image filteredImage = imagesService.applyTransform(transform, filterImage);
        //TODO: Image speichern und neue ID zurückgeben
        int newId = 0; //Ersetzen!
        return newId;
    }
}
