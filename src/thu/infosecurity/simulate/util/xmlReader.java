package thu.infosecurity.simulate.util;

import thu.infosecurity.simulate.model.Soldier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by forest on 2017/5/16.
 */
public class xmlReader {
    static String filepos = "xmlFile//userInfo.xml";

    public static void main(String[] args){
        ArrayList<Soldier> soldierList = getSoldierListFromFile();
        for(Soldier soldier : soldierList){
            System.out.println(soldier);
        }
    }

    /**
     * 读取接口，读取所有士兵列表
     * @return 士兵列表
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
                String secretLevel = node.getElementsByTagName("secretLeven").item(0).getFirstChild().getNodeValue();

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
                user.setPuKey(new BigInteger(puKey));
                user.setPrKey(new BigInteger(prKey));
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
}