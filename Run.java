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
		
		System.out.println("���ϴ� �˰������� �����ϼ���(DES/DESede/AES)");
		algorithm = sc.next();
		System.out.println("���ϴ� ���۸�带 �����ϼ���(ECB/CBC/CFB/OFB/CTR)");
		mode = sc.next();
		System.out.println("ENC(��ȣȭ)/DEC(��ȣȭ)�� �����ϼ���");
		encdec = sc.next();
		System.out.println("ENC(��ȣȭ)/DEC(��ȣȭ)�� ���ڿ��� �Է��ϼ���");
		str = sc.next();
		System.out.println("Ű�� �Է��ϼ��� : ");
		key = sc.next();

		//Ű����
		SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(),algorithm);
		//Cipher ��ü ����
		Cipher cipher = Cipher.getInstance(algorithm+"/"+mode+"/"+"PKCS5Padding");
		//�ʱ⺤�� ����
		IV_16 = new IvParameterSpec(new String("blahblahblahblah").getBytes());
		IV_8 = new IvParameterSpec(new String("blahblah").getBytes());
		//���ڴ� ���ڴ�
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		
		if(encdec.equals("ENC")) //��ȣȭ�� �� ���
		{
			if(mode.equals("ECB")) //ECB�� �ʱ⺤�� �ʿ�x
				cipher.init(Cipher.ENCRYPT_MODE,secretkey);
			else //ECB��带 ������ �ٸ� ������ ��� �ʱ⺤�� �ʿ�
			{
				if(algorithm.equals("AES"))//AES�� ��� �ʱ⺤�� ���̴� 16����Ʈ
					cipher.init(Cipher.ENCRYPT_MODE,secretkey,IV_16);
				else //AES�� �ƴ� ��� �ʱ⺤�� ���̴� 8����Ʈ
					cipher.init(Cipher.ENCRYPT_MODE,secretkey,IV_8);
			}
			
			//���ڿ��� ����Ʈ �迭�� �ٲ� �� ��ȣȭ
			byte[] enc_Bytes = cipher.doFinal(str.getBytes("UTF-8"));
			//���ڵ�
			byte[] encodedBytes = encoder.encode(enc_Bytes);
			//���ڿ��� ���� -> ��ȣȭ �Ϸ�
			String encryption = new String(encodedBytes);
			
			//���
			System.out.println("�� : " + str);
			System.out.println("��ȣȭ : "+ encryption);
		}
		else //��ȣȭ�� �� ���
		{
			if(mode.equals("ECB")) //�ʱ⺤�� �ʿ�x
				cipher.init(Cipher.DECRYPT_MODE,secretkey);
			else //�ʱ⺤�� �ʿ�
			{
				if(algorithm.equals("AES"))//�ʱ⺤�� 16����Ʈ
					cipher.init(Cipher.DECRYPT_MODE,secretkey,IV_16);

				else //�ʱ⺤�� 8����Ʈ
					cipher.init(Cipher.DECRYPT_MODE,secretkey,IV_8);
			}
			
			//���ڿ��� ����Ʈ�迭�� �ٲ� �� ���ڵ�
			byte[] decodedBytes = decoder.decode(str.getBytes());
			//��ȣȭ
			byte[] dec_Bytes = cipher.doFinal(decodedBytes); 
			//���ڿ��� ���� -> ��ȣȭ �Ϸ�
			String decryption = new String(dec_Bytes,"UTF-8");
			
			//���
			System.out.println("��ȣ�� : " + str);
			System.out.println("��ȣȭ : " + decryption);
		}
	}
}
