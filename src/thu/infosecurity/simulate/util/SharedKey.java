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
    public static void main(String[] args){
        System.out.println("Testing for getting sharedKey from soldierList");
    }


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


        return 0;
    }


    /**
     * 为每个士兵分配他的秘钥，生成士兵列表的时候需要使用，其中sharedKey和sharedNumber来自targetObject
     * Made by Sunlin, 2017-05-10
     * Finished by WangNan, xxxx-xx-xx
     */
    public static void setSharedKeyforSoldierList(ArrayList<Soldier> soldierList, Target weaponBox){
        // TODO: 2017/5/19 需要王楠实现



        return ;
    }



    /**
     * Made by Sunlin, 2017-05-08
     * @param pairList 二元组列表
     * @param shareNumber 至少需要的元组数目
     * */
    private static Integer secretSharing(ArrayList<Pair<Integer, Integer>> pairList, int shareNumber){
        // TODO: 2017/5/18 需要王楠实现

        return null;
    }



}
