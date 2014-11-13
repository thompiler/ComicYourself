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
int mode = 0;
int phase = 0;
PFont font;
ControlP5 cp5;
boolean displayButtons = true;



//__________________________________________________________________________________________________________________________
public void setup()
{
	size(1080, 720);
	font = loadFont("CordiaNew-Bold-30.vlw");
  	textFont(font);
	webcam = new Capture(this, 640, 480);

	displayStartButton();
}



//__________________________________________________________________________________________________________________________
public void draw()
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
		background(0xff012E4B);
		fill(0xffEAA3A3);
		font = loadFont("ArialMT-40.vlw");

		textFont(font);
		text("OVERVIEW", 20, 40);

		fill(0xff817575);
		text("photos", 60, 100);
		text("panels", 60, height/2);

		displayAddButtons();
	}
	else if(mode == 2)
	{
		// TAKE A PHOTO mode
		if(phase == 0)
		{
			// show live feed
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

public void displayStartButton()
{
	cp5 = new ControlP5(this);

	cp5.setControlFont(font);

	cp5.addButton("Start")
		.setPosition((width - 80)/2, 650)
		.setSize(80, 40)
		;
}



public void displayAddButtons()
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



public void Start()
{
	println("Start button pressed ");
	mode = 1;
	cp5.hide();
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
