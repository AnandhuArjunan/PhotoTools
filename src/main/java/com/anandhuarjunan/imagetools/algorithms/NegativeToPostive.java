package com.anandhuarjunan.imagetools.algorithms;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class NegativeToPostive implements SimpleMatConvertor {

	@Override
	public Mat convert(Mat input) {
		Mat out = new Mat();
		input.convertTo(out, CvType.CV_8UC3, -1, 255);
		return out;
	}

}
