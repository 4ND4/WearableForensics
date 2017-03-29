package com.example.felix.wearableforensics;

import android.net.Uri;


/**
 * Created by felix on 08/09/2016.
 */
public class Provider {

    private Uri uri;
    private String description;

    public Provider(Uri uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    public Uri getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }
}
