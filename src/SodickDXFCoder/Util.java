/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SodickDXFCoder;

import java.io.File;

/**
 *
 * @author Mats
 */
public final class Util {
    
    private Util() {
        
    }
    public static String stripExtension(String fileName) {
        if(fileName.contains(".")) return fileName.substring(0, fileName.lastIndexOf('.'));
        else return fileName;
    }

    public static String stripFile(String s) {
        if(s.contains(File.separator)) return s.substring(0, s.lastIndexOf(File.separator));
        else return s;
    }

    
}
