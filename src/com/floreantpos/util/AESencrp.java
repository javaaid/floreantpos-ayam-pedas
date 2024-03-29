package com.floreantpos.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESencrp {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',

	'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

	public static String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
        return new BASE64Encoder().encode(encVal);
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
	}

	private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
	}
	
//	public static void main(String[] args) throws Exception {
////		String s = "12458";
////		String encrypt = encrypt(s);
////		System.out.println(encrypt);
//
//		String decrypt = decrypt("4T9H+1LqawVTsVvifd/TxA==");
//		System.out.println(decrypt);
//	}
//
}
