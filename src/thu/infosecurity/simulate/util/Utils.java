package thu.infosecurity.simulate.util;

import java.util.Random;

/**
 * Created by ChenWeihang on 2017/5/17 0017.
 * - 生成随机数
 */
public class Utils {

    public static int generateRandom(int startNum, int endNum){
        Random rndSeed = new Random();
        double rndNumber = rndSeed.nextDouble();
        return (int)(startNum + rndNumber * (endNum - startNum));
    }
}
