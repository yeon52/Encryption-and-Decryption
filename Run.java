package password;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64; 
import java.util.Base64.Decoder; 
import java.util.Base64.Encoder;

public class Run {
	private static IvParameterSpec IV_16;
	private static IvParameterSpec IV_8;
	public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException,
	InvalidKeyException, BadPaddingException, UnsupportedEncodingException,
	IllegalBlockSizeException, InvalidAlgorithmParameterException
	{
		Scanner sc = new Scanner(System.in);
		String algorithm, mode, encdec, str, key;
		
		System.out.println("원하는 알고리즘을 선택하세요(DES/DESede/AES)");
		algorithm = sc.next();
		System.out.println("원하는 동작모드를 선택하세요(ECB/CBC/CFB/OFB/CTR)");
		mode = sc.next();
		System.out.println("ENC(암호화)/DEC(복호화)중 선택하세요");
		encdec = sc.next();
		System.out.println("ENC(암호화)/DEC(복호화)할 문자열을 입력하세요");
		str = sc.next();
		System.out.println("키를 입력하세요 : ");
		key = sc.next();

		//키생성
		SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(),algorithm);
		//Cipher 객체 생성
		Cipher cipher = Cipher.getInstance(algorithm+"/"+mode+"/"+"PKCS5Padding");
		//초기벡터 생성
		IV_16 = new IvParameterSpec(new String("blahblahblahblah").getBytes());
		IV_8 = new IvParameterSpec(new String("blahblah").getBytes());
		//인코더 디코더
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		
		if(encdec.equals("ENC")) //암호화를 할 경우
		{
			if(mode.equals("ECB")) //ECB는 초기벡터 필요x
				cipher.init(Cipher.ENCRYPT_MODE,secretkey);
			else //ECB모드를 제외한 다른 모드들은 모두 초기벡터 필요
			{
				if(algorithm.equals("AES"))//AES일 경우 초기벡터 길이는 16바이트
					cipher.init(Cipher.ENCRYPT_MODE,secretkey,IV_16);
				else //AES가 아닐 경우 초기벡터 길이는 8바이트
					cipher.init(Cipher.ENCRYPT_MODE,secretkey,IV_8);
			}
			
			//문자열을 바이트 배열로 바꾼 후 암호화
			byte[] enc_Bytes = cipher.doFinal(str.getBytes("UTF-8"));
			//인코딩
			byte[] encodedBytes = encoder.encode(enc_Bytes);
			//문자열로 변경 -> 암호화 완료
			String encryption = new String(encodedBytes);
			
			//출력
			System.out.println("평문 : " + str);
			System.out.println("암호화 : "+ encryption);
		}
		else //복호화를 할 경우
		{
			if(mode.equals("ECB")) //초기벡터 필요x
				cipher.init(Cipher.DECRYPT_MODE,secretkey);
			else //초기벡터 필요
			{
				if(algorithm.equals("AES"))//초기벡터 16바이트
					cipher.init(Cipher.DECRYPT_MODE,secretkey,IV_16);

				else //초기벡터 8바이트
					cipher.init(Cipher.DECRYPT_MODE,secretkey,IV_8);
			}
			
			//문자열을 바이트배열로 바꾼 후 디코딩
			byte[] decodedBytes = decoder.decode(str.getBytes());
			//복호화
			byte[] dec_Bytes = cipher.doFinal(decodedBytes); 
			//문자열로 변경 -> 복호화 완료
			String decryption = new String(dec_Bytes,"UTF-8");
			
			//출력
			System.out.println("암호문 : " + str);
			System.out.println("복호화 : " + decryption);
		}
	}
}
