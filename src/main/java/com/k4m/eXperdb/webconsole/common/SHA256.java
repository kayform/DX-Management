package com.k4m.eXperdb.webconsole.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

	public static String SHA256(String str){
		String encryptData = ""; 

		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 

			byte byteData[] = sh.digest();
			
			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			encryptData = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			Globals.logger.error(e.getMessage());
			encryptData = null; 
		}

		return encryptData;
	}
}
