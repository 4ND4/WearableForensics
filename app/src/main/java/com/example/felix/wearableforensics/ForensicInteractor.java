package com.example.felix.wearableforensics;

import android.content.ContentResolver;

/**
 * Created by felix on 26/08/2016.
 */
public interface ForensicInteractor {

    void createPhotos(ContentResolver contentResolver);
    void processProvider(ForensicActivity forensicActivity, Provider provider);
    boolean check(PermissionMgr permissionMgr);
    boolean request(PermissionMgr permissionMgr);
    void deleteFiles();

}