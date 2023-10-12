package com.anandhuarjunan.imagetools.algorithms;

import org.opencv.core.Mat;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;

public abstract class ComplexMatConvertor {
	
	private ToolBar box;

	public ComplexMatConvertor(ToolBar box){
		this.box = box;
	};
	
	public void init() {
		box.getItems().clear();
		loadUI(box);
	}
	
	protected abstract void loadUI(ToolBar box);
	public abstract Mat convert(Mat input);
	
	
}
