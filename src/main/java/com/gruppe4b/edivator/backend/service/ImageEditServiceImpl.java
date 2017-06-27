package com.gruppe4b.edivator.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.gruppe4b.edivator.backend.domain.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImageEditServiceImpl implements ImageEditService {

    private final String bucket = "gruppe-4b.appspot.com";

    private final ImagesService imagesService;

    @Autowired
    private ImageStoreService imageStoreService;

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

    public String resizeImage(String imageId, int wishedWidth, int wishedHeight) {
        Image resizeImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeResize(wishedWidth, wishedHeight);
        Image resizedImage = imagesService.applyTransform(transform, resizeImage);

        String id = new Integer( Math.abs(new Integer(resizedImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(resizedImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public String flip(String imageId,boolean horizontal) {
        Image flippingImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeHorizontalFlip();
        if (horizontal == true) {
            transform = ImagesServiceFactory.makeHorizontalFlip();
        } else if (horizontal == false) {
            transform = ImagesServiceFactory.makeVerticalFlip();
        }
        Image flippedImage = imagesService.applyTransform(transform, flippingImage);

        String id = new Integer( Math.abs(new Integer(flippedImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(flippedImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public String turnLeft(String imageId) {
        Image turnImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeRotate(-90);
        Image turnedImage = imagesService.applyTransform(transform, turnImage);

        String id = new Integer( Math.abs(new Integer(turnedImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(turnedImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public String turnRight(String imageId) {
        Image turnImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeRotate(90);
        Image turnedImage = imagesService.applyTransform(transform, turnImage);

        String id = new Integer( Math.abs(new Integer(turnedImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(turnedImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public String crop(String imageId, float leftBorder, float rightBorder, float topBorder, float bottomBorder) {
        Image croppingImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeCrop(leftBorder,topBorder,rightBorder,bottomBorder);

        Image croppedImage = imagesService.applyTransform(transform, croppingImage);

        String id = new Integer( Math.abs(new Integer(croppedImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(croppedImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public String applyLuckyFilter(String imageId) {
        Image filterImage = imageStoreService.getImageFromCloudStorage(imageId);

        Transform transform = ImagesServiceFactory.makeImFeelingLucky();
        Image filteredImage = imagesService.applyTransform(transform, filterImage);

        String id = new Integer( Math.abs(new Integer(filteredImage.hashCode() + DateTime.now().hashCode()).hashCode())).toString();
        String url = "exception";
        try {
            url = imageStoreService.writeImageToCloudStorage(filteredImage, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(createResponse(url,id));
    }

    public ImageResponse createResponse(String url,String id) {
        return new ImageResponse(url,id);
    }
}
