package senscript_functions;

import java.util.Arrays;

import java.io.ByteArrayInputStream;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Base64;
//import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//

import security.Blowfish;
import security.Operator;
import security.SuperFastHash;

import net.sf.ntru.encrypt.*;

public class Functions {
//added o6/26/2022 for ntru encrypt
	static NtruEncrypt ntru = new NtruEncrypt(EncryptionParameters.APR2011_439_FAST);
	static EncryptionKeyPair kp = ntru.generateKeyPair();
	static EncryptionPrivateKey priv = kp.getPrivate();
    static EncryptionPublicKey pub = kp.getPublic();
// added 06/26/2022
	static byte[] byte_coded;
	static byte[] byte_encoded;
	public static String encryptiontime;
	public static String decryptiontime;
	//Creating key for Blowfish by DA
	static String blow_encrypted;
	static String key = "cupcaborn";
//

	public static String min(String [] args) {
		double min = Integer.MAX_VALUE;
		for(int i=0; i<args.length; i++) {
			if (Double.valueOf(args[i]) < min)
				min = Double.valueOf(args[i]);
		}
		return  ""+min;
	}
	
	public static String mysum(String [] args) {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int s = x + y + 2;
		return  ""+s;
	}
	
	public static String smin(String [] args) {
		double min = Integer.MAX_VALUE;
		String s = "";
		for(int i=0; i<args.length; i++) {
			String [] str = args[i].split("#");
			if (Double.valueOf(str[0]) < min) {
				min = Double.valueOf(str[0]);
				s = str[1];
			}
		}
		return  min+"#"+s;
	}
	
