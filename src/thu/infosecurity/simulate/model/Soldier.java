package thu.infosecurity.simulate.model;

//import javafx.util.Pair;
import com.sun.tools.javac.util.Pair;

import java.awt.*;
import java.util.Set;

/**
 *
 * Created by forest on 2017/5/15.
 * Revised by forest on 2017/6/12
 */
public class Soldier {

    /*基本信息*/
    private int ID;                     //ID
    private String name;                //姓名
    private Point position;             //坐标

    /*用于区分敌人的对称秘钥*/
    private String DESKey;              //用于区分敌我士兵的对称秘钥

    /*公私钥信息*/
    private String puKey;               //公钥
    private String prKey;               //私钥
    /*秘钥共享信息*/
    //private String shareKey;          //格式为：1,45 因此需要手动按照,讲两个数据提取出来
    private Pair<Integer, Integer> shareKey;
    /*访问控制*/
    private String secretLevel;         //采用BLP模型，分文秘密'S'，机密'C'，绝密'T'三个等级
    private Set<String> range;          //海军'S'，陆军'G'，空军'A'

    public static void main(String[] args){
        System.out.println("helloworld");
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getPuKey() {
        return puKey;
    }

    public void setPuKey(String puKey) {
        this.puKey = puKey;
    }

    public String getPrKey() {
        return prKey;
    }

    public void setPrKey(String prKey) {
        this.prKey = prKey;
    }

    public Pair<Integer, Integer> getShareKey() {
        return shareKey;
    }

    public void setShareKey(Pair<Integer, Integer> shareKey) {
        this.shareKey = shareKey;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public Set<String> getRange() {
        return range;
    }

    public void setRange(Set<String> range) {
        this.range = range;
    }

    /*201706.12添加：获得军衔*/
    public String getTitle(){
        if (secretLevel.equals("S")) {
            return "三级士兵";
        }
        if (secretLevel.equals("C")) {
            return "二级士兵";
        }
        if (secretLevel.equals("T")) {
            return "一级士兵";
        }
        return "列兵";
    }

    public void setDESKey(String DESKey) {
        this.DESKey = DESKey;
    }

    public String getDESKey() {
        return DESKey;
    }

    @Override
    public String toString() {
        return "Soldier{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", DESKey='" + DESKey + '\'' +
                ", puKey='" + puKey + '\'' +
                ", prKey='" + prKey + '\'' +
                ", shareKey=" + shareKey +
                ", secretLevel='" + secretLevel + '\'' +
                ", range=" + range +
                '}';
    }
}
