package thu.infosecurity.simulate.util;

import thu.infosecurity.simulate.model.Soldier;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Map;
import java.util.Random;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;

/**
 * 盲签名相关
 * Created by forest on 2017/5/23.
 */
public class blindSignature {
    public static void main(String[] args){
        System.out.println("这里是盲签名测试");

        /*
        BigInteger e = new BigInteger("32663");
        BigInteger d = new BigInteger("23");
        BigInteger n = new BigInteger("42167");

        BigInteger m = new BigInteger("123");       //签名的消息
        BigInteger factor = new BigInteger("30");   //盲因子

        BigInteger blindMsg = blindHideMsg(m, factor, e, n);
        BigInteger blindSig = blindSignature(blindMsg, d, n);
        BigInteger sig = blindRetriveSig(blindSig, factor, n);
        System.out.println("盲签名 = " + sig);
        BigInteger realSig = m.modPow(d, n);
        System.out.println("原签名 = " + realSig);
        */

        ArrayList<Soldier> soldierList = xmlReader.getSoldierListFromFile();
        ArrayList<Soldier> soldiers = new ArrayList<>();
        soldiers.add(soldierList.get(1));
        vote(soldierList, 2);


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


    /**
     * 匿名投票过程，士兵列表负责投票，投票对象为n个人，每人仅可投一票
     */
    public static Integer vote(ArrayList<Soldier> soldierList, int n) {

        /*生成CTF中心的公私钥*/
        Map<String, String> key = RSA_GenerateKey(10, 10);
        BigInteger e = new BigInteger(key.get("e"));
        BigInteger d = new BigInteger(key.get("d"));
        BigInteger pn = new BigInteger(key.get("n"));

        /*初始化——获得m*n张票据*/
        ArrayList<ArrayList<BigInteger>> ticketsList = new ArrayList<>();
        for(int i = 0; i < soldierList.size(); i++) {
            ArrayList<BigInteger> tickets = new ArrayList<>();
            for(int j = 0; j < n; j++) {
                BigInteger ticket = reArrangeTicket(j+1);     //士兵i的j号票
                System.out.println("这里是票据" + ticket);
                tickets.add(ticket);
            }
            ticketsList.add(tickets);
        }

        /*初始化——为每个士兵生成盲化因子，假定盲因子范围在1000-9000*/
        ArrayList<BigInteger> factorList = new ArrayList<>();
        for(int i = 0; i < soldierList.size(); i++) {
            factorList.add(getRandom());
//            System.out.println("factor:" + factorList.get(i));
        }


        /*票据盲签名-第一步：盲化*/
        for(int i = 0; i < soldierList.size(); i++) {
            ArrayList<BigInteger> tickets = ticketsList.get(i);
            for(int j = 0; j < n; j++) {
                BigInteger oldTicket = tickets.get(j);      //原有票
                BigInteger newTicket = blindHideMsg(oldTicket, factorList.get(i), e, pn);
                tickets.set(j, newTicket);
            }
            ticketsList.set(i, tickets);
        }

        /*票据盲签名-第二步：盲化签名*/
        for(int i = 0; i < soldierList.size(); i++) {
            ArrayList<BigInteger> tickets = ticketsList.get(i);
            for(int j = 0; j < n; j++) {
                BigInteger oldTicket = tickets.get(j);      //原有票
                BigInteger newTicket = blindSignature(oldTicket, d, pn);
                tickets.set(j, newTicket);
            }
            ticketsList.set(i, tickets);
        }
        /*票据盲签名-第三步：得到签名*/
        for(int i = 0; i < soldierList.size(); i++) {
            ArrayList<BigInteger> tickets = ticketsList.get(i);
            for(int j = 0; j < n; j++) {
                BigInteger oldTicket = tickets.get(j);      //原有票
                BigInteger newTicket = blindRetriveSig(oldTicket, factorList.get(i), pn);
                tickets.set(j, newTicket);
                System.out.println("签名的票：" + newTicket);
            }
            ticketsList.set(i, tickets);
        }

        /*//解盲测试
        for(int i = 0; i < soldierList.size(); i++) {
            ArrayList<BigInteger> tickets = ticketsList.get(i);
            for(int j = 0; j < n; j++) {
                BigInteger oldTicket = tickets.get(j);      //原有票
                BigInteger newTicket = oldTicket.modPow(e, pn);
                System.out.println("解盲测试：" + getTrueTicket(newTicket));
            }
        }
        */

        /*让投票人自由选票，每人一张票，假设随机选取票数*/
        ArrayList<BigInteger> chooseTicketList = new ArrayList<>();
        for(int i = 0; i < soldierList.size(); i++) {
            int randNumber = (new Random()).nextInt(n);
            System.out.println("randNumber = " + randNumber);
            chooseTicketList.add(ticketsList.get(i).get(randNumber));
            System.out.println("add ticket" + chooseTicketList.get(i));
        }

        /*假定这里是CTF收到了统计的票，CTF对票进行解密并去除盐，并统计票数*/
        int[] ticketNumber = new int[n+1];
        for(int i = 0; i < n + 1; i++)
            ticketNumber[i] = 0;
        for (BigInteger oldTicket : chooseTicketList) {
            /*解密票*/
            BigInteger newTicket = oldTicket.modPow(e, pn);
            ticketNumber[getTrueTicket(newTicket)]++;
        }
        /*输出选票情况*/
        for(int i = 1; i < n + 1; i++)
            System.out.printf("编号%2d拥有%2d张票\n", i, ticketNumber[i]);

        /*选出最大的票*/
        int maxNumber = 0;
        int maxIndex = 0;
        for(int i = 1; i < n + 1; i++) {
            if (ticketNumber[i] > maxNumber) {
                maxNumber = ticketNumber[i];
                maxIndex = i;
            }
        }
        System.out.printf("编号%2d拥有票数最多，为%2d\n", maxIndex, maxNumber);

        return maxNumber;
    }

    /**
     * 输入票号，输出加了盐的票号，取号码前两位连接当前时间纳秒的后四位
     * 如输入1，当前时间纳秒为123456则输出13456
     */
    public static BigInteger reArrangeTicket(int n){
        long time = System.nanoTime() % 10000;
        BigInteger retTicket = new BigInteger(String.valueOf(n)).multiply(new BigInteger("10000")).add(new BigInteger(String.valueOf(time)));
        return retTicket;
    }

    /**
     * 可以看成reArrangeTicket的逆运算，获得真正的票
     * 如输入13456输出1
     */
    public static int getTrueTicket(BigInteger ticket) {
        BigInteger realTicket = ticket.divide(new BigInteger("10000"));
        return realTicket.intValue();
    }


    /**
     * 生成1000-9000的随机数，用于初始化盲因子
     */
    public static BigInteger getRandom(){
        int value = 1000 + (new Random()).nextInt(8000);
//        System.out.println("random value = " + value);
        return new BigInteger(String.valueOf(value));
    }

}
