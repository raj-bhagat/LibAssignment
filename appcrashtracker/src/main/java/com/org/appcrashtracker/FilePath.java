package com.org.appcrashtracker;

import ohos.app.Context;
import ohos.app.Environment;

import java.io.File;

public class FilePath {
    public static File getInternalStorage(Context context) {
        return context.getFilesDir();
    }
    public static File getExternalStorage(Context context) {
        File externalFilesDirPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String externalStoragePath = "";
        int subPathIndex = externalFilesDirPath.getAbsolutePath().indexOf("/emulated/0/");
        if (subPathIndex > 0) {
            subPathIndex += "/emulated/0/".length();
        }
        if (subPathIndex >= 0 && externalFilesDirPath.getAbsolutePath().contains("/storage/")) {
            externalStoragePath = externalFilesDirPath.getAbsolutePath().substring(0, subPathIndex);
        }
        if (externalStoragePath.length() > 0) {
            externalFilesDirPath = new File(externalStoragePath);
        }
        return externalFilesDirPath;
    }
}

