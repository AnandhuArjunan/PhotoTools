package com.anandhuarjunan.imagetools.algorithms.cartoonize;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.algorithms.SimpleMatConvertor;

public class Cartoonize implements SimpleMatConvertor {

	@Override
	public Mat convert(Mat input) {
	
		 // Convert to grayscale
		Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);

        // Apply edge detection
        Mat edges = new Mat();
        Imgproc.Canny(input, edges, 50, 150);

        // Apply color quantization
        Mat quantized = new Mat();
        Imgproc.cvtColor(edges, quantized, Imgproc.COLOR_GRAY2BGR);
        Imgproc.pyrMeanShiftFiltering(quantized, quantized, 21, 51);

        // Apply thresholding
        Mat cartoonized = new Mat();
        Imgproc.threshold(quantized, cartoonized, 60, 255, Imgproc.THRESH_BINARY);
		return cartoonized;
	}

}
