package thu.infosecurity.simulate.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.PointLight;
import javafx.scene.shape.Circle;
import thu.infosecurity.simulate.controller.SceneCreator;
import thu.infosecurity.simulate.controller.SceneInitial;
import thu.infosecurity.simulate.model.Soldier;
import thu.infosecurity.simulate.model.Target;

import java.util.ArrayList;

/**
 * Created by DaFei-PC on 2017-05-16.
 */
public class MainViewController {

    SceneInitial sc;

    @FXML
    private Button startBtn;
    private Button suspendBtn;
    private Button resetBtn;

    @FXML
    private Group sceneGroup;

    @FXML
    private Canvas sceneCanvas;

//    @FXML
//    private PerspectiveCamera sceneCam;

    /**
     * The constructor (is called before the initialize()-method).
     */
    public MainViewController() {
        sc = new SceneInitial();
        sc.initial(0,5);


    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Handle Button event.
        startBtn.setOnAction((ActionEvent event) -> {
            GraphicsContext gc = sceneCanvas.getGraphicsContext2D();
            gc.setFill(Color.GREEN);
            for(Soldier soldier: sc.getSoldierList()){
                gc.fillOval(soldier.getPosition().getX(),soldier.getPosition().getY(),15,15);
            }
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(5);
            gc.strokeRoundRect(sc.getWeaponBox().getPosition().getX(),sc.getWeaponBox().getPosition().getY(),20,20,10,10);
        });


    }
}
