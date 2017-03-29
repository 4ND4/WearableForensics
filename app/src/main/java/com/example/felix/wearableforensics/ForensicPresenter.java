package com.example.felix.wearableforensics;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by felix on 26/08/2016.
 */
public interface ForensicPresenter {

    void createPhotos(ContentResolver contentResolver);
    void deleteFiles();
    Boolean permissions(Context context, Activity activity);
    void processProvider(ForensicActivity forensicActivity, Provider provider);
}