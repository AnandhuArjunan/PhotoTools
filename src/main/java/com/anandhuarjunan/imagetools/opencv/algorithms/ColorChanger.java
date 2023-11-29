package com.anandhuarjunan.imagetools.opencv.algorithms;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.anandhuarjunan.imagetools.helper.OpenCVMatHelper;

import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorChanger extends ComplexMatConvertor {
	private  ColorPicker colorPicker = new ColorPicker();

    private ColorPicker colorPicker2 = new ColorPicker();

	public ColorChanger(ToolBar box,OpenCVMatHelper cvMatHelper) {
		super(box,cvMatHelper);
	}

	@Override
	protected void loadUI(ToolBar box) {

        box.getItems().add(new VBox(new Label("Selected Color"),colorPicker));
        box.getItems().add(new VBox(new Label("Change the Color to "),colorPicker2));
        
        cvMatHelper.getSourceView().setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            Image image = cvMatHelper.getSourceView().getImage();
            if (image != null) {
                int x = (int) (mouseX * image.getWidth() / cvMatHelper.getSourceView().getBoundsInLocal().getWidth());
                int y = (int) (mouseY * image.getHeight() / cvMatHelper.getSourceView().getBoundsInLocal().getHeight());

                Color selectedColor = image.getPixelReader().getColor(x, y);
                colorPicker.setValue(selectedColor);
            }
        });
        
        cvMatHelper.getSourceView().setOnMouseEntered(event -> {
            cvMatHelper.getSourceView().setCursor(Cursor.CROSSHAIR);
        });

        cvMatHelper.getSourceView().setOnMouseExited(event -> {
            // Reset the default cursor when the mouse exits the image view
        	cvMatHelper.getSourceView().setCursor(Cursor.DEFAULT);
        });
	}

	@Override
	public Mat convert(Mat input) {
		// Define the hexadecimal color value (for example, hex for red: #FF0000)
		Color color = colorPicker.getValue();
        String hex = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        String hexColor = hex.substring(1);

        // Convert the hexadecimal color to BGR
        int hexValue = Integer.parseInt(hexColor, 16);
        Scalar bgrColor = new Scalar(hexValue & 0xFF, (hexValue >> 8) & 0xFF, (hexValue >> 16) & 0xFF);

        // Define the range for the color to be removed based on the BGR color
        Scalar lowerThreshold = new Scalar(bgrColor.val[0] - 15, bgrColor.val[1] - 15, bgrColor.val[2] - 15);
        Scalar upperThreshold = new Scalar(bgrColor.val[0] + 15, bgrColor.val[1] + 15, bgrColor.val[2] + 15);

        // Create a mask for the specified color range
        Mat mask = new Mat();
        Core.inRange(input, lowerThreshold, upperThreshold, mask);

        // Invert the mask to select pixels outside the specified color range
        Mat invertedMask = new Mat();
        Core.bitwise_not(mask, invertedMask);

        // Create the resulting image by applying the inverted mask
        Mat result = new Mat();
        input.copyTo(result, invertedMask);
        
        // Define the target color in hexadecimal representation (for example, hex for blue: #0000FF)
        
        Color color2 = colorPicker2.getValue();
        String hex2 = String.format("#%02X%02X%02X",
                (int) (color2.getRed() * 255),
                (int) (color2.getGreen() * 255),
                (int) (color2.getBlue() * 255));

        // Convert the target hexadecimal color to BGR
        int targetHexValue = Integer.parseInt(hex2.substring(1), 16);
        Scalar targetBGRColor = new Scalar(targetHexValue & 0xFF, (targetHexValue >> 8) & 0xFF, (targetHexValue >> 16) & 0xFF);
        for (int y = 0; y < result.rows(); y++) {
            for (int x = 0; x < result.cols(); x++) {
                double[] pixel = result.get(y, x);
                if (mask.get(y, x)[0] > 0) {
                    pixel[0] = targetBGRColor.val[0];
                    pixel[1] = targetBGRColor.val[1];
                    pixel[2] = targetBGRColor.val[2];
                    result.put(y, x, pixel);
                }
            }
        }
        return result;
        
	}

}
