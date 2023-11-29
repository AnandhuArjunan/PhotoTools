package com.anandhuarjunan.imagetools.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class JFXUtil {
	public static String toHexString(Color color) {
		return String.format( "#%02X%02X%02X",
	            (int)( color.getRed() * 255 ),
	            (int)( color.getGreen() * 255 ),
	            (int)( color.getBlue() * 255 ) );
		}
	
	
	
	public static void showInfoAlert(String title , String content) {
		Platform.runLater(()->{
			Alert a = new Alert(AlertType.NONE);
			a.setTitle(title);
			a.setContentText(content);
			a.setAlertType(AlertType.INFORMATION);
			a.show();
		});
		
	}
	
	 public static void downloadImageWithLocationChooser(ImageView imageView) throws IOException {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Select a location to save the image");
	        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

	        File file = fileChooser.showSaveDialog(null);
	        if (file != null) {
	            downloadImage(imageView, file);
	        }
	    }
	 
	 public static void downloadImage(ImageView imageView, File file) throws IOException {
	        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
	        ImageIO.write(bufferedImage, "png", file);
	 }
	 
	 public static File chooseFileDirectoryAction(Button eventNode) {
			FileChooser fileChooser = new FileChooser();
			return fileChooser.showOpenDialog(eventNode.getScene().getWindow());
		}
	



}
