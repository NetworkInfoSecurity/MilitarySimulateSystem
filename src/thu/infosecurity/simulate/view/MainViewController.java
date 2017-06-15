package thu.infosecurity.simulate.view;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import thu.infosecurity.simulate.controller.SceneInitial;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;
import thu.infosecurity.simulate.util.*;

import java.awt.*;
import java.util.*;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class MainViewController {

    SceneInitial sc;

    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private RadioButton rbRandom;
    @FXML
    private RadioButton rbLoad;

    @FXML
    private Button landBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button continueBtn;
    @FXML
    private Button resetBtn;

    @FXML
    private Group sceneGroup;

    @FXML
    private TextArea teamInfoArea;
    @FXML
    private TextArea infoTextArea;

    @FXML
    private TextField soldierNum;
    @FXML
    private TextField boxShareNum;
    @FXML
    private TextField spyNum;

    @FXML
    private TextField boxNum;

    private int method;  //初始化方法，0为随机，1为从文件读取

    private boolean isLand = false;  //表示是否点击降落按钮

    private boolean isOk = false;    //表示是否点击开始按钮

    private static final double MIN_DISTANCE = 50.0; //表示每个士兵方圆MIN_DISTANCE像素以内表示可以进行认证对话等

    private static final double STEP_LENGTH = 40.0; //表示每次上士兵移动的步长

    private ArrayList<Soldier> soldierList = new ArrayList<>();
    private ArrayList<Soldier> team = new ArrayList<>();
    private ArrayList<Soldier> spies = new ArrayList<>();
    private Soldier soldierLeader = new Soldier();
    private boolean gotoBox = true;


    private boolean isOpenBox = false;  //表示打开设备箱子

    private boolean isOpenLetter = false;  //表示信封是否打开

    private Thread refresh;

    //手动点击移动士兵
    private int fromSoldierID = -1;
    private int toSoldierID = -1;

    private boolean isClick = false;  //表示手动移动开始

    private boolean isToLetter = false; //表示是否前往信封

    private ArrayList<Soldier> allMembers = new ArrayList<>();  //表示界面上的所有士兵


    /**
     * The constructor (is called before the initialize()-method).
     */
    public MainViewController() {

        sc = new SceneInitial();

        soldierList.clear();
        team.clear();
        spies.clear();
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
                            else if(isClick)
                            {
                                updateTeamInfo();
                                clickRunOperate();
                                showGroup();
                            }

                            if(isToLetter)
                            {
                                runToLetter();
                                showGroup();
                            }
                        }
                    });
                }
            }

        };
        refresh.start();

        //初始化时添加手动点击面板的事件
        /*
        sceneGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getEventType().toString().equals("MOUSE_CLICKED"))
                {
                    if(!isOk && !isOpenBox)
                    {
                        System.out.println(event.getX() + " : " + event.getY());
                        if(fromSoldierID == -1)
                        {
                            if(team.size() == 0)
                            {
                                for(Soldier sd : sc.getSoldierList())
                                {
                                    if(dis(sd.getPosition(), new Point((int)event.getX(), (int)event.getY())) < 30)
                                    {
                                        fromSoldierID = sd.getID();
//                                        System.out.println("::" + fromSoldierID);
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                for(Soldier sd : team)
                                {
                                    if(dis(sd.getPosition(), new Point((int)event.getX(), (int)event.getY())) < 30)
                                    {
                                        fromSoldierID = soldierLeader.getID();
//                                        System.out.println("::" + fromSoldierID);
                                        break;
                                    }
                                }
                            }

                        }
                        else
                        {
                            if(toSoldierID == -1)
                            {
                                for(Soldier sd : soldierList)
                                {
                                    if(dis(sd.getPosition(), new Point((int)event.getX(), (int)event.getY())) < 30 && sd.getID() != fromSoldierID)
                                    {
                                        toSoldierID = sd.getID();
                                        infoTextArea.appendText("- 队伍向soldier" + toSoldierID + "士兵前进！" + "\r\n");
                                        break;
                                    }
                                }

                                if(toSoldierID == -1)
                                {
                                    if(dis(sc.getWeaponBox().getPosition(), new Point((int)event.getX(), (int)event.getY())) < 50)
                                    {
                                        toSoldierID = 0;   //0表示向箱子走
                                        infoTextArea.appendText("- 队伍向装备箱前进！" + "\r\n");
                                    }

                                    if(dis(sc.getLetter().getPosition(), new Point((int)event.getX(), (int)event.getY())) < 50)
                                    {
                                        toSoldierID = -2;  //-2表示向信封走
                                    }
                                }

                                if(toSoldierID == -1)
                                {
                                    fromSoldierID = -1;
                                }

                            }
                        }

//                        System.out.println(fromSoldierID + " to " + toSoldierID);
                    }

                    //打印出点击后所有的正在移动的士兵对儿
                    if(fromSoldierID != -1 && toSoldierID != -1)
                    {
                        System.out.println(fromSoldierID + " to " + toSoldierID);

                        if(team.size() == 0)   //初始team中的第一个士兵
                        {
                            Soldier soldier = new Soldier();
                            for(Soldier sd : soldierList)
                            {
                                if(sd.getID() == fromSoldierID)
                                {
                                    soldier = sd;
                                    break;
                                }
                            }

                            if(RSA.soldierVerify(sc.getPublicRsaKeyList(),soldier, soldierLeader.getDESKey())){
                                //验证成功，则将其加入团队，结束本函数
                                infoTextArea.appendText("- "+ soldier.getName() + "认证成功，加入队伍，成为指挥官！" + "\r\n");
                                team.add(soldier);
                                soldierLeader = soldier;
                                soldierList.remove(soldier);

                                isClick = true;    //表示单击开始行走
                            } else {
                                //验证失败，说明是间谍 ，继续下一个
                                infoTextArea.appendText("- "+ soldier.getName() + "是间谍！认证失败！" + "\r\n");
                                spies.add(soldier);
                                soldierList.remove(soldier);

                                fromSoldierID = -1;
                                toSoldierID = -1;
                            }
                        }

                    }


                }
            }
        });
        */
    }

    @FXML
    private void handleRbRandom(){
        soldierNum.setDisable(false);
        boxShareNum.setDisable(false);
        spyNum.setDisable(false);
        method = 0;
    }

    @FXML
    private void handleRbLoad(){
        soldierNum.setDisable(true);
        boxShareNum.setDisable(true);
        spyNum.setDisable(true);
        method = 1;
    }
    /**
     * Handle resetBtn Button event.
     */
    @FXML
    private void handleResetBtn()
    {
        isOk = false;

        startBtn.setDisable(false);
        landBtn.setDisable(false);
        pauseBtn.setDisable(false);
        continueBtn.setDisable(false);

        isLand = false;

        isOpenBox = false;

        gotoBox = true;

        sceneGroup.getChildren().clear();  //清空之前的图像

        sc = new SceneInitial();

        infoTextArea.clear();
        teamInfoArea.clear();

        soldierList.clear();

        allMembers.clear();

        team.clear();
        spies.clear();

        fromSoldierID = -1;
        toSoldierID = -1;

        isClick = false;

        isOpenLetter = false;

        isToLetter = false;
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
            startBtn.setDisable(true);
            //创建队伍
            initTeam();
        }
    }

    /**
     * handle ContinueBtn event
     */
    @FXML
    private void handleContinueBtn(){
        if(isLand){
            isOk = true;
            pauseBtn.setDisable(false);
            continueBtn.setDisable(true);
        }
    }

    /**
     * handle pauseBtn event
     */
    @FXML
    private void handlePauseBtn(){
        if(isLand){
            isOk = false;
            pauseBtn.setDisable(true);
            continueBtn.setDisable(false);
        }
    }

    /**
     * Handle landBtn Button event.
     */
    @FXML
    private void handleLandBtn()
    {
        landBtn.setDisable(true);
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

                                //读入间谍人数
                                str = spyNum.getText();
                                isInt = true;
                                if(!str.isEmpty()){
                                    for(int i = 0; i < str.length(); i++){
                                        if(str.charAt(i) >= '0' && str.charAt(i) <= '9'){
                                        } else {
                                            isInt = false;
                                            break;
                                        }
                                    }
                                    if(isInt){
                                        int spyNum = Integer.parseInt(str);
                                        if(!isLand){
                                            isLand = true;

                                            sc.initial(method, soldierNumber, shareNum, spyNum);

                                            //初始化士兵集合
                                            initSoldiers();

                                            //初始化界面显示
                                            showGroup();

                                            //创建队伍
                                            //initTeam();
                                        }
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("错误信息");
                                        alert.setHeaderText(null);
                                        alert.setContentText("请输入合法的间谍人数！");
                                        alert.showAndWait();
                                    }
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("错误信息");
                                    alert.setHeaderText(null);
                                    alert.setContentText("请输入间谍人数！");
                                    alert.showAndWait();
                                }
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
     * 将己方伞兵与间谍打乱在一起
     */
    private void initSoldiers(){
        //添加所有己方士兵
        soldierList = sc.getSoldierList();
        //添加所有间谍
        soldierList.addAll(sc.getSpyList());

        allMembers.addAll(soldierList);
        //打乱顺序
        Collections.shuffle(soldierList);
    }

    /**
     * 为team找到第一个士兵
     */
    private void initTeam(){

        //首先将第一个士兵放入team，自己就是leader
        do{
            Soldier soldier = soldierList.get(0);
            if(RSA.soldierVerify(sc.getPublicRsaKeyList(), soldier, soldierLeader.getDESKey())){
                //验证成功，则将其加入团队，结束本函数
                infoTextArea.appendText("- "+soldierList.get(0).getName() + "认证成功，成为指挥官！" + "\r\n");
                team.add(soldierList.get(0));
                soldierLeader = soldierList.get(0);
                soldierList.remove(0);
                break;
            } else {
                //验证失败，说明是间谍 ，继续下一个
                infoTextArea.appendText("- "+soldierList.get(0).getName() + "是间谍！认证失败！" + "\r\n");
                spies.add(soldierList.get(0));
                soldierList.remove(0);
            }
        } while(!soldierList.isEmpty());

    }
    /**
     * 更新团队信息
     */
    private void updateTeamInfo(){
        teamInfoArea.clear();
        teamInfoArea.appendText("当前指挥官："+"\r\n");
        teamInfoArea.appendText("   - "+soldierLeader.getName()+", "+soldierLeader.getTitle()
                +", 密级"+soldierLeader.getSecretLevel()+"\r\n");
        teamInfoArea.appendText("当前团队成员："+"\r\n");
        for(Soldier soldier: team){
            teamInfoArea.appendText("   - "+soldier.getName()+", "+soldier.getTitle()
                    +", 密级"+soldier.getSecretLevel()+"\r\n");
        }
    }

    /**
     * 从伞兵列表中移除所有间谍
     */
    private void removeSpies(){
        //首先将间谍筛选出来
        Iterator<Soldier> soldierIterator = soldierList.iterator();
        while(soldierIterator.hasNext()){
            Soldier soldier = soldierIterator.next();
            if(!RSA.soldierVerify(sc.getPublicRsaKeyList(), soldier, soldierLeader.getDESKey())){
                infoTextArea.appendText("- "+soldier.getName() + "是间谍！认证失败！" + "\r\n");
                spies.add(soldier);
                soldierIterator.remove();
            }
        }
    }

    /**
     * 每个士兵每次向箱子走一定的距离
     */
    private void runOperate() {

        Target targetBox = sc.getWeaponBox();

        //开箱后没有到达箱子的士兵继续到达箱子
        if(isOpenBox)
        {
            if(!soldierList.isEmpty()) {
                Iterator<Soldier> soldierIterator = soldierList.iterator();
                while (soldierIterator.hasNext()) {
                    Soldier sd = soldierIterator.next();
                    //如果进入leader范围，则开始认证
                    if(dis(soldierLeader.getPosition(), sd.getPosition()) <= MIN_DISTANCE) {
                        infoTextArea.appendText("- " + sd.getName() + "找到队伍！" + "\r\n");
                        if(RSA.soldierVerify(sc.getPublicRsaKeyList(), sd, soldierLeader.getDESKey())){
                            //验证成功，将新成员加入团队
                            infoTextArea.appendText("- "+sd.getName() + "认证成功，加入队伍！" + "\r\n");
                            team.add(sd);
                            //soldierList.remove(sd);
                            soldierIterator.remove();
                            //选举新的leader
                            infoTextArea.appendText("- "+"开始指挥官选举：" + "\r\n");
                            //百万富翁算法选举
                            infoTextArea.appendText("   - "+"百万富翁：");
//                            System.out.println(team.toString());
                            ArrayList<Soldier> leaders = Millionaire.getTopLeader_Million(team);
//                            System.out.println("leaderID: "+leaderID);
                            if(leaders.size() > 1){
                                infoTextArea.appendText("最高军衔不止1人！" + "\r\n");
                                //进行电子投票
                                infoTextArea.appendText("   - "+"电子投票：");
                                //返回最终leader在leaders列表里面的索引
                                int tempIndex = blindSignature.vote(team, leaders.size());
                                //确定leader
                                soldierLeader = leaders.get(tempIndex-1);
                            } else {
                                //确定leader
                                soldierLeader = leaders.get(0);
                            }
                            infoTextArea.appendText("新指挥官为" + soldierLeader.getName()+"\r\n");
                        } else {
                            //验证失败
                            infoTextArea.appendText("- "+sd.getName() + "是间谍！认证失败！" + "\r\n");
                            //确认间谍
                            spies.add(sd);
                            //soldierList.remove(sd);
                            soldierIterator.remove();
                        }
                    }
                    else
                    {
                        Point point = walkOne(sd.getPosition(), soldierLeader.getPosition());   //走一步
                        sd.setPosition(point);
                    }
                }
            }
            else
            {
                isOk = false; //停止演示
            }
            return;
        }

        //模拟操作
        if(gotoBox){
            //向箱子方向前进
            //如果leader进入可验证箱子的范围，则team可以开始验证
            if(dis(soldierLeader.getPosition(), targetBox.getPosition()) <= MIN_DISTANCE + 20) {
                //打开box的操作，输入team，调用共享秘钥模块，获取秘钥
                if (!targetBox.canOpen(team)) {
                    //表示获取秘钥失败，开箱失败
                    infoTextArea.appendText("- "+"验证失败，无法开启装备箱！" + "\r\n");
                    //验证失败就返回，去寻找下一个士兵
                    gotoBox = false;
                } else {
                    //获取秘钥成功，开箱成功
                    infoTextArea.appendText("- "+"验证成功，装备箱开启成功！" + "\r\n");
                    isOpenBox = true; //打开设备box成功
                    //isOk = false; //停止演示
                    //开箱成功后当即把所有间谍移除
                    removeSpies();
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
            if(soldierList.isEmpty()){
                //如果列表里的士兵都读取完了，终止刷新
                isOk = false;
                infoTextArea.appendText("- "+"已无其他士兵！"+"\r\n");
            } else {
                //如果leader进入到下一个士兵范围，则开始认证
                if(dis(soldierLeader.getPosition(), soldierList.get(0).getPosition()) <= MIN_DISTANCE) {
                    infoTextArea.appendText("- 队伍遇到" + soldierList.get(0).getName() + "！" + "\r\n");
                    if(RSA.soldierVerify(sc.getPublicRsaKeyList(), soldierList.get(0), soldierLeader.getDESKey())){
                        //验证成功，将新成员加入团队
                        infoTextArea.appendText("- "+soldierList.get(0).getName() + "认证成功，加入队伍！" + "\r\n");
                        team.add(soldierList.get(0));
                        soldierList.remove(0);
                        //选举新的leader
                        infoTextArea.appendText("- "+"开始指挥官选举：" + "\r\n");
                        //百万富翁算法选举
                        infoTextArea.appendText("   - "+"百万富翁：");
//                        System.out.println(team.toString());
                        ArrayList<Soldier> leaders = Millionaire.getTopLeader_Million(team);
//                        System.out.println("leaderID: "+leaderID);
                        if(leaders.size() > 1){
                            infoTextArea.appendText("最高军衔不止1人！" + "\r\n");
                            //进行电子投票
                            infoTextArea.appendText("   - "+"电子投票：");
                            //返回最终leader在leaders列表里面的索引
                            int tempIndex = blindSignature.vote(team, leaders.size());
                            //确定leader
                            soldierLeader = leaders.get(tempIndex-1);
                        } else {
                            //确定leader
                            soldierLeader = leaders.get(0);
                        }
                        infoTextArea.appendText("新指挥官为" + soldierLeader.getName()+"\r\n");
                        //改变方向
                        gotoBox = true;
                    } else {
                        //验证失败
                        infoTextArea.appendText("- "+soldierList.get(0).getName() + "是间谍！认证失败！" + "\r\n");
                        //确认间谍
                        spies.add(soldierList.get(0));
                        soldierList.remove(0);
                    }
                } else {
                    //team向士兵移动
                    for(int i = 0; i < team.size(); i++){
                        Soldier soldier = team.get(i);
                        Point point = walkOne(soldier.getPosition(), soldierList.get(0).getPosition());   //走一步
                        soldier.setPosition(point);
                    }
                }
            }
        }
    }

    //标签点击事件绑定
    private void MouseClickedEventHandler(Label label, int ID)
    {
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getEventType().toString().equals("MOUSE_CLICKED"))
                {
                    if(!isOk)
                    {
//                        System.out.println("Click: " + event.getX() + " : " + event.getY());

                        if(fromSoldierID == -1)
                        {
                            fromSoldierID = ID;
                            System.out.println("Click1: " + event.getX() + " : " + event.getY() + " ID: " + ID);
                            infoTextArea.appendText("-- ClickFrom-ID: " + ID + "\r\n");
                        }
                        else
                        {
                            if(toSoldierID == -1 && fromSoldierID != ID)
                            {
                                toSoldierID = ID;

                                System.out.println("Click2: " + event.getX() + " : " + event.getY() + " ID: " + ID);
                                infoTextArea.appendText("-- ClickTo-ID: " + ID + "\r\n");

                                if(fromSoldierID == -2 || fromSoldierID == 0)
                                {
                                    fromSoldierID = -1;
                                    toSoldierID = -1;
                                }

                                if(toSoldierID != -1)
                                {
                                    if(toSoldierID == -2)    //-2表示向信封走
                                    {
                                        infoTextArea.appendText("- soldier" + fromSoldierID + "向信封处前进！" + "\r\n");
                                    }
                                    else if(toSoldierID == 0)  //0表示向箱子走
                                    {
                                        infoTextArea.appendText("- soldier" + fromSoldierID + "向装备箱前进！" + "\r\n");
                                    }
                                    else
                                    {
                                        infoTextArea.appendText("- soldier" + fromSoldierID + "向soldier" + toSoldierID + "前进！" + "\r\n");
                                    }
                                }
                            }
                        }

//                        System.out.println(fromSoldierID + " to " + toSoldierID);
                    }

                    //打印出点击后所有的正在移动的士兵对儿
                    if(fromSoldierID != -1 && toSoldierID != -1)
                    {
                        System.out.println("move: " + fromSoldierID + " to " + toSoldierID);

                        if(team.size() == 0)   //初始team中的第一个士兵
                        {
                            Soldier soldier = new Soldier();
                            for(Soldier sd : allMembers)
                            {
                                if(sd.getID() == fromSoldierID)
                                {
                                    soldier = sd;
                                    break;
                                }
                            }

                            if(soldier.getName() == null)
                            {
                                fromSoldierID = -1;
                                toSoldierID = -1;
                                return;
                            }

                            if(RSA.soldierVerify(sc.getPublicRsaKeyList(),soldier, soldierLeader.getDESKey())){
                                //验证成功，则将其加入团队，结束本函数
                                infoTextArea.appendText("- "+ soldier.getName() + "认证成功，成为指挥官！" + "\r\n");
                                team.add(soldier);
                                soldierLeader = soldier;
                                soldierList.remove(soldier);

                                isClick = true;    //表示单击开始行走

                            } else {
                                //验证失败，说明是间谍 ，继续下一个
                                infoTextArea.appendText("- "+ soldier.getName() + "是间谍！认证失败！" + "\r\n");
                                spies.add(soldier);
                                soldierList.remove(soldier);

                                fromSoldierID = -1;
                                toSoldierID = -1;
                            }
                        }
                        else
                        {
//                            boolean isInFrom = false;
//                            for(Soldier sd : team)
//                            {
//                                if(fromSoldierID == sd.getID())
//                                {
//                                    isInFrom = true;
//                                    break;
//                                }
//                            }
//                            boolean isInTo = false;
//                            for(Soldier sd : team)
//                            {
//                                if(toSoldierID == sd.getID())
//                                {
//                                    isInTo = true;
//                                    break;
//                                }
//                            }
//
//                            if(!isInFrom && !isInTo)
//                            {
//                                fromSoldierID = -1;
//                                toSoldierID = -1;
//                            }
                        }

                    }


                }
            }
        });
    }

    //手动单击的士兵动作
    private void clickRunOperate()
    {

        //开箱后没有到达箱子的士兵继续到达箱子
        if(isOpenBox && false)
        {
            System.out.println("isOpenBox After!");
            if(!soldierList.isEmpty()) {
                Iterator<Soldier> soldierIterator = soldierList.iterator();
                while (soldierIterator.hasNext()) {
                    Soldier sd = soldierIterator.next();
                    //如果进入leader范围，则开始认证
                    if(dis(soldierLeader.getPosition(), sd.getPosition()) <= MIN_DISTANCE) {
                        infoTextArea.appendText("- " + sd.getName() + "找到队伍！" + "\r\n");
                        if(RSA.soldierVerify(sc.getPublicRsaKeyList(), sd, soldierLeader.getDESKey())){
                            //验证成功，将新成员加入团队
                            infoTextArea.appendText("- "+sd.getName() + "认证成功，加入队伍！" + "\r\n");
                            team.add(sd);
                            //soldierList.remove(sd);
                            soldierIterator.remove();
                            //选举新的leader
                            infoTextArea.appendText("- "+"开始指挥官选举：" + "\r\n");
                            //百万富翁算法选举
                            infoTextArea.appendText("   - "+"百万富翁：");
//                            System.out.println(team.toString());
                            ArrayList<Soldier> leaders = Millionaire.getTopLeader_Million(team);
//                            System.out.println("leaderID: "+leaderID);
                            if(leaders.size() > 1){
                                infoTextArea.appendText("最高军衔不止1人！" + "\r\n");
                                //进行电子投票
                                infoTextArea.appendText("   - "+"电子投票：");
                                //返回最终leader在leaders列表里面的索引
                                int tempIndex = blindSignature.vote(team, leaders.size());
                                //确定leader
                                soldierLeader = leaders.get(tempIndex-1);
                            } else {
                                //确定leader
                                soldierLeader = leaders.get(0);
                            }
                            infoTextArea.appendText("新指挥官为" + soldierLeader.getName()+"\r\n");
                        } else {
                            //验证失败
                            infoTextArea.appendText("- "+sd.getName() + "是间谍！认证失败！" + "\r\n");
                            //确认间谍
                            spies.add(sd);
                            //soldierList.remove(sd);
                            soldierIterator.remove();
                        }
                    }
                    else
                    {
                        Point point = walkOne(sd.getPosition(), soldierLeader.getPosition());   //走一步
                        sd.setPosition(point);
                    }
                }
            }
            else
            {
//                isClick = false; //停止演示
            }
            return;
        }

        if(!isOk && fromSoldierID != -1 && toSoldierID != -1)
        {
            if(toSoldierID == 0) //0表示箱子
            {
                Target targetBox = sc.getWeaponBox();

                boolean isIn = false; //表示起始点是否在team中
                for(Soldier sd : team)
                {
                    if(sd.getID() == fromSoldierID)
                    {
                        isIn = true;
                        break;
                    }
                }
                if(isIn)
                {
                    for(int i = 0; i < team.size(); i++){
                        Soldier soldier = team.get(i);
                        Point point = walkOne(soldier.getPosition(), targetBox.getPosition());   //走一步
                        soldier.setPosition(point);
                    }
                }
                else
                {
                    Soldier ssd = new Soldier();
                    for(Soldier s : allMembers)
                    {
                        if(s.getID() == fromSoldierID)
                        {
                            ssd = s;
                            break;
                        }
                    }
                    Point point = walkOne(ssd.getPosition(), targetBox.getPosition());   //走一步
                    ssd.setPosition(point);
                }

                //向箱子方向前进
                //如果leader进入可验证箱子的范围，则team可以开始验证
                if(dis(soldierLeader.getPosition(), targetBox.getPosition()) <= MIN_DISTANCE + 20)
                {
                    //打开box的操作，输入team，调用共享秘钥模块，获取秘钥
                    if (!targetBox.canOpen(team)) {
                        //表示获取秘钥失败，开箱失败
                        infoTextArea.appendText("- "+"验证失败，无法开启装备箱！" + "\r\n");
                        //验证失败就返回，去寻找下一个士兵

                    } else {
                        //获取秘钥成功，开箱成功
                        infoTextArea.appendText("- "+"验证成功，装备箱开启成功！" + "\r\n");
                        isOpenBox = true; //打开设备box成功

                        //开箱成功后当即把所有间谍移除
                        removeSpies();
                    }
                    fromSoldierID = -1;
                    toSoldierID = -1;
                }

            }
            else if(toSoldierID == -2)   //-2表示信封
            {
                Soldier soldierLetter = new Soldier();
                for(Soldier sd : allMembers)
                {
                    if(sd.getID() == fromSoldierID)
                    {
                        soldierLetter = sd;
                        break;
                    }
                }

                Point point = walkOne(soldierLetter.getPosition(), sc.getLetter().getPosition());   //走一步
                soldierLetter.setPosition(point);

                if(dis(soldierLetter.getPosition(), sc.getLetter().getPosition()) < MIN_DISTANCE + 10)
                {
                    System.out.println("soildier" + soldierLetter.getID() + "到达机密文件！");
                    if(sc.getLetter().canOpen(soldierLetter))
                    {
                        System.out.println("soildier" + soldierLetter.getID() + "能够打开机密文件！");
                        infoTextArea.appendText("    - soildier" + soldierLetter.getID() + "成功获取机密文件！" + "\r\n");
                        infoTextArea.appendText("    - 内容：" + sc.getWeaponBox().getMessages() + "\r\n");
                    }
                    else
                    {
                        infoTextArea.appendText("    - soildier" + soldierLetter.getID() + "机密文件获取失败！" + "\r\n");
                    }
                    fromSoldierID = -1;
                    toSoldierID = -1;
                }


            }
            else   //表示走向士兵
            {
                boolean isDis = false;
                Soldier soldierNew = new Soldier();

                Soldier soldierTo = new Soldier();
                boolean isToTeam = false;
                for(Soldier sd : team)
                {
                    if(sd.getID() == toSoldierID)
                    {
                        soldierTo = soldierLeader;
                        isToTeam = true;
                        break;
                    }
                }
                if(!isToTeam)
                {
                    for(Soldier sd : allMembers)
                    {
                        if(sd.getID() == toSoldierID)
                        {
                            soldierTo = sd;
                            break;
                        }
                    }
                }

                if(soldierTo.getName() == null)
                {
                    fromSoldierID = -1;
                    toSoldierID = -1;
                    return;
                }

//                System.out.println("soldierTo.ID: " + soldierTo.getID() + " Point:" + soldierTo.getPosition().getX() + " " + soldierTo.getPosition().getY());

                boolean isFromTeam = false; //表示起始点是否在team中
                for(Soldier sd : team)
                {
                    if(sd.getID() == fromSoldierID)
                    {
                        isFromTeam = true;
                        break;
                    }
                }

                if(isFromTeam)
                {
                    for(int i = 0; i < team.size(); i++){
                        Soldier soldier = team.get(i);
                        Point point = walkOne(soldier.getPosition(), soldierTo.getPosition());   //走一步
                        soldier.setPosition(point);
                    }
                    if(dis(soldierLeader.getPosition(), soldierTo.getPosition()) <= MIN_DISTANCE)
                    {
                        soldierNew = soldierTo;
                        isDis = true;
                    }
                }
                else
                {
                    Soldier ssd = new Soldier();
                    for(Soldier s : allMembers)
                    {
                        if(s.getID() == fromSoldierID)
                        {
                            ssd = s;
                            break;
                        }
                    }

                    if(ssd.getName() == null)
                    {
                        fromSoldierID = -1;
                        toSoldierID = -1;
                        return;
                    }

                    Point point = walkOne(ssd.getPosition(), soldierTo.getPosition());   //走一步
                    ssd.setPosition(point);
                    if(dis(ssd.getPosition(), soldierTo.getPosition()) <= MIN_DISTANCE)
                    {
                        soldierNew = ssd;
                        isDis = true;
                    }
                }

                if(!isToTeam && !isFromTeam)
                {
                    if(isDis)
                    {
                        fromSoldierID = -1;
                        toSoldierID = -1;
                        return;
                    }
                }

                if(isDis)
                {
                    infoTextArea.appendText("- 队伍遇到" + soldierNew.getName() + "！" + "\r\n");
                    if(RSA.soldierVerify(sc.getPublicRsaKeyList(), soldierNew, soldierLeader.getDESKey())){
                        //验证成功，将新成员加入团队
                        infoTextArea.appendText("- "+ soldierNew.getName() + "认证成功，加入队伍！" + "\r\n");
                        team.add(soldierNew);
                        soldierList.remove(soldierNew);
                        //选举新的leader
                        infoTextArea.appendText("- "+"开始指挥官选举：" + "\r\n");
                        //百万富翁算法选举
                        infoTextArea.appendText("   - "+"百万富翁：");
//                        System.out.println(team.toString());
                        ArrayList<Soldier> leaders = Millionaire.getTopLeader_Million(team);
//                        System.out.println("leaderID: "+leaderID);
                        if(leaders.size() > 1){
                            infoTextArea.appendText("最高军衔不止1人！" + "\r\n");
                            //进行电子投票
                            infoTextArea.appendText("   - "+"电子投票：");
                            //返回最终leader在leaders列表里面的索引
                            int tempIndex = blindSignature.vote(team, leaders.size());
                            //确定leader
                            soldierLeader = leaders.get(tempIndex-1);
                        } else {
                            //确定leader
                            soldierLeader = leaders.get(0);
                        }
                        infoTextArea.appendText("新指挥官为" + soldierLeader.getName()+"\r\n");

                    } else {
                        //验证失败
                        infoTextArea.appendText("- "+ soldierNew.getName() + "是间谍！认证失败！" + "\r\n");
                        //确认间谍
                        spies.add(soldierNew);
                        soldierList.remove(soldierNew);
                    }

                    fromSoldierID = -1;
                    toSoldierID = -1;
                }

            }


        }
    }

    //当开箱后，所有士兵到达箱子后，点击信封士兵前往信封处
    private void runToLetter()
    {
        if(!isOpenLetter && isOpenBox)
        {
            for(int i = 0; i < team.size(); i++){
                Soldier soldier = team.get(i);
                Point point = walkOne(soldier.getPosition(), sc.getLetter().getPosition());   //走一步
                soldier.setPosition(point);
            }

            if(dis(soldierLeader.getPosition(), sc.getLetter().getPosition()) < MIN_DISTANCE + 20)
            {
                isOpenLetter = true;
                isToLetter = false;
                for(Soldier sd : team)
                {
                    if(sc.getLetter().canOpen(sd))
                    {
                        System.out.println("soildier" + sd.getID() + "能够打开机密文件！");
                        infoTextArea.appendText("    - soildier" + sd.getID() + "能够打开机密文件！" + "\r\n");
                    }
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

        MouseClickedEventHandler(imageLableBox, 0);

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

        Label lableLetter = new Label("机密信函  ");

        MouseClickedEventHandler(imageLableLetter, -2);

//        imageLableLetter.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if(event.getEventType().toString().equals("MOUSE_CLICKED"))
//                {
//                    System.out.println("Click letter!");
//                    isToLetter = true;
//                }
//            }
//        });

        VBox vbl = new VBox();
//        System.out.println("letter:"+sc.getLetter().getPosition().getX()+","+sc.getLetter().getPosition().getY());
        vbl.setLayoutX(sc.getLetter().getPosition().getX());
        vbl.setLayoutY(sc.getLetter().getPosition().getY());
        vbl.getChildren().addAll(imageLableLetter, lableLetter);

        sceneGroup.getChildren().add(vbl);

        //绘制士兵
        int index = 0;
        //绘制团队士兵
        for(Soldier member: team)
        {
            Label imageLable = new Label();
            Image image;
            if(isOpenBox)
            {
                image = new Image(this.getClass().getResourceAsStream("soldierOpen.png"));
            }
            else
            {
                if(member.getID() == soldierLeader.getID()){
                    image = new Image(this.getClass().getResourceAsStream("soldierLeader.png"));
                } else {
                    image = new Image(this.getClass().getResourceAsStream("soldier.png"));
                }
            }

            imageLable.setGraphic(new ImageView(image));

            MouseClickedEventHandler(imageLable, member.getID());

            Label lable = new Label(String.valueOf(member.getID()));

            HBox hb = new HBox();
//            if(isOpenBox)
//            {
//                hb.setLayoutX((index/3) * 40.0 + 70.0);
//                hb.setLayoutY((index%3) * 30.0);
//            }
//            else
            {
//                hb.setLayoutX(member.getPosition().getX());
//                hb.setLayoutY(member.getPosition().getY());
                hb.setLayoutX(soldierLeader.getPosition().getX()+((index/3) * 42.0));
                hb.setLayoutY(soldierLeader.getPosition().getY()+((index%3) * 32.0));

//                member.setPosition(new Point((int)(soldierLeader.getPosition().getX()+((index/3) * 42.0)), (int)(soldierLeader.getPosition().getY()+((index%3) * 32.0))));

            }
            hb.getChildren().addAll(lable, imageLable);

            sceneGroup.getChildren().add(hb);

            index ++;
        }
        //绘制间谍
        for(Soldier spy: spies)
        {
            Label imageLable = new Label();

            Image image;
            image = new Image(this.getClass().getResourceAsStream("soldierEnemy.png"));

            imageLable.setGraphic(new ImageView(image));

            MouseClickedEventHandler(imageLable, spy.getID());

            Label lable = new Label(String.valueOf(spy.getID()));

            HBox hb = new HBox();

            hb.setLayoutX(spy.getPosition().getX());
            hb.setLayoutY(spy.getPosition().getY());

            hb.getChildren().addAll(lable, imageLable);

            sceneGroup.getChildren().add(hb);
        }
        //绘制未检测士兵
        for(Soldier soldier: soldierList)
        {
            Label imageLable = new Label();

            Image image;
            image = new Image(this.getClass().getResourceAsStream("soldier.png"));

            imageLable.setGraphic(new ImageView(image));

            MouseClickedEventHandler(imageLable, soldier.getID());

            Label lable = new Label(String.valueOf(soldier.getID()));

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
