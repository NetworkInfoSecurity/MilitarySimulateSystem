package thu.infosecurity.simulate.util.NumberTheory;

import thu.infosecurity.simulate.util.BaseElement.BigNum;
import java.util.ArrayList;

/**
 * Class for NumberTheory.RSABase
 * Created by ForestNeo on 2016/10/14.
 *
 * 加密接口：public static String encrypt(String plainText, String publicKey, String pn)
 * 解密接口：public static String decrypt(String encryptText, String privateKey, String pn)
 *
 */
public class RSABase {

    public static void main(String[] args) throws Exception{
        String plain = "dagd";
        String plain2 = "dfadgewrtgsfdhgyreq tg34 r5qtyq 543 5y543 4";
        String p = "1123";
        String q = "9181";
        String e = "22461621624637251777456300960187849691598603339644585974793939";
        String d = "7137485809071382629313599946032959736135820990003992923434211286437749856564132416533151563536365992928134427594801669677272579";
        String n = "9975195964643031981159776199535538203609239398164642965205824552352422199365479580668103900418271513891622374287002737177845049";

        String enStr = encrypt(plain, e, n);
        System.out.println("encrypted = " + enStr);

        String deStr = decrypt(enStr, d, n);
        System.out.println("decrypted = " + deStr);
    }

    /**
     * Pad and slice
     * 最后一组数据前面补1，以防最后一组数据出现00，如果全部填充0的话不知道原有几个0
     */
    private static ArrayList<String> stringSlice(String srcString, int length){
        ArrayList<String> stringList = new ArrayList<>();
        int i = 0;
        while(length * (i+1) < srcString.length()){
            String temp = srcString.substring(length * i, length * (i + 1));
            stringList.add(temp);
            i++;
        }
        if(srcString.length() % length == 0) {
            stringList.add(srcString.substring(length * i, srcString.length()));
            return stringList;
        }

//        stringList.add(paddingWithPrefix(srcString.substring(length * i, srcString.length()), length));
        stringList.add(padding(srcString.substring(length * i, srcString.length()), length));
        return stringList;
    }

    /**
     * Change the input string to BigNum that used to Encrypt
     * use ascii
     * 有效的ascii:32-126,不压缩，3位表示一个字符，例如abc->097098099
     * 防止出现首位为0的情况，每次加密首位补1，同时可以进行错误验证。
     * @param input
     * @param length : to get length
     * @return
     */
    public static String RSA_StringToAscii(String input, int length){
        String ret = "";
        for(char ch : input.toCharArray()){
            String str = RSA_CharToString(ch, length);
//            System.out.println("ch = " + ch + "\t\tstr = " + str);
            ret += str;
        }
        return ret;
    }
    /**
     * length = 2: a = 97; 97-32 = 65, ret "65";
     * length = 3: a = 97; ret "097"
     * @param ch
     * @param length
     * @return
     */
    public static String RSA_CharToString(char ch, int length){
        if(length == 3){
            int charValue = (int)ch;
            if(ch < 100)
                return "0" + Integer.toString(charValue);
            return Integer.toString(charValue);
        }
        else{
            System.err.println("Wrong");
            return null;
        }
    }
    public static BigNum RSA_Encrypt(BigNum plainText, BigNum publicKey, BigNum n) throws Exception{
        return plainText.bigNumPowerModFast(publicKey, n);
    }
    public static BigNum RSA_Decrypt(BigNum encryptText, BigNum privateKey, BigNum n) throws Exception{
        return encryptText.bigNumPowerModFast(privateKey, n);
    }

    /**
     * 外部RSA接口
     * 注意切割长度为 n.length - 1
     * 加密后填充长度为 n.length
     */
    public static String encrypt(String plainText, String publicKey, String pn){
        try{
            BigNum p = new BigNum(publicKey);
            BigNum n = new BigNum(pn);

            ArrayList<String> strList = stringSlice(RSA_StringToAscii(plainText, 3), n.length()-1);
//            for(String str : strList){
//                System.out.printf(str);
//            }
//            System.out.println();

//            ArrayList<String> ansList = new ArrayList<>();
            String enStr = "";
            for(String str : strList){
                BigNum x = new BigNum(str);
                BigNum y = RSA_Encrypt(x, p, n);
//                System.out.println("x = " + x + "\t\ty = " + padding(y.toString(), n.length()));
                enStr += padding(y.toString(), n.length());
            }
            return enStr;
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public static String decrypt(String encryptText, String privateKey, String pn){
        try{
            BigNum q = new BigNum(privateKey);
            BigNum n = new BigNum(pn);

            ArrayList<String> strList = stringSlice(encryptText, n.length());
            String ret = "";

            for(int i = 0; i < strList.size() - 1; i++){
                BigNum x = new BigNum(strList.get(i));
                BigNum y = RSA_Decrypt(x, q, n);
//                System.out.println("x = " + x + "\t\ty = " + y);
                ret += padding(y.toString(), n.length() - 1);
            }
            /*处理最后一个分组*/
            BigNum x = new BigNum(strList.get(strList.size()-1));
            BigNum y = RSA_Decrypt(x, q, n);
            String paddedY = padding(y.toString(), n.length() - 1);

//            System.out.println("m = " + x + "\t\tn = " + y);
            int length = (strList.size() - 1) * (n.length() - 1) + paddedY.length();
            length = length % 3;//需要删除前缀的个数
            paddedY = paddedY.substring(length, paddedY.length());
            ret += paddedY;

//            System.out.println("retTemp = " + ret);
            char[] charArray = ret.toCharArray();
            int removeFlag = ret.length() % 3;
            /*每组长度n.length - 1*/
            ret = RSA_AsciiToString(ret);
            return ret;
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    private static String padding(String src, int length){
        if(src.length() == length)
            return src;
        String ret = "";
        for(int i = 0; i < length - src.length(); i++)
            ret += '0';
        return ret + src;
    }

    private static String paddingWithPrefix(String src,int length){
        if(src.length() == length)
            return src;
        String ret = "1";
        for(int i = 0; i < length - src.length() - 1; i++)
            ret += '0';
        return ret + src;
    }


    private static String RSA_AsciiToString(String src){
        if(src.length() % 3 != 0){
            System.err.println("Might found error");
        }

        String ret = "";
        for(int i = 0; i < src.length() / 3; i++) {
            String nowString = src.substring(3*i, 3*(i+1));

            //todo 网络安全大作业添加修改
            if(nowString.equals("000"))
                continue;


            int charValue = Integer.parseInt(nowString);
            ret += (char)charValue;
        }
        return ret;
    }
    
    
}
