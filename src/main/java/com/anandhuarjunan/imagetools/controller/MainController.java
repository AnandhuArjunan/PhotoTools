package com.anandhuarjunan.imagetools.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.utils.BasicEffects;
import com.anandhuarjunan.imagetools.utils.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.utils.OpenCvAlgorithms;
import com.anandhuarjunan.imagetools.utils.RuntimeUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
public class MainController implements Initializable {
	
	private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

	static {
	   LOGGER.setUseParentHandlers(false);
	 }

    @FXML
    private TextField fontLoc;

    
    private  File inputfile = null;
    private  File lutFile = null;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private ProgressBar ramProgressBar;
    
    @FXML
    private Label memAvlStatus;

    @FXML
    private Button fontBrowse;

   // @FXML
  //  private StackPane imageStackPane;
    
    @FXML
    private ImageView imageView;
    
    //@FXML
    //private StackPane imageStackPane1;
    
    @FXML
    private ImageView imageView1;
    
    @FXML
    private Slider brightnessSld;

    @FXML
    private Slider contrastSld;

    @FXML
    private Slider saturationSld;

    @FXML
    private Slider hueSld;
    
    @FXML
    private HBox hbImage;
    
    @FXML
    private HBox showPreviewBtn;

    private Timer ramTimer = new Timer();
    
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    private double[] basicEffectDoubles;
    
    public MainController(){
    	basicEffectDoubles = new double[4];
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		contrastSld.setUserData(0);
		hueSld.setUserData(1);
		brightnessSld.setUserData(2);
		saturationSld.setUserData(3);
		chooseFileDirectoryAction();
		loadRAMStatus();
		imageView.fitWidthProperty().bind(hbImage.widthProperty().divide(2)); 
		imageView.fitHeightProperty().bind(hbImage.heightProperty()); 
		imageView1.fitWidthProperty().bind(hbImage.widthProperty().divide(2)); 
		imageView1.fitHeightProperty().bind(hbImage.heightProperty()); 
		
		//sliderAction(contrastSld,hueSld,brightnessSld,saturationSld);
		
		showPreviewBtn.setOnMouseClicked(e->{
			
			OpenCVMatHelper cvMat = new OpenCVMatHelper();
			cvMat.setOriginalJavaFxImage(imageView.getImage());
			
			
			cvMat.setEffect(OpenCvAlgorithms.convertNegativeToPositiveOrViceVersa(cvMat.getOriginalImage()));
			imageView1.setImage(cvMat.getImagePost());
		});
	}
		



	private void sliderAction(Slider ...sliders) {
		for(Slider slider :sliders) {
			slider.setValue(0);
	        slider.setMax(1);
	        slider.setMin(-1);
	        slider.setMajorTickUnit(0.01);
	        // slider.setMinorTickCount(0.01);
	        slider.setShowTickMarks(true);
	        slider.setShowTickLabels(true);
	        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
	            basicEffectDoubles[(Integer)slider.getUserData()] = newValue.doubleValue();
	            BasicEffects basicEffects = new BasicEffects(basicEffectDoubles[0], basicEffectDoubles[1], basicEffectDoubles[2], basicEffectDoubles[3]);
	            basicEffects.changeEffect(imageView1);
	            System.out.println("h");
	        });
		}
		 
		
	}




	public void shutdown() {
        ramTimer.cancel();
        executorService.shutdownNow();
    }
    
    
private void loadRAMStatus() {
	
		
		TimerTask task = new TimerTask() {
			  @Override
			  public void run() {
				  double percentage = RuntimeUtil.getPercentageUsed();
				  String usedInMB = RuntimeUtil.getUsedMemoryInMiB();
				  String maximumInMB = RuntimeUtil.getMaxMemoryInMiB();
				  
					Platform.runLater(()->{
						ramProgressBar.setProgress(percentage);
						memAvlStatus.setText("Used / Maximum - "+usedInMB +"/"+ maximumInMB);

					});
			  }
			};

			ramTimer.schedule(task, 0l, 1000l);
	}
private void chooseFileDirectoryAction() {
    FileChooser fileChooser = new FileChooser();
    fontBrowse.setOnAction(ev->{
    	 inputfile = fileChooser.showOpenDialog(fontBrowse.getScene().getWindow());
		 if (inputfile != null) {
	            fontLoc.setText(inputfile.getName());
	            Image image = new Image(inputfile.toURI().toString());
	            imageView.setImage(image);
	            imageView1.setImage(image);
	            
         }
    });

}
}
