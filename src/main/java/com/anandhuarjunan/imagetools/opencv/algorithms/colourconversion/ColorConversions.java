package com.anandhuarjunan.imagetools.opencv.algorithms.colourconversion;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.opencv.algorithms.ComplexMatConvertor;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class ColorConversions extends ComplexMatConvertor{

	private ChoiceBox<String> choiceBox = new ChoiceBox<String>();
	Map<String,Integer> map = new HashMap<>();
	public ColorConversions(ToolBar box,OpenCVMatHelper imgProcConst) {
		super(box,imgProcConst);
	}

	@Override
	protected void loadUI(ToolBar box) {
		box.getItems().add(new Label("Colour Spaces : "));
	    box.getItems().add(choiceBox);
	    choiceBox.setValue("Grayscale");
	    addDiffColorSpaces("Grayscale", Imgproc.COLOR_BGR2GRAY);
	    addDiffColorSpaces("HSL", Imgproc.COLOR_RGB2HSV);
	}
	

	@Override
	public Mat convert(Mat input) {
		String str = choiceBox.getSelectionModel().getSelectedItem();
		Mat finalIm = new Mat();
	   Imgproc.cvtColor(input, finalIm, map.get(str));
	    
		return finalIm;
	}
	
	void addDiffColorSpaces(String name,Integer imgProcConst){
		choiceBox.getItems().add(name);
		map.put(name,imgProcConst);
	}

}
