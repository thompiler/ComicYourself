
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
Minim minim;
AudioPlayer Snap, Click;

//mode 4 variables:
color paint = color(0);
int strokeWt = 1;
int flag = 0;
PImage editPhoto;
boolean displayPhoto = true;




//__________________________________________________________________________________________________________________________
void setup()
{
	size(1080, 720);
	background(255);

	buttonFont = loadFont("CordiaNew-Bold-30.vlw");
  	textFont(buttonFont);
	webcam = new Capture(this, 640, 480);
	webcam.start();

	displayStartButton();
	Photos = new PImage[20];
	Panels = new PImage[20];

	minim = new Minim(this);
	Snap = minim.loadFile("snap.wav");
	Click = minim.loadFile("click.wav");
	// sound used is from freesound.org
  	// https://www.freesound.org/people/stijn/sounds/43680/
  	// https://www.freesound.org/people/Snapper4298/sounds/178186/
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
			displayPhoto(numPhotos - 1);
			mode2phase2Buttons();
		}
	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode]
		background(255);
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
	else if(mode == 4)
	{
		if(phase == 1)
		{
			// edit photo hub
			background(255);
			displayPhoto(photoIndex);
			mode4phase1displayButtons();
		}
		else if(phase == 2)
		{
			// simple drawing mode
			mode4phase2draw();
		}
		else if(phase == 3)
		{

		}
		else if(phase == 4)
		{
			// save edits
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
			takePhoto();
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

