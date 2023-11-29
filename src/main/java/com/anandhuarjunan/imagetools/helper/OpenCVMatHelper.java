//This Class handles creating Matrix out a Image and visa versa for Open CV
//This class can be used for any open cv code
package com.anandhuarjunan.imagetools.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import com.anandhuarjunan.imagetools.utils.Quadruple;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenCVMatHelper {
		
    private Mat originalImage;

    private Mat effect;

    private ImageView sourceView = null;

    private ImageView editedView = null;
    
    private List<Quadruple<Mat,Mat,Image,Image>> cache = new ArrayList<>();
    
    public OpenCVMatHelper(ImageView sourceView,ImageView editedView){
    	this.sourceView = sourceView;
    	this.editedView =editedView;
    }

    public void setEffect(Mat newEffect) {
    	Quadruple<Mat,Mat,Image,Image> quadruple = new Quadruple<>(originalImage, effect, sourceView.getImage(), editedView.getImage());
    	if(cache.isEmpty()) {
        	cache.add(quadruple);
    	}
    	
    	this.effect = newEffect;
    	editedView.setImage(getImagePost());
    }

    public void setOriginalImage(Mat originalImage) {
        this.originalImage = originalImage;
        this.sourceView.setImage(getImageOriginal());
    }

    public void setOriginalImageFromFxImage(Image originalImage) {
        this.originalImage = imageToMatrix(originalImage);
        this.sourceView.setImage(originalImage);

    }

    public void setOriginalImageByPath(File originalImagePath) {

        this.originalImage = Imgcodecs.imread(originalImagePath.getPath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        this.sourceView.setImage(getImageOriginal());

    }

    public void swapImages() {
    	if(null != getEffect()) {
			setOriginalImage(getEffect());
		}
    }

    public void swapImagesWithoutMatConversion() {
    	if(null != getEffect() && !effect.equals(originalImage)) {
    		 this.originalImage = getEffect();
    		 sourceView.setImage(editedView.getImage());
		}
		
    }

    public void clear() {
    	originalImage = null;
    	effect = null;
    	sourceView.setImage(null);
    	editedView.setImage(null);
    	cache.clear();
    }
    
    public void clearCurrentContext() {
    	cache.clear();
    }
    
    public void resetCurrentState() {
    	if(!cache.isEmpty()) {
        	applyState(cache.get(0));
    	}
    	clearCurrentContext();
    }

    //Return the original Matrix of the Image
    public Mat getOriginalImage() {
        return originalImage;
    }

    //Return the Matrix after the effect
    public Mat getEffect() {
        return effect;
    }

    //Get the Original Image before the Effect
    public Image getImageOriginal() {
        try {
            return matToImage(originalImage);
        } catch (Exception e) {
            return null;
        }
    }

    //Get the Buffered Image After the Effect
    public Image getImagePost() {
        try {
            // getImageOriginal();
            return matToImage(effect);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
    public void applyState(Quadruple<Mat,Mat,Image,Image> quadruple) {
    	this.originalImage = quadruple.getFirst();
    	this.effect = quadruple.getSecond();
    	this.sourceView.setImage(quadruple.getThird());
    	this.editedView.setImage(quadruple.getFourth());
    }

    //JavaFX image to a Matrix
    public Mat imageToMatrix(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        //Reads every pixel from image and converts to matrix
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = PixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        //CV 4 channel
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;

    }

    public Image mat2Image(Mat frame) {
        try {
            return matToImage(frame);
        } catch (Exception e) {
            System.err.println("Cannot convert the Mat object:" + e);
            return null;
        }
    }

    public Mat imageToMatrix(Image image, int t) {
    	try {
			BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
			return bufferedImage2Mat_v2(bi);
		} catch (Exception e) {
			System.err.println("Cannot convert the Mat object: " + e);
			return null;
		}
    }

    //Matrix to JavaFX image
    public Image matToImage(Mat matrix) {
        return  SwingFXUtils.toFXImage(matToBufferedImage(matrix), null);
      }
    

    //Matrix to Buffered Image
    public BufferedImage matToBufferedImage(Mat original) {
        java.awt.image.BufferedImage image = null;
        int width = original.width();
        int height = original.height();
        int channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }

        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        return image;
    }

    //Buffered Image to Matrix
    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        System.out.println(bi.getType());
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;

    }
    
    public static Mat bufferedImage2Mat_v2(BufferedImage im) {

        im = toBufferedImageOfType(im, BufferedImage.TYPE_3BYTE_BGR);

        // Convert INT to BYTE
        //im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        // Convert bufferedimage to byte array
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();

        // Create a Matrix the same size of image
        Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;

    }

    private static BufferedImage toBufferedImageOfType(BufferedImage original, int type) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() == type) {
            return original;
        }

        // Create a buffered image
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), type);

        // Draw the image onto the new buffer
        Graphics2D g = image.createGraphics();
        try {
            g.setComposite(AlphaComposite.Src);
            g.drawImage(original, 0, 0, null);
        }
        finally {
            g.dispose();
        }

        return image;
    }

	public ImageView getSourceView() {
		return sourceView;
	}


	public ImageView getEditedView() {
		return editedView;
	}


    
}

