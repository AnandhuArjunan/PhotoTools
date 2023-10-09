package com.anandhuarjunan.imagetools.utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class OpenCvAlgorithms {
	
	public static Mat convertNegativeToPositiveOrViceVersa(Mat inputImage) {
		Mat out = new Mat();
		inputImage.convertTo(out, CvType.CV_8UC3, -1, 255);
		return out;
	}
	
	
	
}
