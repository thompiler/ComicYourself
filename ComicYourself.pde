
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
int phase = 1;
PFont font;
ControlP5 cp5;
boolean displayButtons = true;



//__________________________________________________________________________________________________________________________
void setup()
{
	size(1080, 720);
	font = loadFont("CordiaNew-Bold-30.vlw");
  	textFont(font);
	webcam = new Capture(this, 640, 480);

	displayStartButton();
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
			image(webcam, 0, 0);
		}
		else if(phase == 2)
		{
			// show picture taken as freeze frame
		}
		else if(phase == 3)
		{
			// save pitcure to photo array
		}
	}
	else if(mode == 3)
	{
		// MAKE A PANEL mode
		if(phase == 1)
		{
			// show list of taken photos
			//displayPhotos();
		}
		else if(phase == 2)
		{
			// show photo that user clicked large
			// display save or discard buttons
			//displayPhoto(photoIndex);
		}
		else if(phase == 3)
		{
			// save photo in panel array
		}
	}
}


//__________________________________________________________________________________________________________________________
void drawStartScreen()
{
	PImage startLogo = loadImage("logo.png");

	float x = (width - startLogo.width)/2;
	float y = (height - startLogo.height)/2;
	image(startLogo, x, y);	
}



//__________________________________________________________________________________________________________________________
void drawOverview()
{
	background(#012E4B);
	fill(#EAA3A3);
	font = loadFont("ArialMT-40.vlw");

	textFont(font);
	text("OVERVIEW", 20, 40);

	fill(#817575);
	text("photos", 60, 100);
	text("panels", 60, height/2);

	displayAddButtons();
}



//__________________________________________________________________________________________________________________________
void displayStartButton()
{
	cp5 = new ControlP5(this);

	cp5.setControlFont(font);

	cp5.addButton("Start")
		.setPosition((width - 80)/2, 650)
		.setSize(80, 40)
		;
}



//__________________________________________________________________________________________________________________________
void displayAddButtons()
{
	if(displayButtons)
	{
		println("  Add Buttons");
		cp5.addButton("Add Photo")
			.setPosition(200, 100)
			.setSize(200, height/2)
			;
		cp5.addButton("Add Panel")
			.setPosition((width - 80)/2, 650)
			.setSize(80, 40)
			;
		displayButtons = false;
	}
}



//__________________________________________________________________________________________________________________________
public void Start()
{
	println("Start button pressed ");
	mode = 1;
	cp5.hide();
}



//__________________________________________________________________________________________________________________________
void captureEvent(Capture video) 
{
  video.read();
}

