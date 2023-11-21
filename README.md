
# PhotoTools - *A simple tool for image processing that uses javafx and opencv.*
It consists of different operations to apply effects , enhance images , change colour tone .etc.
 

## Screenshots

<img src="screenshots/Screenshot 2023-10-14 145942.png" alt="" width="600"/>
<img src="screenshots/Screenshot 2023-10-14 150053.png" alt="" width="600"/>


## Currently implemented Image Processors
- Basic Operations
    - Negative to Positive Converter.
    - Change colorspace to HSV , Grayscale.
    - Change Color Map to OpenCvs inbuilt presents [HOT , AUTUMN , BONE , COOL , RAINBOW , HSV , JET , OCEAN , PARULA , PINK , SPRING , SUMMER , WINTER].
    - Copy color map from one image and apply to another. [Not Working]
- Image Enhancements
    - Optimized Contrast Enhancement.  [from https://github.com/26hzhang/OptimizedImageEnhance]
    - Single Image Haze Removal Using Dark Channel Prior. [from https://github.com/26hzhang/OptimizedImageEnhance]
    - Under Water Image Enhancement.  [from https://github.com/26hzhang/OptimizedImageEnhance]
  



## Requirements

- Java 8
- Maven
  
## Dependencies 

- Opencv 3.4.2
- Apache commons CSV  v1.10.0
- Apache Commons Lang3 v3.13.0
- Javafx CSS Theme from https://github.com/Col-E/FxThemes
- Font from https://www.fontsquirrel.com/fonts/josefin-sans
- App Icon from icon8.com
- Other Icons - Fontawesomefx [https://bitbucket.org/Jerady/fontawesomefx]
- <a href="https://www.freepik.com/free-vector/images-concept-illustration_5357829.htm#query=image%20flat&position=28&from_view=search&track=ais">Image by storyset</a> on Freepik
- Animations - animateFx [https://github.com/Typhon0/AnimateFX]

## Build
Get the executable jar in target folder

    mvn clean install
    
## Run
    
    java -jar PhotoTools-<version>.jar
