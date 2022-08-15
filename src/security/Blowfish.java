package security;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

// added 06/26/2022
import java.text.DecimalFormat;
import senscript_functions.Functions;

/**
 * @author Abdelkader Laouid
 * @author Massinissa Saoudi
 */

public class Blowfish 
{
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public String key;
	public String message;
	
	public Blowfish(String key, String message)
	{
		this.key = key;
		this.message = message;
	}
	
//added on 06/26/2022
	DecimalFormat format = new DecimalFormat("0.00");
	
// Converts byte array to hex string
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
// Converts hex string to byte array  
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public String encrypt() throws Exception{
		byte[] byteKey	= key.getBytes();
		String IV  	= "12345678";
		
//	Original code	
		
//// Create new Blowfish cipher
//		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
//		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
//	
//		cipher.init(Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes())); 
//		byte [] encoding = cipher.doFinal(message.getBytes());
//		return bytesToHex(encoding);
//	}
//	public String decrypt() throws Exception{
//		byte[] byteKey	= key.getBytes();
//		String IV  	= "12345678";
//		
//// Create new Blowfish cipher
//		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
//		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
// 
//		cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes()));
//		byte [] coded = hexStringToByteArray(message);
//		byte[] decoded = cipher.doFinal(coded);

// added 06/26/2022
		// Create new Blowfish cipher
			SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
			Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
		
//		 		added encryption start time  ----- D.A
			long t3 = System.nanoTime(); //    added by D.A
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes())); 
			byte [] encoding = cipher.doFinal(message.getBytes());
//				added encryption start time  ----- D.A
			long t4 = System.nanoTime();
	        String encrypttime = format.format((t4-t3)/1000000.0);
	        Functions.encryptiontime = encrypttime;
			
			return bytesToHex(encoding);
		}
		public String decrypt(String bytesToHex) throws Exception{
			byte[] byteKey	= key.getBytes();
			String IV  	= "12345678";
			
	// Create new Blowfish cipher
			SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
			Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
	 
//				added decryption start time  ----- D.A
			long t5 = System.nanoTime(); //    added by D.A
			cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes()));
			byte [] coded = hexStringToByteArray(bytesToHex);
			byte[] decoded = cipher.doFinal(coded);
//				added decryption end time  ----- D.A
			long t6 = System.nanoTime();
			String decrypttime = format.format((t6-t5)/1000000.0);
	        Functions.decryptiontime = decrypttime;
	return new String(decoded);
}
}