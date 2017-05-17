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

