package thu.infosecurity.simulate.model;

import java.math.BigInteger;
import java.util.Set;

/**
 * Created by forest on 2017/5/15.
 */
public class Soldier {

    /*基本信息*/
    private String name;                //姓名
    /*公私钥信息*/
    private BigInteger puKey;           //公钥
    private BigInteger prKey;           //私钥
    /*秘钥共享信息*/
    private String shareKey;            //格式为：1,45 因此需要手动按照,讲两个数据提取出来
    /*访问控制*/
    private String secretLevel;         //采用BLP模型，分文秘密'S'，机密'C'，绝密'T'三个等级
    private Set<String> range;          //海军'S'，陆军'G'，空军'A'


    public static void main(String[] args){
        System.out.println("helloworld");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPuKey() {
        return puKey;
    }

    public void setPuKey(BigInteger puKey) {
        this.puKey = puKey;
    }

    public BigInteger getPrKey() {
        return prKey;
    }

    public void setPrKey(BigInteger prKey) {
        this.prKey = prKey;
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

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    @Override
    public String toString() {
        return "Soldier{" +
                "name='" + name + '\'' +
                ", puKey=" + puKey +
                ", prKey=" + prKey +
                ", shareKey='" + shareKey + '\'' +
                ", secretLevel='" + secretLevel + '\'' +
                ", range=" + range +
                '}';
    }
}
