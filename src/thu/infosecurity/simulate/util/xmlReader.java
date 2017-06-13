package thu.infosecurity.simulate.util;

import com.sun.tools.javac.util.Pair;
import thu.infosecurity.simulate.controller.SceneControl;
import thu.infosecurity.simulate.model.Soldier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.*;

/**
 * Created by forest on 2017/5/16.
 *
 */
public class xmlReader {
    public static String filepos = "xmlFile//userInfo.xml";

    public static void main(String[] args){
//        ArrayList<Soldier> soldierList = getSoldierListFromFile();
//        for(Soldier soldier : soldierList){
//            System.out.println(soldier);
//            RSA.soldierVerify(soldierList.get(1));
//        }
        generateSoldierListbyRandom(10, 0);
    }

    /**
     * 读取接口，读取所有士兵列表
     */
    public static ArrayList<Soldier> getSoldierListFromFile(){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(filepos);

            ArrayList<Soldier> soldierList = new ArrayList<>();

            //获得头结点
            NodeList nList = doc.getElementsByTagName("user");
            for(int i = 0; i< nList.getLength() ; i ++){

                Element node = (Element)nList.item(i);
                String name = node.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
                String puKey = node.getElementsByTagName("publicKey").item(0).getFirstChild().getNodeValue();
                String prKey = node.getElementsByTagName("privateKey").item(0).getFirstChild().getNodeValue();
                String shareKey = node.getElementsByTagName("shareKey").item(0).getFirstChild().getNodeValue();

                Integer A = Integer.parseInt(shareKey.split(",")[0]);
                Integer B = Integer.parseInt(shareKey.split(",")[1]);
                Pair<Integer, Integer> shareKeyPair = new Pair<>(A, B);

                String secretLevel = node.getElementsByTagName("secretLevel").item(0).getFirstChild().getNodeValue();

                Element rangesElem = (Element) node.getElementsByTagName("ranges").item(0);
                Set<String> rangeSet = new HashSet<>();

                if(rangesElem != null){
                    NodeList statelist = rangesElem.getElementsByTagName("range");
                    for(int j = 0; j < statelist.getLength(); j++){
                        String rangeNode = statelist.item(j).getFirstChild().getNodeValue();
                        rangeSet.add(rangeNode);
                    }
                }
                //初始化用户并添加
                Soldier user = new Soldier();
                user.setName(name);
                user.setPuKey(puKey);
                user.setPrKey(prKey);
                user.setShareKey(shareKeyPair);
                user.setSecretLevel(secretLevel);
                user.setRange(rangeSet);
                soldierList.add(user);
            }
            return soldierList;
        }catch (Exception e) {
            System.err.println("ERR :: XML-state decoder error");
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * 随机生成士兵列表
     *
     */
    public static ArrayList<Soldier> generateSoldierListbyRandom(int soldierNumber, int startID){
        ArrayList<Soldier> soldierList = new ArrayList<>();
        for(int i = startID; i < startID + soldierNumber; i++) {
            Soldier soldier = new Soldier();
            //ID
            soldier.setID(i + 1);
            //姓名
            soldier.setName("soldier"+(i+1));
            //坐标
            soldier.setPosition(new Point(SceneControl.generateRandom(0,850), SceneControl.generateRandom(0,620)));
            //RSA公私钥
            Map<String, String> key = RSA.RSA_GenerateKey(20, 10);
            soldier.setPuKey(key.get("n") + "," + key.get("e"));
            soldier.setPrKey(key.get("n") + "," + key.get("d"));

            //shareKey
            soldier.setShareKey(new Pair<>(0,0));

            //密级
            String[] secretLevelList = {"S", "C", "T"};
            soldier.setSecretLevel(secretLevelList[(new Random()).nextInt(3)]);
            //范畴集
//            String[] rangeList = {"S", "G", "A"};
            Set<String> rangeSet = new HashSet<>();
            rangeSet.add("S");//假定都是G
            soldier.setRange(rangeSet);
            //System.out.println(soldier);
            //添加成员
            soldierList.add(soldier);
        }
        //共享秘钥在生成所有士兵之后统一生成


        return soldierList;
    }
}
