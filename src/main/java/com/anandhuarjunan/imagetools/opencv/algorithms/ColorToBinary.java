package com.anandhuarjunan.imagetools.opencv.algorithms;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ColorToBinary implements SimpleMatConvertor {

	@Override
	public Mat convert(Mat input) {
		 Mat grayImage = new Mat();
	        Imgproc.cvtColor(input, grayImage, Imgproc.COLOR_BGR2GRAY);

	        // Threshold the grayscale image
	        Mat binaryImage = new Mat();
	        Imgproc.threshold(grayImage, binaryImage, 127, 255, Imgproc.THRESH_BINARY);
		return binaryImage;
	}

}
