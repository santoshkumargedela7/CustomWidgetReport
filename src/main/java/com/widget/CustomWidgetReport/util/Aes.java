package com.widget.CustomWidgetReport.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class Aes {
	
	private static final String ALGORITHM       = "AES";
	private static final String myEncryptionKey = "ThisIsFoundation";
	private static final String UNICODE_FORMAT  = "UTF-8";

	public String encrypt(String valueToEnc){
		try
		{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key);  
			byte[] encValue = c.doFinal(valueToEnc.getBytes(UNICODE_FORMAT));
			String encryptedValue = Base64.getEncoder().encodeToString(encValue);
			return encryptedValue;
		}
		catch(Exception ex)
		{
			return "NA"; 
		}
	}
	public String decrypt(String encryptedValue) {
		try
		{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;	
		}
		catch (BadPaddingException e) {
			return "NA"; 
		}
		catch(Exception ex)
		{
			return "NA"; 
		}
	}

	private Key generateKey() throws Exception {
		byte[] keyAsBytes;
		keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
		return key;
	}		
}