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
int photoIndex = 0;
int mode = 0;
int phase = 1;
PImage frame, mode2Capture;
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
	Photos = new PImage[20];
	Panels = new PImage[20];
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
			mode2phase1Buttons();
		}
		else if(phase == 2)
		{
			// show picture taken as freeze frame
			displayPhoto(numPhotos - 1);
			mode2phase2Buttons();
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
			mode3displayPhotos();
		}
		else if(phase == 2)
		{
			// show photo that user clicked large
			// display save or discard buttons
			displayPhoto(photoIndex);
			mode3displayButtons();
		}
		else if(phase == 3)
		{
			// save photo in panel array
		}
	}
}



//__________________________________________________________________________________________________________________________
public void captureEvent(Capture video) 
{
  video.read();
}



//__________________________________________________________________________________________________________________________
public void keyPressed()
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


public void mousePressed()
{
	switch (mode) 
	{
		case 3: mode3mousePressed(); 
				break;
		
	}
}

// Mode 2: Take a picture

//__________________________________________________________________________________________________________________________
public void drawCam()
{
	frame = webcam;

	pushMatrix();

	//flip across x axis
	scale(-1,1);
	image(frame, -700, 50, 640, 480);
	popMatrix(); 
}



//__________________________________________________________________________________________________________________________
public void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("takePhoto")
			.setPosition(800, 200)
			.setCaptionLabel("C")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addButton("backButton")
			.setPosition(800, 300)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

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
			.setPosition(width/2 + 10, 675)
			.setCaptionLabel("S")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition(width/2 - 50, 675)
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
	try
	{
		mode2Capture = frame.get();
	}
	catch(NullPointerException e)
	{
		println("Could not capture frame! Null pointer!");
	}

	phase = 2;
	cp5.hide();
	displayButtons = true;
	Photos[numPhotos] = mode2Capture;
	numPhotos++;
}



//__________________________________________________________________________________________________________________________
public void backButton()
{
	mode = 1;
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
}


//__________________________________________________________________________________________________________________________
public void mode2phase2back()
{
	phase = 1;
	cp5.hide();  
	displayButtons = true;
	numPhotos--;
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
	image(Photos[index], 100, 75, 800, 600);
}



//__________________________________________________________________________________________________________________________
public void mode3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3back")
      .setPosition(width/2 - 50, 680)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode3save")
      .setPosition(width/2 + 10, 680)
      .setCaptionLabel("S")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode3back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode3save()
{
  println("button: save panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newPanel = Photos[photoIndex];
  Panels[numPanels] = newPanel;
  numPanels++;
}



//__________________________________________________________________________________________________________________________
public void mode3mousePressed()
{
	//image(Photos[i], 80 + i*110, (height/2 + 40), 100, 75);
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
				photoIndex = i;
				phase = 2;
			}
		}
	}
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
  for(int i = 0; i < numPhotos; i++)
    image(Photos[i], 80 + i*90, 140, 80, 60);
  for(int i = 0; i < numPanels; i++)  
    image(Panels[i], 80 + i*90, (height/2 + 40), 80, 60);

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

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ComicYourself" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
