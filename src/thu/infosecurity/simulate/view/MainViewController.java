package thu.infosecurity.simulate.view;

import com.sun.tools.corba.se.idl.Util;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.PointLight;
import javafx.scene.shape.Circle;
import thu.infosecurity.simulate.controller.SceneCreator;
import thu.infosecurity.simulate.controller.SceneInitial;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.ElectronicVote;
import thu.infosecurity.simulate.util.RSA;
import thu.infosecurity.simulate.util.SharedKey;
import thu.infosecurity.simulate.util.Utils;

import javax.management.timer.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

import static thu.infosecurity.simulate.util.Millionaire.getTopLeader_Million;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class MainViewController {

    SceneInitial sc;

    @FXML
    private Button landBtn;

    @FXML
    private Button startBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Group sceneGroup;

    @FXML
    private Canvas sceneCanvas;

    @FXML
    private TextArea teamInfoArea;
    @FXML
    private TextArea infoTextArea;

    @FXML
    private TextField soldierNum;

    @FXML
    private TextField boxShareNum;

    @FXML
    private TextField boxNum;

    private boolean isLand = false;  //表示是否点击降落按钮

    private boolean isOk = false;    //表示是否点击开始按钮

    private static final double MIN_DISTANCE = 50.0; //表示每个士兵方圆MIN_DISTANCE像素以内表示可以进行认证对话等

    private static final double STEP_LENGTH = 40.0; //表示每次上士兵移动的步长

//    private Set<Integer> arriveTargetSet = new HashSet<Integer>();  //表示已经到达box目的地的士兵id

//    private Set<Integer> authSet = new HashSet<>(); //表示已经认证过的士兵集合
//    private Map<String, Set<Integer>> authMap = new HashMap<>(); //表示已经认证过的士兵集合,String是验证的口令，后面是用有同样口令的集合

    private ArrayList<Soldier> team = new ArrayList<>();
    private Soldier soldierLeader = new Soldier();
    private boolean gotoBox = true;
    private int sIndex = 1;

    //private String plain = "We are family! oh yeah!";  //每个士兵要用于跟别人验证的口令，这个也应该是每个士兵的属性！

    private boolean isOpenBox = false;  //表示打开设备箱子

    private Thread refresh;

//    @FXML
//    private PerspectiveCamera sceneCam;

    /**
     * The constructor (is called before the initialize()-method).
     */
    public MainViewController() {

        sc = new SceneInitial();

        team.clear();

//        arriveTargetSet.clear();
//        authSet.clear();
//        authMap.clear();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        //启动线程，每隔300ms更新goup
        refresh = new Thread() {
            public void run(){
                while(true)
                {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //实时刷新
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            if(isOk){
                                updateTeamInfo();
                                runOperate();
                                showGroup();
                            }
                        }
                    });
                }
            }

        };
        refresh.start();

    }


    /**
     * Handle resetBtn Button event.
     */
    @FXML
    private void handleResetBtn()
    {
        isOk = false;

        isLand = false;

        isOpenBox = false;

        sceneGroup.getChildren().clear();  //清空之前的图像

        sc = new SceneInitial();

        team.clear();
//        arriveTargetSet.clear();
//        authSet.clear();
//        authMap.clear();

    }

    /**
     * Handle startBtn Button event.
     */
    @FXML
    private void handleStartBtn()
    {
        if(isLand)
        {
            isOk = true;
        }
    }

    /**
     * Handle landBtn Button event.
     */
    @FXML
    private void handleLandBtn()
    {
        String str = soldierNum.getText();
        boolean isInt = true;
        if(!str.isEmpty()){
            for(int i=0; i<str.length(); i++)   //判断输入是否非数字
            {
                if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                }
                else {
                    isInt = false;
                    break;
                }
            }
            if(isInt) {
                int soldierNumber = Integer.parseInt(str);
                if (soldierNumber != 0 && !isLand) {

                    //读入开箱人数
                    str = boxShareNum.getText();
                    isInt = true;
                    if(!str.isEmpty()){
                        for(int i=0; i<str.length(); i++)   //判断输入是否非数字
                        {
                            if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                            }
                            else {
                                isInt = false;
                                break;
                            }
                        }
                        if(isInt){
                            int shareNum = Integer.parseInt(str);
                            if(shareNum != 0 && !isLand){
                                isLand = true;

                                sc.initial(0, soldierNumber, shareNum);

                                showGroup();

                                //首先将第一个士兵放入team，自己就是leader
                                team.add(sc.getSoldierList().get(0));
                                soldierLeader = sc.getSoldierList().get(0);
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("错误信息");
                            alert.setHeaderText(null);
                            alert.setContentText("请输入合法的开箱人数！");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("错误信息");
                        alert.setHeaderText(null);
                        alert.setContentText("请输入开箱人数！");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误信息");
                alert.setHeaderText(null);
                alert.setContentText("请输入合法的士兵数目！");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText(null);
            alert.setContentText("请输入士兵数目！");
            alert.showAndWait();
        }
    }

    /**
     * Handle event. File -> Quit
     */
    @FXML
    private void handleQuit()
    {
        System.exit(0);
    }

    /**operate methods**/
    /**
     * 两个士兵相互认证
     * @param sA
     * @param sB
     * @return
     */
    private boolean soldierVerify(Soldier sA, Soldier sB){
        boolean verifyResult = false;
        infoTextArea.appendText(sA.getName() + "遇到" + sB.getName() + "！" + "\r\n");
        //验证士兵，但是这样有问题，应该碰到一起再验证，函数的输入应该是两个人
        if(RSA.soldierVerify(sA) && RSA.soldierVerify(sB)){
            infoTextArea.appendText(sA.getName() + "和" + sB.getName() + "认证成功！" + "\r\n");
            verifyResult = true;
        }
        return verifyResult;
    }

    /**
     * 更新团队信息
     */
    private void updateTeamInfo(){
        teamInfoArea.clear();
        teamInfoArea.appendText("当前Leader："+soldierLeader.getName()+"\r\n");
        teamInfoArea.appendText("当前团队Member："+"\r\n");
        for(Soldier soldier: team){
            teamInfoArea.appendText("   - "+soldier.getName()+"\r\n");
        }
    }

    /**
     * 每个士兵每次向箱子走一定的距离
     */
    private void runOperate() {

        Target targetBox = sc.getWeaponBox();

        ArrayList<Soldier> soldierList = sc.getSoldierList();

        //模拟操作
        if(gotoBox){
            //向箱子方向前进
            //如果leader进入可验证箱子的范围，则team可以开始验证
            if(dis(soldierLeader.getPosition(), targetBox.getPosition()) <= MIN_DISTANCE + 20) {
                //打开box的操作，输入team，调用共享秘钥模块，获取秘钥
                Integer key = SharedKey.retrieveSharedKey(team, targetBox.getShareNumber());
                if (key == 0) {
                    //表示获取秘钥失败
                    infoTextArea.appendText("人数不足，获取秘钥失败！" + "\r\n");
                    //验证失败就返回，去寻找下一个士兵
                    gotoBox = false;
                } else {
                    System.out.println("getKey:"+key+", "+"trueKey:"+targetBox.getShareKey());
                    //获取秘钥成功，跟真实秘钥对比
                    if (key.equals(targetBox.getShareKey())) {
                        //验证成功，箱子打开
                        infoTextArea.appendText("验证成功，装备箱开启成功！" + "\r\n");
                        isOpenBox = true; //打开设备box成功
                        isOk = false; //停止演示
                    }
                }
            } else {
                //team向装备箱移动
                for(int i = 0; i < team.size(); i++){
                    Soldier soldier = team.get(i);
                    Point point = walkOne(soldier.getPosition(), targetBox.getPosition());   //走一步
                    soldier.setPosition(point);
                }
            }
        } else {
            //向下一个士兵前进
            //如果leader进入到下一个士兵范围，则开始认证
            if(dis(soldierLeader.getPosition(), soldierList.get(sIndex).getPosition()) <= MIN_DISTANCE) {
                if(soldierVerify(soldierLeader, soldierList.get(sIndex))){
                    //验证成功，将新成员加入团队
                    team.add(soldierList.get(sIndex));
                    //选举新的leader
                    //百万富翁算法选举
                    int leaderID = getTopLeader_Million(team);
                    if(leaderID == -1){
                        infoTextArea.appendText("最高军衔不止1人！" + "\r\n");
                        //进行电子投票
                        leaderID = ElectronicVote.vote(team, (float)1);
                    }
                    //确定leader
                    soldierLeader = team.get(leaderID);
                    //改变方向
                    gotoBox = true;
                    //下次检测下一个士兵
                    sIndex++;
                } else {
                    //验证失败
                    infoTextArea.appendText(soldierList.get(sIndex).getName() + "认证失败！" + "\r\n");
                }
            } else {
                //team向士兵移动
                for(int i = 0; i < team.size(); i++){
                    Soldier soldier = team.get(i);
                    Point point = walkOne(soldier.getPosition(), soldierList.get(sIndex).getPosition());   //走一步
                    soldier.setPosition(point);
                }
            }
        }
    }

    /**
     * show all shape in group.
     */
    @FXML
    private void showGroup() {
        sceneGroup.getChildren().clear();  //清空之前的图像
        sceneGroup.setAutoSizeChildren(true);

        //绘制装备箱
        Label imageLableBox = new Label();
//            Image image = new Image("file:resource/image/box.png");
        Image imageBox;
        if(isOpenBox)
        {
            imageBox = new Image(this.getClass().getResourceAsStream("boxOpen.png"));
        }
        else
        {
            imageBox = new Image(this.getClass().getResourceAsStream("box.png"));
        }
        imageLableBox.setGraphic(new ImageView(imageBox));

        Label lableBox = new Label("     装备箱  ");

        VBox vb = new VBox();
        vb.setLayoutX(sc.getWeaponBox().getPosition().getX());
        vb.setLayoutY(sc.getWeaponBox().getPosition().getY());
        vb.getChildren().addAll(imageLableBox, lableBox);

//        System.out.println("Box Location: " + vb.getLayoutX() + " " + vb.getLayoutY());

        sceneGroup.getChildren().add(vb);

        //绘制信件
        Label imageLableLetter = new Label();
        Image imageLetter;
        imageLetter = new Image(this.getClass().getResourceAsStream("letter.png"));
        imageLableLetter.setGraphic(new ImageView(imageLetter));

        Label lableLetter = new Label("       机密信函  ");

        VBox vbl = new VBox();
//        System.out.println("letter:"+sc.getLetter().getPosition().getX()+","+sc.getLetter().getPosition().getY());
        vbl.setLayoutX(sc.getLetter().getPosition().getX());
        vbl.setLayoutY(sc.getLetter().getPosition().getY());
        vbl.getChildren().addAll(imageLableLetter, lableLetter);

        sceneGroup.getChildren().add(vbl);

        //绘制士兵
        int index = 0;
        for(Soldier soldier: team)
        {
            Label imageLable = new Label();
            Image image;
            if(isOpenBox)
            {
                image = new Image(this.getClass().getResourceAsStream("soldierOpen.png"));
            }
            else
            {
                image = new Image(this.getClass().getResourceAsStream("soldier.png"));
            }

            imageLable.setGraphic(new ImageView(image));

            Label lable = new Label(String.valueOf(index + 1));

            HBox hb = new HBox();
            if(isOpenBox)
            {
                hb.setLayoutX((index/3) * 40.0 + 70.0);
                hb.setLayoutY((index%3) * 30.0);
            }
            else
            {
                hb.setLayoutX(soldier.getPosition().getX());
                hb.setLayoutY(soldier.getPosition().getY());
            }
            hb.getChildren().addAll(lable, imageLable);

            sceneGroup.getChildren().add(hb);

            index ++;
        }

        for(int i = team.size(); i < sc.getSoldierList().size(); i++)
        {
            Soldier soldier = sc.getSoldierList().get(i);
            Label imageLable = new Label();
//            Image image = new Image("file:resource/image/soldier.png");
            Image image;
            image = new Image(this.getClass().getResourceAsStream("soldier.png"));

            imageLable.setGraphic(new ImageView(image));

            Label lable = new Label(String.valueOf(i + 1));

            HBox hb = new HBox();

            hb.setLayoutX(soldier.getPosition().getX());
            hb.setLayoutY(soldier.getPosition().getY());

            hb.getChildren().addAll(lable, imageLable);

            sceneGroup.getChildren().add(hb);
        }

    }

    /**
     * 计算两点间的距离
     * @param a
     * @param b
     * @return
     */
    private double dis(Point a, Point b)
    {
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()));
    }

    /**
     * x向目的地box走一步，返回坐标
     * @param target
     * @return
     */
    private Point walkOne(Point a, Point target)
    {
        double verticalDis = STEP_LENGTH * Math.abs(a.getY()-target.getY()) / dis(a, target);   //竖直的距离差
        double horizontalDis = STEP_LENGTH * Math.abs(a.getX()-target.getX()) / dis(a, target);   //水平的距离差

        Point p = new Point();

        //以下在target的八个方位进行计算
        if(a.getX() <= target.getX() && a.getY() <= target.getY())  //左上
        {
            p.setLocation(a.getX() + horizontalDis, a.getY() + verticalDis);
        }
        else if(a.getX() == target.getX() && a.getY() < target.getY())  //上
        {
            p.setLocation(a.getX() + horizontalDis, a.getY() + verticalDis);
        }
        else if(a.getX() >= target.getX() && a.getY() <= target.getY())  //右上
        {
            p.setLocation(a.getX() - horizontalDis, a.getY() + verticalDis);
        }
        else if(a.getX() > target.getX() && a.getY() == target.getY())  //右
        {
            p.setLocation(a.getX() - horizontalDis, a.getY() + verticalDis);
        }
        else if(a.getX() >= target.getX() && a.getY() >= target.getY())  //右下
        {
            p.setLocation(a.getX() - horizontalDis, a.getY() - verticalDis);
        }
        else if(a.getX() == target.getX() && a.getY() > target.getY())  //下
        {
            p.setLocation(a.getX() - horizontalDis, a.getY() - verticalDis);
        }
        else if(a.getX() <= target.getX() && a.getY() >= target.getY())  //左下
        {
            p.setLocation(a.getX() + horizontalDis, a.getY() - verticalDis);
        }
        else if(a.getX() < target.getX() && a.getY() == target.getY())  //左
        {
            p.setLocation(a.getX() + horizontalDis, a.getY() - verticalDis);
        }

        return p;
    }

}
