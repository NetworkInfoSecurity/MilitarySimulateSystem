package thu.infosecurity.simulate.util;

import java.math.BigInteger;
import java.util.Map;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;

/**
 * 盲签名相关
 * Created by forest on 2017/5/23.
 */
public class blindSignature {
    public static void main(String[] args){
        System.out.println("这里是盲签名测试");

        BigInteger e = new BigInteger("32663");
        BigInteger d = new BigInteger("23");
        BigInteger n = new BigInteger("42167");

        BigInteger m = new BigInteger("123");       //签名的消息
        BigInteger factor = new BigInteger("37");   //盲因子

        BigInteger blindMsg = blindHideMsg(m, factor, e, n);
        BigInteger blindSig = blindSignature(blindMsg, d, n);
        BigInteger sig = blindRetriveSig(blindSig, factor, n);
        System.out.println("盲签名 = " + sig);
        BigInteger realSig = m.modPow(d, n);
        System.out.println("原签名 = " + realSig);
    }

    /**盲签名-盲化*/
    public static BigInteger blindHideMsg(BigInteger msg, BigInteger factor, BigInteger e, BigInteger n){
        BigInteger hideMsg = msg.multiply(factor.modPow(e, n)).mod(n);
        return hideMsg;
    }

    /**盲签名-签名*/
    public static BigInteger blindSignature(BigInteger blindMsg, BigInteger d, BigInteger n){
        BigInteger blindSig = blindMsg.modPow(d, n);
        return blindSig;
    }

    /**盲签名-解盲得到签名*/
    public static BigInteger blindRetriveSig(BigInteger blindSig, BigInteger factor, BigInteger n){
        BigInteger signature = blindSig.multiply(factor.modInverse(n)).mod(n);
        return signature;
    }




}
