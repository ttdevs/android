/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.conceal;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ConcealUtil {
    private static final String DEFAULT_ENCODE = "utf-8";

    public static final String ENTITY = "ttdevs", PREFIX_E = "encrypt_", PREFIX_D = "decrypt_";

    private static SharedPrefsBackedKeyChain msp;
    private static Crypto mCrypto;
    private static Entity mEntity;

    public static void init(Context context) {
        if (null == mCrypto) {
            msp = new SharedPrefsBackedKeyChain(context);
            mCrypto = new Crypto(msp, new SystemNativeCryptoLibrary());
            mEntity = new Entity(ENTITY);
        }
    }

    /**
     * 加密字节数组
     *
     * @param content 原始字节数组
     * @return 密文
     */
    public static byte[] encryptByte(byte[] content) {
        if (!mCrypto.isAvailable()) {
            return null;
        }

        if (null == content || content.length == 0) {
            return null;
        }

        try {
            byte[] cipherText = mCrypto.encrypt(content, mEntity);// 密文数组

            return cipherText; // 密文字符串
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密字节数组
     *
     * @param content 密文
     * @return 原始字节数组
     */
    public static byte[] decryptByte(byte[] content) {
        if (!mCrypto.isAvailable()) {
            return null;
        }

        if (null == content || content.length == 0) {
            return null;
        }

        try {
            byte[] result = mCrypto.decrypt(content, mEntity);

            return result;
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密字符串
     *
     * @param content 原始字符串
     * @return 加密后字节数组
     */
    public static byte[] encryptString(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        try {
            byte[] result = encryptByte(content.getBytes(DEFAULT_ENCODE));
            // TODO
            return result; // 密文
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密出字符串 (字符编码采用默认)
     *
     * @param source 密文
     * @return 原始字符串
     */
    public static String decryptString(byte[] source) {
        if (null == source || source.length == 0) {
            return null;
        }

        try {
            byte[] data = decryptByte(source);
            String result = new String(data, DEFAULT_ENCODE);
            // TODO
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密文件，加密后会在文件前加前缀：encrypt_
     *
     * @param file 文件
     * @return
     */
    public static File encryptFile(File file) {
        if (!mCrypto.isAvailable()) {
            return null;
        }

        if (null == file) {
            return null;
        }

        String originFilePath = file.getAbsolutePath();
        String encryptFilePath = String.format("%s%s%s%s", file.getParent(), File.separator, PREFIX_E, file.getName());

        File encryptFile = new File(encryptFilePath);
        if (encryptFile.exists()) { // 判断最终加密的文件是否存在，若存在就删除
            encryptFile.deleteOnExit();
        }

        try {
            FileInputStream sourceFile = new FileInputStream(originFilePath);

            OutputStream fileOS = new BufferedOutputStream(new FileOutputStream(encryptFile));
            OutputStream out = mCrypto.getCipherOutputStream(fileOS, mEntity);

            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = sourceFile.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
            fileOS.flush();
            out.close();
            fileOS.close();

            sourceFile.close();

            return encryptFile;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File decryptFile(File file) {
        if (!mCrypto.isAvailable()) {
            return null;
        }

        if (null == file) {
            return null;
        }

        String fileName = file.getName();
        fileName = fileName.substring(PREFIX_E.length(), fileName.length());
        String originFilePath = file.getAbsolutePath();
        String decryptFilePath = String.format("%s%s%s%s", file.getParent(), File.separator, PREFIX_D, fileName);

        File decryptFile = new File(decryptFilePath);
        if (decryptFile.exists()) {
            decryptFile.deleteOnExit();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(originFilePath);
            InputStream inputStream = mCrypto.getCipherInputStream(fileInputStream, mEntity);

            OutputStream out = new BufferedOutputStream(new FileOutputStream(decryptFile));
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
            out.close();

            inputStream.close();
            fileInputStream.close();
            return decryptFile;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        }

        return null;
    }
}
