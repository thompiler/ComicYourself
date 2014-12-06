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
PImage [] Photos;
PImage [] stockBackground;
boolean changeBackground = false;
PImage [] Panels;
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
PFont smallFont;
float resizeValue = 100;

//Jason edits for mode 2
boolean removeBackground = false;
int threshold = 70;

// Thom's variables for Milestone 3






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
	Photos = new PImage[20];
	Panels = new PImage[20];

	//added by Jason
	mode2Calibration = webcam.get();

	minim = new Minim(this);
	Snap = minim.loadFile("snap.wav");
	Click = minim.loadFile("click.wav");
	// sound used is from freesound.org
  	// https://www.freesound.org/people/stijn/sounds/43680/
  	// https://www.freesound.org/people/Snapper4298/sounds/178186/
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
		background(255);	
		drawOverview();
	}
	else if(mode == 2)
	{
		background(255);
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
			mode2phase2Buttons();
		}
		else if(phase == 3)
		{
			calibrationPhase();
			mode2phase3buttons();
		}

	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode
		background(255);
		if(phase == 1)
		{
			// show list of taken photos
			mode3displayPhotos();
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
	}
	else if(mode == 4) // edit photo mode
	{
		if(phase == 1)
		{
			// edit photo hub
			background(255);
			displayPhoto(currentPhotoIndex);
			mode4phase1displayButtons();
		}
		else if(phase == 2)
		{
			// simple drawing mode
			mode4phase2draw();
		}
		else if(phase == 3)
		{
            background(255);
            displayResizedPhoto(currentPhotoIndex, resizeValue);
            mode4phase3displayButtons();
		}
		else if(phase == 4)
		{
			// save edits
		}
	}
	else if(mode == 5)
	{
		// Edit Panel mode
		// -simple functions eg: delete a panel
		if(phase == 1)
		{
			background(255);
			displayPanel(currentPanelIndex);
		    mode5phase1displayButtons();	
		}
	}
}


//__________________________________________________________________________________________________________________________
public void captureEvent(Capture video) { video.read(); }


//__________________________________________________________________________________________________________________________
public void keyPressed()
{
	if (key == ' ')
	{
		if( mode == 2 && phase == 1)
		{
			takePhoto();
		}
	}
	if (mode == 4)
	{
		switch(key)
		{
			case('1'):
			// method A to change color
			cp.setArrayValue(new float[] {120, 0, 120, 255});
			break;
			case('2'):
			// method B to change color
			cp.setColorValue(color(255, 0, 0, 255));
			break;
		}
	}
}


//__________________________________________________________________________________________________________________________
public void mousePressed()
{
	switch (mode) 
	{
		case 1: mode1mousePressed();
				break;
		case 3: mode3mousePressed(); 
				break;
		default: break;
	}
}



//__________________________________________________________________________________________________________________________
public void mouseDragged()
{
	if(mode == 4 && phase == 2)
	{
		println("mouseDragged");
 		flag = 1;
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
  	if(c.isFrom(cp))
    {
		int r = PApplet.parseInt(c.getArrayValue(0));
		int g = PApplet.parseInt(c.getArrayValue(1));
		int b = PApplet.parseInt(c.getArrayValue(2));
		int a = PApplet.parseInt(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
}
// Mode 2: Take a picture

//__________________________________________________________________________________________________________________________
public void drawCam()
{  
  textFont(font);
  text("Capture Mode", 20, 40);

  frame = webcam;
  
  //if(removeBackground)
  //	removeBackground(frame);
	
	pushMatrix();

	//flip across x axis
	scale(-1,1);

	if(removeBackground)
		image(removeBackground(frame.get()), -(width - 800)/2 -800, 70, 800, 600);

  else if(changeBackground)
    image(changeBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);

	else
		image(frame, -(width - 800)/2 -800, 70, 800, 600);	
  
	popMatrix(); 
}

public PImage removeBackground(PImage frame)
{       
        
    mode2Calibration.loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        int loc = x + y * frame.width;
        int display = frame.pixels[loc];
        int comparison = mode2Calibration.pixels[loc];
        
        float r1 = red(display); float g1 = green(display); float b1 = blue(display);
        float r2 = red(comparison); float g2 = green(comparison); float b2 = blue(comparison);
        float diff = dist(r1,g1,b1,r2,g2,b2);
        
        if(diff < threshold){
              frame.pixels[loc] = color(255);
        }
        
      }
    }
    frame.updatePixels(); 
    calibratedFrame = frame.get();       
    return calibratedFrame;
}



//__________________________________________________________________________________________________________________________
public void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("takePhoto")
			.setPosition(width/2 - 30, 677)
			.setCaptionLabel("Capture")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(100, 40)
			;

		cp5.addButton("backButton")
			.setPosition(width/2 - 100, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;
                
                cp5.addButton("goToCalibrationPhase")
                        .setPosition(width/2 + 100, 677)
                        .setCaptionLabel("Calibrate")
                        .align(CENTER,CENTER,CENTER,CENTER)
                        .setSize(110, 40)
                        ;

                if(removeBackground){
                cp5.addButton("backgroundSelection")
                        .setPosition(width/2 + 250, 677)
                        .setCaptionLabel("Background")
                        .align(CENTER,CENTER,CENTER,CENTER)
                        .setSize(200, 40)
                        ;
                }
		displayButtons = false;
	}
}



