package cn.hicolin.webview.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static void copyFile(File fromFile, File toFile, boolean rewrite) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }

        if (!toFile.getParentFile().exists()) {
            boolean res = toFile.getParentFile().mkdirs();
            if (!res) {
                Log.d(TAG, "copyFile: mkdir fail");
            }
        }
        if (toFile.exists() && rewrite) {
            boolean res = toFile.delete();
            if (!res) {
                Log.d(TAG, "copyFile: delete fail");
            }
        }

        try {
            FileInputStream fosFrom = new FileInputStream(fromFile);
            FileOutputStream fosTo = new FileOutputStream(toFile);
            byte[] bt = new byte[1024];
            int c;

            while ((c = fosFrom.read(bt)) > 0) {
                fosTo.write(bt, 0 , c);
            }

            fosFrom.close();
            fosTo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
