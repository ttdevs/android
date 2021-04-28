/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.util.Base64;

import org.junit.Test;

import java.text.NumberFormat;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ExampleUnitTest {
    private static byte[] AES_KEY = "123456789012.csdn.mobile".getBytes();
    public static final String CIPHER_ALGORITHM = "DESede";
    public static final SecretKeySpec KEY = new SecretKeySpec(AES_KEY, CIPHER_ALGORITHM);
    @Test
    public void testCSDN() {
        try {
            byte[] data = "22332233".getBytes("UTF-8");

            byte[] input = encrypt(data);
            System.out.println(new String(input, "UTF-8"));

            byte[] output = decrypt(data);
            System.out.println(new String(output, "UTF-8"));

            byte[] encode = Base64.encode(input, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] data) throws Exception{
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KEY);
        return cipher.doFinal(data);
    }
    public static byte[] decrypt(byte[] data) throws Exception{
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, KEY);
        return cipher.doFinal(data);
    }

    @Test
    public void testStringLocale() throws Exception {
        Locale[] locales = new Locale[]{
                Locale.CANADA,
                Locale.CANADA_FRENCH,
                Locale.CHINESE,
                Locale.ENGLISH,
                Locale.FRANCE,
                Locale.GERMAN,
                Locale.GERMANY,
                Locale.ITALIAN,
                Locale.ITALY,
                Locale.JAPAN,
                Locale.JAPANESE,
                Locale.KOREA,
                Locale.KOREAN,
                Locale.PRC,
                Locale.ROOT,
                Locale.SIMPLIFIED_CHINESE,
                Locale.TAIWAN,
                Locale.TRADITIONAL_CHINESE,
                Locale.UK,
                Locale.US
        };

        String weightString = null;
        for (Locale locale : locales) {
            try {
                weightString = formatFloatWithOneDot(locale, 55.4f);
                float weight = Float.parseFloat(weightString);
            } catch (NumberFormatException e) {
                System.out.println(locale + ">>>>>" + weightString + ">>>>>>>>>> error");
                continue;
            }
            System.out.println(locale + ">>>>>" + weightString);
        }
    }

    public static String formatFloatWithOneDot(Locale locale, float number) {
        number = Math.round(number * 10) / 10f;
        return String.format(locale, "%.1f", number);
    }

    @Test
    public void testNumberFormat() {
        try {
            Locale locale = Locale.FRANCE;
            parse(locale, 55f);
            parse(locale, 55.0f);
            parse(locale, 55.4f);
            parse(locale, 55.6f);
            parse(locale, 55.44f);
            parse(locale, 55.48f);
            parse(locale, 55.53f);
            parse(locale, 55.57f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void integerType() {
        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;

        System.out.println(f1 == f2);
        System.out.println(f3 == f4);
    }

    @Test
    public void nullTest() {
        String a = null;
        String b = null;
        System.out.println(a+b);
    }

    private void parse(Locale locale, float source) throws Exception {
        System.out.println(source);
        String parsedString = formatFloatWithOneDot(locale, source);
        System.out.println(parsedString);
        NumberFormat format = NumberFormat.getInstance(locale);
        Number parsedNumber = format.parse(parsedString);
        System.out.println(parsedNumber.floatValue());
        System.out.println(">>>>>>>>");
    }
}