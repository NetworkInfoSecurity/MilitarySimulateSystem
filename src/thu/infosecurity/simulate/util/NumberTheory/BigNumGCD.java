package thu.infosecurity.simulate.util.NumberTheory;
import thu.infosecurity.simulate.util.BaseElement.BigNum;

import java.util.HashMap;
import java.util.Map;


class uvBigNum{
    HashMap<String, BigNum> uv = new HashMap<String, BigNum>();

    public uvBigNum(BigNum u, BigNum v) {
        uv.put("u", u);
        uv.put("v", v);
    }

    public BigNum getu(){
        return uv.get("u");
    }
    public BigNum getv(){
        return uv.get("v");
    }
}

/**
 * GCD算法
 * Created by ForestNeo on 2016/10/14.
 */
public class BigNumGCD {

    private static BigNum bigZero = new BigNum(0);
    private static BigNum bigOne = new BigNum(1);

    public static void main(String[] args) throws Exception{
//        BigNumGCD so = new BigNumGCD();
//        System.out.println("MyRSA for bignumGCD");
//        BigNum bn1 = new BigNum("5463572435247624534634");
//        BigNum bn2 = new BigNum("5633452475464353445223");
//
//        BigNum rd1 = BigNum.generateBigNumByLength(23);
//        BigNum rd2 = BigNum.generateBigNumByLength(23);
//
//        System.out.println("rd1:" + rd1);
//        System.out.println("rd2:" + rd2);
//        BigNum bn3 = so.gcd(rd1, rd2);
//        System.out.println(bn3);
//        BigNum bn4 = so.gcd_b(rd1, rd2);
//        System.out.println(bn4);


        uvBigNum uv = BigNumGCD.getuv(new BigNum(17), new BigNum(23));
        System.out.println("u = " + uv.getu());
        System.out.println("v = " + uv.getv());
    }

    public static BigNum gcd(BigNum bn1, BigNum bn2) throws Exception{
        if(bn1.bigNumMod(bn2).isZero())
            return bn2;
        return gcd(bn2, bn1.bigNumMod(bn2));
    }

    /**
     * the balanced Euclidean algorithm
     */
    public static BigNum gcd_b(BigNum bn1, BigNum bn2) throws Exception{
        if(bn1.bigNumMod(bn2).isZero())
            return bn2;
        if(bn1.bigNumMod(bn2).isBiggerThan(bn2.bigNumDiv(new BigNum("2"))))
            return gcd_b(bn2, bn2.bigNumSub(bn1.bigNumMod(bn2)));
        return gcd(bn2, bn1.bigNumMod(bn2));
    }

    public static String[] getUVout(BigNum a, BigNum b) throws Exception{
        uvBigNum uv = getuv(a, b);
        String[] ret = new String[2];
        ret[0] = uv.getu().toString();
        ret[1] = uv.getv().toString();
        return ret;
    }
    
    public static uvBigNum getuv(BigNum a, BigNum b) throws Exception {
        uvBigNum ret = getuv(a,b,b);
        return new uvBigNum(ret.getu().bigNumMod(b), ret.getv().bigNumMod(b));
    }

    public static uvBigNum getuv(BigNum a, BigNum b, BigNum c) throws Exception{
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);
//        System.out.println("c = " + c);
        if(b.bigNumMod(a.bigNumMod(b)).isSame(bigZero)){
            uvBigNum ret = new uvBigNum(bigOne, c.bigNumSub(a.bigNumDiv(b)).bigNumMod(c));
            return ret;
        }
        uvBigNum newpair = getuv(b, a.bigNumMod(b),c);

//        System.out.println("        u = " + newpair.getu());
//        System.out.println("        v = " + newpair.getv());

        BigNum x = newpair.getv();
        BigNum y = newpair.getu().bigNumAdd(c).bigNumSub(a.bigNumDiv(b).bigNumMul(x).bigNumMod(c));
        uvBigNum ret = new uvBigNum(x, y);
        return ret;
    }

    public static BigNum getReverse(BigNum a, BigNum mod) throws Exception{
        uvBigNum uv = getuv(a, mod);
        //a*u + b*v = 1 mod(p)
        return uv.getu();

    }

}

