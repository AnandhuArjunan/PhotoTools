package com.anandhuarjunan.imagetools.algorithms;

import org.opencv.core.Mat;

import com.anandhuarjunan.imagetools.utils.JFXUtil;

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
