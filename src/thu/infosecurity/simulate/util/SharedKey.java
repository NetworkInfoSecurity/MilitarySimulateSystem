package thu.infosecurity.simulate.util;
import com.sun.tools.javac.util.Pair;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;

import java.util.ArrayList;

/**
 *
 * Created by DaFei-PC on 2017-05-16.
 */
public class SharedKey {
    //static int MIN_NESSARY=10;
    public static void main(String[] args){
        System.out.println("Testing for getting sharedKey from soldierList");



    }
    private static Integer Password = 0;
    static int FACTOR_SIZE = 100;
    static String ERRORINFO="对不起，开启人数少于规定人数";
    //private static double factor[]=new double[MIN_NESSARY];

    /**
     * 外部调用接口
     * Made by Sunlin, 2017-05-18
     * */
    public static Integer retrieveSharedKey(ArrayList<Soldier> soldierList, int shareNumber){
        /*从士兵中获得秘钥数据*/
        ArrayList<Pair<Integer, Integer>> pairList = new ArrayList<>();
        for (Soldier soldier : soldierList) {
            pairList.add(soldier.getShareKey());
        }
        /*利用秘钥数据获得共享秘钥*/
        return secretSharing(pairList, shareNumber);
    }


    /**
     * 外部调用接口，生成士兵列表的时候用来生成共享秘钥
     * Made by Sunlin, 2017-05-19
     * Finished by WangNan, xxxx-xx-xx
     */
    public static int generateKey(){
        // TODO: 2017/5/19 需要王楠实现
        Password=((int)(1+Math.random()*FACTOR_SIZE));
        System.out.println("密钥:"+Password);
        return Password;
    }


    /**
     * 为每个士兵分配他的秘钥，生成士兵列表的时候需要使用，其中sharedKey和sharedNumber来自targetObject
     * Made by Sunlin, 2017-05-10
     * Finished by WangNan, xxxx-xx-xx
     */
    public static void setSharedKeyforSoldierList(ArrayList<Soldier> soldierList, Target weaponBox) {
        // TODO: 2017/5/19 需要王楠实现
        Integer pass = weaponBox.getShareKey();
        int size = soldierList.size();
        int shareNumber = weaponBox.getShareNumber();
        //System.out.println("sharenumber1:"+weaponBox.getShareNumber());
        Integer factor1[] = new Integer[shareNumber];
        factor1 = SharedKey.setRandomFactor(pass, shareNumber);
        //Integer messageContent = 0;
        for (int j = 0; j <size; j++) {
            Soldier soldier = soldierList.get(j);
            Integer ID = soldier.getID();
            Integer messageContent = 0;
          //  System.out.println("sharenumber1ID:"+soldier.getID());
            for (int i = 0; i < shareNumber; i++) {
                //System.out.println("sharenumber1factor:"+factor1[i]);
                Integer temp = factor1[i] * (int)Math.pow(ID, i);
               // System.out.println("sharenumber1factors:"+temp);
                messageContent = messageContent + temp;
               // System.out.println("sharenumber1factors1:"+messageContent);
            }
            //soldier.setShareKey(Pair<ID, messageContent>);
            Pair soldierShareKey = new Pair(ID, messageContent);
            soldier.setShareKey(soldierShareKey);
           // System.out.println("ID:"+soldier.getShareKey().fst);
           // System.out.println("SHAREKEY:"+soldier.getShareKey().snd);
        }


            return ;
    }



    /**
     * Made by Sunlin, 2017-05-08
     * @param pairList 二元组列表
     * @param shareNumber 至少需要的元组数目
     * */
    private static Integer secretSharing(ArrayList<Pair<Integer, Integer>> pairList, int shareNumber){
        // TODO: 2017/5/18 需要王楠实现
        int size=pairList.size();
        if(size<shareNumber)
            return 0;
        size=shareNumber;
        //System.out.println("sharenumber:"+size);
        Integer X[]=new Integer[size];
        Integer Y[]=new Integer[size];
        //System.out.println("SHAREKEYTNUM:"+shareNumber);
       // Pair pairsingle = new Pair(ID, messageContent);
        for(int i=0;i<size;i++){
            X[i]=pairList.get(i).fst;
            Y[i]=pairList.get(i).snd;
          //  System.out.println("IDX:"+X[i]);
           // System.out.println("SHAREKEYT:"+Y[i]);
        }
        double result1=0;
        for(int i=0;i<size;i++){
            double temp=1;
            for(int j=0;j<size;j++)
                if(i!=j) {
                    //System.out.println("IDX1:"+X[i]);
                   // System.out.println("IDX2:"+X[j]);
                    temp = temp * (0 - X[j]) / (X[i] - X[j]);
                    //System.out.println("result0:"+temp);
                }
            //System.out.println("result1:"+temp);
            temp=temp*Y[i];
            //System.out.println("result2:"+temp);
            result1+=temp;
            //System.out.println("result3:"+result);
        }
//        System.out.println("result:"+result);
        Integer result = (int)result1;
        return result;
        //return null;
    }

    private static Integer[] setRandomFactor(Integer Password,int size){
        Integer factor[]=new Integer[size];
        factor[0]=Password;
        for(int i=1;i<size;i++){
            factor[i]=((int)(1+Math.random()*FACTOR_SIZE));
        }
        return factor;
    }

}
