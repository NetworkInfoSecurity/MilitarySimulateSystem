package thu.infosecurity.simulate.util.BaseElement;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * class for prime
 * Created by forestneo on 2016/11/15.
 */
public class Prime{


    public static void main(String[] args) throws Exception{

        System.out.println("System generating prime");
        long time01 = System.currentTimeMillis();
        BigInteger prime1 = BigInteger.probablePrime(768, new Random());
        long time02 = System.currentTimeMillis();
        System.out.println(prime1);
        System.out.println("time_start:" + (time02-time01));

        System.out.println("This is class for prime");

        System.out.println(new BigNum(2).bigNumPower(new BigNum(768)).length());
        System.out.println(new BigNum(2).bigNumPower(new BigNum(1024)).length());



        Prime pt = new Prime();

        long time1 = System.currentTimeMillis();
        BigNum prime = pt.generage(232);
        long time2 = System.currentTimeMillis();
        System.out.println("generate prime: " + prime);
        System.out.println("time:" + (time2-time1));


        BigInteger bi = prime.toBigInteger();
        System.out.println(bi.isProbablePrime(10));
        long time3 = System.currentTimeMillis();
        System.out.println("time2:" + (time3-time2));

        //pt.MyRSA(323);
    }

    public void test(int length) throws Exception{
        BigNum test = new BigNum();
        long time1 = System.currentTimeMillis();
        BigNum prime = test.generateBigNumByLengthForPrime(length);
        long time2 = System.currentTimeMillis();
        if (prime.isPrime(10)) {
            System.out.println("Got Prime: " + prime);
        } else {
            System.out.println("Not Prime: " + prime);
        }
        long time3 = System.currentTimeMillis();

        System.out.println("Time1 : " + (time2-time1) + "mm");
        System.out.println("Time2 : " + (time3-time2) + "mm");
        System.out.println("generating times:" + (time3-time1));

    }

    public BigNum generage(int length) throws Exception{
        BigNum test = new BigNum();
        BigNum prime;

        int round = 0;
        while(true){
            round++;
            long time1 = System.currentTimeMillis();
            prime = test.generateBigNumByLengthForPrime(length);
            long time2 = System.currentTimeMillis();
            if(prime.isPrime(5)){
                long time3 = System.currentTimeMillis();
                break;
            }
            long time3 = System.currentTimeMillis();
            System.out.println("Round " + round +", Generating Time"+(time2-time1) +", TestingTime: " + (time3-time2));
        }
        return prime;
    }

}
