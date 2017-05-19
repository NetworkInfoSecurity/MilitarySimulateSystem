package thu.infosecurity.simulate.controller;

import com.sun.tools.javac.util.Pair;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.Utils;
import thu.infosecurity.simulate.util.xmlReader;

import java.awt.*;
import java.math.BigInteger;
import java.util.*;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;
import static thu.infosecurity.simulate.util.xmlReader.getSoldierListFromFile;

/**
 * Created by ChenWeihang on 2017/5/16 0016.
 */
public class SceneCreator {

    public SceneCreator() { }

    /**
     * 初始化模拟场景需要的伞兵信息
     * 比如几个伞兵、伞兵的初始坐标等
     * @param method: 0: 程序生成，1：XML文件读取
     */
    public void SoldierListInit(int method, int soldierNum){

        /* 生成伞兵列表 */
        ArrayList<Soldier> soldierList = new ArrayList<>();

        if(method == 1){  //读取伞兵信息列表，soldierNum无用
             /* 读取伞兵列表 */
            xmlReader usersReader = new xmlReader();
            soldierList = usersReader.getSoldierListFromFile();
            //生成伞兵的ID和坐标
            for(int i = 0; i < soldierList.size(); i++){
                soldierList.get(i).setID(i+1);
                soldierList.get(i).setPosition(new Point(Utils.generateRandom(0,1000), Utils.generateRandom(0,800)));
            }
        } else {
            for(int i = 0; i < soldierNum; i++){
                Soldier soldier = new Soldier();
                //ID，姓名，坐标
                soldier.setID(i+1);
                soldier.setName("Soloier" + (i+1));
                soldier.setPosition(new Point(Utils.generateRandom(0,1000), Utils.generateRandom(0,800)));
                //公私钥信息
                Map<String, String> key = RSA_GenerateKey(20, 10);
                String e = key.get("e");
                String d = key.get("d");
                String n = key.get("n");
                soldier.setPuKey(n + "," + e);
                soldier.setPrKey(n + "," + d);
                //秘钥共享信息
                soldier.setShareKey(new Pair<Integer, Integer>(i+1, Utils.generateRandom(0,1000)));
                //访问控制
                soldier.setSecretLevel("S");  //暂时，测试
                Set<String> set = new HashSet<>();
                set.add("G");
                soldier.setRange(set); //暂时，测试
                //添加成员
                soldierList.add(soldier);
            }
        }

        //测试
        /*System.out.println("Soldiers:");
        for(Soldier soldier: soldierList){
            System.out.println(soldier);
        }*/
    }

    /**
     * 初始化模拟场景需要的装备箱信息
     * 比如几个装备箱、装备箱的初始坐标等
     */
    public void TackleBoxListInit(int targetNum){

        /* 生成装备箱列表 */
        ArrayList<Target> targetList = new ArrayList<>();
        for(int i = 0; i < targetNum; i++){
            Target tgt = new Target("TackleBox1",new Point(Utils.generateRandom(0,1000), Utils.generateRandom(0,800)),"S","G",true,3, new Integer("1234"));
            targetList.add(tgt);
        }

        //测试
        /*System.out.println("TackleBoxes:");
        for(Target target: targetList){
            System.out.println(target);
        }*/
    }

    /**
     * 降落过程模拟
     * 降落指令发出，伞兵装备箱依次落地
     */
    public void landing(){

    }

    /**
     * 开始模拟整个军事行动过程
     * - 伞兵开始移动
     * - 伞兵遇到其他士兵进行认证
     * - 认证成功选出leader
     * - 小队跟随leader前进
     * - 发现装备箱尝试去开启
     * - 若人数不够，记录装备箱位置继续前进
     * - 增加到足够人数回到装备箱所在处再次开启
     */
    public void SimulateAction(){

    }


    public static void main(String[] args) {

        SceneCreator scene = new SceneCreator();
        //SceneInit test
        scene.SoldierListInit(0,5);
        scene.TackleBoxListInit(1);
    }
}
