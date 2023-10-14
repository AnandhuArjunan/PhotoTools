package com.anandhuarjunan.imagetools.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.anandhuarjunan.imagetools.algorithms.ComplexMatConvertor;
import com.anandhuarjunan.imagetools.algorithms.ComplxMatArgumentsValidator;
import com.anandhuarjunan.imagetools.algorithms.SimpleMatConvertor;
import com.anandhuarjunan.imagetools.constants.Constants;
import com.anandhuarjunan.imagetools.helper.AlgorithmsMetadataHelper;
import com.anandhuarjunan.imagetools.model.AlgorithmTreeDataModel;
import com.anandhuarjunan.imagetools.utils.JFXUtil;
import com.anandhuarjunan.imagetools.utils.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.utils.RuntimeUtil;

import animatefx.animation.FadeIn;
import animatefx.animation.Tada;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

	static {
		LOGGER.setUseParentHandlers(false);
	}

	@FXML
	private TextField fontLoc;

	private File inputfile = null;

	@FXML
	private BorderPane borderPane;

	@FXML
	private ProgressBar ramProgressBar;

	@FXML
	private Label memAvlStatus;

	@FXML
	private Button fontBrowse;

	@FXML
	private ImageView imageView;

	@FXML
	private ImageView imageView1;

	@FXML
	private HBox hbImage;

    @FXML
    private Button processImage;

	@FXML
	private Button download;

	@FXML
	private TreeView<AlgorithmTreeDataModel> opTreeView;
	
	@FXML
	private VBox imageContainer;
	
    @FXML
    private ToolBar toolBar;
    
    @FXML
    private VBox sourceImageCont;
    
    @FXML
    private Label prcImageInfo;

    @FXML
    private ProgressBar progressbarIndicator;
    
    @FXML
    private StackPane imageStatckView;

	private Timer ramTimer = new Timer();

	private AlgorithmTreeDataModel algorithmTreeDataModel = null;

	private AlgorithmsMetadataHelper algorithmsMetadata = new AlgorithmsMetadataHelper();

	private ComplexMatConvertor complexMatConvertor = null;
	

	 @FXML
	private ImageView impImageView;
	
 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {			
			chooseFileDirectoryAction();
			loadRAMStatus();
			algorithmsMetadata.initializeOperationsView(opTreeView);
			imageView.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView.fitHeightProperty().bind(hbImage.heightProperty());
			imageView1.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView1.fitHeightProperty().bind(hbImage.heightProperty());
			download.disableProperty().bind(imageView1.imageProperty().isNull());
			processImage.disableProperty().bind(imageView.imageProperty().isNull().or(opTreeView.getSelectionModel().selectedItemProperty().isNull()));
			impImageView.fitHeightProperty().bind(imageContainer.heightProperty().divide(1.3));
			impImageView.fitWidthProperty().bind(imageContainer.widthProperty().divide(1.3));
	        // Make the toolbar autoscrollable
	        toolBar.prefWidthProperty().bind(imageContainer.widthProperty());
	        toolBar.maxWidthProperty().bind(imageContainer.widthProperty());
	        
	      

			opTreeView.getSelectionModel().selectedItemProperty().addListener(initToolBarGui()); //Initiate toolbar if the selected operation requires it.

			download.setOnAction(downloadImageAction());

			processImage.setOnAction(processImageAction());
			
			removeToolBar();

		} catch (Exception e) {
			JFXUtil.showInfoAlert("Application Error !", e.getMessage());
		}
	}


	
	private ChangeListener<? super TreeItem<AlgorithmTreeDataModel>> initToolBarGui() {
		return (observable, oldValue, newValue) -> {
			algorithmTreeDataModel = newValue.getValue();
			if(Constants.COMPLEX_MAT.equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())) {
				try {
					addToolBar();
					Class<?> class1 = Class.forName(algorithmTreeDataModel.getAlgorithmCodePath());
					Constructor<?> constructor = class1.getConstructor(ToolBar.class);
					complexMatConvertor = (ComplexMatConvertor)constructor.newInstance(toolBar);	
					complexMatConvertor.init();
				}catch(Exception e) {
					JFXUtil.showInfoAlert("Could not initialize Operations UI !", e.getMessage());
				}
				
			}else{
				removeToolBar();
			}
			
		};
	}

	public void shutdown() {
		ramTimer.cancel();
	}

	private void loadRAMStatus() {

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				double percentage = RuntimeUtil.getPercentageUsed();
				String usedInMB = RuntimeUtil.getUsedMemoryInMiB();
				String maximumInMB = RuntimeUtil.getMaxMemoryInMiB();

				Platform.runLater(() -> {
					ramProgressBar.setProgress(percentage);
					memAvlStatus.setText("Used / Maximum - " + usedInMB + "/" + maximumInMB);

				});
			}
		};

		ramTimer.schedule(task, 0l, 1000l);
	}
	
	private EventHandler<ActionEvent> downloadImageAction() {
		return e-> {
			try {
				JFXUtil.downloadImageWithLocationChooser(imageView1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
	}

	private void chooseFileDirectoryAction() {
		FileChooser fileChooser = new FileChooser();
		fontBrowse.setOnAction(ev -> {
			inputfile = fileChooser.showOpenDialog(fontBrowse.getScene().getWindow());
			if (inputfile != null) {
				fontLoc.setText(inputfile.getName());
				Image image = new Image(inputfile.toURI().toString());
				imageView.setImage(image);
				imageView1.setImage(image);
				imageContainer.toFront();
				impImageView.setVisible(false);
			}
		});

	}
	
	private EventHandler<ActionEvent> processImageAction() {
		return e -> {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					OpenCVMatHelper cvMat = new OpenCVMatHelper();
					cvMat.setOriginalImageByPath(inputfile);
						try {
							if(Constants.COMPLEX_MAT.equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())) {
								
								if(complexMatConvertor instanceof ComplxMatArgumentsValidator) {
									ComplxMatArgumentsValidator argumentsValidator = (ComplxMatArgumentsValidator) complexMatConvertor;
									if(!argumentsValidator.validate()) {
										JFXUtil.showInfoAlert("Input Validation Failed", "Some fields are mandatory to input !");
										cancel();
									}
								}
								if(!isCancelled()) {
									cvMat.setEffect(complexMatConvertor.convert(cvMat.getOriginalImage()));
									imageView1.setImage(cvMat.getImagePost());
								}
								
							}else if(Constants.SIMPLE_MAT.equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())){

								SimpleMatConvertor convertor = (SimpleMatConvertor) Class.forName(algorithmTreeDataModel.getAlgorithmCodePath()).newInstance();
								cvMat.setEffect(convertor.convert(cvMat.getOriginalImage()));
								imageView1.setImage(cvMat.getImagePost());
							}
							
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
							JFXUtil.showInfoAlert("Processing failed !", e1.getMessage());
						}
					return null;
				}
			};
			task.setOnSucceeded(ev->new FadeIn(imageView1).play());;
			progressbarIndicator.visibleProperty().bind(task.runningProperty());
			prcImageInfo.visibleProperty().bind(task.runningProperty());
			 new Thread(task).start();
			
		};
		
	}
	
	void addToolBar() {
		if(!imageContainer.getChildren().contains(toolBar)) {
			imageContainer.getChildren().add(0, toolBar);
			new FadeIn(imageContainer).play();

		}
	}
	void removeToolBar() {
		if(imageContainer.getChildren().contains(toolBar)) {
			toolBar.getItems().clear();
			new FadeIn(imageContainer).play();
			imageContainer.getChildren().remove(toolBar);
		}  
	}
	
}
