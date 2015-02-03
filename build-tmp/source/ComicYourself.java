import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import java.awt.*; 
import controlP5.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ComicYourself extends PApplet {


// group members:    Thomas Cassady & Jason Huang
// class:  	CSC 690
// project: Final
// date:    11/13/2014
// file:	ComicYourself.pde


//__________________________________________________________________________________________________________________________






//__________________________________________________________________________________________________________________________
Capture webcam;
ArrayList <PImage> Photos;
ArrayList <PImage> Panels;
PImage [] stockBackground;
boolean changeBackground = false;
int numPhotos = 0;
int numPanels = 0;
int currentPanelIndex = 0;
int currentPhotoIndex = 0;
int tintValue = 255;
int mode = 0;
int phase = 1;
PImage frame, mode2Capture, mode2Calibration, calibratedFrame, editedFrame, exportedComic;
PFont font;
ControlP5 cp5;
boolean displayButtons = true;
boolean displayExportedComic = false;
PFont buttonFont;
Minim minim;
AudioPlayer Snap, Click;

//mode 4 variables:
int paint = color(0);
int strokeWt = 1;
int flag = 0;
PImage editPhoto;
boolean displayPhoto = true;
ColorPicker cp;
ColorPicker cp2;
PFont smallFont;
float resizeValue = 100;

//Jason edits for mode 2
boolean removeBackground = false;
int threshold = 70, hueThreshold = 70, satThreshold = 70, brtThreshold = 70;

// Thom's variables for Milestone 3
ArrayList <Integer> Layers;
ArrayList <Integer> LayersX;
ArrayList <Integer> LayersY;
int numLayers = 0;
ArrayList <Integer> PanelSizes;
int numHalfPanels = 0;
int halfX = (width - 800)/2;
int halfY = 70;
int rectX1 = 0;
int rectY1 = 0;
int rectX2 = 0;
int rectY2 = 0;
String textBubble = "";
int cropX1 = 0;
int cropY1 = 0;
int cropX2 = 0;
int cropY2 = 0;

// Variables for Mode 7: Flickr Search
String flickrSearchQuery = "";
ArrayList <PImage> flickrPhotoList;
JSONObject json;
int flickrPhotoIndex;
int backgroundIndex;
boolean useCustomBackground = false;
boolean inHSBmode = false;
int backgroundColor = 0xff464646;
CColor backButtonColor = new CColor(0xff9B1919, 0xffD86262, 0xffFFFFFF, 0xffFFFFFF, 0xffFFFFFF);
int displayIndex = 0;
int displayPhotoIndex = 0;

//__________________________________________________________________________________________________________________________
public void setup()
{
	size(1080, 720);
	background(255);

	buttonFont = loadFont("CordiaNew-Bold-30.vlw");
    smallFont = loadFont("Calibri-18.vlw");
  	textFont(buttonFont);
	webcam = new Capture(this, 640, 480);
	webcam.start();

	displayStartButton();
	Photos = new ArrayList <PImage> ();
	Panels = new ArrayList <PImage> ();
	Layers = new ArrayList <Integer> ();
	LayersX = new ArrayList <Integer> ();
	LayersY = new ArrayList <Integer> ();
	PanelSizes = new ArrayList <Integer> ();

	//added by Jason
	mode2Calibration = webcam.get();

	minim = new Minim(this);
	Snap = minim.loadFile("snap.wav");
	Click = minim.loadFile("click.wav");
	// sound used is from freesound.org
  	//   https://www.freesound.org/people/stijn/sounds/43680/
  	//   https://www.freesound.org/people/Snapper4298/sounds/178186/

  	// added in mode 7
  	flickrPhotoList = new ArrayList <PImage> ();
}


//__________________________________________________________________________________________________________________________
public File[] listFiles(String directory)
{
	File file = new File(directory);

	if(file.isDirectory())
	{
    	File[] files = file.listFiles();
    	return files;
	}
	else
    	return null;
}


//__________________________________________________________________________________________________________________________
public void draw()
{
	if(mode == 0)
	{
		// START SCREEN mode
		background(255);
		drawStartScreen();
	}
	else if(mode == 1)
	{
		// OVERVIEW mode
		if(phase == 1)
			drawOverview();
		else if(phase == 2)
			mode1phase2display();
	}
	else if(mode == 2)
	{
		//background(255);
		background(backgroundColor);
		// TAKE A PHOTO mode
		if(phase == 1)
		{
			// show live feed
			drawCam();
			mode2phase1Buttons();
		}
		else if(phase == 2)
		{
			// show picture taken as freeze frame
			textFont(font);
	       	text("Do you want to keep this picture?", 20, 40);
			displayPhoto(numPhotos - 1);
			//println("numPhotos: "+numPhotos);
			mode2phase2Buttons();
		}
		else if(phase == 3)
		{
			calibrationPhase();
			mode2phase3buttons();
		}
		else if(phase == 4)
			mode2phase4display();

	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode
		//background(255);
		background(backgroundColor);
		if(phase == 1)
		{
			// show list of taken photos
			mode3displayPhotos(displayIndex);
			mode3phase1displayButtons();
			textFont(font);
  			fill(0xff817575);
			text("Select a photo from the list to add to a panel.", 20, 40);
		}
		else if(phase == 2)
		{
			// show photo that user clicked large
			// display save or discard buttons
			displayPhoto(currentPhotoIndex);
			mode3phase2displayButtons();
		}
		else if(phase == 3)
		{
			mode3phase3display();
		}
		else if(phase == 4)
		{
			mode3phase4display();
		}
	}
	else if(mode == 4) // edit photo mode
	{
		if(phase == 1)
		{
			// edit photo hub
			textFont(font);
  			fill(0xff817575);
  			background(backgroundColor);
  			text("Edit Photo", 20, 40);
			displayPhoto(currentPhotoIndex);
			mode4phase1displayButtons();
		}
		else if(phase == 2)
		{
			// simple drawing mode
			textFont(font);
  			fill(0xff817575);
			text("Draw", 20, 40);
			mode4phase2draw();
		}
		else if(phase == 3)
		{
			// Simple resize of full photo
			textFont(font);
  			fill(0xff817575);
            background(backgroundColor);
            text("Resize", 20, 40);
            displayResizedPhoto(currentPhotoIndex, resizeValue);
            mode4phase3displayButtons();
		}
		else if(phase == 4)
		{
			// Layer photos phase
			mode4phase4display();
			
		}
		else if(phase == 5)
		{
			// pick a photo to add as a layer
			mode4phase5display();
		}
		else if(phase == 6)
		{
			// select and crop mode
			mode4phase6display();
		}
	}
	else if(mode == 5)
	{
		// Edit Panel mode
		// -simple functions eg: delete a panel
		if(phase == 1)
		{
			//background(255);
			background(backgroundColor);
			displayPanel(currentPanelIndex);
		    mode5phase1displayButtons();	
		}
	}
	else if(mode == 6)
	{
		if(phase == 1)
			mode6phase1display();
		else if(phase == 2)
			mode6phase2display();
	}
	else if(mode == 7) // Flickr image search mode
	{
		if(phase == 1)
			mode7phase1display();
		else if(phase == 2)
			mode7phase2display();		
		else if(phase == 3)
			mode7phase3display();
	}
    /*
    else if (mode==8)
    {
      mode8draw();
      mode8dispayButon();
    }
    */
}


//__________________________________________________________________________________________________________________________
public void captureEvent(Capture video) { video.read(); }


//__________________________________________________________________________________________________________________________
public void keyPressed()
{
	if (key == ' ')
	{
		if( mode == 2 && phase == 1)
			takePhoto();
		if( mode == 2 && phase == 3)
			takeCalibrationPhoto();
	}
}


//__________________________________________________________________________________________________________________________
public void mousePressed()
{
	switch (mode) 
	{
		case 1: mode1mousePressed();
				break;
		case 2: mode2mousePressed();
				break;
		case 3: mode3mousePressed(); 
				break;
		case 4: mode4mousePressed();
				break;
		case 6: mode6mousePressed();
				break;
		case 7: mode7mousePressed();
				break;
        default: break;
	}
}


//__________________________________________________________________________________________________________________________
public void mouseDragged()
{
	if(mode == 4) 
	{
		if(phase == 2)
		{
			//println("mouseDragged");
 			flag = 1;
 		}
 		if(phase == 4 && numLayers > 0)
 		{
 			println("x: "+mouseX+" y:"+mouseY);
 			LayersX.set(numLayers-1, mouseX);
 			LayersY.set(numLayers-1, mouseY);
 		}
 		if(phase == 6)
 		{
 			if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
			{
				cropX2 = mouseX;
				cropY2 = mouseY;
			}	
 		}
 	}
 	else if(mode == 3 && phase == 3)
 	{
 		if(mouseY < 70)
 			halfY = 70; 
 		else if(mouseY > (70+300))
 			halfY = 70+300;
 		else
 			halfY = mouseY;
 	}
 	else if(mode == 3 && phase == 4)
 	{
 		if(mouseX < (width-800)/2)
 			halfX = (width-800)/2; 
 		else if(mouseX > width/2)
 			halfX = width/2;
 		else
 			halfX = mouseX;
 	}
 	else if(mode == 6 && phase ==1)
	{
		if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
		{
			rectX2 = mouseX;
			rectY2 = mouseY;
		}	
	}
}


//__________________________________________________________________________________________________________________________
public void mouseReleased()
{
	if(mode == 4 && phase == 2)
	{
		flag = 0;
		println("mouse released");
	}
}


//__________________________________________________________________________________________________________________________
public void controlEvent(ControlEvent c)
{
	// For use in Mode 4: Edit Photo
	// This function sends the values from the color slider into the paint variable
  	if(mode == 4 && c.isFrom(cp))
    {
		int r = PApplet.parseInt(c.getArrayValue(0));
		int g = PApplet.parseInt(c.getArrayValue(1));
		int b = PApplet.parseInt(c.getArrayValue(2));
		int a = PApplet.parseInt(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
  	else if(mode == 6 && c.isFrom(cp))
    {
		int r = PApplet.parseInt(c.getArrayValue(0));
		int g = PApplet.parseInt(c.getArrayValue(1));
		int b = PApplet.parseInt(c.getArrayValue(2));
		int a = PApplet.parseInt(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
  	else if( mode == 6 && c.isAssignableFrom(Textfield.class)) 
  	{
    	println("controlEvent: accessing a string from controller '"
            +c.getName()+"': "
            +c.getStringValue()
            );
  	}
}



public void input(String theText)
{
  // automatically receives results from controller input
  println("a textfield event for controller 'input' : "+theText);
}
// Mode 2: Take a picture

//==========================================================================================================================
public void drawCam()
{  
  textFont(font);
  text("Capture Mode", 20, 40);

  frame = webcam;
	
	pushMatrix();

	//flip across x axis
	scale(-1,1);

  if(changeBackground && useCustomBackground)
    image(displayCustomBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);
  else if(changeBackground)
    image(changeBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);
  else if(removeBackground)
    image(removeBackground(frame.get()), -(width - 800)/2 -800, 70, 800, 600);
	else
		image(frame, -(width - 800)/2 -800, 70, 800, 600);	
  
	popMatrix(); 
}


//__________________________________________________________________________________________________________________________
public PImage removeBackground(PImage frame)
{       
        
    mode2Calibration.loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        

        if(inHSBmode)
        {
          colorMode(HSB, 255);
          
          int loc = x + y * frame.width;
          int display = frame.pixels[loc];
          int comparison = mode2Calibration.pixels[loc];
          
          float h1 = hue(display),
            s1 = saturation(display),
            b1 = brightness(display),
            h2 = hue(comparison),
            s2 = saturation(comparison), 
            b2 = brightness(comparison)
            ;

          float hueDiff = abs(h1-h2),
            satDiff = abs(s1-s2),
            brtDiff = abs(b1-b2)
            ;

          if(hueDiff < hueThreshold && satDiff < satThreshold && brtDiff < brtThreshold)
            frame.pixels[loc] = color(255);
        }
        else
        {
          colorMode(RGB, 255);

          int loc = x + y * frame.width;
          int display = frame.pixels[loc];
          int comparison = mode2Calibration.pixels[loc];

          float r1 = red(display); float g1 = green(display); float b1 = blue(display);
          float r2 = red(comparison); float g2 = green(comparison); float b2 = blue(comparison);
          float diff = dist(r1,g1,b1,r2,g2,b2);

          if(diff < threshold)
                frame.pixels[loc] = color(255);
        }
      }
    }
    frame.updatePixels(); 
    calibratedFrame = frame.get();       
    return calibratedFrame;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode2phase1back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 50;

		cp5.addButton("takePhoto")
			.setPosition(left + offset, 677)
			.setCaptionLabel("Capture")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(100, 40)
			;

    offset += 110;
                
    cp5.addButton("goToCalibrationPhase")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Replace Background")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(250, 40)
      ;

    offset += 260;

    cp5.addButton("mode2phase1flickr")
      .setPosition((width+800)/2+10, 70)
      .setCaptionLabel("flickr")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;  

    if(removeBackground)
    {
      cp5.addButton("backgroundSelection")
        .setPosition(left + offset, 677)
        .setCaptionLabel("Background")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(180, 40)
        ;

      offset += 190;

      cp5.addButton("pickCustomBackground")
        .setPosition(left + offset, 677)
        .setCaptionLabel("Use Photo")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(150, 40)
        ;

      cp5.addSlider("thresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("hueThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("satThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40 + 30)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("brtThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40 + 30 + 30)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addButton("mode2phase1hsbMode")
        .setPosition((width+800)/2+10, 170)
        .setCaptionLabel("HSB")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(80, 40)
        ; 

      cp5.getController("thresholdSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);        
    }
		displayButtons = false;
	}
}


//__________________________________________________________________________________________________________________________
public void takePhoto()
{
  Snap.play();
  try
  {
    if(removeBackground)
    {
      //println("removeBackground");
      mode2Capture = calibratedFrame.get();
    }
    else if(changeBackground)
    {
      //println("changeBackground");
      mode2Capture = editedFrame.get();                
    }
    else
    {
      //println("regularBackground");
      mode2Capture = frame.get();
    }
  }
  catch(NullPointerException e)
  {
    println("Could not capture frame! Null pointer!");
  }

  phase = 2;
  cp5.hide();
  displayButtons = true;
  mirror(mode2Capture);
  Photos.add(mode2Capture);
  numPhotos++;
}


//__________________________________________________________________________________________________________________________
public void mode2phase1back()
{
  mode = 1;
  cp5.hide();  
  displayButtons = true;
  removeBackground = false;
  changeBackground = false;
  useCustomBackground = false;
}


//__________________________________________________________________________________________________________________________
public void goToCalibrationPhase()
{
  phase = 3;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode2phase1flickr()
{
  mode = 7;
  phase = 1;
  cp5.hide();  
  displayButtons = true;
  flickrSearchQuery = "";
}


//__________________________________________________________________________________________________________________________
public void backgroundSelection()
{
  loadStockBackground();
  changeBackground = true;
  removeBackground = false;  
}


//__________________________________________________________________________________________________________________________
public void thresholdSize(int value)
{
  threshold = value;
}

//__________________________________________________________________________________________________________________________
public void hueThresholdSize(int value)
{
  hueThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void satThresholdSize(int value)
{
  satThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void brtThresholdSize(int value)
{
  brtThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void mode2phase1hsbMode()
{
  if(!inHSBmode)
    inHSBmode = true;
  else
    inHSBmode = false;
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase2Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("mode2phase2save")
			.setPosition((width-800)/2 + 50, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
      .setColor(backButtonColor)
			;

		displayButtons = false;
	}
}


//__________________________________________________________________________________________________________________________
public void mirror(PImage capImg) {
  capImg.loadPixels();
  for (int y=0; y<capImg.height; y++) {
    for (int x=0; x<capImg.width/2; x++) {
      int loc = x + y * capImg.width;
      int mirroredLoc = capImg.width-1 - x + y * capImg.width;
      int temp = capImg.pixels[loc];
      capImg.pixels[loc] = capImg.pixels[mirroredLoc];
      capImg.pixels[mirroredLoc] = temp;
    }
  }
  capImg.updatePixels();
}


//__________________________________________________________________________________________________________________________
public void mode2phase2save()
{
	phase = 1;
	mode = 1;
	cp5.hide();
	displayButtons = true;
	removeBackground = false;
  changeBackground = false;
  useCustomBackground = false;
}


//__________________________________________________________________________________________________________________________
public void mode2phase2back()
{
	phase = 1;
	cp5.hide();  
	displayButtons = true;
	numPhotos--;
  Photos.remove(numPhotos);
  //println("button back to overview ");
}


//==========================================================================================================================
public void calibrationPhase()
{
  textFont(font);
  text("Please step off screen and take a photo of your background.", 20, 40);
  
  frame = webcam;

  pushMatrix();

  //flip across x axis
  scale(-1,1);
  image(frame, -(width - 800)/2 -800, 70, 800, 600);
  popMatrix(); 
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase3buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;

    cp5.addButton("mode2phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("takeCalibrationPhoto")
      .setPosition(left + 50, 677)
      .setCaptionLabel("Capture")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;

    displayButtons = false;
  }
  
}


//__________________________________________________________________________________________________________________________
public void takeCalibrationPhoto()
{
  Snap.play();
  try
  {
    mode2Calibration = frame.get();
  }
  catch(NullPointerException e)
  {
    println("Could not capture frame! Null pointer!");
  }

  phase = 1;
  cp5.hide();
  displayButtons = true;
  //mirror(mode2Calibration);
  removeBackground = true;
}


//__________________________________________________________________________________________________________________________
public void mode2phase3back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void loadStockBackground()
{
  String path = sketchPath+"/stockbackground/"; //folder of images rename as needed
  File[] files = listFiles(path);
  stockBackground = new PImage[files.length];

  for(int i=0; i<files.length; i++)
  {
    stockBackground[i]=loadImage(files[i].getAbsolutePath());
    imageResize(stockBackground[i]);
  }
}


//__________________________________________________________________________________________________________________________
public void imageResize(PImage img)
{
  img.resize(640,480);
}


//__________________________________________________________________________________________________________________________
public void pickCustomBackground()
{
  phase = 4;
  cp5.hide();  
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public PImage changeBackground(PImage frame)
{       
    stockBackground[0].loadPixels();
    image(stockBackground[0], -(width - 800)/2 -800, 70, 800, 600);
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        int loc = x + y * frame.width;
        int display = frame.pixels[loc];        
        
        if(display == color(255)){
              frame.pixels[loc] = stockBackground[0].pixels[loc];
        }
        
      }
    }
    frame.updatePixels(); 
    editedFrame = frame.get();       
    return editedFrame;
}


//__________________________________________________________________________________________________________________________
public PImage displayCustomBackground(PImage frame)
{       
  PImage resizedBackground = Photos.get(backgroundIndex);
  resizedBackground.resize(frame.width, frame.height);
  resizedBackground.loadPixels();
  frame.loadPixels();

  image(resizedBackground, -(width - 800)/2 -800, 70, 800, 600);

  for (int y=0; y < frame.height; y++)
  {
    for (int x=0; x < frame.width; x++)
    {
      int loc = x + y * frame.width;
      int display = frame.pixels[loc];        
      
      if(display == color(255))
        frame.pixels[loc] = resizedBackground.pixels[loc];
    }
  }

  frame.updatePixels(); 
  editedFrame = frame.get();       
  return editedFrame;
}


//==========================================================================================================================
public void mode2phase4display()
{
  background(backgroundColor);
  text("Click a photo to use as a background.", 20, 40);
  mode3displayPhotos();
  mode2phase4buttons();
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase4buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;

    cp5.addButton("mode2phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode2mousePressed()
{
  if(phase == 4)
  {
    for(int i = 0; i < numPhotos; i++)
    {
      int photoX = 80 + i*110;
      int photoY = height/2;

      if(mouseX >= photoX 
        && mouseX <= photoX + 100 
        && mouseY >= photoY 
        && mouseY <= photoY + 75)
      {
        backgroundIndex = i;
        phase = 1;
        useCustomBackground = true;
        changeBackground = true;
        //removeBackground = false;
        println(" use a custom photo background ");

        cp5.hide();
        displayButtons = true;
      }
    }
  }
}
// Mode 3: Add a panel
//		Phase 1: show horizontal list of photos to add
//		Phase 2: show clicked photo large '<' button goes back and 'S' button adds clicked photo to panels array


//__________________________________________________________________________________________________________________________
public void mode3displayPhotos()
{
	background(backgroundColor);
	for(int i = 0; i < numPhotos; i++)
		image(Photos.get(i), 80 + i*110, height/2, 100, 75);
}



//__________________________________________________________________________________________________________________________
public void mode3displayPhotos(int index)
{
  background(backgroundColor);
  for(int i = index; i < numPhotos; i++)
    image(Photos.get(i), 80 + (i-index)*110, height/2, 100, 75);
}


//__________________________________________________________________________________________________________________________
public void displayPhoto(int index)
{
	image(Photos.get(index), (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode3phase1left")
      .setPosition((width-800)/2+50, 677)
      .setCaptionLabel("left")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode3phase1right")
      .setPosition((width-800)/2+50+90, 677)
      .setCaptionLabel("right")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase1back()
{
  println("button: back to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}



//__________________________________________________________________________________________________________________________
public void mode3phase1left()
{
  if(numPhotos > 8)
  {
    displayIndex--;
    if(displayIndex < 0)
      displayIndex = 0;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase1right()
{
  if(numPhotos > 8)
  {
    displayIndex++;
    if(displayIndex > numPhotos-4)
      displayIndex = numPhotos-4;
  }
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase2back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase2save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode3phase2makeHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Horizontal Half")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(220, 40)
      ;

    offset += 220 + 10;

    cp5.addButton("mode3phase2makeHalfVertical")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Vertical Half")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(170, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase2back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2save()
{
  println("button: save panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newPanel = Photos.get(currentPhotoIndex);
  Panels.add(newPanel);
  PanelSizes.add(1);
  numPanels++;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2makeHalf()
{
  println("button: display half photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  halfX = (width - 800)/2;
  halfY = 70;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2makeHalfVertical()
{
  println("button: display vertical half photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  halfX = (width - 800)/2;
  halfY = 70;
}


//===========================================================================================================================
public void mode3phase3display()
{
  background(backgroundColor);
  text("Move the rectangle to pick region to save", 20, 40);
  mode3phase3displayButtons();
  displayPhoto(currentPhotoIndex);
  stroke(255);
  noFill();
  strokeWeight(3);
  rect(halfX, halfY, 800, 300);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase3saveHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save Region")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(140, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase3back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 2;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode3phase3saveHalf()
{
  println("button: save half panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newHalfPanel = createImage(640, 480/2-8, RGB); 
  newHalfPanel.copy(Photos.get(currentPhotoIndex), 0, halfY-70, 640, 480/2-8, 0, 0, 640, 480/2-8);
  Panels.add(newHalfPanel);
  PanelSizes.add(2);
  numPanels++;
  numHalfPanels++;
}


//__________________________________________________________________________________________________________________________
public void mode3mousePressed()
{
	if(phase == 1)
	{
		for(int i = 0; i < numPhotos; i++)
		{
			int photoX = 80 + i*110;
			int photoY = height/2;

			if(mouseX >= photoX 
				&& mouseX <= photoX + 100 
				&& mouseY >= photoY 
				&& mouseY <= photoY + 75)
			{
				currentPhotoIndex = i;
				phase = 2;

        cp5.hide();
        displayButtons = true;
			}
		}
	}
}


//===========================================================================================================================
public void mode3phase4display()
{
  background(backgroundColor);
  text("Move the rectangle to pick region to save", 20, 40);
  mode3phase4displayButtons();
  displayPhoto(currentPhotoIndex);
  stroke(255);
  noFill();
  strokeWeight(3);
  rect(halfX, halfY, 400, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase4displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase4saveHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save Region")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(140, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase4saveHalf()
{
  println("button: save half panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newHalfPanel = createImage(640/2-8, 480, RGB); 
  newHalfPanel.copy(Photos.get(currentPhotoIndex), halfX-(width-800)/2, halfY-70, 640/2-8, 480, 0, 0, 640/2-8, 480);
  Panels.add(newHalfPanel);
  PanelSizes.add(3);
  numPanels++;
  numHalfPanels++;
}
// File: Mode4.pde
// Mode 4 is the photo editing hub
//	Phase 1 = editing hub with buttons to different phases
//  Phase 2 = simple drawing mode (ie: ms paint)
//	Phase 3 = resize a photo


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase1back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode4phase1draw")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Draw")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode4phase1select")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Select")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode4phase1text")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Text")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;
    
    cp5.addButton("mode4phase1resize")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Resize")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(90, 40)
      ;

    offset += 90 + 10;

    cp5.addButton("mode4phase1layer")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Layer")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(90, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase1back()
{
  println("button: back to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode4phase1select()
{
  phase = 6;
  cp5.hide();
  displayButtons = true;
  cropX1 = 0;
  cropY1 = 0;
  cropX2 = 0;
  cropY2 = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase1draw()
{
  println("button: edit photo");
  phase = 2;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(backgroundColor);
  paint = color(255, 128, 0, 255);
}


//__________________________________________________________________________________________________________________________
public void mode4phase1text()
{
  println("button: add text to photo");
  mode = 6;
  phase = 1;
  rectX1 = 0;
  rectY1 = 0;
  cp5.hide();
  displayButtons = true;
}

//__________________________________________________________________________________________________________________________
public void mode4phase1resize()
{
  println("button: resize photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(backgroundColor);
  //displayResizePhoto = true;
  resizeValue = 100;
}


//__________________________________________________________________________________________________________________________
public void mode4phase1layer()
{
  println("button: layer photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(backgroundColor);
}


//==========================================================================================================================
public void mode4phase2draw()
{
  fill(0xff909090);
  textFont(smallFont);
  text("Brush size:", (width - 100)/2 - 30, 15);
  textFont(buttonFont);
        
	mode4phase2displayButtons();

	if(displayPhoto)
	{
		displayPhoto(currentPhotoIndex);
		displayPhoto = false;
	}

	stroke(backgroundColor);
	fill(backgroundColor);
	ellipse((width - 200)/2 - 60, 20, 60, 60);

	fill(paint);
  stroke(paint);
	ellipse((width - 200)/2 - 60, 20, strokeWt, strokeWt);

	stroke(paint);
	strokeWeight(strokeWt);

	if(flag == 1
		&& mouseX >= (width - 800)/2
		&& mouseX <= (width - 800)/2 + 800
		&& mouseY >= 70
		&& mouseY <= 70 + 600
    )
		  line(mouseX, mouseY, pmouseX, pmouseY);

  if(displayPhoto)
  {
    displayPhoto(currentPhotoIndex);
    displayPhoto = false;
  }
}


//__________________________________________________________________________________________________________________________
public void displayResizedPhoto(int index, float resize)
{
  if(resize <= 100)
    image(Photos.get(index), (width - (800 * (resize/100)))/2, 70 + (300 - (600 * (resize/100)/2)), 800 * (resize/100), 600 * (resize/100));
  else
    displayPhoto(index);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);
    fill(0xff909090);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase2back")
  		.setPosition(left, 677)
  		.setCaptionLabel("<")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(40, 40)
      .setColor(backButtonColor)
  		;

    offset += 40 + 10;

    cp5.addButton("mode4phase2save")
  		.setPosition(left + offset, 677)
  		.setCaptionLabel("Save")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(80, 40)
  		;

    cp5.addSlider("brushSize")
    	.setCaptionLabel("")
    	.setPosition((width - 100)/2 - 30, 20)
    	.setSize(100, 20)
    	.setRange(1, 50)
      .setValue(5)
    	;

    cp5.getController("brushSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    cp5.setControlFont(smallFont);

  	cp = cp5.addColorPicker("colorPicker")
  		.setPosition((width + 100)/2 + 20, 5)
  		.setColorValue(color(255, 128, 0, 255))
  		;

    cp5.setControlFont(buttonFont);

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode4phase2back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode4phase2save()
{
	println("button: save to photo list");
	mode = 1;
	phase = 1;
	cp5.hide();
	displayButtons = true;

	// save edited photo to photo list
	PImage screenShot = get();
  editPhoto = createImage(640, 480, RGB);
	editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, 640, 480);
	Photos.add(editPhoto);
	numPhotos++;
}


//__________________________________________________________________________________________________________________________
public void brushSize(int theBrushSize) { strokeWt = theBrushSize; }


//__________________________________________________________________________________________________________________________
public void picker(int col)
{
  println("picker\talpha:"
    +alpha(col)
    +"\tred:"+red(col)
    +"\tgreen:"+green(col)
    +"\tblue:"+blue(col)
    +"\tcol"+col)
    ;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase2back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;
      
    cp5.addButton("mode4phase3save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
      
    cp5.addSlider("imageSize")
      .setCaptionLabel("")
      .setPosition((width - 100)/2 - 30, 20)
      .setSize(100, 20)
      .setRange(1, 200)
      .setValue(100)
      ;

    cp5.getController("imageSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase3save()
{
  println("button: save resized photo to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true; 
            
  // save edited photo to photo list
  if(resizeValue <= 100)
  {
    int resizedHeight = (int)(480 * (resizeValue/100));
    int resizedWidth = (int)(640 * (resizeValue/100));
    int resizedHeight2 = (int)(600 * (resizeValue/100));
    int resizedWidth2 = (int)(800 * (resizeValue/100));
    int resizedX = (int)((width - (800 * (resizeValue/100)))/2);
    int resizedY = (int)(70 + (300 - (600 * (resizeValue/100)/2)));
    PImage screenShot = get();
    println("Created resized photo with dimensions: "+resizedWidth+", "+resizedHeight);
    editPhoto = createImage(resizedWidth, resizedHeight, RGB);
    //editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, resizedWidth, resizedHeight);
    editPhoto.copy(screenShot, resizedX, resizedY, resizedWidth2, resizedHeight2, 0, 0, resizedWidth, resizedHeight);
    Photos.add(editPhoto);
    numPhotos++;
  }
  else
    Photos.get(currentPhotoIndex).resize((int)(Photos.get(currentPhotoIndex).width*(resizeValue/100)), (int)(Photos.get(currentPhotoIndex).height*(resizeValue/100)));
}


//__________________________________________________________________________________________________________________________
public void imageSize(float value)
{
  println(value);
  resizeValue = value;
}



//==========================================================================================================================
public void mode4phase4display()
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  displayPhoto(currentPhotoIndex);

  for(int i = 0; i < numLayers; i++)
  {
    int layerWidth = (int)((1.25f)*Photos.get(Layers.get(i)).width);
    int layerHeight = (int)((1.25f)*Photos.get(Layers.get(i)).height);
    println("("+LayersX.get(i)+", "+LayersY.get(i)+") dims: "+layerWidth+", "+layerHeight);
    image(Photos.get(Layers.get(i)), LayersX.get(i), LayersY.get(i), layerWidth, layerHeight);
  }

  noStroke();
  fill(backgroundColor);
  rect(0, 0, width, 70);    //top
  rect(0, 0, (width-800)/2, height); // left
  rect(0, 70+600, width, height-670); // bottom
  rect((width+800)/2, 0, width-(width+800)/2, height); // right
  text("Layer", 20, 40);
  mode4phase4displayButtons();
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase4displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase4back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode4phase4addPhoto")
      .setPosition(left + offset, 677)
      .setCaptionLabel("+Photo")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;
    
    cp5.addButton("mode4phase4save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase4back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
  numLayers = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase4save()
{
  println("button: save to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // save edited photo to photo list
  PImage screenShot = get();
  editPhoto = createImage(640, 480, RGB);
  editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, 640, 480);
  Photos.add(editPhoto);
  numPhotos++;
  numLayers = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase4addPhoto()
{
  textFont(font);
  fill(0xff817575);
  println("button: pick a photo from photo list");
  phase = 5;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}


//==========================================================================================================================
public void mode4phase5display() // add photo as layer mode
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  mode3displayPhotos(displayIndex);
  text("Pick a photo to add as a layer", 20, 40);
  mode4phase5displayButtons();
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase5displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase5back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode4phase5left")
      .setPosition((width-800)/2+50, 677)
      .setCaptionLabel("left")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode4phase5right")
      .setPosition((width-800)/2+50+90, 677)
      .setCaptionLabel("right")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode4phase5back()
{
  println("button: back to photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase5left()
{
  displayIndex--;
  if(displayIndex < 0)
    displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase5right()
{
  displayIndex++;
  if(displayIndex > numPhotos-4)
    displayIndex = numPhotos-4;
}


//__________________________________________________________________________________________________________________________
public void mode4mousePressed()
{
  if(phase == 5)
  {
    for(int i = 0; i < numPhotos; i++)
    {
      int photoX = 80 + i*110;
      int photoY = height/2;

      if(mouseX >= photoX 
        && mouseX <= photoX + 100 
        && mouseY >= photoY 
        && mouseY <= photoY + 75)
      {
        Layers.add(i);
        LayersX.add((width - 800)/2);
        LayersY.add(70);
        numLayers++;
        phase = 4;
        println("-- added photo as layer");
        displayIndex = 0;

        cp5.hide();
        displayButtons = true;
      }
    }
  }
  else if(phase == 6)
  {
    if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
    {
      println("y: "+mouseY+"  x: "+mouseX);
      cropX1 = mouseX;
      cropY1 = mouseY;
    }
  }
}



//==========================================================================================================================
public void mode4phase6display() // Simple selection and crop mode
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  displayPhoto(currentPhotoIndex);
  text("Click and drag crop selection", 20, 40);
  mode4phase6displayButtons();
  if(cropX1 != 0 && cropX2 != 0)
  {
    noFill();
    strokeWeight(1);
    stroke(255);
    rect(cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1);
  }
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase6displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase6back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode4phase6save")
      .setPosition((width-800)/2 + 50, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase6back()
{
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode4phase6save()
{
  println("button: cropped photo saved to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // save edited photo to photo list

  int temp;
  if(cropX1 > cropX2)
  {
    temp = cropX1;
    cropX1 = cropX2;
    cropX2 = temp;
  }
  if(cropY1 > cropY2)
  {
    temp = cropY1;
    cropY1 = cropY2;
    cropY2 = temp;
  }


  displayPhoto(currentPhotoIndex);
  PImage screenShot = get();

  int cropWidth = cropX2 - cropX1;
  int cropHeight = cropY2 - cropY1;
  cropWidth = (int)(cropWidth * 0.8f);
  cropHeight = (int)(cropHeight * 0.8f);

  editPhoto = createImage(cropWidth, cropHeight, RGB);
  editPhoto.copy(screenShot, cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1, 0, 0, cropWidth, cropHeight);
  Photos.add(editPhoto);
  numPhotos++;
}
// Edit Panel mode



//__________________________________________________________________________________________________________________________
public void displayPanel(int index)
{
	image(Panels.get(index), (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode5phase1displayButtons()
{
	if(displayButtons)
  	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    int left = (width-800)/2;
    	int offset = 0;

	    cp5.addButton("mode5phase1back")
	      .setPosition(left, 677)
	      .setCaptionLabel("<")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(40, 40)
	      .setColor(backButtonColor)
	      ;

	    offset += 40 + 10;

	    cp5.addButton("mode5phase1delete")
	      .setPosition(left + offset, 677)
	      .setCaptionLabel("Delete")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(90, 40)
	      ;

	    offset += 90 + 10;
	    
	    cp5.addButton("mode5phase1left")
	      .setPosition(left + offset, 677)
	      .setCaptionLabel("Left")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    offset += 80 + 10;

	    cp5.addButton("mode5phase1right")
	      .setPosition(left + offset, 677)
	      .setCaptionLabel("Right")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode5phase1back()
{
	println("button: back to photo list");
	mode = 1;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode5phase1delete()
{
  	println("button: deleted panel: " + currentPanelIndex);
  	mode = 1;
  	phase = 1;
  	cp5.hide();
  	displayButtons = true;

  	// delete panel and move panels behind it forward
  	/*
  	for(int i = currentPanelIndex + 1; i < numPanels; i++)
  	{
  		Panels[i - 1] = Panels[i];
  	}*/
  	Panels.remove(currentPanelIndex);
  	PanelSizes.remove(currentPanelIndex);
  	numPanels--;
}



// Add text bubbles mode


//==========================================================================================================================
public void mode6phase1display()
{
	background(backgroundColor);
	mode6phase1displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	text("Click and drag to make the textbox size.", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(255);
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		stroke(255);
		strokeWeight(10);
		
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);

		noStroke();
	}
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode6phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;
      
   cp5.addButton("mode6phase1save")
      .setPosition((width-800)/2 + 50, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode6phase1back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase1save()
{
  	println("button: save text bubble");
  	if(rectX1 != 0)
	{
	  	phase = 2;
	  	cp5.hide();
	  	displayButtons = true;
	  	paint = color(255, 255, 255, 255);
	}
	else
		text("Please make a rectangle first.", 200, 40);

}


//__________________________________________________________________________________________________________________________
public void mode6mousePressed()
{
	if(phase == 1)
	{
		if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
		{
			rectX1 = mouseX;
			rectY1 = mouseY;
		}	
	}
}



//==========================================================================================================================
public void mode6phase2display()
{
	background(backgroundColor);
	mode6phase2displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	fill(255);
	text("Enter text", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(paint);
		noStroke();
		
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		strokeWeight(10);
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);
		noStroke();
	}
	
	fill(0,0,0);
	int bubbleBorder = 10;
	text(cp5.get(Textfield.class,"textBubble").getText(), 
		rectX1+bubbleBorder, rectY1+bubbleBorder, 
		rectX2 - rectX1 - bubbleBorder, rectY2 - rectY1 - bubbleBorder);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode6phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase2back")
		.setPosition((width-800)/2, 677)
		.setCaptionLabel("<")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(40, 40)
		.setColor(backButtonColor)
		;
      
   	cp5.addButton("mode6phase2save")
		.setPosition((width-800)/2 + 50, 677)
		.setCaptionLabel("Save")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(80, 40)
		;

	cp5.addTextfield("textBubble")
		.setCaptionLabel("Add text")
		.setPosition(width/2, 5)
		.setSize(200,40)
		.setFont(smallFont)
		.setFocus(true)
		.setColor(color(255,0,0))
		;

	cp = cp5.addColorPicker("colorPicker2")
  		.setPosition((width + 100)/2 + 220, 5)
  		.setColorValue(color(255, 255, 255, 255))
  		;

    displayButtons = false;
  }
}




//__________________________________________________________________________________________________________________________
public void mode6phase2back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase2save()
{
	println("button: save to photo list");
	mode = 1;
	phase = 1;
	cp5.hide();
	displayButtons = true;

	// save edited photo to photo list
	PImage screenShot = get();
  	editPhoto = createImage(640, 480, RGB);
	editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, 640, 480);
	Photos.add(editPhoto);
	numPhotos++;
}
// file: Mode7.pde
// description: Mode 7 allows the user to search flickr for a photo and add that photo to their project


//==========================================================================================================================
public void mode7phase1display()
{
	background(backgroundColor);
	mode7phase1displayButtons();
	text("Search flickr for a photo to add.", 20, 40);
	text("Use '+' to separate tags.", 20, 80);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase1displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    int left = (width-800)/2;

	    cp5.addButton("mode7phase1back")
			.setPosition(left, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

		cp5.addTextfield("flickrSearchQuery")
			.setCaptionLabel("Enter Query")
			.setPosition((width-290)/2, (height-40)/2)
			.setSize(200, 40)
			.setFont(smallFont)
			.setFocus(true)
			.setColor(color(255,0,0))
			;

		cp5.addButton("flickrSearchButton")
			.setPosition((width+200)/2 - 20, (height-40)/2)
			.setCaptionLabel("Search")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(90, 40)
	      	;

	    displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void flickrSearchButton()
{
	println("button: flickr search button");
	phase = 2;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = cp5.get(Textfield.class,"flickrSearchQuery").getText();
	getFlickrData();
}


//__________________________________________________________________________________________________________________________
public void mode7phase1back()
{
	println("button: back to add photo");
	mode = 2;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}


//==========================================================================================================================
public void mode7phase2display()
{
	background(backgroundColor);
	mode7phase2displayButtons();
	text("Click on a photo to add.", 20, 40);
	text("Showing results for \""+flickrSearchQuery+"\"", 20, 80);

	int x=0, y=0, size = flickrPhotoList.size();
	float flickrW, flickrH, compW = 640, compH = 480, compAspectRatio = compW/compH;

	for (int i = 0; i < size; i++)
	{
		flickrW = (float)flickrPhotoList.get(i).width;
		flickrH = (float)flickrPhotoList.get(i).height;
		float aspectRatio = flickrW/flickrH;

		if(aspectRatio >= compAspectRatio)
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 200, 200 * flickrH/flickrW);
		else
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 150 * flickrW/flickrH, 150);

		x++;
		if (x >= 5)
		{
		  	x = 0;
		  	y++;
		}
	}
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase2displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase2back")
			.setPosition(width-60, 20)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase2back()
{
	phase = 1;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = "";
	flickrPhotoList = new ArrayList <PImage> ();
}


//__________________________________________________________________________________________________________________________
public void getFlickrData() 
{
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
	String request = api + "&per_page=25&format=json&nojsoncallback=1&extras=geo";
	request += "&api_key=" + "b4b2de9fa436d4629313b6463f254d69"; 
	String tags = flickrSearchQuery;
	request += "&tags=" + tags;

	//String userId = "88935360@N05"; // 690 test
	//request += "&user_id=" + userId;
	//request += "&min_upload_date=2012-07-01";
	//request += "&bbox=-125,32,-120,40";
	//request += "&lat=38";
	//request += "&lon=-122";
	//request += "&tags=bill%20burroughs";

	println("Sent request: " + request);
	json = loadJSONObject(request);
	println("Received from flickr: " + json);

	JSONObject photos = json.getJSONObject("photos");
	JSONArray photo = photos.getJSONArray("photo");
	println("Found " + photo.size() + " photos");

	for (int i=0; i<photo.size (); i++) 
	{
		JSONObject pic = photo.getJSONObject(i);
		// get parameters to construct url
		int farm = pic.getInt("farm");
		String server = pic.getString("server");
		String id = pic.getString("id");
		String secret = pic.getString("secret");
		String url = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
		String title = pic.getString("title");
		println("Photo " + i + " " + title + " " + url);
		PImage img = loadImage(url);

		if(img.width/img.height >= 640/480)
			img.resize(640, 640 * img.height/img.width);
		else
			img.resize(480 * img.width/img.height, 480);

		flickrPhotoList.add(img);
	}
}


//__________________________________________________________________________________________________________________________
public void mode7mousePressed()
{
	if(phase == 2)
	{
		int x = 0, y = 0, 
			photoY, photoX, photoW, photoH, 
			size = flickrPhotoList.size();

		for(int i = 0; i < size; i++)
		{

			float compW = 640, 
				compH = 480, 
				compAspectRatio = compW/compH,
				flickrW = (float)flickrPhotoList.get(i).width,
				flickrH = (float)flickrPhotoList.get(i).height,
				aspectRatio = flickrW/flickrH
				;

			if(aspectRatio >= compAspectRatio)
			{
				photoW = 200;
				photoH = (int)(200 * flickrH/flickrW);
			}
			else
			{
				photoW = (int)(150 * flickrW/flickrH);
				photoH = 150;
			}


			photoX = 20 + x*210;
			photoY = 100+y*160;

			if(mouseY < photoY + photoH && mouseY > photoY 
				&& mouseX < photoX + photoW && mouseX > photoX)
			{
				flickrPhotoIndex = i;
				phase = 3;
				cp5.hide();
				displayButtons = true;
				break;
			}

			x++;
			if (x >= 5)
			{
			  	x = 0;
			  	y++;
			}
		}
	}
}


//==========================================================================================================================
public void mode7phase3display()
{
	background(backgroundColor);
	mode7phase3displayButtons();

	float flickrW = (float)flickrPhotoList.get(flickrPhotoIndex).width,
		flickrH = (float)flickrPhotoList.get(flickrPhotoIndex).height,
		aspectRatio = flickrW/flickrH,
		compW = 800,
		compH = 600,
		compAspectRatio = compW/compH
		;

	if(aspectRatio >= compAspectRatio)
		image(flickrPhotoList.get(flickrPhotoIndex), (width - 800)/2, 70, 800, 800/aspectRatio);
	else
		image(flickrPhotoList.get(flickrPhotoIndex), (width - 600 * aspectRatio)/2, 70, 600 * aspectRatio, 600);
	//println("aspectRatio: "+aspectRatio+"  compAspectRatio: "+compAspectRatio);
	//image(flickrPhotoList.get(flickrPhotoIndex), (width - 800)/2, 70);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase3displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase3back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

	    cp5.addButton("mode7phase3save")
			.setPosition((width-800)/2 + 60, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase3back()
{
	phase = 2;
	cp5.hide();
	displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode7phase3save()
{
	mode = 1;
	phase = 1;
	flickrSearchQuery = "";
	cp5.hide();
	displayButtons = true;
	Photos.add(flickrPhotoList.get(flickrPhotoIndex));
	numPhotos++;
	flickrPhotoList = new ArrayList <PImage> ();
}
// Mode 0: Start Screen
// Mode 1: Overview

//__________________________________________________________________________________________________________________________
public void drawStartScreen()
{
  PImage startLogo = loadImage("logo.png");
  image(startLogo, (width - startLogo.width)/2, (height - startLogo.height)/2);  
}



//==========================================================================================================================
public void drawOverview()
{
  background(backgroundColor);
  fill(0xffEAA3A3);
  font = loadFont("ArialMT-40.vlw");

  textFont(font);
  text("OVERVIEW", 20, 40);

  fill(0xff817575);
  text("photos", 60, 100);
  text("panels", 60, height/2);

  displayAddButtons();

  // display rectangles to show where photos are displayed
  for(int i = 0; i < 5; i++)
  {
    noStroke();
    fill(0xff558CAD);
    rect(80 + i*90, 140, 80, 60);
    rect(80 + i*90, (height/2 + 40), 80, 60);
  }

  // display photos and panels created
  fill(0xffCE235F);
  for(int i = displayPhotoIndex; i < numPhotos; i++)
  {
    image(Photos.get(i), 80 + (i - displayPhotoIndex)*90, 140, 80, 60);

    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= 140
      && mouseY <= 140 + 60)
      text("Edit", 80 + i*90, 140 + 35);
  }
  for(int i = 0; i < numPanels; i++) 
  {
    if(PanelSizes.get(i) == 1)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 80, 60);
    else if(PanelSizes.get(i) == 2)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 80, 30);
    else if(PanelSizes.get(i) == 3)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 40, 60);
    // show "Edit" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
      text("Edit", 80 + i*90, (height/2 + 40 + 35));
  }
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void displayStartButton()
{
  cp5 = new ControlP5(this);

  cp5.setControlFont(buttonFont);

  cp5.addButton("startButton")
    .setPosition((width - 80)/2, 650)
    .setCaptionLabel("Start")
    .align(CENTER,CENTER,CENTER,CENTER)
    .setSize(80, 40)
    ;
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void displayAddButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    println("  Add Buttons");

    cp5.addButton("addPhoto")
      .setPosition(200, 100 - 32)
      .setCaptionLabel("+")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode1phase1left")
      .setPosition(200 + 60, 100 - 32)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode1phase1right")
      .setPosition(200 + 60 + 50, 100 - 32)
      .setCaptionLabel(">")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;      

    cp5.addButton("addPanel")
      .setPosition(200, height/2 - 32)
      .setCaptionLabel("+")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode1export")
      .setPosition(250, 7)
      .setCaptionLabel("Export")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;

    displayButtons = false;
  }
}




//__________________________________________________________________________________________________________________________
public void displayPhotos()
{
  for(int i = 0; i < numPhotos - currentPhotoIndex; i++)
  {
    image(Photos.get(currentPhotoIndex + i), 80, (height/2 + 40) + i*70, 80, 60);
  }
}



//__________________________________________________________________________________________________________________________
public void startButton()
{
  println("Start button pressed ");
  mode = 1;
  cp5.hide();
}



//__________________________________________________________________________________________________________________________
public void mode1phase1left()
{
  if(numPhotos>7)
  {
    displayPhotoIndex--;
    if(displayPhotoIndex<0)
      displayPhotoIndex = 0;
  }
}


//__________________________________________________________________________________________________________________________
public void mode1phase1right()
{
  if(numPhotos>7)
  {
    displayPhotoIndex++;
    if(displayPhotoIndex<0)
      displayPhotoIndex = 0;
  }
}


//__________________________________________________________________________________________________________________________
public void addPhoto()
{
  println("+photo button pressed");
  mode = 2;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode1export()
{
  phase = 2;
  cp5.hide();
  displayButtons = true;
  displayExportedComic = false;
}


//__________________________________________________________________________________________________________________________
public void addPanel()
{
  println("+panel button pressed");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode1mousePressed()
{
  for(int i = 0; i < numPhotos; i++)
  {
    int photoX = 80 + i*110;
    int photoY = 140;

    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= 140
      && mouseY <= 140 + 60)
    {
      mode = 4;
      currentPhotoIndex = i;
      phase = 1;
      cp5.hide();
      displayButtons = true;
    }
  }

  for(int i = 0; i < numPanels; i++) 
  {
    // show "Edit" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
    {
      mode = 5;
      phase = 1;
      cp5.hide();
      displayButtons = true;
      currentPanelIndex = i;
    }
  }
}



//==========================================================================================================================
public void mode1phase2display()
{
  background(backgroundColor);
  text("Pick your export preference", 20, 40);
  mode1phase2buttons();

  if(displayExportedComic)
  {
    fill(0xff817575);
    float comicH = exportedComic.height,
      comicW = exportedComic.width,
      ratio = (comicH/comicW)
      ;
    image(exportedComic, (width-800)/2, 80, 800, 800*ratio);
    //text("Exported Comic: ", 80, (height/2 + 150));
  }
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode1phase2buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode1phase2back")
      .setPosition(60, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode1exportLong")
      .setPosition(width - 230, 10)
      .setCaptionLabel("Export as Strip")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(210, 40)
      ;
      
    cp5.addButton("mode1exportBlock")
      .setPosition(width - 230, 60)
      .setCaptionLabel("Export as Block")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(210, 40)
      ;
/*
    cp5.addButton("mode1customExport")
      .setPosition(width - 220, 110)
      .setCaptionLabel("Custom Export")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(200, 40)
      ;
*/
    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode1phase2back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
  //Photos.remove(numPhotos);
  println("button back to overview ");
}


//__________________________________________________________________________________________________________________________
public void mode1exportLong()
{
  println("Export button pressed ");

  if(numPanels > 0)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 16;
    PImage comicStrip = createImage(border + (border + 640)*(numPanels - numHalfPanels/2), 480 + 2 * border, RGB);
    comicStrip.loadPixels();
    int Max = (border + (border + 640)*(numPanels - numHalfPanels/2));
    for (int i = 0; i < Max; i++)
      for (int j = 0; j < 480 + 2 * border; j++)
        comicStrip.pixels[j*(border + (border + 640)*(numPanels - numHalfPanels/2)) + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    int numBlocks = 0;

    for(int i = 0; i < numPanels; i++)
    {
      boolean written = false;
      boolean writtenVertical = false;
      println(i);
      int cX = border + (640 + border) * numBlocks;
      int cY = border;
      Panels.get(i).loadPixels();

      if(PanelSizes.get(i) == 1)
      {
        comicStrip.copy(Panels.get(i), 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes.get(i) == 2) 
      {
        if(i > 0)
        {
          int cX2 = border + (640 + border) * (numBlocks-1);
          int cY2 = border + 480/2 + 8;
          if(PanelSizes.get(i-1) == 2)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX2, cY2, 640, 480/2-8);
            numBlocks--;
            written = true;
          }
        }
        if(!written)
          comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX, cY, 640, 480/2-8);
      }
      else if(PanelSizes.get(i) == 3) 
      {
        if(i > 0)
        {
          int cX3 = border + (640 + border) * (numBlocks-1) + 640/2 + 8;
          int cY3 = border;
          if(PanelSizes.get(i-1) == 3)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX3, cY3, 640/2-8, 480);
            numBlocks--;
            writtenVertical = true;
          }
        }
        if(!writtenVertical)
          comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX, cY, 640/2-8, 480);
      }
      numBlocks++;
    }


    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }
}


//__________________________________________________________________________________________________________________________
public void mode1exportBlock()
{
  println("Export button pressed as 3 wide strip");

  if(numPanels > 3)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 16;
    int totalBlocks = numPanels - numHalfPanels/2;
    int rowBlocks = totalBlocks/3;

    println("totalBlocks: "+totalBlocks+"   rowBlocks: "+rowBlocks);

    //PImage comicStrip = createImage(border + (border + 640)*(totalBlocks), 480 + 2 * border, RGB);

    PImage comicStrip = createImage(border + (border + 640)*3, border + (480 + border)*rowBlocks, RGB);

    comicStrip.loadPixels();
    //int Max = (border + (border + 640)*(numPanels - numHalfPanels/2));
    int MaxX = comicStrip.width;
    int MaxY = comicStrip.height;
    for (int i = 0; i < MaxX; i++)
      for (int j = 0; j < MaxY; j++)
        comicStrip.pixels[j*MaxX + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    int numBlocks = 0;
    int numHalves = 0;
    for(int i = 0; i < numPanels; i++)
    {
      boolean written = false;
      boolean writtenVertical = false;
      println("num: "+i);
      int cX = border + (640 + border) * (numBlocks%3);
      int cY = border + (numBlocks/3)*(480+border);
/*
      if(numBlocks == 3 || numBlocks == 6 || numBlocks == 9)
      {
        cY = border + (numBlocks/3)*(480+border);
      }
      else
      {
        cY = border;
      }
*/
      Panels.get(i).loadPixels();

      if(PanelSizes.get(i) == 1)
      {
        println("cX: "+cX+"   cY: "+cY);
        comicStrip.copy(Panels.get(i), 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes.get(i) == 2) 
      {
          int cX2 = border + (640 + border) * ((numBlocks-1)%3);
          // int cY2 = border + 480/2 + 8;
          int cY2 = border + ((numBlocks-1)/3)*(480+border) + 480/2 + 8;
        if(i > 0 && numHalves%2 == 1)
        {
          //int cX2 = border + (640 + border) * ((numBlocks)%3);
          // int cY2 = border + 480/2 + 8;
          //int cY2 = border + ((numBlocks-1)/3)*(480+border) + 480/2 + 8;

          //if(PanelSizes.get(i-1) == 2)
          //{
            comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX2, cY2, 640, 480/2-8);
            numBlocks--;
            written = true;
            println("h2 cX2: "+cX2+"   cY2: "+cY2+"  cY: "+cY);
            numHalves++;
          //}
        }
        else //if(!written)
        {
          comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX, cY, 640, 480/2-8);
          println("h1 cX: "+cX+"   cY: "+cY);
          numHalves++;
        }
      }
      else if(PanelSizes.get(i) == 3) 
      {
        if(i > 0 && numHalves%2 == 1)
        {
          int cX3 = border + (640 + border) * ((numBlocks-1)%3) + 640/2 + 8;
          int cY3 = border + ((numBlocks-1)/3)*(480+border);
          //int cY3 = border;
          //if(PanelSizes.get(i-1) == 3 && i%2 == 0)
          //{
            comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX3, cY3, 640/2-8, 480);
            numBlocks--;
            writtenVertical = true;
            println("v2 cX3: "+cX3+"   cY: "+cY);
            numHalves++;
          //}
        }
        else //if(!writtenVertical)
        {
          comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX, cY, 640/2-8, 480);
          println("v1 cX: "+cX+"   cY: "+cY);
          numHalves++;
        }
      }
      numBlocks++;
    }

    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Block Exported to File");
    comicStrip.save("comicStripBlock.png");
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ComicYourself" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
