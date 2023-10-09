package com.anandhuarjunan.imagetools;


import org.opencv.core.Core;

import com.anandhuarjunan.imagetools.controller.MainController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nu.pattern.OpenCV;
public class Main extends Application {

@Override
public void start(Stage stage) throws Exception {
	OpenCV.loadShared();
	//Application.setUserAgentStylesheet(getClass().getResource("/theme/app.css").toString());
	
	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
	Parent root = loader.load();
	MainController controller = loader.getController();
	final Scene scene = new Scene(root);
	stage.initStyle(StageStyle.DECORATED);
	stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/icon.png")));
	stage.setTitle("PhotoTools");
	stage.setOnHidden(e->{
		controller.shutdown();
		Platform.exit();
	});
	stage.setScene(scene);
	stage.show();
}

public static void main(String[] args) {
	launch(args);
	
	
}
}
