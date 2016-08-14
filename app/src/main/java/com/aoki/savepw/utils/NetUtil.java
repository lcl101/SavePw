package com.aoki.savepw.utils;

import android.util.Log;

import com.aoki.savepw.data.Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by aoki on 16-8-7.
 */
public class NetUtil {
    private static final byte[] W_CMD_I = "w_i".getBytes(); //协商写服务器 c->s
    private static final String W_CMD_N = "w_n";            //请求写下一行 s->c
    private static final byte[] W_CMD_C = "w_c".getBytes();	//写服务器结束 c->s
    private static final byte[] R_CMD_I = "r_i".getBytes();	//协商读服务器 c->s
    private static final byte[] R_CMD_N = "r_n".getBytes();	//请求读下一行 c->s
    private static final String R_CMD_C = "r_c";	//读服务器结束 s->c
    private static final String ERR_CMD = "err_cmd";

    public static boolean backup(String ip, int port){
        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;
        try {
            socket = new Socket(ip, port);
            //读取服务器端数据
            input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            out = new DataOutputStream(socket.getOutputStream());
            Log.d("Licl-savepw","data size: "+Data.getData().size());
            byte[] buf = new byte[1024];
            int c = 0;
            String data = null;
            out.write(W_CMD_I);
            c = input.read(buf);
            data = new String(Arrays.copyOfRange(buf,0,c));
            if (W_CMD_N.equals(data)) {
                for (String str : Data.getData()) {
                    out.write(str.getBytes());
                    c = input.read(buf);
                    System.out.println("read count="+c);
                    data = new String(Arrays.copyOfRange(buf,0,c));
                    data = data.trim();
                    Log.d("Licl-savepw","read ddd:" + data + ", c=" + c);
                    if (!W_CMD_N.equals(data)) {
                        Log.d("Licl-savepw","ret msg: " + data);
                        return false;
                    }
                }
                out.write(W_CMD_C);
            }
            input.close();
            out.close();
        }
        catch (Exception e){
            Log.e("Licl-savepw","error msg: " + e.getMessage());
            return false;
        }
        finally{
            try {
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
                if (socket != null) {
                    socket.close();
                }
            }
            catch (Exception e1){
                //忽略
            }
        }
        return true;
    }

    public static boolean read(String ip, int port){
        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;
        try {
            socket = new Socket(ip, port);
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Data.clean();
            String data = null;
            out.write(R_CMD_I);
            byte[] buf = new byte[1024];
            int c = 0;
            while(true) {
                c = input.read(buf);
                data = new String(Arrays.copyOfRange(buf,0,c));
                data = data.trim();
                Log.d("Licl-savepw","ret msg: " + data);
                if (R_CMD_C.equals(data)){
                    break;
                }
                Data.press(data);
                out.write(R_CMD_N);
            }
            return true;
        }
        catch (Exception e){
            Log.e("Licl-savepw","error msg: " + e.getMessage());
            Data.reClean();
            return false;
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
                if (socket != null) {
                    socket.close();
                }
            }
            catch (Exception e1){
                //忽略
            }
        }
    }
}
