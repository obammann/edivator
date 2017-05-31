package com.gruppe4b.edivator.backend.service;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.*;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This Service-Implementation uses the Google Cloud Storage (not Google Datastore!) to store and load images
 */
public class DefaultImageStoreService implements ImageStoreService {


    protected final String bucket;
    protected final ImagesService imagesService;
    protected final GcsService gcsService;

    public DefaultImageStoreService(String bucket) {
        this.bucket = bucket;
        this.imagesService = ImagesServiceFactory.getImagesService();
        this.gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                .initialRetryDelayMillis(10)
                .retryMaxAttempts(2)
                .totalRetryPeriodMillis(5000)
                .build());
    }

    @Override
    public void writeImageToCloudStorage(Image image, String name) throws IOException {
        byte[] data = image.getImageData();
        // TODO: get the real MIME-Type from the data..
        gcsService.createOrReplace(
                new GcsFilename(bucket, name),
                new GcsFileOptions.Builder().mimeType("image/jpeg").build(),
                ByteBuffer.wrap(data)
        );
    }

    @Override
    public Image getImageFromCloudStorage(String name) {
        return null;
    }

    @Override
    public Image getImageFromByteArray(byte[] image) {
        return ImagesServiceFactory.makeImage(image);
    }
}