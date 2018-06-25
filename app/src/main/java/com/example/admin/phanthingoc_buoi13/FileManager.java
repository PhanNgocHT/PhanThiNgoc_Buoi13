package com.example.admin.phanthingoc_buoi13;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 5/1/2017.
 */

public class FileManager {
    public static final String ROOT_PATH= Environment.getExternalStorageDirectory().getPath();
    public File[] readFolder(String path) {
        File file=new File(path);
        return file.listFiles();
    }


}
