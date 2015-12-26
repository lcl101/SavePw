package com.aoki.savepw.utils;

import com.aoki.savepw.Config;
import com.aoki.savepw.Encryption;


/**
 * Created by aoki on 15-12-22.
 */
public class MyDes implements Encryption {
    public static final int EN_STQ = 1;
    public static final int DE_STQ = 2;

    private static byte[] IP_Table = {
            58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
    };

    private static byte[] IPR_Table = {
            40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,  9, 49, 17, 57, 25
    };

    private static byte[] E_Table = {
            32,  1,  2,  3,  4,  5,  4,  5,  6,  7,  8,  9,
            8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
    };

    private static byte[] P_Table = {
            16, 7, 20, 21, 29, 12, 28, 17, 1,  15, 23, 26, 5,  18, 31, 10,
            2,  8, 24, 14, 32, 27, 3,  9,  19, 13, 30, 6,  22, 11, 4,  25
    };

    private static byte[] PC1_Table = {
            57, 49, 41, 33, 25, 17,  9,  1, 58, 50, 42, 34, 26, 18,
            10,  2, 59, 51, 43, 35, 27, 19, 11,  3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,  7, 62, 54, 46, 38, 30, 22,
            14,  6, 61, 53, 45, 37, 29, 21, 13,  5, 28, 20, 12,  4
    };

    private static byte[]  PC2_Table = {
            14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10,
            23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };

    private static byte[]  LOOP_Table = {
            1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1
    };

    private static byte[] S_Box = {
            // S1
            14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
            0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
            4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
            15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13,
            // S2
            15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
            3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
            0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
            13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9,
            // S3
            10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
            13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
            13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
            1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12,
            // S4
            7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
            13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
            10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
            3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14,
            // S5
            2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
            14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
            4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
            11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3,
            // S6
            12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
            10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
            9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
            4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13,
            // S7
            4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
            13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
            1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
            6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12,
            // S8
            13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
            1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
            7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
            2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11
    };

    private byte[][] subKey;

    public void makeKey(String key){
        if (key.length()!=8)
            throw new RuntimeException("密码必须8位");
        subKey = new byte[16][48];
        makeSubKey(subKey,key.substring(0,8).getBytes());
    }

    private void rotatel(byte[] in, int len, int loop,int beginPos){
        byte[] tmp = new byte[256];
        System.arraycopy(in, beginPos+0, tmp, 0, loop);
        System.arraycopy(in, beginPos+loop, in, beginPos+0, len-loop);
        System.arraycopy(tmp, 0, in, beginPos+len-loop, loop);
    }

    private void byteToBit(byte[] out, byte[] in, int bits){
        for(int i=0; i<bits; i++){
            out[i] = (byte)(((in[i/8])>>(i%8)) & 0x01);
        }
    }

    private void makeSubKey(byte[][] pSubKey,byte[] key){
        byte[] k = new byte[64];
        byteToBit(k,key,64);
        transform(k,k,PC1_Table,56);
        for(int i=0; i<16; i++){
            rotatel(k, 28, LOOP_Table[i],0);
            rotatel(k, 28, LOOP_Table[i],28);
            transform(pSubKey[i], k, PC2_Table, 48);
        }
    }

    private void transform(byte[] out,byte[] in,byte[] table, int len){
        byte[] tmp = new byte[256];
        for(int i=0; i<len; i++)
            tmp[i] = in[table[i]-1];
        System.arraycopy(tmp, 0, out, 0, len);
    }

    private void press(byte[] inoutBlock,byte[][] pSubKey,int type){
        byte[] m=new byte[64], tmp=new byte[32];// *Li = &M[0], *Ri = &M[32];
        byte[] li = new byte[32], ri = new byte[32];
        byteToBit(m, inoutBlock, 64);
        transform(m, m, IP_Table, 64);
        System.arraycopy(m,0,li,0,32);
        System.arraycopy(m,32,ri,0,32);
        if( type == EN_STQ ){
            for(int i=0; i<16; i++) {
                System.arraycopy(ri, 0, tmp, 0, 32);
                f_func(ri, pSubKey[i]);
                xor(ri, li, 32);
                System.arraycopy(tmp, 0, li, 0, 32);
            }
        }else{
            for(int i=15; i>=0; i--) {
                System.arraycopy(li, 0, tmp, 0, 32);
                f_func(li, pSubKey[i]);
                xor(li, ri, 32);
                System.arraycopy(tmp, 0, ri, 0, 32);
            }

        }
        System.arraycopy(li,0,m,0,32);
        System.arraycopy(ri,0,m,32,32);
        transform(m, m, IPR_Table, 64);
        bitToByte(inoutBlock, m, 64);
    }

