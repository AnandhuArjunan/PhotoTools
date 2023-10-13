package com.anandhuarjunan.imagetools.algorithms;

import java.io.File;
import java.util.Objects;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.utils.JFXUtil;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;

public class CopyColorFromPhoto extends ComplexMatConvertor implements ComplxMatArgumentsValidator{

	
	File file = null;
	public CopyColorFromPhoto(ToolBar box) {
		super(box);
	}
	

	@Override
	protected void loadUI(ToolBar box) {
		box.getItems().add(new Label("Copy Color From "));
		TextField field = new TextField();
		box.getItems().add(field);
		Button eventNode = new Button("Browse");
		box.getItems().add(eventNode);
		eventNode.setOnAction(ev->{
			file = JFXUtil.chooseFileDirectoryAction(eventNode);
			if(Objects.nonNull(file)) {
				field.setText(file.getPath());

			}
		});
	}

	@Override
	public Mat convert(Mat input) {
		Mat sourceImage = Imgcodecs.imread(file.getPath());
        Mat targetImage = input;

        // Convert the source image to HSV color space
        Mat sourceColorMap = sourceImage.clone();
        Imgproc.cvtColor(sourceColorMap, sourceColorMap, Imgproc.COLOR_BGR2HSV);

        // Iterate over the pixels of the target image and set the color of each pixel to the corresponding color in the source color map
        for (int i = 0; i < targetImage.rows(); i++) {
            for (int j = 0; j < targetImage.cols(); j++) {
                double[] sourcePixel = sourceColorMap.get(i, j);
                double[] targetPixel = targetImage.get(i, j);

                // Set the color of the target pixel to the corresponding color in the source color map
                targetPixel[0] = sourcePixel[0];
                targetPixel[1] = sourcePixel[1];
                targetPixel[2] = sourcePixel[2];

                // Update the target pixel
                targetImage.put(i, j, targetPixel);
            }
        }

        // Convert the target image back to BGR color space
        Imgproc.cvtColor(targetImage, targetImage, Imgproc.COLOR_HSV2BGR);
		return targetImage;
	}


	@Override
	public boolean validate() {
		
		return false;
	}

}
