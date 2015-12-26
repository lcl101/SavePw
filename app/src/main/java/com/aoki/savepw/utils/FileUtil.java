package com.aoki.savepw.utils;

import android.os.Environment;

import com.aoki.savepw.Config;
import com.aoki.savepw.data.Data;
import com.aoki.savepw.data.Password;
import com.aoki.savepw.excepton.AppException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by aoki on 15-12-24.
 */
public class FileUtil {
    private static boolean initMark = false;

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        else {
            throw new AppException(ErrorCode.getErrorCode("util","find"));
        }
        return sdDir.toString();
    }

    public static boolean createDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.exists() && dir.isDirectory()) {
            return true;
        }
        else {
            return dir.mkdirs();
        }
    }

    public static void readData(){
        if (initMark) return;
        String sdPath = getSDPath();
        String absPath = sdPath+ Config.dataFile;
        File file = new File(absPath);
        if (!file.exists()){
            return;
        }
        ///////////////////////////////////
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file.getAbsolutePath());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";
            while ( (str = bufferedReader.readLine()) != null ) {
                if ("".equals(str)){
                    continue;
                }
                Data.press(str);
            }
            initMark = true;
        }
        catch (IOException e) {
            throw new AppException(ErrorCode.getErrorCode("util","read"),e);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                //
            }
        }
        ///////////////////////////////////
    }

    public static void writeData(){
        if (!Data.modify) return;
        String sdPath = getSDPath();
        String absPath = sdPath+ Config.dataFile;
        File file = new File(absPath);
        BufferedWriter bw = null;
        try {
            if (file.exists()){
                file.delete();
            }
            List<String> d = Data.getData();
            if (null == d) return;
            bw = new BufferedWriter(new FileWriter(file, true));
            for (String str : d) {
                bw.write(str);
            }
            Data.modify = false;
        }
        catch(IOException ioe){
            throw new AppException(ErrorCode.getErrorCode("util","write"),ioe);
        }
        finally {
            if (null != bw){
                try {
                    bw.flush();
                    bw.close();
                }
                catch (IOException e){

                }
            }
        }
    }
}
