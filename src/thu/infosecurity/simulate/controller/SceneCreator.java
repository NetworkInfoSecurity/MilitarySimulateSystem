package thu.infosecurity.simulate.controller;

import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.Utils;
import thu.infosecurity.simulate.util.xmlReader;

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;

import static thu.infosecurity.simulate.util.xmlReader.getSoldierListFromFile;

/**
 * Created by ChenWeihang on 2017/5/16 0016.
 */
public class SceneCreator {

    public Utils utils;

    public SceneCreator() {
        this.utils = new Utils();
    }

    /**
     * 初始化模拟场景需要的各个元素
     * 比如几个伞兵、几个装备箱、伞兵装备箱的初始坐标等
     */
    public void SceneInit(int soldierNum, int targetNum){
        /* 读取伞兵列表 */
        xmlReader usersReader = new xmlReader();
        ArrayList<Soldier> soldierList = usersReader.getSoldierListFromFile();
        //生成伞兵的ID和坐标
        for(int i = 0; i < soldierList.size(); i++){
            soldierList.get(i).setID(i+1);
            soldierList.get(i).setPosition(new Point(utils.generateRandom(0,1000),utils.generateRandom(0,800)));
        }

        /* 生成装备箱列表 */
        ArrayList<Target> targetList = new ArrayList<>();
        for(int i = 0; i < targetNum; i++){
            Target tgt = new Target("TackleBox1","S","G",true,3, new BigInteger("1234"));
            targetList.add(tgt);
        }

        //测试
       /* System.out.println("Soldiers:");
        for(Soldier soldier: soldierList){
            System.out.println(soldier);
        }
        System.out.println("TackleBoxes:");
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
        scene.SceneInit(5,1);
    }
}
