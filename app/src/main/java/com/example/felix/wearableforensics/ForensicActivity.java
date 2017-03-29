package com.example.felix.wearableforensics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by felix on 26/08/2016.
 */
public class ForensicActivity extends Activity implements ForensicView {

    private TextView mTextView;
    private Button bCapture;
    private ForensicPresenter presenter;

    private final Uri uriCallLog = CallLog.Calls.CONTENT_URI;
    private final Uri uriContacts = ContactsContract.Contacts.CONTENT_URI;
    private final Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private final Uri uriProfile = ContactsContract.Profile.CONTENT_URI;
    private final Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private final Uri uriWellness = Uri.parse("content://com.asus.wear.wellness.provider/step_count");    //guessing thanks to rooted device I can know the table name

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                bCapture = (Button) stub.findViewById(R.id.btn_capture);
                bCapture.setEnabled(setPermissions());

            }

        });

        presenter = new ForensicPresenterImpl(this);

    }

    @Override public void showMessage(String message) {
        System.out.println(message);
    }

    private boolean setPermissions()
    {
        return presenter.permissions(this.getApplicationContext(),this);
    }

    public void btnCapture(View target) {

        presenter.deleteFiles();
        presenter.processProvider(this, new Provider(uriContacts,"contacts"));
        presenter.processProvider(this, new Provider(uriPhone,"phone"));
        presenter.processProvider(this, new Provider(uriCallLog,"calls"));
        presenter.processProvider(this, new Provider(uriProfile,"profile"));
        presenter.processProvider(this, new Provider(uriEmail,"email"));
        presenter.processProvider(this, new Provider(uriWellness,"steps"));
        presenter.createPhotos(getContentResolver());

    }
}