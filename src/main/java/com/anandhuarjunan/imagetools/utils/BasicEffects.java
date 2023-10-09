package com.anandhuarjunan.imagetools.utils;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class BasicEffects {
    private double contrast, hue, brightness, saturation;

    //Constructor
    public BasicEffects(double contrast, double hue, double brightness, double saturation) {
        this.contrast = contrast;
        this.hue = hue;
        this.brightness = brightness;
        this.saturation = saturation;
    }

    //ChangeEffects
    public void changeEffect(ImageView imageView) {
        //Set the contrast huhe brightness and saturation
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(contrast);
        colorAdjust.setHue(hue);
        colorAdjust.setBrightness(brightness);
        colorAdjust.setSaturation(saturation);
        imageView.setEffect(colorAdjust);
    }
}
