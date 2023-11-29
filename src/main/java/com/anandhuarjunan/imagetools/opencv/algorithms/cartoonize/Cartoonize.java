package com.anandhuarjunan.imagetools.opencv.algorithms.cartoonize;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.opencv.algorithms.SimpleMatConvertor;


public class Cartoonize implements SimpleMatConvertor {

	@Override
	public Mat convert(Mat input) {
	/*
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
		
		*/
		
		
		
		  Mat img = new Mat();
	        Imgproc.cvtColor(input, img, Imgproc.COLOR_BGR2BGRA);

	        int width = input.width();
	        int height = input.height();

	        Mat blurred = new Mat();
	        Imgproc.blur(img, blurred, new Size(40, 40));

	        Mat mask = new Mat(blurred.rows(), blurred.cols(), CvType.CV_8U, new Scalar(0, 0, 0));
	        Point center = new Point(width / 2., height / 2.);
	        int radius = width / 6;
	        Imgproc.circle(mask, center, radius, new Scalar(255,255,255), -1, 8, 0);
	        Mat masked = new Mat(blurred.rows(), blurred.cols(), CvType.CV_8U);
	        blurred.copyTo(masked, mask);
	        Mat thresh = new Mat();
	        Imgproc.threshold(mask, thresh, 1, 255, Imgproc.THRESH_BINARY);
	        List<MatOfPoint> contours = new ArrayList<>();
	        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	        Rect rect = Imgproc.boundingRect(contours.get(0));
	        Mat blurCircle = masked.submat(rect);

	        Mat bigBlurCircle = new Mat(img.rows(), img.cols(), CvType.CV_8UC4);
	        Rect roi = new Rect((width / 3) + 1, (height / 3) + 4, blurCircle.width(), blurCircle.height());
	        blurCircle.copyTo(new Mat(bigBlurCircle, roi));

	        Mat mask2 = new Mat(img.rows(), img.cols(), CvType.CV_8U, new Scalar(0, 0, 0));
	        Imgproc.circle(mask2, center, radius, new Scalar(255,255,255), -1, 8, 0);
	        Mat masked2 = new Mat(img.rows(), img.cols(), CvType.CV_8U);
	        Mat res = new Mat(img.rows(), img.cols(), CvType.CV_8UC4);
	        Core.bitwise_not(mask2, res);
	        img.copyTo(masked2, res);
	        Mat thresh2 = new Mat();
	        Imgproc.threshold(res, thresh2, 1, 255, Imgproc.THRESH_BINARY);
	        List<MatOfPoint> contours2 = new ArrayList<>();
	        Imgproc.findContours(thresh2, contours2, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	        Rect rect2 = Imgproc.boundingRect(contours2.get(0));
	        Mat imageWithoutCircle = masked2.submat(rect2);

	        Mat result = new Mat(img.rows(), img.cols(), CvType.CV_8UC4);
	        Core.addWeighted(imageWithoutCircle, 1, bigBlurCircle, 1, 0, result);

	        return result;
		

	}
	
}
