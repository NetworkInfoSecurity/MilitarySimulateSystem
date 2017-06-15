package thu.infosecurity.simulate.controller;

import com.sun.tools.javac.util.Pair;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.MyDES;
import thu.infosecurity.simulate.util.SharedKey;
import thu.infosecurity.simulate.util.xmlReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static thu.infosecurity.simulate.util.RSA.RSA_GenerateKey;
import static thu.infosecurity.simulate.util.RSA.soldierVerify;

/**
 * 初始化士兵和目标信息，初始化过程
 *      1. 初始化箱子（共享秘钥）和信件信息
 *      2. 初始化信件
 *      3. 初始化士兵(初始化共享秘钥)
 *      4. 初始化公钥列表，辨识己方成员
 *      5. 初始化间谍列表
 *
 * Created by forest on 2017/5/19.
 */
public class SceneInitial {

    //场景元素
    private Target weaponBox;
    private Target letter;
    private ArrayList<Soldier> soldierList;
    private ArrayList<String> publicRsaKeyList;
    private ArrayList<Soldier> spyList;

    public static void main(String[] args){
        System.out.println("这里是测试初始化士兵和目标信息");

        SceneInitial si = new SceneInitial();
        si.initial(0, 7, 3, 1);
//        System.out.println(si.weaponBox);
//        System.out.println(si.letter);
//        for (Soldier soldier : si.soldierList) {
//            System.out.println(soldier);
//        }

        /*测试共享秘钥是否可行*/

        ArrayList<Soldier> team = new ArrayList<>();
        for(int i = 0; i < si.soldierList.size(); i++){
            System.out.println("teamNum: "+(i+1));
            team.add(si.soldierList.get(i));
            Integer key = SharedKey.retrieveSharedKey(team, si.weaponBox.getShareNumber());
            System.out.println("sharedKey = " + key + ", " + "trueKey = " + si.getWeaponBox().getShareKey()
                    +", " + si.getWeaponBox().canOpen(team));
            ;
        }
    }

    public void initial(int method, int soldierNum, int boxShareNum, int spyNum){
        //初始化装备箱
        weaponBox =initialWeaponBox(boxShareNum);
        //初始化机密信函
        letter = initialLetter();
        //初始化伞兵列表
        soldierList = initialSoldierList(method, soldierNum, weaponBox);

        //2017.06.13 forest 添加敌我秘钥
        initialDESKey();

        //初始化己方公钥列表
        publicRsaKeyList = initialPublicRsaKeyList();
        //初始化间谍列表
        spyList = initialSpyList(spyNum, soldierNum);

        //将所有士兵的信息打印出来帮助调试
        System.out.println("=====================Soliders");
        for (Soldier soldier : soldierList)
            System.out.println(soldier);
        System.out.println("=====================Spies");
        for (Soldier spy : spyList)
            System.out.println(spy);

        System.out.println("=====================WeaponBox");
        System.out.println(weaponBox);
        System.out.println("=====================Letter");
        System.out.println(letter);


        /*测试信封能否打开*/
        System.out.println("我方士兵打开信封测试：" + letter.canOpen(soldierList.get(0)));
        System.out.println("敌方士兵打开信封测试：" + letter.canOpen(spyList.get(0)));


        return ;
    }

    private void initialDESKey(){
        String key = MyDES.generateDESkey();
//        System.out.println("key = " + key);
        for (Soldier soldier : soldierList) {
            soldier.setDESKey(key);
        }
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
            soldierList = xmlReader.generateSoldierListbyRandom(soldierNum, 0);
            /*初始化共享秘钥*/
            SharedKey.setSharedKeyforSoldierList(soldierList, weaponBox);
        } else {
            /*文件读取*/
            soldierList =  xmlReader.getSoldierListFromFile();
            SharedKey.setSharedKeyforSoldierList(soldierList, weaponBox);
        }
        return soldierList;
    }

    /**
     * 初始化武器箱信息
     * @return
     */
    private Target initialWeaponBox(int boxShareNum){
        Target weaponBox = new Target();
        /*设置基本信息*/
        weaponBox.setObjName("weapon_box");
        weaponBox.setPosition(new Point(0, 0));
//        weaponBox.setPosition(new Point(Utils.generateRandom(10,880), Utils.generateRandom(10,650)));
        /*箱子不需要BLP模型，只需要秘钥共享即可*/
        weaponBox.setShareFlag(true);
        weaponBox.setShareNumber(boxShareNum);
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
        letter.setPosition(new Point(SceneControl.generateRandom(0, 780), SceneControl.generateRandom(0, 620)));
        /*设置访问控制信息*/
        letter.setSecretLevel("S");
        letter.setRange("S");
        /*设置共享秘钥信息*/
        letter.setShareFlag(false);
        return letter;
    }

    /**
     * 用初始化的伞兵列表初始化公钥列表
     */
    private ArrayList<String> initialPublicRsaKeyList(){
        ArrayList<String> publicKeyList = new ArrayList<>();
        //用所有士兵列表填充
        for(Soldier soldier: soldierList){
            publicKeyList.add(soldier.getPuKey());
        }
        return publicKeyList;
    }

    /**
     * 初始化间谍列表，间谍不初始化共享秘钥
     * @param spyNum
     * @return
     */
    private ArrayList<Soldier> initialSpyList(int spyNum, int startID){
        ArrayList<Soldier> spyList = new ArrayList<>();
        /*初始化一般信息*/
        spyList = xmlReader.generateSoldierListbyRandom(spyNum, startID);
        for (Soldier spy : spyList) {
            //设置spy无法打开信封
//            Set<String> rangeSet = new HashSet<>();
//            rangeSet.add("")
            spy.setRange(null);
        }
        return spyList;
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

    public ArrayList<String> getPublicRsaKeyList() {
        return publicRsaKeyList;
    }

    public void setPublicRsaKeyList(ArrayList<String> publicRsaKeyList) {
        this.publicRsaKeyList = publicRsaKeyList;
    }

    public ArrayList<Soldier> getSpyList() {
        return spyList;
    }

    public void setSpyList(ArrayList<Soldier> spyList) {
        this.spyList = spyList;
    }
}
