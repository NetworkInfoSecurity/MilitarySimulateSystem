package thu.infosecurity.simulate.controller;

import com.sun.tools.javac.util.Pair;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.SharedKey;
import thu.infosecurity.simulate.util.Utils;
import thu.infosecurity.simulate.util.xmlReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;

/**
 * 初始化士兵和目标信息，初始化过程
 *      1. 初始化箱子（共享秘钥）和信件信息
 *      2. 初始化信件
 *      3. 初始化士兵(初始化共享秘钥)
 *
 * Created by forest on 2017/5/19.
 */
public class SceneInitial {
    private Target weaponBox;
    private Target letter;
    private ArrayList<Soldier> soldierList;

    public static void main(String[] args){
        System.out.println("这里是测试初始化士兵和目标信息");

        SceneInitial si = new SceneInitial();
        si.initial(0, 5);
        System.out.println(si.weaponBox);
        System.out.println(si.letter);

    }

    public void initial(int method, int soldierNum){
        weaponBox =initialWeaponBox();
        letter = initialLetter();
        soldierList = initialSoldierList(method, soldierNum, weaponBox);

        return ;
    }

    /**
     * 初始化模拟场景需要的伞兵信息
     * 比如几个伞兵、伞兵的初始坐标等
     * @param method: 0: 程序生成，1：XML文件读取
     */
    private ArrayList<Soldier> initialSoldierList(int method, int soldierNum, Target weaponBox){
        ArrayList<Soldier> soldierList = new ArrayList<>();
        /*随机生成*/
        if (method == 0) {
            /*初始化一般信息*/
            soldierList = xmlReader.generateSoldierListbyRandom(soldierNum);
            /*初始化共享秘钥*/
            SharedKey.setSharedKeyforSoldierList(soldierList, weaponBox);
        } else {
            /*文件读取*/
            soldierList =  xmlReader.getSoldierListFromFile();
        }
        return soldierList;
    }

    /**
     * 初始化武器箱信息
     * @return
     */
    private Target initialWeaponBox(){
        Target weaponBox = new Target();
        /*设置基本信息*/
        weaponBox.setObjName("weapon_box");
        weaponBox.setPosition(new Point(0, 0));
//        weaponBox.setPosition(new Point(Utils.generateRandom(10,880), Utils.generateRandom(10,650)));
        /*箱子不需要BLP模型，只需要秘钥共享即可*/
        weaponBox.setShareFlag(true);
        weaponBox.setShareNumber(4);
        weaponBox.setShareKey(SharedKey.generateKey());

        return weaponBox;
    }


    /**
     * 初始化信件信息
     */
    private Target initialLetter(){
        Target letter = new Target();
        /*设置基本信息*/
        letter.setObjName("letter");
        letter.setPosition(new Point(Utils.generateRandom(0,1000), Utils.generateRandom(0,800)));
        /*设置访问控制信息*/
        letter.setSecretLevel("S");
        letter.setRange("G");
        /*设置共享秘钥信息*/
        letter.setShareFlag(false);
        return letter;
    }

    public Target getWeaponBox() {
        return weaponBox;
    }

    public void setWeaponBox(Target weaponBox) {
        this.weaponBox = weaponBox;
    }

    public Target getLetter() {
        return letter;
    }

    public void setLetter(Target letter) {
        this.letter = letter;
    }

    public ArrayList<Soldier> getSoldierList() {
        return soldierList;
    }

    public void setSoldierList(ArrayList<Soldier> soldierList) {
        this.soldierList = soldierList;
    }
}
