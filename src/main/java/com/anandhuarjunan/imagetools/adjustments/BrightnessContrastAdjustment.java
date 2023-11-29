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
public class BrightnessContrastAdjustment extends MixedSingleSliderAdjustment {

	public BrightnessContrastAdjustment(ToolBar box,OpenCVMatHelper cvMatHelper) {
		super(box,cvMatHelper);
	}
	   public static Mat adjustBrightness(Mat image, double brightnessChange) {

	        Mat dst = new Mat(image.size(), image.type());

	        
	        Scalar scalar;
	        if (brightnessChange >= 0) {
	            scalar = new Scalar(brightnessChange, brightnessChange, brightnessChange);
	            Core.add(image, scalar, dst);
	        } else {
	            scalar = new Scalar(-brightnessChange, -brightnessChange, -brightnessChange);
	            Core.subtract(image, scalar, dst);
	        }


	        return dst;
	    }
	   
	   public static Mat adjustContrast(Mat image,double contrast) {
	       Mat mat = new Mat();
	        // Convert the image to a float type to perform arithmetic operations
	        image.convertTo(mat, -1, contrast, 0);

return mat;

	   }
	   
	  
	


	@Override
	protected String name() {
		return "Brightness and Contrast";
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
	        
	        Slider contrast = new Slider();
	        contrast.setValue(1);
	        contrast.setMax(2);
	        contrast.setMin(0);
	        contrast.setMajorTickUnit(0.1);
	        contrast.setShowTickMarks(true);
	        contrast.setShowTickLabels(true);
	        
	        List<Pair<String,Slider>> sliders = new ArrayList<>();
	        sliders.add(new Pair<String, Slider>("Brightness", satura));
	        sliders.add(new Pair<String, Slider>("Contrast", contrast));

	        return sliders;
	        
		}
	


	@Override
	protected List<BiFunction<Mat, Double, Mat>> processes() {
		List<BiFunction<Mat, Double, Mat>> biFunctions = new ArrayList<BiFunction<Mat,Double,Mat>>();
		biFunctions.add(BrightnessContrastAdjustment::adjustBrightness);
		biFunctions.add(BrightnessContrastAdjustment::adjustContrast);
		return biFunctions;
		
	}



	
	
}
