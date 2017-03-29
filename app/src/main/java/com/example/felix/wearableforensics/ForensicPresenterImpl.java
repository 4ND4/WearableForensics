package com.example.felix.wearableforensics;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by felix on 26/08/2016.
 * MITM for presentation and model
 */
public class ForensicPresenterImpl implements ForensicPresenter {

    private ForensicView forensicView;
    private ForensicInteractor forensicInteractor;

    public ForensicPresenterImpl(ForensicView forensicView) {
        this.forensicView = forensicView;
        this.forensicInteractor = new ForensicInteractorImpl();
    }

    @Override
    public void createPhotos(ContentResolver contentResolver) {

        if (forensicView != null) {
            forensicView.showMessage("Processing " + "createPhotos");
            forensicInteractor.createPhotos(contentResolver);
        }
    }

    @Override
    public void deleteFiles() {
        if(forensicView!=null){
            forensicView.showMessage("Processing " + "deleteFiles");
            forensicInteractor.deleteFiles();
        }
    }

    @Override
    public Boolean permissions(Context context, Activity activity) {

        if (forensicView != null) {
            forensicView.showMessage("Processing " + "permissions");

            PermissionMgr permission = new PermissionMgr(context, activity);

            permission.set(new Permission(Manifest.permission.READ_CALL_LOG, "Read Call LOG"));
            permission.set(new Permission(Manifest.permission.READ_CONTACTS, "Read Contacts"));
            permission.set(new Permission(Manifest.permission.READ_SMS, "READ SMS"));
            permission.set(new Permission(Manifest.permission.READ_CALENDAR, "READ Calendar"));
            permission.set(new Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE External Storage"));

            boolean check = forensicInteractor.check(permission);

            if (!check) {
                //Ask for permissions

                System.out.println("Access Denied");
                System.out.println("Requesting Access");

                forensicInteractor.request(permission);

                return false;

            } else {
                return true;
            }
        }

        return true;

    }

    public void processProvider(ForensicActivity forensicActivity, Provider provider) {

        if (forensicView != null) {
            forensicView.showMessage("Processing " + "processProvider");

        }

        forensicInteractor.processProvider(forensicActivity, provider);

    }
}