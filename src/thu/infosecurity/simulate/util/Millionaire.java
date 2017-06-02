package thu.infosecurity.simulate.util;

import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class Millionaire {

    //测试，当A与B相等时会返回B
    public static void main(String arg[]){
        ArrayList<Soldier> soldiers = new ArrayList<Soldier>();

        Map<String,String> map1 = new HashMap<String,String>();
        Map<String,String> map2 = new HashMap<String,String>();
        Map<String,String> map3 = new HashMap<String,String>();
        map1 = RSA.RSA_GenerateKey(20,8);
        Soldier s1 = new Soldier();
        s1.setID(1);
        String str1 = map1.get("e") + "," + map1.get("n");
        String str2 = map1.get("d") + "," + map1.get("n");
        s1.setPuKey(str1);
        s1.setPrKey(str2);
        s1.setSecretLevel("T");
        soldiers.add(s1);

        map2 = RSA.RSA_GenerateKey(20,8);
        Soldier s2 = new Soldier();
        s2.setID(2);
        String str3 = map2.get("e") + "," + map2.get("n");
        String str4 = map2.get("d") + "," + map2.get("n");

        s2.setPuKey(str3);
        s2.setPrKey(str4);
        s2.setSecretLevel("S");
        soldiers.add(s2);

        map3 = RSA.RSA_GenerateKey(20,8);
        Soldier s3 = new Soldier();
        s3.setID(3);
        String str5 = map3.get("e") + "," + map3.get("n");
        String str6 = map3.get("d") + "," + map3.get("n");

        s3.setPuKey(str5);
        s3.setPrKey(str6);
        s3.setSecretLevel("S");
        soldiers.add(s3);

        int res = getTopLeader_Million(soldiers);
        System.out.println(res);
    }

    public static int getTopLeader_Million( ArrayList<Soldier> soldierList){
        Soldier top_Soldier = soldierList.get(0);
        for(int i=1;i<soldierList.size();i++){
            Soldier temp_Soldier = soldierList.get(i);
            if(agreeLeader(top_Soldier,temp_Soldier)){
                top_Soldier = temp_Soldier;
            }
        }
        return top_Soldier.getID();
    }

    /**
     * 比较士兵A和士兵B级别大小，若A>B返回true，否则返回false
     * */
    public static Boolean agreeLeader(Soldier A,Soldier B){
        String B_puKey = B.getPuKey();
        ArrayList<String> list_1 = new ArrayList<String>();
        ArrayList<String> list_2 = new ArrayList<String>();
        list_1 = startProcess(A,B_puKey);
        String random_num1 = list_1.get(0);
        list_2 = middleProcess(B,list_1.get(1));
        return lastProcess(list_2,A,B,random_num1);
    }

    /**
     *生成一个大随机数，然后用士兵B的公开密钥加密，再减去士兵A的等级值
     * */
    public static ArrayList<String> startProcess(Soldier A,String puKey){
        ArrayList<String> list = new ArrayList<String>();
        BigInteger lowerBound = new BigInteger("898960356");
        Random random = new Random();
        int random_num = random.nextInt(50);
        BigInteger bigNum = new BigInteger(random_num+"");
        bigNum = bigNum.add(lowerBound);//随机的大质数

        String[] str = puKey.split(",");
        String part1_puKey = str[0];
        String part2_puKey = str[1];
        System.out.println("part2_puKey:" + part2_puKey);
        BigInteger result = encrypt(bigNum, new BigInteger(part1_puKey), new BigInteger(part2_puKey));
        int level_num = Target.toLevel(A.getSecretLevel())+1;//等级从1开始，1、2、3
        BigInteger level = new BigInteger(level_num+"");
        result = result.subtract(level);//减去A的秘密值

        list.add(bigNum.toString());
        list.add(result.toString());
        return list;

    }

    /**
     *使用B的私钥对num+i进行解密（i的范围为1-20），然后选取一个略小于startProcess函数中的随机值，进行模运算，然后再把数组返回
     * */
    public static ArrayList<String> middleProcess(Soldier B,String num){
        BigInteger upperBound = new BigInteger("898960356");
        Random random = new Random();
        int random_num = random.nextInt(50);
        BigInteger bigNum = new BigInteger(random_num+"");
        bigNum = upperBound.subtract(bigNum);
        ArrayList<String> list = new ArrayList<String>();
        BigInteger start = new BigInteger(num);
        for(int i=1;i<=20;i++){

            BigInteger temp1 = new BigInteger(i+"");
            temp1 = start.add(temp1);
            String[] str = B.getPrKey().split(",");
            String part1_prKey = str[0];
            String part2_prKey = str[1];
            BigInteger temp2 = decrypt(temp1, new BigInteger(part1_prKey), new BigInteger(part2_prKey));
            temp2 = temp2.mod(bigNum);
            System.out.println("temp2:"+temp2);

            if( i > 1 + Target.toLevel(B.getSecretLevel()) ){  //把等级值加1
                temp2 = temp2.add(new BigInteger("1"));
                list.add(temp2.toString());
            }
            else
            {
                list.add(temp2.toString());
            }
        }
        list.add(bigNum.toString());
        return  list;
    }

    /**
     *A士兵检测数组里的第level个值是否同第一个随机值模第二个随机值，不等的话A的等级大于B，
     * */
    public static boolean lastProcess(ArrayList<String> list,Soldier A,Soldier B,String random1){
        BigInteger random_A = new BigInteger(random1);
        BigInteger random_B = new BigInteger(list.get(list.size()-1));
        int result1 = Integer.parseInt(random_A.mod(random_B).toString());
        System.out.println("result1=" + result1);
        int result2 = Integer.parseInt(list.get( Target.toLevel(A.getSecretLevel())));
        //int result2 = Integer.parseInt(list.get( 1 ));
        System.out.println("result2=" + result2);
        if(result1 != result2){
            return false;
        }else{
            return true;
        }

    }
    //加密
    public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n){
        //System.out.println(m.modPow(e,n));
        //System.out.println("n: " + n.toString());
        return m.modPow(e, n);
    }
    //解密
    public static BigInteger decrypt(BigInteger m, BigInteger d, BigInteger n){
        return m.modPow(d, n);
    }

}

