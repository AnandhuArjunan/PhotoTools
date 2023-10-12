package com.anandhuarjunan.imagetools.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.algorithms.ComplexMatConvertor;
import com.anandhuarjunan.imagetools.algorithms.SimpleMatConvertor;
import com.anandhuarjunan.imagetools.helper.AlgorithmsMetadataHelper;
import com.anandhuarjunan.imagetools.model.AlgorithmTreeDataModel;
import com.anandhuarjunan.imagetools.utils.BasicEffects;
import com.anandhuarjunan.imagetools.utils.JFXUtil;
import com.anandhuarjunan.imagetools.utils.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.utils.RuntimeUtil;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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

	private Timer ramTimer = new Timer();

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	private double[] basicEffectDoubles;

	private AlgorithmTreeDataModel algorithmTreeDataModel = null;

	private AlgorithmsMetadataHelper algorithmsMetadata = new AlgorithmsMetadataHelper();

	public MainController() {
		basicEffectDoubles = new double[4];
	}
	ComplexMatConvertor complexMatConvertor = null;
	
 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {			
			processImage.setDisable(true);
			chooseFileDirectoryAction();
			loadRAMStatus();
			imageView.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView.fitHeightProperty().bind(hbImage.heightProperty());
			imageView1.fitWidthProperty().bind(hbImage.widthProperty().divide(2));
			imageView1.fitHeightProperty().bind(hbImage.heightProperty());
			algorithmsMetadata.initializeOperationsView(opTreeView);
			removeToolBar();

	        // Make the toolbar autoscrollable
	        toolBar.prefWidthProperty().bind(imageContainer.widthProperty());
	        toolBar.maxWidthProperty().bind(imageContainer.widthProperty());

			opTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				algorithmTreeDataModel = newValue.getValue();
				if("COMPLEX".equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())) {
					try {
						addToolBar();
						Class<?> class1 = Class.forName(algorithmTreeDataModel.getAlgorithmCodePath());
						Constructor<?> constructor = class1.getConstructor(ToolBar.class);
						complexMatConvertor = (ComplexMatConvertor)constructor.newInstance(toolBar);	
						complexMatConvertor.init();
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}else{
					removeToolBar();
				}
				
				processImageButtonActions();
			});

			URL is = MainController.class.getResource("/icons/loading.gif");

			download.setOnAction(e -> {
				try {
					JFXUtil.downloadImageWithLocationChooser(imageView1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			processImage.setOnMouseClicked(e -> {

				executorService.execute(() -> {
					try {
						imageView1.setImage(new Image(is.toURI().toString()));
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
					OpenCVMatHelper cvMat = new OpenCVMatHelper();

					cvMat.setOriginalImageByPath(inputfile);

						try {
							if("COMPLEX".equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())) {
								cvMat.setEffect(complexMatConvertor.convert(cvMat.getOriginalImage()));
								imageView1.setImage(cvMat.getImagePost());
							}else if("SIMPLE".equalsIgnoreCase(algorithmTreeDataModel.getAlgorithmCodeComplex())){

								SimpleMatConvertor convertor = (SimpleMatConvertor) Class.forName(algorithmTreeDataModel.getAlgorithmCodePath()).newInstance();
								cvMat.setEffect(convertor.convert(cvMat.getOriginalImage()));
								imageView1.setImage(cvMat.getImagePost());
							}else {
								processImage.setDisable(true);
							}
							
							
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
							e1.printStackTrace();
						}

						

				});

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processImageButtonActions() {
		if(null != inputfile && null != algorithmTreeDataModel && StringUtils.isNotEmpty(algorithmTreeDataModel.getAlgorithmCodePath())) {
			processImage.setDisable(false);
		}else {
			processImage.setDisable(true);
		}
	}

	private void sliderAction(Slider... sliders) {
		for (Slider slider : sliders) {
			slider.setValue(0);
			slider.setMax(1);
			slider.setMin(-1);
			slider.setMajorTickUnit(0.01);
			// slider.setMinorTickCount(0.01);
			slider.setShowTickMarks(true);
			slider.setShowTickLabels(true);
			slider.valueProperty().addListener((observable, oldValue, newValue) -> {
				basicEffectDoubles[(Integer) slider.getUserData()] = newValue.doubleValue();
				BasicEffects basicEffects = new BasicEffects(basicEffectDoubles[0], basicEffectDoubles[1],
						basicEffectDoubles[2], basicEffectDoubles[3]);
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

				Platform.runLater(() -> {
					ramProgressBar.setProgress(percentage);
					memAvlStatus.setText("Used / Maximum - " + usedInMB + "/" + maximumInMB);

				});
			}
		};

		ramTimer.schedule(task, 0l, 1000l);
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
				processImageButtonActions();
			}
		});

	}
	
	void addToolBar() {
		if(!imageContainer.getChildren().contains(toolBar)) {
		
			imageContainer.getChildren().add(0, toolBar);
			 
		}
		
	}
	void removeToolBar() {
		toolBar.getItems().clear();
	
		imageContainer.getChildren().remove(toolBar);
	      
	}
	
}