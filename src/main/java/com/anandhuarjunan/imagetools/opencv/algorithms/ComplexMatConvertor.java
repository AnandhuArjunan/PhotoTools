package com.anandhuarjunan.imagetools.opencv.algorithms;

import org.opencv.core.Mat;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.utils.JFXUtil;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;

public abstract class ComplexMatConvertor {
	
	private ToolBar box;
	protected OpenCVMatHelper cvMatHelper;

	protected ComplexMatConvertor(ToolBar box,OpenCVMatHelper cvMatHelper){
		this.box = box;
		this.cvMatHelper = cvMatHelper;
	}
	
	public void init() {
		box.getItems().clear();
		loadUI(box);
	}
	
	protected abstract void loadUI(ToolBar box);
	public abstract Mat convert(Mat input);
	
	public Mat validateAndConvert(Mat input) {
		if(this instanceof ComplxMatArgumentsValidator) {
			ComplxMatArgumentsValidator argumentsValidator = (ComplxMatArgumentsValidator) this;
			if(!argumentsValidator.validate()) {
				JFXUtil.showInfoAlert("Input Validation Failed", "Some fields are mandatory to input !");
			}else {
				return convert(input);
			}
		}else {
			return convert(input);
		}
		return input;
	}
	
	
}
