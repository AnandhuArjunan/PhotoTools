package com.anandhuarjunan.imagetools.algorithms.colourconversion;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.algorithms.ComplexMatConvertor;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class ColorSpaces extends ComplexMatConvertor{
	private ChoiceBox<String> choiceBox = new ChoiceBox<String>();
	Map<String,Integer> map = new HashMap<>();
	public ColorSpaces(ToolBar box) {
		super(box);
	}
	@Override
	protected void loadUI(ToolBar box) {
		box.getItems().add(new Label("Colour Spaces : "));
	    box.getItems().add(choiceBox);
	    choiceBox.setValue("HOT");

	    addDiffColorMaps("HOT",Imgproc.COLORMAP_HOT);
	    addDiffColorMaps("AUTUMN",Imgproc.COLORMAP_AUTUMN);
	    addDiffColorMaps("BONE",Imgproc.COLORMAP_BONE);
	    addDiffColorMaps("COOL",Imgproc.COLORMAP_COOL);
	    addDiffColorMaps("RAINBOW",Imgproc.COLORMAP_RAINBOW);
	    addDiffColorMaps("HSV",Imgproc.COLORMAP_HSV);
	    addDiffColorMaps("JET",Imgproc.COLORMAP_JET);
	    addDiffColorMaps("OCEAN",Imgproc.COLORMAP_OCEAN);
	    addDiffColorMaps("PARULA",Imgproc.COLORMAP_PARULA);
	    addDiffColorMaps("PINK",Imgproc.COLORMAP_PINK);
	    addDiffColorMaps("SPRING",Imgproc.COLORMAP_SPRING);
	    addDiffColorMaps("SUMMER",Imgproc.COLORMAP_SUMMER);
	    addDiffColorMaps("WINTER",Imgproc.COLORMAP_WINTER);
		
	}
	@Override
	public Mat convert(Mat input) {
		String str = choiceBox.getSelectionModel().getSelectedItem();
		Mat finalIm = new Mat();
		Imgproc.applyColorMap(input, finalIm, map.get(str));
		return finalIm;
	}
	void addDiffColorMaps(String name,Integer imgProcConst){
		choiceBox.getItems().add(name);
		map.put(name,imgProcConst);
	}
}
