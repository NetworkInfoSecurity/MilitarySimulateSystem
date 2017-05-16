package thu.infosecurity.simulate.util;
import thu.infosecurity.simulate.util.BaseElement.BigNum;
import thu.infosecurity.simulate.util.NumberTheory.RSABase;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class RSA {
    public static void main(String[] args){
        System.out.println("this is rsa");


        String plain = "dfadgewrtgsfdhgyreq tg34 r5qtyq 543 5y543 4";
        String e = "22461621624637251777456300960187849691598603339644585974793939";
        String d = "7137485809071382629313599946032959736135820990003992923434211286437749856564132416533151563536365992928134427594801669677272579";
        String n = "9975195964643031981159776199535538203609239398164642965205824552352422199365479580668103900418271513891622374287002737177845049";

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

    public static String RSA_GenerateKey(int length, int accuracy){

        try{
            BigNum p = BigNum.generatePrime(length, accuracy);

        }catch (Exception e){
            System.out.println(e.toString());
        }


        return null;
    }

}
