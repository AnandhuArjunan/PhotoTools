package com.anandhuarjunan.imagetools.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.anandhuarjunan.imagetools.adjustments.BrightnessContrastAdjustment;
import com.anandhuarjunan.imagetools.adjustments.DirectAdjustments;
import com.anandhuarjunan.imagetools.adjustments.HueSaturationAdjustment;
import com.anandhuarjunan.imagetools.constants.Constants;
import com.anandhuarjunan.imagetools.helper.AlgorithmsMetadataHelper;
import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.model.AlgorithmTreeDataModel;
import com.anandhuarjunan.imagetools.model.Groups;
import com.anandhuarjunan.imagetools.opencv.algorithms.ComplexMatConvertor;
import com.anandhuarjunan.imagetools.opencv.algorithms.ComplxMatArgumentsValidator;
import com.anandhuarjunan.imagetools.opencv.algorithms.SimpleMatConvertor;
import com.anandhuarjunan.imagetools.utils.JFXUtil;
import com.anandhuarjunan.imagetools.utils.RuntimeUtil;

import animatefx.animation.FadeIn;
import animatefx.animation.Tada;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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
	private TreeView<Object> opTreeView;
	
    @FXML
    private TreeView<DirectAdjustments> bOps;
	
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
    
    @FXML
    private Button resetCc;

	private Timer ramTimer = new Timer();

	private AlgorithmTreeDataModel algorithmTreeDataModel = null;

	private AlgorithmsMetadataHelper algorithmsMetadata = new AlgorithmsMetadataHelper();

	private ComplexMatConvertor complexMatConvertor = null;
	
	private OpenCVMatHelper cvMat = null;

	 @FXML
	private ImageView impImageView;
	
 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {	
			
			cvMat = new OpenCVMatHelper(imageView,imageView1);
			chooseFileDirectoryAction();
			loadRAMStatus();

			//Load Ops
			algorithmsMetadata.initializeOperationsView(opTreeView);
			loadAdjustments();
			//----
			
			imageView.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView.fitHeightProperty().bind(hbImage.heightProperty());
			imageView1.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView1.fitHeightProperty().bind(hbImage.heightProperty());
			download.disableProperty().bind(imageView1.imageProperty().isNull());
			processImage.disableProperty().bind(imageView.imageProperty().isNull().or(Bindings.createBooleanBinding(()->{
				TreeItem<Object> treeItem = opTreeView.getSelectionModel().getSelectedItem();
				return treeItem!=null && !treeItem.isLeaf();
			}, opTreeView.getSelectionModel().selectedItemProperty())).or(Bindings.createBooleanBinding(()->{
				TreeItem<DirectAdjustments> treeItem = bOps.getSelectionModel().getSelectedItem();
				return treeItem!=null && treeItem.isLeaf();
			}, bOps.getSelectionModel().selectedItemProperty())));
			impImageView.fitHeightProperty().bind(imageContainer.heightProperty().divide(1.3));
			impImageView.fitWidthProperty().bind(imageContainer.widthProperty().divide(1.3));
	        // Make the toolbar autoscrollable
	        toolBar.prefWidthProperty().bind(imageContainer.widthProperty());
	        toolBar.maxWidthProperty().bind(imageContainer.widthProperty());
	        opTreeView.disableProperty().bind(progressbarIndicator.visibleProperty());

			opTreeView.getSelectionModel().selectedItemProperty().addListener(initToolBarGui()); //Initiate toolbar if the selected operation requires it.

			bOps.disableProperty().bind(imageView.imageProperty().isNull());
			
			opTreeView.disableProperty().bind(imageView.imageProperty().isNull());

			download.setOnAction(downloadImageAction());

			processImage.setOnAction(processImageAction());
			
			resetCc.setOnAction(ev->resetCurretChanges());
			
			download.disableProperty().bind(progressbarIndicator.visibleProperty());
			
			
			imageView.setEffect(new DropShadow());
			imageView1.setEffect(new DropShadow());

			
			removeToolBar();
			

		} catch (Exception e) {
			e.printStackTrace();
			JFXUtil.showInfoAlert("Application Error !", e.getMessage());
		}
	}
	
	
	private void resetCurretChanges() {
		cvMat.resetCurrentState();
		
		if(!opTreeView.getSelectionModel().isEmpty() && null != opTreeView.getSelectionModel().selectedItemProperty().getValue()
				&& opTreeView.getSelectionModel().selectedItemProperty().getValue().isLeaf()) {
			resetCc.disableProperty().unbind();
			resetCc.setDisable(true);
		}else if(!bOps.getSelectionModel().isEmpty() && null != bOps.getSelectionModel().selectedItemProperty().getValue()
				&& bOps.getSelectionModel().selectedItemProperty().getValue().isLeaf()){
			bOps.getSelectionModel().selectedItemProperty().get().getValue().init();
		}
	
	}
	
	
	private void loadAdjustments() {
		TreeItem<DirectAdjustments> rootItem = new TreeItem<>();
        
        rootItem.getChildren().addAll(listOfAdjustments());
        bOps.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
        	if(newValue != null && newValue.isLeaf()) {
        		resetCc.disableProperty().unbind();
        		resetCc.setDisable(false);
				cvMat.swapImagesWithoutMatConversion();
        		opTreeView.getSelectionModel().clearSelection();
        		addToolBar();
            	newValue.getValue().init();
        	}
        });
        bOps.setShowRoot(false);
		bOps.setRoot(rootItem);
		bOps.getSelectionModel().clearSelection();

	}
	
	
	private List<TreeItem<DirectAdjustments>> listOfAdjustments(){
		List<TreeItem<DirectAdjustments>> treeItems = new ArrayList<>();
		TreeItem<DirectAdjustments> basicAdjustment = new TreeItem<>();
        basicAdjustment.setValue(new BrightnessContrastAdjustment(toolBar,cvMat));
        treeItems.add(basicAdjustment);
        
        TreeItem<DirectAdjustments> hue = new TreeItem<>();
        hue.setValue(new HueSaturationAdjustment(toolBar,cvMat));
        treeItems.add(hue);

        return treeItems;
	}



	private ChangeListener<? super TreeItem<Object>> initToolBarGui() {
		return (observable, oldValue, newValue) -> {
			if(null != newValue) {
				bOps.getSelectionModel().clearSelection();
				
				if(newValue.isLeaf() && newValue.getValue() instanceof AlgorithmTreeDataModel) {
					resetCc.disableProperty().unbind();
					resetCc.setDisable(true);
					cvMat.swapImagesWithoutMatConversion();
					
					algorithmTreeDataModel = (AlgorithmTreeDataModel)newValue.getValue();
					if(Constants.COMPLEX_MAT.equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())) {
						try {
							addToolBar();
							Class<?> class1 = Class.forName(algorithmTreeDataModel.getAlgorithmCodePath());
							Constructor<?> constructor = class1.getConstructor(ToolBar.class,OpenCVMatHelper.class);
							complexMatConvertor = (ComplexMatConvertor)constructor.newInstance(toolBar,cvMat);	
							complexMatConvertor.init();
						}catch(Exception e) {
							JFXUtil.showInfoAlert("Could not initialize Operations UI !", e.getMessage());
						}
						
					}else{
						removeToolBar();
					}	
				}else if(newValue.getValue() instanceof Groups) {
					removeToolBar();
				}	
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
				if(bOps.getSelectionModel().getSelectedItem() != null) {
					bOps.getSelectionModel().getSelectedItem().getValue().init();
				}
				cvMat.clear();
				cvMat.setOriginalImageByPath(inputfile);
				fontLoc.setText(inputfile.getName());
				imageView1.setImage(imageView.getImage());
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
								}
								
							}else if(Constants.SIMPLE_MAT.equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())){

								SimpleMatConvertor convertor = (SimpleMatConvertor) Class.forName(algorithmTreeDataModel.getAlgorithmCodePath()).newInstance();
								cvMat.setEffect(convertor.convert(cvMat.getOriginalImage()));
							}
					
					return null;
				}
			};
			task.setOnSucceeded(ev->new FadeIn(imageView1).play());;
			progressbarIndicator.visibleProperty().bind(task.runningProperty());
			prcImageInfo.visibleProperty().bind(task.runningProperty());
			task.setOnFailed(ev->{
				JFXUtil.showInfoAlert("Processing failed !", task.getException().getMessage());			
			});
			resetCc.disableProperty().bind(task.onSucceededProperty().isNull());
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
