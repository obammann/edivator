package com.gruppe4b.edivator.backend.service;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import org.springframework.stereotype.Service;

/**
 * This Service-Implementation uses the Google Cloud Storage (not Google Datastore!) to store and load images
 */
@Service
public class ImageStoreServiceImpl implements ImageStoreService {

    private final String bucket = "edivator_image_store_europe";
    protected final ImagesService imagesService;
    protected final GcsService gcsService;
    protected final BlobstoreService blobstoreService;

    public ImageStoreServiceImpl() {
        this.imagesService = ImagesServiceFactory.getImagesService();
        this.gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                .initialRetryDelayMillis(10)
                .retryMinAttempts(1)
                .retryMaxAttempts(2)
                .totalRetryPeriodMillis(5000)
                .build());
        this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    }

    @Override
    public String writeImageToCloudStorage(Image image, String name) throws IOException {
        byte[] data = image.getImageData();
        // TODO: get the real MIME-Type from the data..
        gcsService.createOrReplace(
                new GcsFilename(bucket, name),
                new GcsFileOptions.Builder().mimeType("image/jpeg").build(),
                ByteBuffer.wrap(data)
        );

        return imagesService.getServingUrl(ServingUrlOptions.Builder
                .withGoogleStorageFileName("/gs/" + bucket + "/" + name)
                .crop(false)
                .imageSize(0));
    }

    @Override
    public Image getImageFromCloudStorage(String name) {
        BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/" + bucket + "/" + name);
        Image blobImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
        return blobImage;
    }

    @Override
    public Image getImageFromByteArray(byte[] image) {
        return ImagesServiceFactory.makeImage(image);
    }
}
