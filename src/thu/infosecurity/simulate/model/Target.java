package thu.infosecurity.simulate.model;

import java.math.BigInteger;
import java.util.ArrayList;
/**
 * Created by forest on 2017/5/16.
 */
public class Target {
    public String objName;                  //客体名称

    private String secretLevel;             //访问控制::客体密级
    private String range;                   //访问控制::客体范畴

    private boolean shareFlag;              //秘钥共享::是否需要多人才能打开
    private int shareNumber;                //秘钥共享::需要共享的人数
    private BigInteger shareKey;            //秘钥共享::秘钥

    public Target(String objName, String secretLevel, String range, boolean shareFlag, int shareNumber, BigInteger shareKey) {
        this.objName = objName;
        this.secretLevel = secretLevel;
        this.range = range;
        this.shareFlag = shareFlag;
        this.shareNumber = shareNumber;
        this.shareKey = shareKey;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public boolean isShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(boolean shareFlag) {
        this.shareFlag = shareFlag;
    }

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public BigInteger getShareKey() {
        return shareKey;
    }

    public void setShareKey(BigInteger shareKey) {
        this.shareKey = shareKey;
    }

    @Override
    public String toString() {
        return "Target{" +
                "objName='" + objName + '\'' +
                "secretLevel='" + secretLevel + '\'' +
                "range='" + range + '\'' +
                "shareFlag='" + shareFlag + '\'' +
                "shareNumber='" + shareNumber + '\''+
                "shareFlag='" + shareFlag + '\'' +
                "}";
    }

    //需要秘钥共享才能打开内容，比如最终的武器箱
    public boolean canOpen(ArrayList<Soldier> userList){
        // TODO: 2017/5/15 需要王楠实现接口
//        BigInteger generateKey = generateShareKey(shareNumber, userList);
//        if(0 == shareKey.compareTo(generateKey))
//            return true;
        return false;
    }


    //利用访问控制看能否查看内容，比如地上一个信件，士兵捡到信件看可否打开
    public boolean canOpen(Soldier user){
        if( shareFlag && toLevel(this.secretLevel) < toLevel(user.getSecretLevel()) && user.getRange().contains(range))
            return true;
        return false;
    }

    /*辅助函数，用户访问控制*/
    public static int toLevel(String level){
        if(level.equals("s") || level.equals("S"))
            return 0;
        if(level.equals("c") || level.equals("C"))
            return 1;
        if(level.equals("t") || level.equals("T"))
            return 2;

        System.err.println("密级判断错误，请检查用户和客体密级是否越界，只能存在'S','C','T'三种密级");
        return 0;
    }
}

