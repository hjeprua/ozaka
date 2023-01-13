package com.Server.ninja.Server;

import java.util.Arrays;

public class ReadData {
    public static void main(String[] args) {
        final byte[] ab = NinjaUtil.getFile("C:\\Users\\KHONG MINH TIEN\\Desktop\\NjaPrivate\\cache\\requestold");
        final byte[] ab1 = NinjaUtil.getFile("C:\\Users\\KHONG MINH TIEN\\Desktop\\NjaPrivate\\cache\\request");
        //System.out.println(Arrays.toString(ab));
        //System.out.println(ab.length);
        //System.out.println(Arrays.toString(ab1));
//        System.out.println(ab1.length);
        for (int i = 0; i < ab.length; i++) {
            if (ab[i] != ab1[i]) {
                System.out.println("" + i);
            }
        }
    }
}
