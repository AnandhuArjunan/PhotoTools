package com.anandhuarjunan.imagetools.opencv.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.utils.JFXUtil;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;

public class CopyColorFromPhoto extends ComplexMatConvertor implements ComplxMatArgumentsValidator{

	
	File file = null;
	public CopyColorFromPhoto(ToolBar box, OpenCVMatHelper cvMatHelper) {
		super(box, cvMatHelper);
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
        
        return null;

    }

   
	@Override
	public boolean validate() {
		
		return true;
	}

}
