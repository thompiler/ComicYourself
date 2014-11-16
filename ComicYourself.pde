
// group members:    Thomas Cassady & Jason Huang
// class:  	CSC 690
// project: Final
// date:    11/13/2014
// file:	ComicYourself.pde


//__________________________________________________________________________________________________________________________
import processing.video.*;
import java.awt.*;
import controlP5.*;
import gab.opencv.*;



//__________________________________________________________________________________________________________________________
Capture webcam;
PImage [] Photos;
PImage [] Panels;
int numPhotos = 0;
int numPanels = 0;
int currPhotoIndex = 0;
int photoIndex = 0;
int mode = 0;
int phase = 1;
PImage frame, mode2Capture;
PFont font;
ControlP5 cp5;
boolean displayButtons = true;
PFont buttonFont;



//__________________________________________________________________________________________________________________________
void setup()
{
	size(1080, 720);
	buttonFont = loadFont("CordiaNew-Bold-30.vlw");
  	textFont(buttonFont);
	webcam = new Capture(this, 640, 480);
	webcam.start();

	displayStartButton();
	Photos = new PImage[20];
	Panels = new PImage[20];
}



//__________________________________________________________________________________________________________________________
void draw()
{
	background(255);	

	if(mode == 0)
	{
		// START SCREEN mode
		drawStartScreen();
	}
	else if(mode == 1)
	{
		// OVERVIEW mode
		drawOverview();
	}
	else if(mode == 2)
	{
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
			displayPhoto(numPhotos - 1);
			mode2phase2Buttons();
		}
	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode
		if(phase == 1)
		{
			// show list of taken photos
			mode3displayPhotos();
		}
		else if(phase == 2)
		{
			// show photo that user clicked large
			// display save or discard buttons
			displayPhoto(photoIndex);
			mode3displayButtons();
		}
	}
}



//__________________________________________________________________________________________________________________________
void captureEvent(Capture video) 
{
  video.read();
}



//__________________________________________________________________________________________________________________________
void keyPressed()
{
	if (key == ' ')
	{
		if( mode == 2 && phase == 1)
		{
			try
			{
				mode2Capture = frame.get();      //takes the PImage frame and saves it to PImage mode2Capture
			}
			catch(NullPointerException e)
			{
				println("No picture taken! No webcam found!");
			}
			phase = 2;
			displayButtons = true;
		}
	}
}



//__________________________________________________________________________________________________________________________
void mousePressed()
{
	switch (mode) 
	{
		case 3: mode3mousePressed(); 
				break;
		default: break;
	}
}

