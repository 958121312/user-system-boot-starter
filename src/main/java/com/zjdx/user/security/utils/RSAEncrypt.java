package com.zjdx.user.security.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * RSA算法密码加密
 * @author yuntian
 * @since 2022-02-21
 */
public class RSAEncrypt implements PasswordEncoder {

	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDzy2zEOu39jsLRKrwZclylTwrBzpt833GCG2PsOFy71gUGFYxCtVWJ+wg9gGOTd7jC/lVydNsVqxPNyo9WqeAePY7NfbGYEbgrf9zV0LF1RmbVtdfN+pTVK1Ovq9KyCuQqCbgNL9D676ax1Y1V1F2wlcUvrRNUY8RTXorctuW0LQIDAQAB";

	/**
	 * 私钥
	 */
	public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPPLbMQ67f2OwtEqvBlyXKVPCsHOm3zfcYIbY+w4XLvWBQYVjEK1VYn7CD2AY5N3uML+VXJ02xWrE83Kj1ap4B49js19sZgRuCt/3NXQsXVGZtW11836lNUrU6+r0rIK5CoJuA0v0PrvprHVjVXUXbCVxS+tE1RjxFNeity25bQtAgMBAAECgYEAr4DzRPSTXWSHdOKJ1NvSPCMBwHGG18dXRG2JaoVMpOA2b6HVfjRiZtlzniquBHw8YwuK4cGj8h5dPFmlBKN4NYUzp1LZy0QZiUedHJHRoEvy/VssZyWyiLCj8OvELOt4+HDRIyFKrz8xxhgEcQ8tw7a4d2QquM7kq/OvD+Z3hZUCQQD6fxittaEKIDe2KoSE9rczDrQNk+TdHYjVHnOCirhuiB2fAjE4wCEygnMmjvkp/YaQ+nCcV9mi2ZNJdCgUFar3AkEA+SaiY1BxpnSw794KWXC1IrWk2uilhWZruHo26m9e2+1rt6OchTL/8IWItqySxCf85GyPCmIVfND4t/GFwxiM+wJBALsW+iOjqGebsICKOPfmHOYxcSFC+Ih9apliaPtzCFTlrNn6FTFBrom/DKob1tUZ2cumgT0EEDSlNHCEJPr7Q/kCQBf/CJTYIpoNkx0HHagPmuKE7CPJVGfMvYM/AhsCLvivnThKocCScOQReGFfraMG1uyJKB2v5LHozouoz2FC9BECQCvbS2BExm4bgd+t8oOoAb1vxnrPHbQJo+8RE0Ot4/NnMd6uOePD6CXAQxRxI8kUynu31yGQwajgkzoBJWDwkUA=";

	/**
	 * 随机生成密钥对 
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
		Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		// 初始化密钥对生成器，密钥大小为96-1024位  
		keyPairGen.initialize(1024,new SecureRandom());  
		// 生成一个密钥对，保存在keyPair中  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));  
		// 得到私钥字符串  
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));  
		// 将公钥和私钥保存到Map
		keyMap.put(0,publicKeyString);  //0表示公钥
		keyMap.put(1,privateKeyString);  //1表示私钥
		return keyMap;
	}  
	/** 
	 * RSA公钥加密 
	 *  
	 * @param str 
	 *            加密字符串
	 * @param publicKey 
	 *            公钥 
	 * @return 密文 
	 * @throws Exception 
	 *             加密过程中的异常信息 
	 */
	private static String encrypt( String str, String publicKey ) {
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = null;
		try {
			pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
			//RSA加密
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			String outStr = Base64.encodeBase64String(getMaxResultEncrypt(str, cipher));
			return outStr;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getMaxResultEncrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
		byte[] inputArray = str.getBytes();
		int inputLength = inputArray.length;
		// 最大加密字节数，超出最大字节数需要分组加密
		int MAX_ENCRYPT_BLOCK = 117;
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return resultBytes;
	}

	private static byte[] getMaxResultDecrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] inputArray = Base64.decodeBase64(str.getBytes("UTF-8"));
		int inputLength = inputArray.length;
		// 最大解密字节数，超出最大字节数需要分组加密
		int MAX_ENCRYPT_BLOCK = 128;
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return resultBytes;
	}

	/** 
	 * RSA私钥解密
	 *  
	 * @param str 
	 *            加密字符串
	 * @param privateKey 
	 *            私钥 
	 * @return 铭文
	 * @throws Exception 
	 *             解密过程中的异常信息 
	 */
	private static String decrypt(String str, String privateKey) {
		//64位解码加密后的字符串
		byte[] inputByte = new byte[0];
		try {
			inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);
		RSAPrivateKey priKey = null;
		//RSA解密
		Cipher cipher = null;
		try {
			priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			return new String(getMaxResultDecrypt(str, cipher));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String encode(CharSequence charSequence) {
		return encrypt(charSequence.toString(), PUBLIC_KEY);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodePassword) {
		return Objects.equals(decrypt(encodePassword, PRIVATE_KEY), rawPassword.toString());
	}

	@Override
	public boolean upgradeEncoding(String encodedPassword) {
		return PasswordEncoder.super.upgradeEncoding(encodedPassword);
	}
}