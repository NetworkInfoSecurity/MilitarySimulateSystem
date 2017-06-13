package thu.infosecurity.simulate.util;

import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class MyDES {

    /**
     * @param args
     */
    public static void main(String[] args) {

        for(int i = 0; i < 20; i++) {
            System.out.println(MyDES.generateDESkey());
        }

        String content = "DESTest";
        // 密码长度必须是8的倍数
        String password = "12345678";
        System.out.println("密　钥：" + password);
        System.out.println("加密前：" + content);
        byte[] result = encrypt(content, password);
        System.out.println("加密后：" + new String(result));
        String decryResult = decrypt(result, password);
        System.out.println("解密后：" + decryResult);
    }

    /*生成DES秘钥，8位*/
    public static String generateDESkey(){
        StringBuilder ret = new StringBuilder("");
        for(int i = 0; i < 8; i++) {
            ret.append((new Random()).nextInt(9)+1);
        }
        return ret.toString();

    }

    /**
     * 加密
     *
     * @param content
     *            待加密内容
     * @param key
     *            加密的密钥
     * @return
     */
    public static byte[] encrypt(String content, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content.getBytes());
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content
     *            待解密内容
     * @param key
     *            解密的密钥
     * @return
     */
    public static String decrypt(byte[] content, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}