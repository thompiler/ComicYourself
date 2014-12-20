
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
color paint = color(0);
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
int backgroundColor = #464646;
CColor backButtonColor = new CColor(#9B1919, #D86262, #FFFFFF, #FFFFFF, #FFFFFF);


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
			println("numPhotos: "+numPhotos);
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
  			fill(#817575);
  			background(backgroundColor);
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
			// Simple resize of full photo
			textFont(font);
  			fill(#817575);
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
    else if (mode==8)
    {
      mode8draw();
      mode8dispayButon();
    
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
			takePhoto();
		if( mode == 2 && phase == 3)
			takeCalibrationPhoto();
	}
}


//__________________________________________________________________________________________________________________________
void mousePressed()
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
		case 8: mode8mousePressed();
                                break;
                default: break;
	}
}

void mouseClicked(){
  if(mode==8){
    mode8mouseClicked();
  }
}


//__________________________________________________________________________________________________________________________
void mouseDragged()
{
	if(mode == 4) 
	{
		if(phase == 2)
		{
			println("mouseDragged");
 			flag = 1;
 		}
 		if(phase == 4 && numLayers > 0)
 		{
 			LayersX.add(mouseX);
 			LayersY.add(mouseY);
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
        else if(mode == 8){
          mode8mouseDragged();
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
        if(mode == 8){
            mode8mouseReleased();
        }
}


//__________________________________________________________________________________________________________________________
public void controlEvent(ControlEvent c)
{
	// For use in Mode 4: Edit Photo
	// This function sends the values from the color slider into the paint variable
  	if(mode == 4 && c.isFrom(cp))
    {
		int r = int(c.getArrayValue(0));
		int g = int(c.getArrayValue(1));
		int b = int(c.getArrayValue(2));
		int a = int(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
  	else if(mode == 6 && c.isFrom(cp))
    {
		int r = int(c.getArrayValue(0));
		int g = int(c.getArrayValue(1));
		int b = int(c.getArrayValue(2));
		int a = int(c.getArrayValue(3));
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
