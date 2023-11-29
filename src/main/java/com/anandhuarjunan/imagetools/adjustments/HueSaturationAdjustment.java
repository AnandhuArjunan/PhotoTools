package com.anandhuarjunan.imagetools.adjustments;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;
import com.anandhuarjunan.imagetools.helper.ThreadPoolHelper;

import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
/*
 * Brightness , Contrast,Saturation,Hue
 */
public class HueSaturationAdjustment extends MixedSingleSliderAdjustment {

	public HueSaturationAdjustment(ToolBar box,OpenCVMatHelper cvMatHelper) {
		super(box,cvMatHelper);
	}
	   public static Mat adjustSaturation(Mat image, double saturationValue) {

		    Mat hsvImage = new Mat();
	        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

	        // Split the HSV image into its channels
	        List<Mat> hsvChannels = new ArrayList<>();
	        Core.split(hsvImage, hsvChannels);

	        // Scale the saturation channel to adjust saturation
	        Mat saturationChannel = hsvChannels.get(1); // Saturation channel is at index 1
	        saturationChannel.convertTo(saturationChannel, CvType.CV_32F);
	        Core.multiply(saturationChannel, new Scalar(saturationValue), saturationChannel);

	        // Clip values between 0 and 255
	        Core.MinMaxLocResult minMax = Core.minMaxLoc(saturationChannel);
	        double maxVal = minMax.maxVal;
	        if (maxVal > 255) {
	            Core.divide(saturationChannel, new Scalar(maxVal / 255), saturationChannel);
	        }

	        saturationChannel.convertTo(saturationChannel, CvType.CV_8U);
	        hsvChannels.set(1, saturationChannel);

	        // Merge the adjusted HSV channels
	        Core.merge(hsvChannels, hsvImage);

	        // Convert the HSV image back to BGR color space
	        Mat outputImage = new Mat();
	        Imgproc.cvtColor(hsvImage, outputImage, Imgproc.COLOR_HSV2BGR);
	        return outputImage;
	        
	    }
	   
	   public static Mat adjustHue(Mat image,double hue) {
		   Mat hsvImage = new Mat();
	        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

	        // Modify the hue value (e.g., adding 50 to the hue)
	        Scalar scalar = new Scalar(hue, 0, 0); // Modify the hue by 50 degrees
	        Core.add(hsvImage, scalar, hsvImage);

	        // Convert the modified image back to BGR color space
	        Mat modifiedImage = new Mat();
	        Imgproc.cvtColor(hsvImage, modifiedImage, Imgproc.COLOR_HSV2BGR);
			return modifiedImage;

	   }
	   
	  
	


	@Override
	protected String name() {
		return "Hue and Saturation";
	}

	@Override
	protected List<Pair<String,Slider>> sliders() {
		 Slider satura = new Slider();
		 satura.setValue(0);
		 satura.setMax(255);
		 satura.setMin(-255);
		 satura.setMajorTickUnit(1);
		 satura.setShowTickMarks(true);
		 satura.setShowTickLabels(true);
	        
	        Slider hue = new Slider();
	        hue.setValue(0);
	        hue.setMax(179);
	        hue.setMin(0);
	        hue.setMajorTickUnit(1);
	        hue.setShowTickMarks(true);
	        hue.setShowTickLabels(true);
	        
	        List<Pair<String,Slider>> sliders = new ArrayList<>();
	        sliders.add(new Pair<String, Slider>("Saturation", satura));
	        sliders.add(new Pair<String, Slider>("Hue", hue));

	        return sliders;
	        
		}
	


	@Override
	protected List<BiFunction<Mat, Double, Mat>> processes() {
		List<BiFunction<Mat, Double, Mat>> biFunctions = new ArrayList<BiFunction<Mat,Double,Mat>>();
		biFunctions.add(HueSaturationAdjustment::adjustSaturation);
		biFunctions.add(HueSaturationAdjustment::adjustHue);
		return biFunctions;
		
	}



	
	
}
