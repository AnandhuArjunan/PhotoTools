package com.anandhuarjunan.imagetools.adjustments;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.opencv.core.Mat;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 * For operations that requires only one slider adjustment.ex : brightness operation requires only a slider.
 */
public abstract class MixedSingleSliderAdjustment extends DirectAdjustments{

	public MixedSingleSliderAdjustment(ToolBar box, OpenCVMatHelper cvMatHelper) {
		super(box, cvMatHelper);
	}

	@Override
	protected void loadUI() {
		List<Pair<String, Slider>> sliders = sliders();
		
		sliders.forEach(pr->{
			 box.getItems().add(new VBox(new Label(pr.getKey()),pr.getValue()));
		});
		
		List<BiFunction<Mat, Double, Mat>> processes= processes();
		sliders.forEach(sl->{
			sl.getValue().valueProperty().addListener((observable, oldValue, newValue) -> {
				
				Pair<BiFunction<Mat, Double, Mat>, Double> pair = null;
				List<Pair<BiFunction<Mat, Double, Mat>, Double>> list = new ArrayList<Pair<BiFunction<Mat,Double,Mat>,Double>>();
				
				List<Double> sliderVal = sliders.stream().map(Pair::getValue).map(Slider::getValue).collect(Collectors.toList());
				for(int i=0;i<sliderVal.size();i++) {
					pair = new Pair<>(processes.get(i), sliderVal.get(i));
					list.add(pair);
				}
				
				cvMatHelper.setEffect(chainBiFunctions(cvMatHelper.getOriginalImage(),list));
			});

		});
		
		
	}

	protected abstract List<Pair<String, Slider>> sliders();
	
	protected abstract List<BiFunction<Mat, Double, Mat>> processes();
	
	 @SuppressWarnings("unused")
	private static Mat chainBiFunctions(Mat inputImage, List<Pair<BiFunction<Mat, Double, Mat>, Double>> operations) {
	        Mat resultImage = inputImage.clone();
	        for (Pair<BiFunction<Mat, Double, Mat>, Double> operation : operations) {
	            resultImage = operation.getKey().apply(resultImage, operation.getValue());
	        }
	        return resultImage;
	    }
	
	
}
