
// group members:    Thomas Cassady & Jason Huang
// class:  	CSC 690
// project: Final
// date:    11/13/2014
// file:	ComicYourself.pde


//__________________________________________________________________________________________________________________________
import processing.video.*;
import java.awt.*;
import controlP5.*;
import ddf.minim.* ;




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
color paint = color(0);
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
int [] Layers;
int [] LayersX;
int [] LayersY;
int numLayers = 0;





//__________________________________________________________________________________________________________________________
void setup()
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
	Layers = new int[10];
	LayersX = new int[10];
	LayersY = new int[10];

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
File[] listFiles(String directory)
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
void draw()
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
  			fill(#817575);
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
			textFont(font);
  			fill(#817575);
  			background(#012E4B);
  			text("Edit Photo", 20, 40);
			displayPhoto(currentPhotoIndex);
			mode4phase1displayButtons();
		}
		else if(phase == 2)
		{
			// simple drawing mode
			textFont(font);
  			fill(#817575);
			text("Draw", 20, 40);
			mode4phase2draw();
		}
		else if(phase == 3)
		{
			textFont(font);
  			fill(#817575);
            background(#012E4B);
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
void captureEvent(Capture video) { video.read(); }


//__________________________________________________________________________________________________________________________
void keyPressed()
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
void mousePressed()
{
	switch (mode) 
	{
		case 1: mode1mousePressed();
				break;
		case 3: mode3mousePressed(); 
				break;
		case 4: mode4mousePressed();
				break;
		default: break;
	}
}



//__________________________________________________________________________________________________________________________
void mouseDragged()
{
	if(mode == 4 && phase == 2)
	{
		println("mouseDragged");
 		flag = 1;
 	}
}


//__________________________________________________________________________________________________________________________
void mouseReleased()
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
		int r = int(c.getArrayValue(0));
		int g = int(c.getArrayValue(1));
		int b = int(c.getArrayValue(2));
		int a = int(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
}