    private void f_func(byte[] in, byte[] ki)
    {
        byte[] mr = new byte[48];
        transform(mr, in, E_Table, 48);
        xor(mr, ki, 48);
        s_func(in, mr);
        transform(in, in, P_Table, 32);
    }
    private void s_func(byte[] out, byte[] in)
    {
        byte[] tmp = new byte[4];
        byte[] tmps = new byte[1];
        for(int i=0,j,k; i<8; i++) {
            j = (in[i*6+0]<<1) + in[i*6+5];
            k = (in[i*6+1]<<3) + (in[i*6+2]<<2) + (in[i*6+3]<<1) + in[i*6+4];
            tmps[0] = S_Box[i*(4*16)+j*16+k];
            byteToBit(tmp, tmps, 4);
            System.arraycopy(tmp,0, out, i*4, 4);
        }
    }

    private void xor(byte[] ina, byte[] inb, int len)
    {
        for(int i=0; i<len; i++)
            ina[i] ^= inb[i];
    }

    private void bitToByte(byte[] out, byte[] in, int bits)
    {
        //init out
        for(int i=0; i<out.length; i++){
            out[i]=0;
        }
        for(int i=0; i<bits; i++)
            out[i/8] |= in[i]<<(i%8);
    }

    public void en(byte[] inoutBlock){
        int datalen = inoutBlock.length;
        byte[] tmp = new byte[8];
        for(int i = 0, j = datalen >> 3; i < j; ++ i)
        {
            System.arraycopy(inoutBlock, i*8, tmp, 0, 8);
            press(tmp,subKey, EN_STQ);//ENCRYPT
//            press(tmp,subKey[1], DE_STQ);//DECRYPT
//            press(tmp,subKey[0], EN_STQ);//ENCRYPT
            System.arraycopy(tmp, 0, inoutBlock, i*8, 8);
        }
    }

    public void de(byte[] inoutBlock){
        int datalen = inoutBlock.length;
        byte[] tmp = new byte[8];
        for(int i = 0, j = datalen >> 3; i < j; ++ i)
        {
            System.arraycopy(inoutBlock, i*8, tmp, 0, 8);
            press(tmp,subKey, DE_STQ);//ENCRYPT
//            press(tmp,subKey[1], EN_STQ);//DECRYPT
//            press(tmp,subKey[0], DE_STQ);//ENCRYPT
            System.arraycopy(tmp, 0, inoutBlock, i*8, 8);
        }
    }

    public String en(String src){
        if (null == src || src.trim().length()<1){
            return "";
        }
        byte[] data = src.getBytes(Config.CHARSET);
        int padLen = data.length % 8;
        int enDataLen = data.length + ((padLen==0)?0:8-padLen);
        byte[] data1;
        if (data.length == enDataLen){
            data1 = data;
        }
        else {
            data1 = new byte[enDataLen];
            System.arraycopy(data,0,data1,0,data.length);
            //补码
            for (int i=data.length; i<enDataLen; i++){
                data1[i] = (byte)' ';
            }
        }
        en(data1);
        return Hex.byte2hex(data1);
    }

    public String de(String src){
        if (null == src || src.trim().length()<1){
            return "";
        }
        byte[] data = Hex.hex2byte(src);
        de(data);
        String r = new String(data,Config.CHARSET);
        r = r.trim();
        return r;
    }
    //only test
    public static void main(String[] args){
        MyDes d = new MyDes();
        d.makeKey("12345678");
        String s = "李春林";
        String es = d.en(s);
        String ds = d.de(es);
        System.out.println(s);
        System.out.println(es);
        System.out.println(ds);
    }
}