//__________________________________________________________________________________________________________________________
public void mode2phase2Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("mode2phase2save")
			.setPosition(width/2 + 40, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition(width/2 - 50, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

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
			mode2Capture = calibratedFrame.get();
		else if(changeBackground)
                        mode2Capture = editedFrame.get();                
                else
			mode2Capture = frame.get();


	}
	catch(NullPointerException e)
	{
		println("Could not capture frame! Null pointer!");
	}

	phase = 2;
	cp5.hide();
	displayButtons = true;
	mirror(mode2Capture);
	Photos[numPhotos] = mode2Capture;
	numPhotos++;
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
public void backButton()
{
	mode = 1;
	cp5.hide();  
	displayButtons = true;
}

public void goToCalibrationPhase()
{
        phase = 3;
        cp5.hide();
        displayButtons = true;
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
}


//__________________________________________________________________________________________________________________________
public void mode2phase2back()
{
	phase = 1;
	cp5.hide();  
	displayButtons = true;
	numPhotos--;
}

//_________________________________________________________________
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


//-----------------------------
public void mode2phase3buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("takeCalibrationPhoto")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("C")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode2phase3back")
      .setPosition(width/2 - 50, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
  
}

//----------------------------------
public void takeCalibrationPhoto(){
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
//-----------------------------
public void mode2phase3back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
}

//_________________________________________________________________
//_________________________________________________________________
//_________________________________________________________________
//_________________________________________________________________
//_________________________________________________________________
//_________________________________________________________________

public void loadStockBackground()
{
  String path = sketchPath+"/stockbackground/"; //folder of images rename as needed
  File[] files = listFiles(path);
  stockBackground = new PImage[files.length];
  for(int i=0; i<files.length; i++){
    stockBackground[i]=loadImage(files[i].getAbsolutePath());
    imageResize(stockBackground[i]);
  }
}

public void imageResize(PImage img){
  img.resize(640,480);
}

public void backgroundSelection()
{
  loadStockBackground();
  changeBackground = true;
  removeBackground = false;  
}

public PImage changeBackground(PImage frame)
{       
        
    stockBackground[0].loadPixels();
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
// Mode 3: Add a panel
//		Phase 1: show horizontal list of photos to add
//		Phase 2: show clicked photo large '<' button goes back and 'S' button adds clicked photo to panels array


//__________________________________________________________________________________________________________________________
public void mode3displayPhotos()
{
	background(0xff012E4B);
	for(int i = 0; i < numPhotos; i++)
		image(Photos[i], 80 + i*110, height/2, 100, 75);
}



//__________________________________________________________________________________________________________________________
public void displayPhoto(int index)
{
	image(Photos[index], (width - 800)/2, 70, 800, 600);
}

//__________________________________________________________________________________________________________________________
public void displayResizedPhoto(int index, float resize)
{
  image(Photos[index], (width - (800 * (resize/100)))/2, 70 + (300 - (600 * (resize/100)/2)), 800 * (resize/100), 600 * (resize/100));
}



//__________________________________________________________________________________________________________________________
public void mode3phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3phase1back")
      .setPosition(width/2 - 10, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
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
}



//__________________________________________________________________________________________________________________________
public void mode3phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3phase2back")
      .setPosition(width/2 - 150, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode3phase2save")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("S")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode3pase2back()
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
  PImage newPanel = Photos[currentPhotoIndex];
  Panels[numPanels] = newPanel;
  numPanels++;
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
// File: Mode4.pde
// Mode4 is the photo editing hub
//	Phase 1 = editing hub with buttons to different phases
//  Phase 2 = simple drawing mode (ie: ms paint)
//	Phase 2 = selection and removal


//__________________________________________________________________________________________________________________________
public void mode4phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase1back")
      .setPosition(width/2 - 50, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode4phase1draw")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("Draw")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
    
    cp5.addButton("mode4phase1resize")
      .setPosition(width/2 + 100, 677)
      .setCaptionLabel("Resize")
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
public void mode4phase1draw()
{
  println("button: edit photo");
  phase = 2;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
  paint = color(255, 128, 0, 255);
}


//__________________________________________________________________________________________________________________________
public void mode4phase1resize()
{
  println("button: resize photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
  //displayResizePhoto = true;
  resizeValue = 100;
}


//__________________________________________________________________________________________________________________________
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

	stroke(255, 255, 255);
	fill(255, 255, 255);
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
}


//__________________________________________________________________________________________________________________________
public void mode4phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);
    fill(0xff909090);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase2back")
  		.setPosition(width/2 - 50, 677)
  		.setCaptionLabel("<")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(40, 40)
  		;

    cp5.addButton("mode4phase2save")
  		.setPosition(width/2 + 10, 677)
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
    	.setNumberOfTickMarks(10)
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
	Photos[numPhotos] = editPhoto;
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


//__________________________________________________________________________________________________________________________
public void mode4phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase2back")
      .setPosition(width/2 - 50, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;
      
   cp5.addButton("mode4phase2save")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
      
    cp5.addSlider("imageSize")
      .setCaptionLabel("")
      .setPosition((width - 100)/2 - 30, 20)
      .setSize(100, 20)
      .setRange(1, 100)
      .setDefaultValue(100)
      .setValue(100)
      .setNumberOfTickMarks(50)
      ;

    cp5.getController("imageSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    displayButtons = false;
  }
}


public void imageSize(float value)
{
  println(value);
  resizeValue = value;
}


// Edit Panel mode



//__________________________________________________________________________________________________________________________
public void displayPanel(int index)
{
	image(Panels[index], (width - 800)/2, 70, 800, 600);
}


//__________________________________________________________________________________________________________________________
public void mode5phase1displayButtons()
{
	if(displayButtons)
  	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode5phase1back")
	      .setPosition((width-800)/2, 677)
	      .setCaptionLabel("<")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(40, 40)
	      ;

	    cp5.addButton("mode5phase1delete")
	      .setPosition(width/2 - 20, 677)
	      .setCaptionLabel("Delete")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(90, 40)
	      ;
	    
	    cp5.addButton("mode5phase1left")
	      .setPosition(width/2 + 100, 677)
	      .setCaptionLabel("Left")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    cp5.addButton("mode5phase1right")
	      .setPosition(width/2 + 200, 677)
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
  	for(int i = currentPanelIndex + 1; i < numPanels; i++)
  	{
  		Panels[i - 1] = Panels[i];
  	}
  	numPanels--;
}




//__________________________________________________________________________________________________________________________
public void mode5phase1left()
{
	println("button: back to photo list");
	mode = 1;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}



// Mode 0: Start Screen
// Mode 1: Overview

//__________________________________________________________________________________________________________________________
public void drawStartScreen()
{
  PImage startLogo = loadImage("logo.png");

  float x = (width - startLogo.width)/2;
  float y = (height - startLogo.height)/2;
  image(startLogo, x, y);  
}



//__________________________________________________________________________________________________________________________
public void drawOverview()
{
  background(0xff012E4B);
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
  for(int i = 0; i < numPhotos; i++)
  {
    image(Photos[i], 80 + i*90, 140, 80, 60);

    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= 140
      && mouseY <= 140 + 60)
      text("Edit", 80 + i*90, 140 + 35);
  }
  for(int i = 0; i < numPanels; i++) 
  {
    image(Panels[i], 80 + i*90, (height/2 + 40), 80, 60);

    // show "Edit" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
      text("Edit", 80 + i*90, (height/2 + 40 + 35));
  }
  if(displayExportedComic)
  {
    fill(0xff817575);
    float ratio = (exportedComic.width/exportedComic.height);
    image(exportedComic, 80, (height/2 + 190), 100*ratio, 100);
    text("Exported Comic: ", 80, (height/2 + 150));
  }
}



//__________________________________________________________________________________________________________________________
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



//__________________________________________________________________________________________________________________________
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
    image(Photos[currentPhotoIndex + i], 80, (height/2 + 40) + i*70, 80, 60);
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
  println("Export button pressed ");

  if(numPanels > 0)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 15;
    PImage comicStrip = createImage(border + (border + 640)*numPanels, 480 + 2 * border, RGB);
    comicStrip.loadPixels();

    for (int i = 0; i < (border + (border + 640)*numPanels); i++)
      for (int j = 0; j < 480 + 2 * border; j++)
        comicStrip.pixels[j*(border + (border + 640)*numPanels) + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    for(int i = 0; i < numPanels; i++)
    {
      int cX = border + (640 + border) * i;
      int cY = border;
      Panels[i].loadPixels();
      for(int x = 0; x < 640; x++)
      {
        for(int y = 0; y < 480; y++)
        {
          comicStrip.pixels[y*comicStrip.width + cY*comicStrip.width + cX + x] = Panels[i].pixels[y*640 + x];
        }
      }
    }
    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }
}



//__________________________________________________________________________________________________________________________
public void addPanel()
{
  println("+panel button pressed");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
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

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ComicYourself" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
