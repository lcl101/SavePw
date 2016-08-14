package com.aoki.savepw;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aoki on 16-8-7.
 */
public class TestServer {
    public static final int PORT = 6213;//监听的端口号

    public static void main(String[] args) {
        System.out.println("服务器启动...\n");
        TestServer server = new TestServer();
        server.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;
        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                // 向客户端回复信息
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                try {
                    while (true) {
                        String str = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                        // 处理客户端数据
                        System.out.println("客户端发过来的内容:" + str);
                        if ("w_c".equals(str)) {
                            break;
                        }
                        out.writeUTF("w_i");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                out.close();
                input.close();
            } catch (Exception e) {
                System.out.println("服务器 run 异常: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("服务端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
    }
}
