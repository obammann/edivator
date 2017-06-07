package com.gruppe4b.edivator.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
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
    public byte[] convertFileToByteArray(String imageFile) throws IOException {

        FileChannel fileChannel;
        ByteBuffer byteBuffer;
        try (FileInputStream fileInputStream = new FileInputStream(new File("img/image.png"))) {
            fileChannel = fileInputStream.getChannel();
        }
        byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(byteBuffer);

        return byteBuffer.array();
    }
}
