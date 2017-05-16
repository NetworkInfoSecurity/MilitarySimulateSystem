package thu.infosecurity.simulate.util;
import thu.infosecurity.simulate.util.BaseElement.BigNum;
import thu.infosecurity.simulate.util.NumberTheory.RSABase;
import thu.infosecurity.simulate.util.NumberTheory.BigNumGCD;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class RSA {
    public static void main(String[] args){
        System.out.println("this is rsa");


        String plain = "dfadgewrtgsfdhgyreq tg34 r5qtyq 543 5y543 4";

        /*Key generation testing*/
        Map<String, String> key = RSA_GenerateKey(20, 10);
        String e = key.get("e");
        String d = key.get("d");
        String n = key.get("n");

        /*RSA testing*/
        String enStr = RSA_Encrypt(plain, e, n);
        System.out.println("encrypted = " + enStr);

        String deStr = RSA_Decrypt(enStr, d, n);
        System.out.println("decrypted = " + deStr);
    }




    public static String RSA_Encrypt(String plainText, String publicKey, String pn){
        return RSABase.encrypt(plainText,publicKey,pn);
    }

    public static String RSA_Decrypt(String encryptText, String privateKey, String pn){
        return RSABase.decrypt(encryptText, privateKey, pn);
    }

    /**
     * 生成秘钥（e,d,n）其中(e,n)为公钥，(d,n)为私钥
     * @param length
     * @param accuracy
     * @return
     */
    public static Map<String, String> RSA_GenerateKey(int length, int accuracy){
        try{
            BigNum p = BigNum.generatePrime(length, accuracy);
            BigNum q = BigNum.generatePrime(length, accuracy);
            BigNum n = p.bigNumMul(q);
            BigNum phi_n = p.bigNumSub(BigNum.BigOne).bigNumMul(q.bigNumSub(BigNum.BigOne));
            BigNum e = BigNum.generateNumberPrimeTo(phi_n);
            BigNum d = BigNumGCD.getReverse(e, phi_n);

            Map<String, String> retMap = new HashMap<>();
            retMap.put("e", e.toString());
            retMap.put("d", d.toString());
            retMap.put("n", n.toString());
            return retMap;
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

}
