package thu.infosecurity.simulate.model;

//import javafx.util.Pair;
import com.sun.tools.javac.util.Pair;

import java.util.Set;

/**
 * Created by forest on 2017/5/15.
 */
public class Soldier {

    /*基本信息*/
    private int ID;                     //ID
    private String name;                //姓名
    /*公私钥信息*/
    private String puKey;               //公钥
    private String prKey;           //私钥
    /*秘钥共享信息*/
    //private String shareKey;            //格式为：1,45 因此需要手动按照,讲两个数据提取出来
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

    @Override
    public String toString() {
        return "Soldier{" +
                "name='" + name + '\'' +
                ", puKey=" + puKey +
                ", prKey=" + prKey +
                ", shareKey='" + shareKey.toString() + '\'' +
                ", secretLevel='" + secretLevel + '\'' +
                ", range=" + range +
                '}';
    }
}
