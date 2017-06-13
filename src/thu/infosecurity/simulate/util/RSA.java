package thu.infosecurity.simulate.util;
import thu.infosecurity.simulate.controller.SceneControl;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.util.BaseElement.BigNum;
import thu.infosecurity.simulate.util.NumberTheory.RSABase;
import thu.infosecurity.simulate.util.NumberTheory.BigNumGCD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sunlin on 2017-05-16.
 *
 * 功能：提供
 * 接口：加密，解密，生成秘钥，验证士兵
 */
public class RSA {
    public static void main(String[] args){
        System.out.println("this is rsa");


        String plain = "dfadgewrtgsfdhgyreq tg34 r5qtyq 543 5y543 4";

        /*Key generation testing*/
        Map<String, String> key = RSA_GenerateKey(3, 10);
        String e = key.get("e");
        String d = key.get("d");
        String n = key.get("n");

        System.out.println(n + "," + e);
        System.out.println(n + "," + d);


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
     * @param length        秘钥长度
     * @param accuracy      测试精确度（数据越大表示生成的素数越可靠，建议10以上）
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

    /** 验证一个士兵是否合法*/
    public static boolean soldierVerify(ArrayList<String> publicKeyList, Soldier soldier){
//        System.out.println("Now, verify soldier" + soldier.getName());
        String rndStr = randomString(5);

        //判断公钥列表中是否有该成员的公钥
        if(SceneControl.findPublicKey(publicKeyList, soldier)){
            String n = soldier.getPuKey().split(",")[0];
            String e = soldier.getPuKey().split(",")[1];
            String d = soldier.getPrKey().split(",")[1];

            /*按照题目要求，假定这里的公钥是所有人可以获取的*/
            String enStr = RSA_Encrypt(rndStr, e, n);

            /*私钥是需要验证的用户自己才有*/
            String deStr = RSA_Decrypt(enStr, d, n);

            if(deStr.equals(rndStr)) {
                System.out.println("士兵" + soldier.getName() + "验证成功！");
                return true;
            } else {
                System.out.println("士兵" + soldier.getName() + "验证失败！");
                return false;
            }
        } else {
            System.out.println("士兵" + soldier.getName() + "是间谍！");
            return false;
        }
    }

    /** 产生一个随机的字符串, 用于士兵验证*/
    private static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

}
