package thu.infosecurity.simulate.controller;

import com.sun.tools.javac.util.Pair;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.xmlReader;

import java.awt.*;
import java.util.*;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;

/**
 * Created by ChenWeihang on 2017/5/16 0016.
 */
public class SceneControl {

    /**
     * 生成随机数
     * @param startNum
     * @param endNum
     * @return
     */
    public static int generateRandom(int startNum, int endNum){
        Random rndSeed = new Random();
        double rndNumber = rndSeed.nextDouble();
        return (int)(startNum + rndNumber * (endNum - startNum));
    }

    /**
     * 查找士兵是否是己方成员
     * @param publicKeyList
     * @param soldier
     * @return
     */
    public static boolean findPublicKey(ArrayList<String> publicKeyList, Soldier soldier){
        boolean findResult = false;
        String pubKey = soldier.getPuKey();
        for(String key: publicKeyList){
            if(soldier.getPuKey().equals(key)){
                findResult = true;
                break;
            }
        }
        return findResult;
    }
}
