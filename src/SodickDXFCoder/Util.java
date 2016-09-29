/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SodickDXFCoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author Mats
 */
public final class Util {

    private Util() {

    }

    public static String stripExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        } else {
            return fileName;
        }
    }

    public static String stripFile(String s) {
        if (s.contains(File.separator)) {
            return s.substring(0, s.lastIndexOf(File.separator));
        } else {
            return s;
        }
    }

    public static void writeLineToBw(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    public static void writeToBw(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
    }

    public static String convertToDecimal(String text) {
        double value;
        String result = "-9999.9";
        try {
            value = Double.parseDouble(text);
            DecimalFormat df = new DecimalFormat("0.0###");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
            result = df.format(value);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Felaktigt tal");
        }

        return result;

    }

}
