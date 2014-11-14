import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import java.awt.*; 
import controlP5.*; 
import gab.opencv.*; 

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
PImage [] Panels;
int numPhotos = 0;
int numPanels = 0;
int currPhotoIndex = 0;
int mode = 0;
int phase = 1;
PImage frame;
PFont font;
ControlP5 cp5;
boolean displayButtons = true;
PFont buttonFont;



//__________________________________________________________________________________________________________________________
public void setup()
{
	size(1080, 720);
	buttonFont = loadFont("CordiaNew-Bold-30.vlw");
  	textFont(buttonFont);
	webcam = new Capture(this, 640, 480);
	webcam.start();

	displayStartButton();
}



//__________________________________________________________________________________________________________________________
public void draw()
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
			displayPhotos();
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

	for(int i = 0; i < numPhotos; i++)
		image(Photos[i], 80, 140 + i*70, 80, 60);

	for(int i = 0; i < numPanels; i++)
		image(Panels[i], 80, (height/2 + 40) + i*70, 80, 60);
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

		displayButtons = false;
	}
}



//__________________________________________________________________________________________________________________________
public void drawCam()
{
	println("---drawcam called");
	frame = webcam.get();
    pushMatrix();

    //flip across x axis
    scale(-1,1);
    image(frame, -width, 0, width*(3/4), height*(3/4));
    popMatrix();
}



//__________________________________________________________________________________________________________________________
public void displayPhotos()
{
	for(int i = 0; i < numPhotos - currPhotoIndex; i++)
	{
		image(Photos[currPhotoIndex + i], 80, (height/2 + 40) + i*70, 80, 60);
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
public void addPanel()
{
	println("+panel button pressed");
	mode = 3;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void captureEvent(Capture video) 
{
  video.read();
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
