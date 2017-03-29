package com.example.felix.wearableforensics;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by felix on 26/08/2016.
 */
public class ForensicInteractorImpl implements ForensicInteractor {

    @Override
    public void createPhotos(ContentResolver contentResolver) {
        int numRecords;
        String extension = ".jpg";  //TODO put somewhere

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.PHOTO_URI},   //projection (I just want photo uri's)
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI + " IS NOT NULL ", null, null);

        numRecords = cursor != null ? cursor.getCount() : 0;

        if (numRecords > 0) {

            System.out.println("Photo Records :" + numRecords);

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                File directory = Environment.getExternalStoragePublicDirectory("forensics");

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                int counter = 0;
                String filename = "file";

                while (cursor.moveToNext()) {

                    Uri imageUri = Uri.parse(cursor.getString(0));

                    counter++;

                    try {
                        InputStream inputStream = contentResolver.openInputStream(imageUri);

                        File file;
                        file = new File(directory, filename + counter + extension);

                        OutputStream outputStream = new FileOutputStream(file);

                        IOUtils.copy(inputStream != null ? inputStream : null, outputStream);
                        outputStream.close();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Closing cursor photo...");

            cursor.close();

        } else {
            System.out.println("Photo cursor is null...");
        }
    }

    @Override
    public void processProvider(ForensicActivity forensicActivity, Provider provider) {

        int numRecords;
        String extension = ".txt";  //TODO put somewhere

        try {
            Cursor cursor = forensicActivity.getContentResolver().query(provider.getUri(), null, null, null, null);

            if (cursor != null) {
                numRecords = cursor.getCount();

                System.out.println(provider.getDescription() + " Records :" + numRecords);

                //Create csv header

                String headerCSV = createStringCSV(cursor.getColumnNames());

                writeFile(headerCSV, provider.getDescription() + extension);

                System.out.println("Processing " + provider.getDescription() + "...");

                while (cursor.moveToNext()) {

                    int size = cursor.getColumnCount();

                    String[] Array = new String[size];

                    for (int i = 0; i < size; i++) {

                        String property = cursor.getString(i);

                        if (property != null)
                            Array[i] = property;
                        else
                            Array[i] = "empty"; //TODO change to maybe a null value
                    }

                    String message = createStringCSV(Array);

                    writeFile(message, provider.getDescription() + extension);

                }

                System.out.println("Closing cursor " + provider.getDescription() + "...");

                cursor.close();

            } else
                System.out.println(provider.getDescription() + " cursor is null...");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeFile(String message, String filename) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File directory = Environment.getExternalStoragePublicDirectory("forensics");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file;

            try {

                file = new File(directory, filename);
                if (!file.isFile())
                    file.createNewFile();

                FileOutputStream stream = new FileOutputStream(file, true);
                OutputStreamWriter writer = new OutputStreamWriter(stream);

                writer.write(message);
                writer.append("\n");    //append a new line

                writer.close();

            } catch (Exception e) {

                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private String createStringCSV(String[] Array) {
        String message = "";
        boolean flag = false;

        for (String s : Array) {
            if (!flag) {
                message = s;
                flag = true;
            } else {
                message += "|" + s;
            }
        }

        return message;
    }

    @Override
    public boolean check(PermissionMgr permissionMgr) {
        return permissionMgr.check();
    }

    @Override
    public boolean request(PermissionMgr permissionMgr) {

        return permissionMgr.request();

    }

    public void deleteFiles() {

        try {

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                File directory = Environment.getExternalStoragePublicDirectory("forensics");

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                if(directory.listFiles()!=null) {
                    for (File child : directory.listFiles()) {
                        child.delete();
                    }
                }

                System.out.println("forensics folder emptied...");

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}