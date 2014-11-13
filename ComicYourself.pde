
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
int mode = 0;
int phase = 0;
PFont font;
ControlP5 cp5;



//__________________________________________________________________________________________________________________________
void setup()
{
	size(1080, 720);
	font = createFont("", 20);
  	textFont(font, 20);
	webcam = new Capture(this, 640, 480);


	displayStart();

}



//__________________________________________________________________________________________________________________________
void draw()
{
	background(255);	

	if(mode == 0)
	{
		// START SCREEN mode
		PImage startLogo = loadImage("logo.png");

		float x = (width - startLogo.width)/2;
		float y = (height - startLogo.height)/2;
		image(startLogo, x, y);

	}
	else if(mode == 1)
	{
		// OVERVIEW mode
	}
	else if(mode == 2)
	{
		// TAKE A PHOTO mode
		if(phase == 0)
		{
			// show live feed
			//opencv.loadImage(webcam);
			image(webcam, 0, 0);
		}
		else if(phase == 1)
		{
			// show picture taken as freeze frame
		}
		else if(phase == 2)
		{
			// save pitcure to photo array
		}
	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode
		if(phase == 0)
		{
			// show list of taken photos
			//displayPhotos();
		}
		else if(phase == 1)
		{
			// show photo that user clicked large
			// display save or discard buttons
			//displayPhoto(photoIndex);
		}
		else if(phase == 2)
		{
			// save photo in panel array
		}
	}

	
}

void displayStart()
{
	cp5 = new ControlP5(this);

	cp5.addButton("Start")
		.setPosition((width - 80)/2, 650)
		.setSize(80, 30)
		//.setValue(100)
		;	
}


public void Start()
{
	println("Start button pressed ");
	mode = 1;
}



//__________________________________________________________________________________________________________________________
void captureEvent(Capture video) 
{
  video.read();
}