package thu.infosecurity.simulate.util;

import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import thu.infosecurity.simulate.model.Target.*;

/**
 * Created by DaFei-PC on 2017-05-16.
 * Revised by Sun Lin on 2017-05-17
 */
public class ElectronicVote {
    public static void main(String[] args){
        System.out.println("This is electronic Vote System Testing");
        /*产生随机数测试*/
        /*
        Random rndSeed = new Random();
        int rndNumber = rndSeed.nextInt(Integer.MAX_VALUE);
        for(int i = 0; i < 10; i++)
            System.out.println("rnd number = " + rndSeed.nextInt(Integer.MAX_VALUE));
        */

        /*底层函数测试案例*/
        /*
        ArrayList<Integer> valList = new ArrayList<>();
        valList.add(new Integer(30));
        valList.add(new Integer(25));
        valList.add(new Integer(8));
        valList.add(new Integer(2));
        for(int i = 0; i < 20; i++)
            System.out.println("投票结果：" + voteByDifferentialPrivacy(valList, 1));
            */

        /*士兵测试案例*/
        ArrayList<Soldier> soldierList = xmlReader.getSoldierListFromFile();
        for(int i = 0; i < 10; i++) {
            int index = vote(soldierList, (float)1);
            System.out.println("vote result :" + index);
        }
    }


    /** 差分隐私机制实现匿名投票
     * 条件：三人以上
     * 限制：需要给定准确度参数
     * 缺点：参数提供准确率，不是100%准确，如果知道了参数，那么n-1人合谋可以知道剩下那个人的等级
     * 参考论文：The Algorithmic foundations of Differential Privacy
     * 作者：孙林
     * @param valList 每个人的投票
     * @param epsilon 准确度参数
     * */
    private static int voteByDifferentialPrivacy(ArrayList<Integer> valList, float epsilon){
        ArrayList<BigDecimal> ticketList = new ArrayList<>();

        /*DP计算用到sum*/
        BigDecimal sum = new BigDecimal(0);

        for (Integer intVal : valList){
            BigDecimal temp = new BigDecimal(Math.exp(epsilon / 2 * intVal));
//            System.out.println(temp);
            sum = sum.add(temp);
            ticketList.add(temp);
        }

        /*接下来按照DP对数组处理，并得到最大投票*/
        for(int i = 0; i < ticketList.size(); i++) {
            BigDecimal nowTicket = ticketList.get(i).divide(sum, 10, BigDecimal.ROUND_HALF_DOWN);
//            System.out.println("nowTicket" + nowTicket);
            ticketList.set(i, nowTicket);
        }

        /*按照正比概率选择输出*/
        Random rndSeed = new Random();
        int rndNumber = rndSeed.nextInt(Integer.MAX_VALUE);
        BigDecimal rndDecimal = new BigDecimal(rndNumber);

//        System.out.println("rndDecimal = " + rndDecimal);
        for(int i = 0; i < ticketList.size(); i++) {
//            System.out.println("index : " + i + ":   " + ticketList.get(i).multiply(new BigDecimal(Integer.MAX_VALUE)));
            if(ticketList.get(i).multiply(new BigDecimal(Integer.MAX_VALUE)).compareTo(rndDecimal) > 0)
                return i;
            rndDecimal = rndDecimal.subtract(ticketList.get(i).multiply(new BigDecimal(Integer.MAX_VALUE)));
//            System.out.println("update :     " + rndDecimal);
        }
        /*跑到这里应该是上面出问题了，打印出现场*/
        System.err.println("DP 匿名投票出现问题，这是问题现场");
        System.err.println("rndNumber = " + rndNumber);
        for(BigDecimal nowTicket : ticketList){
            System.err.println("ticket = " + nowTicket);
        }
        return -1;
    }


    /** DP匿名投票算法外部接口 */
    public static int vote(ArrayList<Soldier> soldierList, float epsilon) {
        ArrayList<Integer> scoreList = new ArrayList<>();
        for (Soldier soldier : soldierList) {
            int score = Target.toLevel(soldier.getSecretLevel());
            System.out.print(score + ", ");
            scoreList.add(new Integer(score));
        }
        /*连续三次结果一样才认为投票成功*/
        while(true){
            int a = voteByDifferentialPrivacy(scoreList, epsilon);
            int b = voteByDifferentialPrivacy(scoreList, epsilon);
            int c = voteByDifferentialPrivacy(scoreList, epsilon);
            if(a == b && a == c)
                return a;
        }
    }
}
