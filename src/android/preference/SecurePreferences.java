package android.preference;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;


public class SecurePreferences {

	
    private static final String TAG="SecurePreferences";
	
	
    private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };

	public String encrypt(String strToEncrypt)
	{
	    
	    Log.d(TAG,"String to be Encrypted "+strToEncrypt);
	    try
	    {
	        if (strToEncrypt.equals("")) return strToEncrypt;
	    	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        final String encryptedString = android.util.Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes()),android.util.Base64.DEFAULT);
	        return encryptedString;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return null;
	
	}
	
	public String decrypt(String strToDecrypt)
	{
	    Log.d(TAG,"String to be decrypted "+strToDecrypt);
	    try
	    {
	        if (strToDecrypt.equals("")) return strToDecrypt;
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        final String decryptedString = new String(cipher.doFinal(android.util.Base64.decode(strToDecrypt,android.util.Base64.DEFAULT)));
	        return decryptedString;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
		}
	    return null;
	}
	
	
	/*public  String encrypt(Context context,String message) throws Exception

	{

	String message1=android.util.Base64.encodeToString(message.getBytes(),android.util.Base64.DEFAULT);
	String salt =  context.getResources().getString(R.string.EncryptionKey);
	SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
	Cipher c = Cipher.getInstance("AES");
	c.init(Cipher.ENCRYPT_MODE, key);
	byte[] encVal = c.doFinal(message1.getBytes());
	String encrypted=Base64.encodeToString(encVal, android.util.Base64.DEFAULT);
	return encrypted;

	}

	//Decryption

	public String decrypt(Context context,String message) throws Exception

	{

	String salt = context.getResources().getString(R.string.EncryptionKey);
	Cipher c = Cipher.getInstance("AES");
	SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
	c.init(Cipher.DECRYPT_MODE, key);
	byte[] decordedValue = Base64.decode(message.getBytes(), android.util.Base64.DEFAULT);
	byte[] decValue = c.doFinal(decordedValue);
	String decryptedValue = new String(decValue);
	String decoded=new String(Base64.decode(decryptedValue, android.util.Base64.DEFAULT));
	return decoded;
	} 
*/
}