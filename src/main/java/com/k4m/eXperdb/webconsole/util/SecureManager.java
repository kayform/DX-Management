package com.k4m.eXperdb.webconsole.util;

import java.security.Key;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 일반 DES 암/복호 모듈 
 */
public class SecureManager {
	private static String Key = "WebCash9";
	
	/**
	 * @param strSecurityKey 암/복호 키값
	 * @param strIV 암/복호용 Initialization vector	
	 */
	private static IvParameterSpec getIvParam(String strSecurityKey)
	{
		byte[] byteTemp =  strSecurityKey.getBytes();
		byteTemp=strSecurityKey.getBytes();
		IvParameterSpec ivParam=new IvParameterSpec(byteTemp);
		return ivParam;
	}
	
	public static void setKey(String keyValue) throws Exception {
        Key = keyValue;
    }
	
	/**
	 * 암/복호용 키 변수를 생성한다.
	 * @param keyValue key로 사용될 문자열 
	 * @return 암/복호용 key
	 * @throws Exception
	 */
	private static Key getKey(String keyValue) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(keyValue.getBytes());
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
	
	/**
	 * 문자열을 암호화한다
	 * @param strValue 암호화할 문자열 
	 * @return 암호화된 문자열
	 */
	public static String encrypt(String strValue) throws Exception{
		try{			
			Key _key = getKey(Key);
			IvParameterSpec ivParam = getIvParam(Key);
			if (strValue==null || strValue=="")
				return "";
			
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/CBC/PKCS5Padding");
		    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, _key,ivParam);
		    
		    byte[] inputBytes1 = strValue.getBytes();
	        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
	       
	        return new String((Base64.encodeBase64(outputBytes1)));
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 문자열을 암호화한다
	 * @param strValue 암호화할 문자열 
	 * @return 암호화된 문자열
	 */
	public static byte[] encryptToByte(String strValue) throws Exception{
		try{			
			Key _key = getKey(Key);
			IvParameterSpec ivParam = getIvParam(Key);
			if (strValue==null || strValue=="")
				return null;
			
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/CBC/PKCS5Padding");
		    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, _key,ivParam);
		    
		    byte[] inputBytes1 = strValue.getBytes();
	        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
	       ;
	        return outputBytes1;
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 문자열을 복호화한다
	 * @param strValue 복호화할 문자열 
	 * @return 복호화된 문자열
	 */
	public static String decrypt(String strValue) throws Exception{
		try{
			Key _key = getKey(Key);
			IvParameterSpec ivParam = getIvParam(Key);
			
			if (strValue==null || strValue=="")
				return "";
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/CBC/PKCS5Padding");
		    cipher.init(javax.crypto.Cipher.DECRYPT_MODE, _key,ivParam);
		   
		    byte[] inputBytes1 = Base64.decodeBase64(strValue);
		    byte[] outputBytes2 = cipher.doFinal(inputBytes1);

		    String strResult = new String(outputBytes2);
		    return strResult;

		}catch(Exception e){
			throw e;
		}		
	}	
	
	/**
	 * 문자열을 복호화한다
	 * @param strValue 복호화할 문자열 
	 * @return 복호화된 문자열
	 */
	public static String decryptFromByte(byte[] strValue) throws Exception{
		try{
			Key _key = getKey(Key);
			IvParameterSpec ivParam = getIvParam(Key);
			
			if (strValue==null || strValue.length == 0)
				return "";
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/CBC/PKCS5Padding");
		    cipher.init(javax.crypto.Cipher.DECRYPT_MODE, _key,ivParam);
		    byte[] outputBytes2 = cipher.doFinal(strValue);

		    String strResult = new String(outputBytes2);
		    return strResult;

		}catch(Exception e){
			throw e;
		}		
	}
	
}
