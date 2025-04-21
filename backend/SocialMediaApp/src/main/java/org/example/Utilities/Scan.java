package org.example.Utilities;

import java.util.Scanner;

public class Scan {
    private static Scanner sc;
    private Scan(){
    }
    public static Scanner getInstance(){
        if(sc==null){
            sc=new Scanner(System.in);
        }
        return sc;
    }

}