	public static String angle(String [] args) {
		if(args.length < 6)
			try {
				throw new Exception("angle (Functions class): argument number is < 6: "+args.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		double prec_X = Double.valueOf(args[0]);
		double prec_Y = Double.valueOf(args[1]);
		double local_X = Double.valueOf(args[2]);
		double local_Y = Double.valueOf(args[3]);
		double next_X = Double.valueOf(args[4]);
		double next_Y = Double.valueOf(args[5]);
		prec_X = prec_X - local_X;
		prec_Y = prec_Y - local_Y;
		next_X = next_X - local_X;
		next_Y = next_Y - local_Y;
		
		double b = 0;
		double a = 0;
		if(prec_X==next_X && prec_Y==next_Y) 
			b = Math.PI*2;
		else{
			a = Math.atan2(prec_X ,prec_Y);
			if (a < 0)
				a = (2 * Math.PI) + a;
			b = Math.atan2(next_X, next_Y);
			if (b < 0)
				b = (2 * Math.PI) + b;
			b = b - a;
			if (b < 0)
				b = (2 * Math.PI) + b;
		}
		return ""+b;
	}
	
	public static String check(String [] args) {
		
		int [][] g = {{1,0,1,0,1},{1,1,0,0,1},{1,1,1,1,0}};
		
		//String s = args[0].replaceAll("#", "");
		
		String [] s = args[0].split("#");
		
		System.out.println(s);
		int [] vp = new int [5];
		vp[0] = s[2].charAt(0)-'0';
		vp[1] = s[7].charAt(0)-'0';
		vp[2] = s[12].charAt(0)-'0';
		vp[3] = s[17].charAt(0)-'0';
		vp[4] = s[22].charAt(0)-'0';

		String [] id = new String [5];
		id[0] = s[1]+"";
		id[1] = s[6]+"";
		id[2] = s[11]+"";
		id[3] = s[16]+"";
		id[4] = s[21]+""; 
		 
		System.out.println(Arrays.toString(vp));
		
		int c1 = (vp[0] * g[0][0] + vp[1] * g[0][1] + vp[2] * g[0][2] + vp[3] * g[0][3] + vp[4] * g[0][4])%2;
		int c2 = (vp[0] * g[1][0] + vp[1] * g[1][1] + vp[2] * g[1][2] + vp[3] * g[1][3] + vp[4] * g[1][4])%2;
		int c3 = (vp[0] * g[2][0] + vp[1] * g[2][1] + vp[2] * g[2][2] + vp[3] * g[2][3] + vp[4] * g[2][4])%2;
		
		boolean error1 = false;
		boolean error2 = false;
		boolean error3 = false;
		
		for (int i=0; i<5; i++) {
			int cp1 = s[i*5+3].charAt(0)-'0';
			int cp2 = s[i*5+4].charAt(0)-'0';
			int cp3 = s[i*5+5].charAt(0)-'0';
			error1 = (c1==cp1);
			error2 = (c2==cp2);
			error3 = (c3==cp3);
		}
		
		String state = "";
		int malicious = -1;
		System.out.println(error1);
		System.out.println(error2);
		System.out.println(error3);
		if(error1 && error2 && error3) { state = "[OK]"; malicious = -1;}
		if(!error1 && !error2 && !error3) { state = "[MALICIOUS "+id[0]+"]"; malicious = 0;}
		if(error1 && !error2 && !error3) { state = "[MALICIOUS "+id[1]+"]"; malicious = 1;}
		if(!error1 && error2 && !error3) { state = "[MALICIOUS "+id[2]+"]"; malicious = 2;}
		if(error1 && error2 && !error3) { state = "[MALICIOUS "+id[3]+"]"; malicious = 3;}
		if(!error1 && !error2 && error3) { state = "[MALICIOUS "+id[4]+"]"; malicious = 4;}

		String sOut = state+ " "+ Arrays.toString(vp);
		
		if (malicious != -1) {
			vp[malicious] = 1 - vp[malicious];
			sOut += " -> "+ Arrays.toString(vp);
		}
		
		return sOut;
	}
	
	public static String fmu(String [] args) {
		double muo = Double.valueOf(args[0]);
		double hop = Double.valueOf(args[1]);
		double eng = Double.valueOf(args[2]);
		double mu = (muo*hop+eng)/(hop+1);
		return ""+mu;
	}
	
	public static String fsigma(String [] args) {
		double mu = Double.valueOf(args[0]);
		double sigmao = Double.valueOf(args[1]);
		double hop = Double.valueOf(args[2]);
		double eng = Double.valueOf(args[3]);
		double sigma = Math.sqrt((1/(hop+1))*((sigmao*sigmao)+((eng-mu)*(eng-mu))));
		return ""+sigma;
	}
	
	public static String factor(String [] args) {
		double mu = Double.valueOf(args[0]);
		double sigma = Double.valueOf(args[1]);
		double eng = Double.valueOf(args[2]);
		double f = mu - sigma * Math.sin(Math.PI*(mu/eng));
		return ""+f;
	}

	// Original Code
	
//	public static String encrypt(String [] args)throws Exception {
//		  String valToReturn = "";
//		  Blowfish b = new Blowfish(args[0],args[1]);
//		  valToReturn = b.encrypt();
//		  return valToReturn;
//		}
//		
//		public static String decrypt(String [] args) throws Exception {
//			String valToReturn = "";
//			
//		  Blowfish b = new Blowfish(args[0],args[1]);
//		  valToReturn = b.decrypt();
//		  return valToReturn;
//		}
	
	// Updated to add args[0] and args[1] by DA - Damilola Alao
			public static String encrypt(String [] args)throws Exception {
				//System.out.println("Ï am using blowfish encryption....");
				  String valToReturn = "";
				  String msg = args[0];// added by DA
				  if (msg != null) {
				  //Blowfish b = new Blowfish(args[0],args[1]);
				  Blowfish b = new Blowfish(key,msg); //Updated to add args[0] and args[1] by DA
				  String encryptedtext = b.encrypt();
				  String decryptedtext = b.decrypt(encryptedtext);
				  valToReturn = decryptedtext;
				  }
				  return valToReturn;
				}

			//testing blowfish
				
	
		public static String hash(String [] args) throws Exception {
			String valToReturn = "";
			
		  SuperFastHash b = new SuperFastHash(args[0],Long.valueOf(args[1]) ,Integer.valueOf(args[2]),Integer.valueOf(args[3]));
		  valToReturn = Long.toString(b.calculate());
		  return valToReturn;
		}
		public static String mod(String [] args) throws Exception {
			String valToReturn = "";
			
		  Operator b = new Operator(Long.valueOf(args[0]),Long.valueOf(args[1]));
		  valToReturn = b.mod();
		  return valToReturn;
		}
	
	public static String myf(String [] args) {
		String valToReturn = "";
		
		// String s = args[0] ; // -> is the name of the first argument of the function myf
		// double x =  Double.valueOf(args[5]); // -> is the (double) value of the first argument of the function myf 
		
		//TODO
		// Your program here
		
		return valToReturn;
	}
	
//added 06/26/2022
//_______________________________________________ beginning of code
//	 Added  NtruEncrypt - Damilola Alao
public static String thencrypt(String [] args) {
	String valToReturn = "";
	String msg = args[0];

	
	if(msg != null) {
       
   DecimalFormat format = new DecimalFormat("0.00");
   //System.out.println("Intitial message: " + msg);
   long t1 = System.nanoTime();
   long t2 = System.nanoTime();
//   
   //System.out.println("Key generation time: " + format.format((t2-t1)/1000000.0) + " ms");
   long t3 = System.nanoTime();
   byte[] encode = ntru.encrypt(msg.getBytes(), kp.getPublic());
//   
   long t4 = System.nanoTime();
   String encrypttime = format.format((t4-t3)/1000000.0);
   encryptiontime = encrypttime;
//   System.out.println("Encryption time: " + format.format((t4-t3)/1000000.0) + " ms");
//   System.out.println("----------------------------------------------------------");
//
   long t5 = System.nanoTime();
   byte[] decode = ntru.decrypt(encode, kp);
//      
   long t6 = System.nanoTime();
   String decrypttime = format.format((t6-t5)/1000000.0);
   decryptiontime = decrypttime;
//   System.out.println("Decrytption time: " + format.format((t6-t5)/1000000.0) + " ms");
//   System.out.println();
//   System.out.println("-------------------------------------------------------");
//   
   try {
		valToReturn = new String(decode, "UTF-8");
		//System.out.println("Decrypted Messge: " + valToReturn3);
		//System.out.println("-------------------------------------------------------");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//      
//   System.out.println( "Key generation time: " + format.format((t2-t1)/1000000.0) + " ms\r\n" +
//   					"Encrytption time: " + format.format((t4-t3)/1000000.0) + " ms\r\n" + 
//   					"Decrytption time: " + format.format((t6-t5)/1000000.0) + " ms\r\n" + decode);
}
	return valToReturn;
	
}
	
}
