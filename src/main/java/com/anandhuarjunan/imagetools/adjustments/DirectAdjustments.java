package com.anandhuarjunan.imagetools.adjustments;



import org.opencv.core.Mat;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.helper.ThreadPoolHelper;

import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;

public abstract class DirectAdjustments {
	
	protected ToolBar box;
	protected OpenCVMatHelper cvMatHelper;

	protected DirectAdjustments(ToolBar box,OpenCVMatHelper cvMatHelper){
		this.box = box;
		this.cvMatHelper = cvMatHelper;
	}
	
	public void init() {
		box.getItems().clear();
		loadUI();	
	}
	
	protected abstract void loadUI();	
	protected abstract String name();	

	@Override
	public String toString() {
		return name();
	}
}
