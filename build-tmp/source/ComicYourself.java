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
int paint = color(0);
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
int backgroundColor = 0xff464646;
CColor backButtonColor = new CColor(0xff9B1919, 0xffD86262, 0xffFFFFFF, 0xffFFFFFF, 0xffFFFFFF);
int displayIndex = 0;

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
			mode3displayPhotos(displayIndex);
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
  			fill(0xff817575);
  			background(backgroundColor);
  			text("Edit Photo", 20, 40);
			displayPhoto(currentPhotoIndex);
			mode4phase1displayButtons();
		}
		else if(phase == 2)
		{
			// simple drawing mode
			textFont(font);
  			fill(0xff817575);
			text("Draw", 20, 40);
			mode4phase2draw();
		}
		else if(phase == 3)
		{
			// Simple resize of full photo
			textFont(font);
  			fill(0xff817575);
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
public void captureEvent(Capture video) { video.read(); }


//__________________________________________________________________________________________________________________________
public void keyPressed()
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
public void mousePressed()
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

public void mouseClicked(){
  if(mode==8){
    mode8mouseClicked();
  }
}


//__________________________________________________________________________________________________________________________
public void mouseDragged()
{
	if(mode == 4) 
	{
		if(phase == 2)
		{
			//println("mouseDragged");
 			flag = 1;
 		}
 		if(phase == 4 && numLayers > 0)
 		{
 			println("x: "+mouseX+" y:"+mouseY);
 			LayersX.set(numLayers-1, mouseX);
 			LayersY.set(numLayers-1, mouseY);
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
public void mouseReleased()
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
		int r = PApplet.parseInt(c.getArrayValue(0));
		int g = PApplet.parseInt(c.getArrayValue(1));
		int b = PApplet.parseInt(c.getArrayValue(2));
		int a = PApplet.parseInt(c.getArrayValue(3));
		paint = color(r, g, b, a);
		println("event\talpha:"+a+"\tred:"+r+"\tgreen:"+g+"\tblue:"+b+"\tcol"+paint);
  	}
  	else if(mode == 6 && c.isFrom(cp))
    {
		int r = PApplet.parseInt(c.getArrayValue(0));
		int g = PApplet.parseInt(c.getArrayValue(1));
		int b = PApplet.parseInt(c.getArrayValue(2));
		int a = PApplet.parseInt(c.getArrayValue(3));
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
PImage[] photos;
PImage[] gallery;
PImage draggedImage;
PImage[] boxImage;
PImage[] hboxImage;
PImage[] vboxImage;
int selectedPicture;
int imageX, imageY;
boolean drawDragImage;
boolean drawBoxImage;
boolean vb1, vb2, vb3, vb4, vb5, vb6, vb7, vb8, vb9; 
boolean hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9; 
boolean box1, box2, box3, box4, box5, box6, box7, box8, box9;
//half horizontal boxes //half vertical boxes
boolean b1h1, b1h2, b1v1, b1v2, 
        b2h1, b2h2, b2v1, b2v2, 
        b3h1, b3h2, b3v1, b3v2, 
        b4h1, b4h2, b4v1, b4v2, 
        b5h1, b5h2, b5v1, b5v2, 
        b6h1, b6h2, b6v1, b6v2, 
        b7h1, b7h2, b7v1, b7v2, 
        b8h1, b8h2, b8v1, b8v2, 
        b9h1, b9h2, b9v1, b9v2;
        
int b1h1y, b1h2y, b1v1x, b1v2x, 
    b2h1y, b2h2y, b2v1x, b2v2x, 
    b3h1y, b3h2y, b3v1x, b3v2x, 
    b4h1y, b4h2y, b4v1x, b4v2x, 
    b5h1y, b5h2y, b5v1x, b5v2x, 
    b6h1y, b6h2y, b6v1x, b6v2x, 
    b7h1y, b7h2y, b7v1x, b7v2x, 
    b8h1y, b8h2y, b8v1x, b8v2x, 
    b9h1y, b9h2y, b9v1x, b9v2x;
    
int fb1h1y, fb1h2y, fb1v1x, fb1v2x, 
    fb2h1y, fb2h2y, fb2v1x, fb2v2x, 
    fb3h1y, fb3h2y, fb3v1x, fb3v2x, 
    fb4h1y, fb4h2y, fb4v1x, fb4v2x, 
    fb5h1y, fb5h2y, fb5v1x, fb5v2x, 
    fb6h1y, fb6h2y, fb6v1x, fb6v2x, 
    fb7h1y, fb7h2y, fb7v1x, fb7v2x, 
    fb8h1y, fb8h2y, fb8v1x, fb8v2x, 
    fb9h1y, fb9h2y, fb9v1x, fb9v2x;
        
boolean drawBox1Image, drawBox2Image, drawBox3Image,
        drawBox4Image, drawBox5Image, drawBox6Image, 
        drawBox7Image, drawBox8Image, drawBox9Image;
        
boolean db1h1, db1h2, db1v1, db1v2, 
        db2h1, db2h2, db2v1, db2v2, 
        db3h1, db3h2, db3v1, db3v2, 
        db4h1, db4h2, db4v1, db4v2, 
        db5h1, db5h2, db5v1, db5v2, 
        db6h1, db6h2, db6v1, db6v2, 
        db7h1, db7h2, db7v1, db7v2, 
        db8h1, db8h2, db8v1, db8v2, 
        db9h1, db9h2, db9v1, db9v2;
        
boolean imageSelected;

boolean b2l,b3l,b4l,b5l,b6l,b7l,b8l,b9l;


public void Jassetup()
{
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  background(200);
  strokeCap(SQUARE);

  vb1=false; vb2=false; vb3=false;
  vb4=false; vb5=false; vb6=false;
  vb7=false; vb8=false; vb9=false;
  
  hb1=false; hb2=false; hb3=false;
  hb4=false; hb5=false; hb6=false;
  hb7=false; hb8=false; hb9=false;

  drawDragImage=false;
  imageX=0;
  imageY=0;
  
  boxImage = new PImage[9];
  hboxImage = new PImage[18];
  vboxImage = new PImage[18];
  
/*  
  String path = sketchPath+"/data/"; //folder of images rename as needed
  File[] files = listFiles(path);
  photos = new PImage[files.length];
  for(int i=0; i<files.length; i++){
    photos[i]=loadImage(files[i].getAbsolutePath());
  }
  */
}

public void mode8dispayButon(){
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);
  
  cp5.addButton("mode2phase1back")
      .setPosition(10, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;
      
      displayButtons = false;
  }

}
/*
File[] listFiles(String directory){
  File file = new File(directory);
  if(file.isDirectory()){
    File[] files = file.listFiles();
    return files;
  } else {
    return null;
  }
}

void drawImageGallery(){
  //loading and resizing gallery
  PImage[] gallery = new PImage[photos.length];
  for(int i=0; i<photos.length; i++){
    gallery[i] = photos[i].get();
    if(gallery[i].height > 50)
      gallery[i].resize(0,50);
    if(gallery[i].width > 50)
      gallery[i].resize(50,0);
  }
  
  
  //gallery display on
  int i = 0;
  for(int col = 0; col < 6; col++){
    for(int row = 0; row < 2; row++){      
      imageMode(CORNER);
      image(gallery[i], (750+(row*75)), (50+(col*75)));
      i++;
    }
  }
  
}
*/



public void mode8draw()
{  
  background(200);
  drawBoxImage();
  comicPage();
  //drawImageGallery();
  drawImageDragging();
  
}

public void comicPage(){
  
  
  //topline
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,52,258,52);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,52,462,52);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,52,666,52);
  
/////////////////////////////////////////////////////////////////// 
//1
///////////////////////////////////////////////////////////////////
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,50,52,208);             //|                                        
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,50,256,208);                     //|                              
  //first row vertical
  //left third line                                    //|                    
//  if(b2l){
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,50,460,208);                                //|                    
//  } else {
  
//  }
  //first row vertical
  //left fourth line                                             //|          
//  if(b3l){
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,50,664,208);                                          //|        
//  } else {
  
//  }
  
  //first row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,206,258,206);
  //center line                               2_________3
//  if(b2l){
  strokeWeight(4);
  stroke(255);
  line(254,206,462,206);
//  } else {
  
//  }
  //right line                                          3_________4
//  if(b3l){
  strokeWeight(4);
  stroke(255);
  line(458,206,666,206);
//  } else {
//  }
/////////////////////////////////////////////////////////////////// 
//2
///////////////////////////////////////////////////////////////////
  //second row vertical           //|
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,204,52,362);            //|                                        
  
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,204,256,362);                    //|                              
  //first row vertical
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,204,460,362);                               //|                    
  //first row vertical
  //left fourth line                                             //|          
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,204,664,362);                                         //|        
  
  //second row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,360,258,360);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,360,462,360);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,360,666,360);
  
/////////////////////////////////////////////////////////////////// 
//3
///////////////////////////////////////////////////////////////////
  //third row vertical            //|
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,358,52,516);            //|                                        
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,358,256,516);                    //|                              
  //first row vertical
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,358,460,516);                               //|                    
  //first row vertical
  //left fourth line                                             //|          
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,358,664,516);                                         //|        
  
  //third row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,514,258,514);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,514,462,514);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,514,666,514);
  
/////////////////////////////////////////////////////////////////// 
//1
///////////////////////////////////////////////////////////////////  
  //vertical half row1 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb1){
    stroke(255);
  } else {  
    stroke(210);                 //|                                        
  }
  line(154,50,154,208);           //|                                          
  
  //vertical half row1 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb2){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,50,358,208);                     //|                                
  
  //vertical half row1 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb3){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,50,562,208);                                //|                    
  
  //horizontal half row1 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb1){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(50,129,258,129);
  //horizontal half row1 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb2){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(254,129,462,129);
  //horizontal half row1 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb3){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(458,129,666,129);

/////////////////////////////////////////////////////////////////// 
//2
///////////////////////////////////////////////////////////////////  
  //vertical half row2 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb4){
    stroke(255);
  } else {
    stroke(210);                 //|                                        
  }
  line(154,204,154,362);          //|                                          
  
  //vertical half row2 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb5){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,204,358,362);                    //|                                
  
  //vertical half row2 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb6){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,204,562,362);                               //|                    
  
  //horizontal half row2 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb4){
    stroke(255);
  } else {
    stroke(210);
  }
  line(50,283,258,283);
  //horizontal half row2 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb5){
    stroke(255);
  } else {
    stroke(210);
  }
  line(254,283,462,283);
  //horizontal half row2 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb6){
    stroke(255);
  } else {
    stroke(210);
  }
  line(458,283,666,283);

/////////////////////////////////////////////////////////////////// 
//3
///////////////////////////////////////////////////////////////////
  //vertical half row3 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb7){
    stroke(255);
  } else {
    stroke(210);                 //|                                        
  }
  line(154,358,154,516);          //|                                          
  
  //vertical half row3 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb8){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,358,358,516);                    //|                                
  
  //vertical half row3 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb9){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,358,562,516);                               //|                    
  
  //horizontal half row3 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb7){
    stroke(255);
  } else {
    stroke(210);
  }
  line(50,437,258,437);
  //horizontal half row3 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb8){
    stroke(255);
  } else {
    stroke(210);
  }
  line(254,437,462,437);
  //horizontal half row3 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb9){
    stroke(255);
  } else {
    stroke(210);
  }
  line(458,437,666,437);  
}







public void drawImageDragging(){
  if(imageSelected){
  if(drawDragImage){
    imageMode(CENTER);
    image(draggedImage, imageX, imageY);
  }
  
  if(drawBoxImage){
  if(box1){
    imageMode(CENTER);
    image(draggedImage, 154, 129);
  }
  if(b1v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b1v1x),0,98,150), 103, 129);
  }
  if(b1v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b1v2x),0,98,150), 205, 129);
  }
  if(b1h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b1h1y,200,73), 154, 90);
  }
  if(b1h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b1h2y,200,73), 154, 167);
  }
  
  if(box2){
    imageMode(CENTER);
    image(draggedImage, 358, 129);
  }
  if(b2v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b2v1x),0,98,150), 307, 129);
  }
  if(b2v2){
    imageMode(CENTER);
    image(draggedImage.get((460-b2v2x),0,98,150), 409, 129);
  }
  if(b2h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b2h1y,200,73), 358, 90);
  }
  if(b2h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b2h2y,200,73), 358, 167);
  }
  
  if(box3){
    imageMode(CENTER);
    image(draggedImage, 562, 129);
  }
  if(b3v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b3v1x),0,98,150), 511, 129);
  }
  if(b3v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b3v2x),0,98,150), 613, 129);
  }
  if(b3h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b3h1y,200,73), 562, 90);
  }
  if(b3h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b3h2y,200,73), 562, 167);
  }
  
  
  
  if(box4){
    imageMode(CENTER);
    image(draggedImage, 154, 283);
  }
  if(b4v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b4v1x),0,98,150), 103, 283);
  }
  if(b4v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b4v2x),0,98,150), 205, 283);
  }
  if(b4h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b4h1y,200,73), 154, 244);
  }
  if(b4h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b4h2y,200,73), 154, 321);
  }
  
  
  
  if(box5){
    imageMode(CENTER);
    image(draggedImage, 358, 283);
  }
  if(b5v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b5v1x),0,98,150), 307, 283);
  }
  if(b5v2){
    imageMode(CENTER);
    image(draggedImage.get((458-b5v2x),0,98,150), 409, 283);
  }
  if(b5h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b5h1y,200,73), 358, 244);
  }
  if(b5h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b5h2y,200,73), 358, 321);
  }
  
  
  
  if(box6){
    imageMode(CENTER);
    image(draggedImage, 562, 283);
  }
  if(b6v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b6v1x),0,98,150), 511, 283);
  }
  if(b6v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b6v2x),0,98,150), 613, 283);
  }
  if(b6h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b6h1y,200,73), 562, 244);
  }
  if(b6h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b6h2y,200,73), 562, 321);
  }
  
  
  if(box7){
    imageMode(CENTER);
    image(draggedImage, 154, 437);
  }
  if(b7v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b7v1x),0,98,150), 103, 437);
  }
  if(b7v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b7v2x),0,98,150), 205, 437);
  }
  if(b7h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b7h1y,200,73), 154, 398);
  }
  if(b7h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b7h2y,200,73), 154, 475);
  }
  
  
  
  if(box8){
    imageMode(CENTER);
    image(draggedImage, 358, 437);
  }
  if(b8v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b8v1x),0,98,150), 307, 437);
  }
  if(b8v2){
    imageMode(CENTER);
    image(draggedImage.get((460-b8v2x),0,98,150), 409, 437);
  }
  if(b8h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b8h1y,200,73), 358, 398);
  }
  if(b8h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b8h2y,200,73), 358, 475);
  }
  
  
  if(box9){
    imageMode(CENTER);
    image(draggedImage, 562, 437);
  }
  if(b9v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b9v1x),0,98,150), 511, 437);
  }
  if(b9v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b9v2x),0,98,150), 613, 437);
  }
  if(b9h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b9h1y,200,73), 562, 398);
  }
  if(b9h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b9h2y,200,73), 562, 475);
  }
    
  }
  }
}



public void drawBoxImage(){
  //box1
  if(drawBox1Image && !(vb1||hb1)){
    imageMode(CENTER);
    image(boxImage[0], 154, 129);
  }
  if(db1v1 && vb1){
    imageMode(CENTER);
    image(vboxImage[0].get((fb1v1x),0,98,150), 103, 129);
  }
  if(db1v2 && vb1){
    imageMode(CENTER);
    image(vboxImage[1].get((fb1v2x),0,98,150), 205, 129);
  }
  if(db1h1 && hb1){
    imageMode(CENTER);
    image(hboxImage[0].get(0,fb1h1y,200,73), 154, 90);
  }
  if(db1h2 && hb1){
    imageMode(CENTER);
    image(hboxImage[1].get(0,fb1h2y,200,73), 154, 167);
  }
  
  //box2
  if(drawBox2Image && !(vb2||hb2)){
    imageMode(CENTER);
    image(boxImage[1], 358, 129);
  }
  if(db2v1 && vb2){
    imageMode(CENTER);
    image(vboxImage[2].get((fb2v1x),0,98,150), 307, 129);
  }
  if(db2v2 && vb2){
    imageMode(CENTER);
    image(vboxImage[3].get((fb2v2x),0,98,150), 409, 129);
  }
  if(db2h1 && hb2){
    imageMode(CENTER);
    image(hboxImage[2].get(0,fb2h1y,200,73), 358, 90);
  }
  if(db2h2 && hb2){
    imageMode(CENTER);
    image(hboxImage[3].get(0,fb2h2y,200,73), 358, 167);
  }
  
  //box3
  if(drawBox3Image && !(vb3||hb3)){
    imageMode(CENTER);
    image(boxImage[2], 562, 129);
  }
  if(db3v1 && vb3){
    imageMode(CENTER);
    image(vboxImage[4].get((fb3v1x),0,98,150), 511, 129);
  }
  if(db3v2 && vb3){
    imageMode(CENTER);
    image(vboxImage[5].get((fb3v2x),0,98,150), 613, 129);
  }
  if(db3h1 && hb3){
    imageMode(CENTER);
    image(hboxImage[4].get(0,fb3h1y,200,73), 562, 90);
  }
  if(db3h2 && hb3){
    imageMode(CENTER);
    image(hboxImage[5].get(0,fb3h2y,200,73), 562, 167);
  }
  
  
  
  //box4
  if(drawBox4Image && !(vb4||hb4)){
    imageMode(CENTER);
    image(boxImage[3], 154, 283);
  }
  if(db4v1 && vb4){
    imageMode(CENTER);
    image(vboxImage[6].get((fb4v1x),0,98,150), 103, 283);
  }
  if(db4v2 && vb4){
    imageMode(CENTER);
    image(vboxImage[7].get((fb4v2x),0,98,150), 205, 283);
  }
  if(db4h1 && hb4){
    imageMode(CENTER);
    image(hboxImage[6].get(0,fb4h1y,200,73), 154, 244);
  }
  if(db4h2 && hb4){
    imageMode(CENTER);
    image(hboxImage[7].get(0,fb4h2y,200,73), 154, 321);
  }
  
  
  //box5
  if(drawBox5Image && !(vb5||hb5)){
    imageMode(CENTER);
    image(boxImage[4], 358, 283);
  }
  if(db5v1 && vb5){
    imageMode(CENTER);
    image(vboxImage[8].get((fb5v1x),0,98,150), 307, 283);
  }
  if(db5v2 && vb5){
    imageMode(CENTER);
    image(vboxImage[9].get((fb5v2x),0,98,150), 409, 283);
  }
  if(db5h1 && hb5){
    imageMode(CENTER);
    image(hboxImage[8].get(0,fb5h1y,200,73), 358, 244);
  }
  if(db5h2 && hb5){
    imageMode(CENTER);
    image(hboxImage[9].get(0,fb5h2y,200,73), 358, 321);
  }
  
  
  
  
  //box6
  if(drawBox6Image && !(vb6||hb6)){
    imageMode(CENTER);
    image(boxImage[5], 562, 283);
  }
  if(db6v1 && vb6){
    imageMode(CENTER);
    image(vboxImage[10].get((fb6v1x),0,98,150), 511, 283);
  }
  if(db6v2 && vb6){
    imageMode(CENTER);
    image(vboxImage[11].get((fb6v2x),0,98,150), 613, 283);
  }
  if(db6h1 && hb6){
    imageMode(CENTER);
    image(hboxImage[10].get(0,fb6h1y,200,73), 562, 244);
  }
  if(db6h2 && hb6){
    imageMode(CENTER);
    image(hboxImage[11].get(0,fb6h2y,200,73), 562, 321);
  }
  
  
  
  
  //box7
  if(drawBox7Image && !(vb7||hb7)){
    imageMode(CENTER);
    image(boxImage[6], 154, 437);
  }
  if(db7v1 && vb7){
    imageMode(CENTER);
    image(vboxImage[12].get((fb7v1x),0,98,150), 103, 437);
  }
  if(db7v2 && vb7){
    imageMode(CENTER);
    image(vboxImage[13].get((fb7v2x),0,98,150), 205, 437);
  }
  if(db7h1 && hb7){
    imageMode(CENTER);
    image(hboxImage[12].get(0,fb7h1y,200,73), 154, 398);
  }
  if(db7h2 & hb7){
    imageMode(CENTER);
    image(hboxImage[13].get(0,fb7h2y,200,73), 154, 475);
  }
  
  
  
  //box8
  if(drawBox8Image && !(vb8||hb8)){
    imageMode(CENTER);
    image(boxImage[7], 358, 437);
  }
  if(db8v1 && vb8){
    imageMode(CENTER);
    image(vboxImage[14].get((fb8v1x),0,98,150), 307, 437);
  }
  if(db8v2 && vb8){
    imageMode(CENTER);
    image(vboxImage[15].get((fb8v2x),0,98,150), 409, 437);
  }
  if(db8h1 && hb8){
    imageMode(CENTER);
    image(hboxImage[14].get(0,fb8h1y,200,73), 358, 398);
  }
  if(db8h2 && hb8){
    imageMode(CENTER);
    image(hboxImage[15].get(0,fb8h2y,200,73), 358, 475);
  }
  
  
  
  //box9
  if(drawBox9Image && !(vb9||hb9)){
    imageMode(CENTER);
    image(boxImage[8], 562, 437);
  }
  if(db9v1 && vb9){
    imageMode(CENTER);
    image(vboxImage[16].get((fb9v1x),0,98,150), 511, 437);
  }
  if(db9v2 && vb9){
    imageMode(CENTER);
    image(vboxImage[17].get((fb9v2x),0,98,150), 613, 437);
  }
  if(db9h1 && hb9){
    imageMode(CENTER);
    image(hboxImage[16].get(0,fb9h1y,200,73), 562, 398);
  }
  if(db9h2 && hb9){
    imageMode(CENTER);
    image(hboxImage[17].get(0,fb9h2y,200,73), 562, 475);
  }
  
  
}

public void mode8export()
{
  println("Export button pressed ");
    int wide = 0;
    int heigh = 0;
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels
    if(drawBox1Image || db1v1 || db1v2 || db1h1 || db1h2){
      wide = 672;
      heigh = 512;
    }
    
    if(drawBox2Image || db2v1 || db2v2 || db2h1 || db2h2){
      wide = 1328;
      heigh = 512;
    }
    
    if(drawBox3Image || db3v1 || db3v2 || db3h1 || db3h2){
      wide = 1984;
      heigh = 512;
    }
    
    if(drawBox4Image || db4v1 || db4v2 || db4h1 || db4h2){
      wide = 672;
      heigh = 1008;
    }
    
    if(drawBox5Image || db5v1 || db5v2 || db5h1 || db5h2){
      wide = 1328;
      heigh = 1008;
    }
    
    if(drawBox6Image || db6v1 || db6v2 || db6h1 || db6h2){
      wide = 1984;
      heigh = 1008;
    }
    
    if(drawBox7Image || db7v1 || db7v2 || db7h1 || db7h2){
      wide = 672;
      heigh = 1504;
    }
    
    if(drawBox8Image || db8v1 || db8v2 || db8h1 || db8h2){
      wide = 1328;
      heigh = 1504;
    }
    
    if(drawBox9Image || db9v1 || db9v2 || db9h1 || db9h2){
      wide = 1984;
      heigh = 1504;
    }
    
    
    
    int border = 16;
    
    PImage comicStrip = createImage(wide, heigh, RGB);
    
    comicStrip.loadPixels();
    
    for (int y = 0; y < heigh; y++){
      for (int x = 0; x < wide; x++){
        int loc = x + y * wide;
        comicStrip.pixels[loc] = color(255, 255, 255);
      }
    }
    // 2. loop through panels and write them to output pimage
    if(drawBox1Image || db1v1 || db1v2 || db1h1 || db1h2){
      if(drawBox1Image && !(vb1||hb1)){
        comicStrip.copy(boxImage[0], 0, 0, 640, 480, 16, 16, 640, 480);
      }
      if(db1v1 && vb1){
        comicStrip.copy(vboxImage[0], 0, 0, 312, 480, 16, 16, 312, 480);
      }
      if(db1v2 && vb1){
        comicStrip.copy(vboxImage[1], 0, 0, 312, 480, 336, 16, 312, 480);
      }
      if(db1h1 && hb1){
        comicStrip.copy(hboxImage[0], 0, 0, 480, 232, 16, 16, 640, 232);
      }
      if(db1h2 && hb1){
        comicStrip.copy(hboxImage[1], 0, 0, 480, 232, 264, 16, 640, 232);
      }
    }
    
    if(drawBox2Image || db2v1 || db2v2 || db2h1 || db2h2){
      if(drawBox2Image && !(vb2||hb2)){
        comicStrip.copy(boxImage[1], 0, 0, 640, 480, 672, 16, 640, 480);
      }
      if(db1v1 && vb1){
        comicStrip.copy(vboxImage[2], 0, 0, 312, 480, 672, 16, 312, 480);
      }
      if(db1v2 && vb1){
        comicStrip.copy(vboxImage[3], 0, 0, 312, 480, 1000, 16, 312, 480);
      }
      if(db1h1 && hb1){
        comicStrip.copy(hboxImage[2], 0, 0, 480, 232, 672, 16, 640, 232);
      }
      if(db1h2 && hb1){
        comicStrip.copy(hboxImage[3], 0, 0, 480, 232, 672, 264, 640, 232);
      }
      
    }
    
    if(drawBox3Image || db3v1 || db3v2 || db3h1 || db3h2){
      comicStrip.copy(boxImage[2], 0, 0, 640, 480, 1328, 16, 640, 480);
    }
    
    if(drawBox4Image || db4v1 || db4v2 || db4h1 || db4h2){
      comicStrip.copy(boxImage[3], 0, 0, 640, 480, 16, 512, 640, 480);
    }
    
    if(drawBox5Image || db5v1 || db5v2 || db5h1 || db5h2){
      comicStrip.copy(boxImage[4], 0, 0, 640, 480, 672, 512, 640, 480);
    }
    
    if(drawBox6Image || db6v1 || db6v2 || db6h1 || db6h2){
      comicStrip.copy(boxImage[5], 0, 0, 640, 480, 1328, 512, 640, 480);
    }
    
    if(drawBox7Image || db7v1 || db7v2 || db7h1 || db7h2){
      comicStrip.copy(boxImage[6], 0, 0, 640, 480, 16, 1008, 640, 480);
    }
    
    if(drawBox8Image || db8v1 || db8v2 || db8h1 || db8h2){
      comicStrip.copy(boxImage[7], 0, 0, 640, 480, 672, 1008, 640, 480);
    }
    
    if(drawBox9Image || db9v1 || db9v2 || db9h1 || db9h2){
      comicStrip.copy(boxImage[8], 0, 0, 640, 480, 1328, 1008, 640, 480);
    }
    


    comicStrip.updatePixels();
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }

public void mode8mouseClicked(){
  if(mouseX>=458 && mouseX<=462 && mouseY>=54 && mouseY<=204){
    if(b2l==false){
      b2l=true;
    }
    b2l=false;
  }
  
  if(mouseX>=662 && mouseX<=666 && mouseY>=54 && mouseY<=204){
    if(b3l==false){
      b3l=true;
    }
    b3l=false;
  }
  
  if(mouseX>=254 && mouseX<=258 && mouseY>=208 && mouseY<=358){
    if(b4l==false){
      b4l=true;
    }
    b4l=false;
  }
  
  if(mouseX>=458 && mouseX<=462 && mouseY>=208 && mouseY<=358){
    if(b5l==false){
      b5l=true;
    }
    b5l=false;
  }
  
  if(mouseX>=662 && mouseX<=666 && mouseY>=208 && mouseY<=358){
    if(b6l==false){
      b6l=true;
    }
    b6l=false;
  }
  
  if(mouseX>=254 && mouseX<=258 && mouseY>=362 && mouseY<=512){
    if(b7l==false){
      b7l=true;
    }
    b7l=false;
  }
  
  if(mouseX>=458 && mouseX<=462 && mouseY>=362 && mouseY<=512){
    if(b8l==false){
      b8l=true;
    }
    b8l=false;
  }
  
  if(mouseX>=662 && mouseX<=666 && mouseY>=362 && mouseY<=512){
    if(b9l==false){
      b9l=true;
    }
    b9l=false;
  }
  
  
  
/////////////////////////////////////////////////////////////////// 
//1
///////////////////////////////////////////////////////////////////
  if(mouseX>=152 && mouseX<=156 && mouseY>=54 && mouseY<=204 && hb1 == false){
     if(vb1){
       vb1 = false;
       b1v1 = false;
       b1v2 = false;       
     } else {
       vb1 = true;
       b1v1 = true;
       b1v2 = true;
     }    
  }
  if(mouseX>=356 && mouseX<=360 && mouseY>=54 && mouseY<=204 && hb2 == false){
     if(vb2){
       vb2 = false;
       b2v1 = false;
       b2v2 = false;
     } else {
       vb2 = true;
       b2v1 = true;
       b2v2 = true;
     }    
  }
  if(mouseX>=560 && mouseX<=564 && mouseY>=54 && mouseY<=204 && hb3 == false){
     if(vb3){
       vb3 = false;
       b3v1 = false;
       b3v2 = false;
     } else {
       vb3 = true;
       b3v1 = true;
       b3v2 = true;
     }    
  }
///////////////////////////////////////////////////////////////////
  if(mouseX>=54 && mouseX<=254 && mouseY>=129 && mouseY<=133 && vb1 == false){
     if(hb1){
       hb1 = false;
       b1h1 = false;
       b1h2 = false;       
     } else {
       hb1 = true;
       b1h1 = true;
       b1h2 = true;;
     }    
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=129 && mouseY<=133 && vb2 == false){
     if(hb2){
       hb2 = false;
       b2h1 = false;
       b2h2 = false;
     } else {
       hb2 = true;
       b2h1 = true;
       b2h2 = true;
     }    
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=129 && mouseY<=133 && vb3 == false){
     if(hb3){
       hb3 = false;
       b3h1 = false;
       b3h2 = false;
     } else {
       hb3 = true;
       b3h1 = true;
       b3h2 = true;
     }    
  }
  
/////////////////////////////////////////////////////////////////// 
//2
///////////////////////////////////////////////////////////////////
  if(mouseX>=152 && mouseX<=156 && mouseY>=208 && mouseY<=358 && hb4 == false){
     if(vb4){
       vb4 = false;
       b4v1 = false;
       b4v2 = false;
     } else {
       vb4 = true;
       b4v1 = true;
       b4v2 = true;
     }    
  }
  if(mouseX>=356 && mouseX<=360 && mouseY>=208 && mouseY<=358 && hb5 == false){
     if(vb5){
       vb5 = false;
       b5v1 = false;
       b5v2 = false;
     } else {
       vb5 = true;
       b5v1 = true;
       b5v2 = true;
     }    
  }
  if(mouseX>=560 && mouseX<=564 && mouseY>=208 && mouseY<=358 && hb6 == false){
     if(vb6){
       vb6 = false;
       b6v1 = false;
       b6v2 = false;
     } else {
       vb6 = true;
       b6v1 = true;
       b6v2 = true;
     }    
  }
///////////////////////////////////////////////////////////////////
  if(mouseX>=54 && mouseX<=254 && mouseY>=283 && mouseY<=287 && vb4 == false){
     if(hb4){
       hb4 = false;
       b4h1 = false;
       b4h2 = false;
     } else {
       hb4 = true;
       b4h1 = true;
       b4h2 = true;
     }    
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=283 && mouseY<=287 && vb5 == false){
     if(hb5){
       hb5 = false;
       b5h1 = false;
       b5h2 = false;
     } else {
       hb5 = true;
       b5h1 = true;
       b5h2 = true;
     }    
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=283 && mouseY<=287 && vb6 == false){
     if(hb6){
       hb6 = false;
       b6h1 = false;
       b6h2 = false;
     } else {
       hb6 = true;
       b6h1 = true;
       b6h2 = true;
     }    
  }

/////////////////////////////////////////////////////////////////// 
//3
///////////////////////////////////////////////////////////////////
  if(mouseX>=152 && mouseX<=156 && mouseY>=362 && mouseY<=512 && hb7 == false){
     if(vb7){
       vb7 = false;
       b7v1 = false;
       b7v2 = false;
     } else {
       vb7 = true;
       b7v1 = true;
       b7v2 = true;
     }    
  }
  if(mouseX>=356 && mouseX<=360 && mouseY>=362 && mouseY<=512 && hb8 == false){
     if(vb8){
       vb8 = false;
       b8v1 = false;
       b8v2 = false;
     } else {
       vb8 = true;
       b8v1 = true;
       b8v2 = true;
     }    
  }
  if(mouseX>=560 && mouseX<=564 && mouseY>=362 && mouseY<=512 && hb9 == false){
     if(vb9){
       vb9 = false;
       b9v1 = false;
       b9v2 = false;
     } else {
       vb9 = true;
       b9v1 = true;
       b9v2 = true;
     }    
  }
///////////////////////////////////////////////////////////////////
  if(mouseX>=54 && mouseX<=254 && mouseY>=437 && mouseY<=441 && vb7 == false){
     if(hb7){
       hb7 = false;
       b7h1 = false;
       b7h2 = false;
     } else {
       hb7 = true;
       b7h1 = true;
       b7h2 = true;
     }    
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=437 && mouseY<=441 && vb8 == false){
     if(hb8){
       hb8 = false;
       b8h1 = false;
       b8h2 = false;
     } else {
       hb8 = true;
       b8h1 = true;
       b8h2 = true;
     }    
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=437 && mouseY<=441 && vb9 == false){
     if(hb9){
       hb9 = false;
       b9h1 = false;
       b9h2 = false;
     } else {
       hb9 = true;
       b9h1 = true;
       b9h2 = true;
     }    
  }
  
  if(mouseX>= 1030 && mouseX<=1080 && mouseY>=670 && mouseY<=720){
    mode1export();
  }
}

public void mode8mouseDragged(){  
  imageX = mouseX;
  imageY = mouseY;
  
  //box1  
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=204 && !(vb1||hb1)){
    box1=true;
    
  }else{
    box1=false;
  }
  //////////////vb1
  if(mouseX>=54 && mouseX<=152 && mouseY>=54 && mouseY<=204 && vb1){
    b1v1=true;
    b1v1x=mouseX;
  }else{b1v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=54 && mouseY<=204 && vb1){
    b1v2=true;
    b1v2x=mouseX;
  }else{b1v2=false;}
  //////////////hb1
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=127 && hb1){
    b1h1=true;
    b1h1y=mouseY;
  }else{b1h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=131 && mouseY<=204 && hb1){
    b1h2=true;
    b1h2y=mouseY;
  }else{b1h2=false;}
  //box2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=204 && !(vb2||hb2)){
    box2=true;
  }else{
    box2=false;
  }
  //vb2
  if(mouseX>=258 && mouseX<=356 && mouseY>=54 && mouseY<=204 && vb2){
    b2v1=true;
    b2v1x=mouseX;
  }else{b2v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=54 && mouseY<=204 && vb2){
    b2v2=true;
    b2v2x=mouseX;
  }else{b2v2=false;}
  //hb2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=127 && hb2){
    b2h1=true;
    b2h1y=mouseY;
  }else{b2h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=131 && mouseY<=204 && hb2){
    b2h2=true;
    b2h2y=mouseY;
  }else{b2h2=false;}
  //box3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=204 && !(vb3||hb3)){
    box3=true;
  }else{
    box3=false;
  }
  //vb3
  if(mouseX>=462 && mouseX<=560 && mouseY>=54 && mouseY<=204 && vb3){
    b3v1=true;
    b3v1x=mouseX;
  }else{b3v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=54 && mouseY<=204 && vb3){
    b3v2=true;
    b3v2x=mouseX;
  }else{b3v2=false;}
  //hb3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=127 && hb3){
    b3h1=true;
    b3h1y=mouseY;
  }else{b3h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=131 && mouseY<=204 && hb3){
    b3h2=true;
    b3h2y=mouseY;
  }else{b3h2=false;}
  //box4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=358 && !(vb4||hb4)){
    box4=true;
  }else{
    box4=false;
  }
  //vb4
  if(mouseX>=54 && mouseX<=152 && mouseY>=208 && mouseY<=358 && vb4){
    b4v1=true;
    b4v1x=mouseX;
  }else{b4v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=208 && mouseY<=358 && vb4){
    b4v2=true;
    b4v2x=mouseX;
  }else{b4v2=false;}
  //hb4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=281 && hb4){
    b4h1=true;
    b4h1y=mouseY;
  }else{b4h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=285 && mouseY<=358 && hb4){
    b4h2=true;
    b4h2y=mouseY;
  }else{b4h2=false;}
  //box5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=358 && !(vb5||hb5)){
    box5=true;
  }else{
    box5=false;
  }
  //vb5
  if(mouseX>=258 && mouseX<=356 && mouseY>=208 && mouseY<=358 && vb5){
    b5v1=true;
    b5v1x=mouseX;
  }else{b5v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=208 && mouseY<=358 && vb5){
    b5v2=true;
    b5v2x=mouseX;
  }else{b5v2=false;}
  //hb5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=281 && hb5){
    b5h1=true;
    b5h1y=mouseY;
  }else{b5h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=285 && mouseY<=358 && hb5){
    b5h2=true;
    b5h2y=mouseY;
  }else{b5h2=false;}
  //box6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=358 && !(vb6||hb6)){
    box6=true;
  }else{
    box6=false;
  }
  //vb6
  if(mouseX>=462 && mouseX<=560 && mouseY>=208 && mouseY<=358 && vb6){
    b6v1=true;
    b6v1x=mouseX;
  }else{b6v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=208 && mouseY<=358 && vb6){
    b6v2=true;
    b6v2x=mouseX;
  }else{b6v2=false;}
  //hb6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=281 && hb6){
    b6h1=true;
    b6h1y=mouseY;
  }else{b6h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=285 && mouseY<=358 && hb6){
    b6h2=true;
    b6h2y=mouseY;
  }else{b6h2=false;}
  //box7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=512 && !(vb7||hb7)){
    box7=true;
  }else{
    box7=false;
  }
  //vb7
  if(mouseX>=54 && mouseX<=152 && mouseY>=362 && mouseY<=512 && vb7){
    b7v1=true;
    b7v1x=mouseX;
  }else{b7v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=362 && mouseY<=512 && vb7){
    b7v2=true;
    b7v2x=mouseX;
  }else{b7v2=false;}
  //hb7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=435 && hb7){
    b7h1=true;
    b7h1y=mouseY;
  }else{b7h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=439 && mouseY<=512 && hb7){
    b7h2=true;
    b7h2y=mouseY;
  }else{b7h2=false;}
  //box8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=512 && !(vb8||hb8)){
    box8=true;
  }else{
    box8=false;
  }
  //vb8
  if(mouseX>=258 && mouseX<=356 && mouseY>=362 && mouseY<=512 && vb8){
    b8v1=true;
    b8v1x=mouseX;
  }else{b8v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=362 && mouseY<=512 && vb8){
    b8v2=true;
    b8v2x=mouseX;
  }else{b8v2=false;}
  //hb8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=435 && hb8){
    b8h1=true;
    b8h1y=mouseY;
  }else{b8h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=439 && mouseY<=512 && hb8){
    b8h2=true;
    b8h2y=mouseY;
  }else{b8h2=false;}
  //box9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=512 && !(vb9||hb9)){
    box9=true;
  }else{
    box9=false;
  }
  //vb9
  if(mouseX>=462 && mouseX<=560 && mouseY>=362 && mouseY<=512 && vb9){
    b9v1=true;
    b9v1x=mouseX;
  }else{b9v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=362 && mouseY<=512 && vb9){
    b9v2=true;
    b9v2x=mouseX;
  }else{b9v2=false;}
  //hb9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=435 && hb9){
    b9h1=true;
    b9h1y=mouseY;
  }else{b9h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=439 && mouseY<=512 && hb9){
    b9h2=true;
    b9h2y=mouseY;
  }else{b9h2=false;}
  ////////////////////////////////////////////////////////////
  if(mouseX<=666 && mouseX>=50 && mouseY<=516 && mouseY>=50){
    drawDragImage=false;
  } else {
    drawDragImage=true;
  }
  
}

public void mode8mousePressed(){
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=50 && mouseY<=100){
    draggedImage = photos[0].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=50 && mouseY<=100){
    draggedImage = photos[1].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=125 && mouseY<=175){
    draggedImage = photos[2].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=125 && mouseY<=175){
    draggedImage = photos[3].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=200 && mouseY<=250){
    draggedImage = photos[4].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=200 && mouseY<=250){
    draggedImage = photos[5].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=275 && mouseY<=325){
    draggedImage = photos[6].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=275 && mouseY<=325){
    draggedImage = photos[7].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  if(drawDragImage){
    if(draggedImage.height > 150)
      draggedImage.resize(0,150);
    if(draggedImage.width > 200)
      draggedImage.resize(50,0);
  }
  
  
}

public void mode8mouseReleased(){
  
  //box1
  
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=204 && !(vb1||hb1)){
    box1=false;
    if(imageSelected){
    boxImage[0]=draggedImage.get();
    drawBox1Image=true;
    }
  }
  //////////////vb1
  if(mouseX>=54 && mouseX<=152 && mouseY>=54 && mouseY<=204 && vb1){
    b1v1=false;
    fb1v1x=154-b1v1x;
    if(imageSelected){
      vboxImage[0]=draggedImage.get();
    db1v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=54 && mouseY<=204 && vb1){
    b1v2=false;
    fb1v2x=256-b1v2x;
    if(imageSelected){
      vboxImage[1]=draggedImage.get();
    db1v2=true;
    }
  }
  //////////////hb1
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=127 && hb1){
    b1h1=false;
    fb1h1y=129-b1h1y;
    if(imageSelected){
      hboxImage[0]=draggedImage.get();
    db1h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=131 && mouseY<=204 && hb1){
    b1h2=false;
    fb1h2y=206-b1h2y;
    if(imageSelected){
      hboxImage[1]=draggedImage.get();
    db1h2=true;
    }
  }
  
  
  
  //box2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=204 && !(vb2||hb2)){
    box2=false;
    if(imageSelected){
    boxImage[1]=draggedImage.get();
    drawBox2Image=true;
    }
  }
  //vb2
  if(mouseX>=258 && mouseX<=356 && mouseY>=54 && mouseY<=204 && vb2){
    b2v1=false;
    fb2v1x=358-b2v1x;
    if(imageSelected){
      vboxImage[2]=draggedImage.get();
    db2v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=54 && mouseY<=204 && vb2){
    b2v2=false;
    fb2v2x=460-b2v2x;
    if(imageSelected){
      vboxImage[3]=draggedImage.get();
    db2v2=true;
    }
  }
  //hb2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=127 && hb2){
    b2h1=false;
    fb1h1y=129-b1h1y;
    if(imageSelected){
      hboxImage[2]=draggedImage.get();
    db2h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=131 && mouseY<=204 && hb2){
    b2h2=false;
    fb1h2y=206-b1h2y;
    if(imageSelected){
      hboxImage[3]=draggedImage.get();
    db2h2=true;
    }
  }
  
  
  
  //box3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=204){
    box3=false;
    if(imageSelected){
    boxImage[2]=draggedImage.get();
    drawBox3Image=true;
    }
  }
  //vb3
  if(mouseX>=462 && mouseX<=560 && mouseY>=54 && mouseY<=204 && vb3){
    b3v1=false;
    fb3v1x=562-b3v1x;
    if(imageSelected){
      vboxImage[4]=draggedImage.get();
    db3v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=54 && mouseY<=204 && vb3){
    b3v2=false;
    fb3v2x=664-b3v2x;
    if(imageSelected){
      vboxImage[5]=draggedImage.get();
    db3v2=true;
    }
  }
  //hb3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=127 && hb3){
    b3h1=false;
    fb3h1y=129-b3h1y;
    if(imageSelected){
      hboxImage[4]=draggedImage.get();
    db3h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=131 && mouseY<=204 && hb3){
    b3h2=false;
    fb3h2y=206-b3h2y;
    if(imageSelected){
      hboxImage[5]=draggedImage.get();
    db3h2=true;
    }
  }
  
  
  //box4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=358){
    box4=false;
    if(imageSelected){
    boxImage[3]=draggedImage.get();
    drawBox4Image=true;
    }
  }
  //vb4
  if(mouseX>=54 && mouseX<=152 && mouseY>=208 && mouseY<=358 && vb4){
    b4v1=false;
    fb4v1x=154-b4v1x;
    if(imageSelected){
      vboxImage[6]=draggedImage.get();
    db4v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=208 && mouseY<=358 && vb4){
    b4v2=false;
    fb4v2x=256-b4v2x;
    if(imageSelected){
      vboxImage[7]=draggedImage.get();
    db4v2=true;
    }
  }
  //hb4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=281 && hb4){
    b4h1=false;
    fb4h1y=283-b4h1y;
    if(imageSelected){
      hboxImage[6]=draggedImage.get();
    db4h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=285 && mouseY<=358 && hb4){
    b4h2=false;
    fb4h2y=360-b4h2y;
    if(imageSelected){
      hboxImage[7]=draggedImage.get();
    db4h2=true;
    }
  }
  
  
  
  //box5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=358){
    box5=false;
    if(imageSelected){
    boxImage[4]=draggedImage.get();
    drawBox5Image=true;
    }
  }
  //vb5
  if(mouseX>=258 && mouseX<=356 && mouseY>=208 && mouseY<=358 && vb5){
    b5v1=false;
    fb5v1x=358-b5v1x;
    if(imageSelected){
      vboxImage[8]=draggedImage.get();
    db5v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=208 && mouseY<=358 && vb5){
    b5v2=false;
    fb5v2x=458-b5v2x;
    if(imageSelected){
      vboxImage[9]=draggedImage.get();
    db5v2=true;
    }
  }
  //hb5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=281 && hb5){
    b5h1=false;
    fb5h1y=283-b5h1y;
    if(imageSelected){
      hboxImage[8]=draggedImage.get();
    db5h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=285 && mouseY<=358 && hb5){
    b5h2=false;
    fb5h2y=360-b5h2y;
    if(imageSelected){
      hboxImage[9]=draggedImage.get();
    db5h2=true;
    }
  }
  
  
  
  //box6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=358){
    box6=false;
    if(imageSelected){
    boxImage[5]=draggedImage.get();
    drawBox6Image=true;
    }
  }
  //vb6
  if(mouseX>=462 && mouseX<=560 && mouseY>=208 && mouseY<=358 && vb6){
    b6v1=false;
    fb6v1x=562-b6v1x;
    if(imageSelected){
      vboxImage[10]=draggedImage.get();
    db6v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=208 && mouseY<=358 && vb6){
    b6v2=false;
    fb6v2x=664-b6v2x;
    if(imageSelected){
      vboxImage[11]=draggedImage.get();
    db6v2=true;
    }
  }
  //hb6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=281 && hb6){
    b6h1=false;
    fb6h1y=283-b6h1y;
    if(imageSelected){
      hboxImage[10]=draggedImage.get();
    db6h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=285 && mouseY<=358 && hb6){
    b6h2=false;
    fb6h2y=360-b6h2y;
    if(imageSelected){
      hboxImage[11]=draggedImage.get();
    db6h2=true;
    }
  }
  
  
  
  //box7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=512){
    box7=false;
    if(imageSelected){
    boxImage[6]=draggedImage.get();
    drawBox7Image=true;
    }
  }
  //vb7
  if(mouseX>=54 && mouseX<=152 && mouseY>=362 && mouseY<=512 && vb7){
    b7v1=false;
    fb7v1x=154-b7v1x;
    if(imageSelected){
      vboxImage[12]=draggedImage.get();
    db7v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=362 && mouseY<=512 && vb7){
    b7v2=false;
    fb7v2x=256-b7v2x;
    if(imageSelected){
      vboxImage[13]=draggedImage.get();
    db7v2=true;
    }
  }
  //hb7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=435 && hb7){
    b7h1=false;
    fb7h1y=437-b7h1y;
    if(imageSelected){
      hboxImage[12]=draggedImage.get();
    db7h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=439 && mouseY<=512 && hb7){
    b7h2=false;
    fb7h2y=514-b7h2y;
    if(imageSelected){
      hboxImage[13]=draggedImage.get();
    db7h2=true;
    }
  }
  
  
  
  //box8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=512){
    box8=false;
    if(imageSelected){
    boxImage[7]=draggedImage.get();
    drawBox8Image=true;
    }
  }
  //vb8
  if(mouseX>=258 && mouseX<=356 && mouseY>=362 && mouseY<=512 && vb8){
    b8v1=false;
    fb8v1x=358-b8v1x;
    if(imageSelected){
      vboxImage[14]=draggedImage.get();
    db8v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=362 && mouseY<=512 && vb8){
    b8v2=false;
    fb8v2x=460-b8v2x;
    if(imageSelected){
      vboxImage[15]=draggedImage.get();
    db8v2=true;
    }
  }
  //hb8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=435 && hb8){
    b8h1=false;
    fb8h1y=437-b8h1y;
    if(imageSelected){
      hboxImage[14]=draggedImage.get();
    db8h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=439 && mouseY<=512 && hb8){
    b8h2=false;
    fb8h2y=514-b8h2y;
    if(imageSelected){
      hboxImage[15]=draggedImage.get();
    db8h2=true;
    }
  }
  
  
  
  //box9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=512){
    box9=false;
    if(imageSelected){
    boxImage[8]=draggedImage.get();
    drawBox9Image=true;
    }
  }
  //vb9
  if(mouseX>=462 && mouseX<=560 && mouseY>=362 && mouseY<=512 && vb9){
    b9v1=false;
    fb9v1x=562-b9v1x;
    if(imageSelected){
      vboxImage[16]=draggedImage.get();
    db9v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=362 && mouseY<=512 && vb9){
    b9v2=false;
    fb9v2x=664-b9v2x;
    if(imageSelected){
      vboxImage[17]=draggedImage.get();
    db9v2=true;
    }
  }
  //hb9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=435 && hb9){
    b9h1=false;
    fb9h1y=437-b9h1y;
    if(imageSelected){
      hboxImage[16]=draggedImage.get();
    db9h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=439 && mouseY<=512 && hb9){
    b9h2=false;
    fb9h2y=514-b9h2y;
    if(imageSelected){
      hboxImage[17]=draggedImage.get();
    db9h2=true;
    }
  }
  
  
  
  
  drawDragImage = false;
  drawBoxImage = false;
  imageSelected = false;
}

// Mode 2: Take a picture

//==========================================================================================================================
public void drawCam()
{  
  textFont(font);
  text("Capture Mode", 20, 40);

  frame = webcam;
	
	pushMatrix();

	//flip across x axis
	scale(-1,1);

  if(changeBackground && useCustomBackground)
    image(displayCustomBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);
  else if(changeBackground)
    image(changeBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);
  else if(removeBackground)
    image(removeBackground(frame.get()), -(width - 800)/2 -800, 70, 800, 600);
	else
		image(frame, -(width - 800)/2 -800, 70, 800, 600);	
  
	popMatrix(); 
}


//__________________________________________________________________________________________________________________________
public PImage removeBackground(PImage frame)
{       
        
    mode2Calibration.loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        

        if(inHSBmode)
        {
          colorMode(HSB, 255);
          
          int loc = x + y * frame.width;
          int display = frame.pixels[loc];
          int comparison = mode2Calibration.pixels[loc];
          
          float h1 = hue(display),
            s1 = saturation(display),
            b1 = brightness(display),
            h2 = hue(comparison),
            s2 = saturation(comparison), 
            b2 = brightness(comparison)
            ;

          float hueDiff = abs(h1-h2),
            satDiff = abs(s1-s2),
            brtDiff = abs(b1-b2)
            ;

          if(hueDiff < hueThreshold && satDiff < satThreshold && brtDiff < brtThreshold)
            frame.pixels[loc] = color(255);
        }
        else
        {
          colorMode(RGB, 255);

          int loc = x + y * frame.width;
          int display = frame.pixels[loc];
          int comparison = mode2Calibration.pixels[loc];

          float r1 = red(display); float g1 = green(display); float b1 = blue(display);
          float r2 = red(comparison); float g2 = green(comparison); float b2 = blue(comparison);
          float diff = dist(r1,g1,b1,r2,g2,b2);

          if(diff < threshold)
                frame.pixels[loc] = color(255);
        }
      }
    }
    frame.updatePixels(); 
    calibratedFrame = frame.get();       
    return calibratedFrame;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode2phase1back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 50;

		cp5.addButton("takePhoto")
			.setPosition(left + offset, 677)
			.setCaptionLabel("Capture")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(100, 40)
			;

    offset += 110;
                
    cp5.addButton("goToCalibrationPhase")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Replace Background")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(250, 40)
      ;

    offset += 260;

    cp5.addButton("mode2phase1flickr")
      .setPosition((width+800)/2+10, 70)
      .setCaptionLabel("flickr")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;  

    if(removeBackground)
    {
      cp5.addButton("backgroundSelection")
        .setPosition(left + offset, 677)
        .setCaptionLabel("Background")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(180, 40)
        ;

      offset += 190;

      cp5.addButton("pickCustomBackground")
        .setPosition(left + offset, 677)
        .setCaptionLabel("Use Photo")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(150, 40)
        ;

      cp5.addSlider("thresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("hueThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("satThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40 + 30)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addSlider("brtThresholdSize")
        .setCaptionLabel("")
        .setPosition((width+800)/2+10, 170 + 60 + 40 + 30 + 30)
        .setSize(100, 20)
        .setRange(1, 250)
        .setValue(70)
        ;

      cp5.addButton("mode2phase1hsbMode")
        .setPosition((width+800)/2+10, 170)
        .setCaptionLabel("HSB")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(80, 40)
        ; 

      cp5.getController("thresholdSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);        
    }
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
    {
      println("removeBackground");
      mode2Capture = calibratedFrame.get();
    }
    else if(changeBackground)
    {
      println("changeBackground");
      mode2Capture = editedFrame.get();                
    }
    else
    {
      println("regularBackground");
      mode2Capture = frame.get();
    }
  }
  catch(NullPointerException e)
  {
    println("Could not capture frame! Null pointer!");
  }

  phase = 2;
  cp5.hide();
  displayButtons = true;
  mirror(mode2Capture);
  Photos.add(mode2Capture);
  numPhotos++;
}


//__________________________________________________________________________________________________________________________
public void mode2phase1back()
{
  mode = 1;
  cp5.hide();  
  displayButtons = true;
  removeBackground = false;
  changeBackground = false;
  useCustomBackground = false;
}


//__________________________________________________________________________________________________________________________
public void goToCalibrationPhase()
{
  phase = 3;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode2phase1flickr()
{
  mode = 7;
  phase = 1;
  cp5.hide();  
  displayButtons = true;
  flickrSearchQuery = "";
}


//__________________________________________________________________________________________________________________________
public void backgroundSelection()
{
  loadStockBackground();
  changeBackground = true;
  removeBackground = false;  
}


//__________________________________________________________________________________________________________________________
public void thresholdSize(int value)
{
  threshold = value;
}

//__________________________________________________________________________________________________________________________
public void hueThresholdSize(int value)
{
  hueThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void satThresholdSize(int value)
{
  satThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void brtThresholdSize(int value)
{
  brtThreshold = value;
}

//__________________________________________________________________________________________________________________________
public void mode2phase1hsbMode()
{
  if(!inHSBmode)
    inHSBmode = true;
  else
    inHSBmode = false;
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase2Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("mode2phase2save")
			.setPosition((width-800)/2 + 50, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
      .setColor(backButtonColor)
			;

		displayButtons = false;
	}
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
public void mode2phase2save()
{
	phase = 1;
	mode = 1;
	cp5.hide();
	displayButtons = true;
	removeBackground = false;
  changeBackground = false;
  useCustomBackground = false;
}


//__________________________________________________________________________________________________________________________
public void mode2phase2back()
{
	phase = 1;
	cp5.hide();  
	displayButtons = true;
	numPhotos--;
  Photos.remove(numPhotos);
}


//==========================================================================================================================
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


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase3buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;

    cp5.addButton("mode2phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("takeCalibrationPhoto")
      .setPosition(left + 50, 677)
      .setCaptionLabel("Capture")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;

    displayButtons = false;
  }
  
}


//__________________________________________________________________________________________________________________________
public void takeCalibrationPhoto()
{
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


//__________________________________________________________________________________________________________________________
public void mode2phase3back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void loadStockBackground()
{
  String path = sketchPath+"/stockbackground/"; //folder of images rename as needed
  File[] files = listFiles(path);
  stockBackground = new PImage[files.length];

  for(int i=0; i<files.length; i++)
  {
    stockBackground[i]=loadImage(files[i].getAbsolutePath());
    imageResize(stockBackground[i]);
  }
}


//__________________________________________________________________________________________________________________________
public void imageResize(PImage img)
{
  img.resize(640,480);
}


//__________________________________________________________________________________________________________________________
public void pickCustomBackground()
{
  phase = 4;
  cp5.hide();  
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
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


//__________________________________________________________________________________________________________________________
public PImage displayCustomBackground(PImage frame)
{       
  PImage resizedBackground = Photos.get(backgroundIndex);
  resizedBackground.resize(frame.width, frame.height);
  resizedBackground.loadPixels();
  frame.loadPixels();

  for (int y=0; y < frame.height; y++)
  {
    for (int x=0; x < frame.width; x++)
    {
      int loc = x + y * frame.width;
      int display = frame.pixels[loc];        
      
      if(display == color(255))
        frame.pixels[loc] = resizedBackground.pixels[loc];
    }
  }

  frame.updatePixels(); 
  editedFrame = frame.get();       
  return editedFrame;
}


//==========================================================================================================================
public void mode2phase4display()
{
  background(backgroundColor);
  text("Click a photo to use as a background.", 20, 40);
  mode3displayPhotos();
  mode2phase4buttons();
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode2phase4buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;

    cp5.addButton("mode2phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode2mousePressed()
{
  if(phase == 4)
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
        backgroundIndex = i;
        phase = 1;
        useCustomBackground = true;
        changeBackground = true;
        //removeBackground = false;
        println(" use a custom photo background ");

        cp5.hide();
        displayButtons = true;
      }
    }
  }
}
// Mode 3: Add a panel
//		Phase 1: show horizontal list of photos to add
//		Phase 2: show clicked photo large '<' button goes back and 'S' button adds clicked photo to panels array


//__________________________________________________________________________________________________________________________
public void mode3displayPhotos()
{
	background(backgroundColor);
	for(int i = 0; i < numPhotos; i++)
		image(Photos.get(i), 80 + i*110, height/2, 100, 75);
}



//__________________________________________________________________________________________________________________________
public void mode3displayPhotos(int index)
{
  background(backgroundColor);
  for(int i = index; i < numPhotos; i++)
    image(Photos.get(i), 80 + (i-index)*110, height/2, 100, 75);
}


//__________________________________________________________________________________________________________________________
public void displayPhoto(int index)
{
	image(Photos.get(index), (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode3phase1left")
      .setPosition((width-800)/2+50, 677)
      .setCaptionLabel("left")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode3phase1right")
      .setPosition((width-800)/2+50+90, 677)
      .setCaptionLabel("right")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
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
  displayIndex = 0;
}



//__________________________________________________________________________________________________________________________
public void mode3phase1left()
{
  displayIndex--;
  if(displayIndex < 0)
    displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode3phase1right()
{
  displayIndex++;
  if(displayIndex > numPhotos-4)
    displayIndex = numPhotos-4;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase2back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase2save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode3phase2makeHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Horizontal Half")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(220, 40)
      ;

    offset += 220 + 10;

    cp5.addButton("mode3phase2makeHalfVertical")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Vertical Half")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(170, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase2back()
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
  PImage newPanel = Photos.get(currentPhotoIndex);
  Panels.add(newPanel);
  PanelSizes.add(1);
  numPanels++;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2makeHalf()
{
  println("button: display half photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  halfX = (width - 800)/2;
  halfY = 70;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2makeHalfVertical()
{
  println("button: display vertical half photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  halfX = (width - 800)/2;
  halfY = 70;
}


//===========================================================================================================================
public void mode3phase3display()
{
  background(backgroundColor);
  text("Move the rectangle to pick region to save", 20, 40);
  mode3phase3displayButtons();
  displayPhoto(currentPhotoIndex);
  stroke(255);
  noFill();
  strokeWeight(3);
  rect(halfX, halfY, 800, 300);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase3saveHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save Region")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(140, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase3back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 2;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode3phase3saveHalf()
{
  println("button: save half panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newHalfPanel = createImage(640, 480/2-8, RGB); 
  newHalfPanel.copy(Photos.get(currentPhotoIndex), 0, halfY-70, 640, 480/2-8, 0, 0, 640, 480/2-8);
  Panels.add(newHalfPanel);
  PanelSizes.add(2);
  numPanels++;
  numHalfPanels++;
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


//===========================================================================================================================
public void mode3phase4display()
{
  background(backgroundColor);
  text("Move the rectangle to pick region to save", 20, 40);
  mode3phase4displayButtons();
  displayPhoto(currentPhotoIndex);
  stroke(255);
  noFill();
  strokeWeight(3);
  rect(halfX, halfY, 400, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode3phase4displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode3phase4saveHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save Region")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(140, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase4saveHalf()
{
  println("button: save half panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newHalfPanel = createImage(640/2-8, 480, RGB); 
  newHalfPanel.copy(Photos.get(currentPhotoIndex), halfX-(width-800)/2, halfY-70, 640/2-8, 480, 0, 0, 640/2-8, 480);
  Panels.add(newHalfPanel);
  PanelSizes.add(3);
  numPanels++;
  numHalfPanels++;
}
// File: Mode4.pde
// Mode 4 is the photo editing hub
//	Phase 1 = editing hub with buttons to different phases
//  Phase 2 = simple drawing mode (ie: ms paint)
//	Phase 3 = resize a photo


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase1back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode4phase1draw")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Draw")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode4phase1select")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Select")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode4phase1text")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Text")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;
    
    cp5.addButton("mode4phase1resize")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Resize")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(90, 40)
      ;

    offset += 90 + 10;

    cp5.addButton("mode4phase1layer")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Layer")
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
public void mode4phase1select()
{
  phase = 6;
  cp5.hide();
  displayButtons = true;
  cropX1 = 0;
  cropY1 = 0;
  cropX2 = 0;
  cropY2 = 0;
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
public void mode4phase1text()
{
  println("button: add text to photo");
  mode = 6;
  phase = 1;
  rectX1 = 0;
  rectY1 = 0;
  cp5.hide();
  displayButtons = true;
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
public void mode4phase1layer()
{
  println("button: layer photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
}


//==========================================================================================================================
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
public void displayResizedPhoto(int index, float resize)
{
  if(resize <= 100)
    image(Photos.get(index), (width - (800 * (resize/100)))/2, 70 + (300 - (600 * (resize/100)/2)), 800 * (resize/100), 600 * (resize/100));
  else
    displayPhoto(index);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);
    fill(0xff909090);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase2back")
  		.setPosition(left, 677)
  		.setCaptionLabel("<")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(40, 40)
      .setColor(backButtonColor)
  		;

    offset += 40 + 10;

    cp5.addButton("mode4phase2save")
  		.setPosition(left + offset, 677)
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
	Photos.add(editPhoto);
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


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase2back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;
      
    cp5.addButton("mode4phase3save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
      
    cp5.addSlider("imageSize")
      .setCaptionLabel("")
      .setPosition((width - 100)/2 - 30, 20)
      .setSize(100, 20)
      .setRange(1, 200)
      .setValue(100)
      ;

    cp5.getController("imageSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase3save()
{
  println("button: save resized photo to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true; 
            
  // save edited photo to photo list
  if(resizeValue <= 100)
  {
    int resizedHeight = (int)(480 * (resizeValue/100));
    int resizedWidth = (int)(640 * (resizeValue/100));
    int resizedHeight2 = (int)(600 * (resizeValue/100));
    int resizedWidth2 = (int)(800 * (resizeValue/100));
    int resizedX = (int)((width - (800 * (resizeValue/100)))/2);
    int resizedY = (int)(70 + (300 - (600 * (resizeValue/100)/2)));
    PImage screenShot = get();
    println("Created resized photo with dimensions: "+resizedWidth+", "+resizedHeight);
    editPhoto = createImage(resizedWidth, resizedHeight, RGB);
    //editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, resizedWidth, resizedHeight);
    editPhoto.copy(screenShot, resizedX, resizedY, resizedWidth2, resizedHeight2, 0, 0, resizedWidth, resizedHeight);
    Photos.add(editPhoto);
    numPhotos++;
  }
  else
    Photos.get(currentPhotoIndex).resize((int)(Photos.get(currentPhotoIndex).width*(resizeValue/100)), (int)(Photos.get(currentPhotoIndex).height*(resizeValue/100)));
}


//__________________________________________________________________________________________________________________________
public void imageSize(float value)
{
  println(value);
  resizeValue = value;
}



//==========================================================================================================================
public void mode4phase4display()
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  displayPhoto(currentPhotoIndex);

  for(int i = 0; i < numLayers; i++)
  {
    int layerWidth = (int)((1.25f)*Photos.get(Layers.get(i)).width);
    int layerHeight = (int)((1.25f)*Photos.get(Layers.get(i)).height);
    println("("+LayersX.get(i)+", "+LayersY.get(i)+") dims: "+layerWidth+", "+layerHeight);
    image(Photos.get(Layers.get(i)), LayersX.get(i), LayersY.get(i), layerWidth, layerHeight);
  }

  noStroke();
  fill(backgroundColor);
  rect(0, 0, width, 70);    //top
  rect(0, 0, (width-800)/2, height); // left
  rect(0, 70+600, width, height-670); // bottom
  rect((width+800)/2, 0, width-(width+800)/2, height); // right
  text("Layer", 20, 40);
  mode4phase4displayButtons();
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase4displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode4phase4back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    offset += 40 + 10;

    cp5.addButton("mode4phase4addPhoto")
      .setPosition(left + offset, 677)
      .setCaptionLabel("+Photo")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;
    
    cp5.addButton("mode4phase4save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase4back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
  numLayers = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase4save()
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
  Photos.add(editPhoto);
  numPhotos++;
  numLayers = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase4addPhoto()
{
  textFont(font);
  fill(0xff817575);
  println("button: pick a photo from photo list");
  phase = 5;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}


//==========================================================================================================================
public void mode4phase5display() // add photo as layer mode
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  mode3displayPhotos(displayIndex);
  text("Pick a photo to add as a layer", 20, 40);
  mode4phase5displayButtons();
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase5displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase5back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode4phase5left")
      .setPosition((width-800)/2+50, 677)
      .setCaptionLabel("left")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode4phase5right")
      .setPosition((width-800)/2+50+90, 677)
      .setCaptionLabel("right")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode4phase5back()
{
  println("button: back to photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase5left()
{
  displayIndex--;
  if(displayIndex < 0)
    displayIndex = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase5right()
{
  displayIndex++;
  if(displayIndex > numPhotos-4)
    displayIndex = numPhotos-4;
}


//__________________________________________________________________________________________________________________________
public void mode4mousePressed()
{
  if(phase == 5)
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
        Layers.add(i);
        LayersX.add((width - 800)/2);
        LayersY.add(70);
        numLayers++;
        phase = 4;
        println("-- added photo as layer");
        displayIndex = 0;

        cp5.hide();
        displayButtons = true;
      }
    }
  }
  else if(phase == 6)
  {
    if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
    {
      println("y: "+mouseY+"  x: "+mouseX);
      cropX1 = mouseX;
      cropY1 = mouseY;
    }
  }
}



//==========================================================================================================================
public void mode4phase6display() // Simple selection and crop mode
{
  textFont(font);
  fill(0xff817575);
  background(backgroundColor);
  displayPhoto(currentPhotoIndex);
  text("Click and drag crop selection", 20, 40);
  mode4phase6displayButtons();
  if(cropX1 != 0 && cropX2 != 0)
  {
    noFill();
    strokeWeight(1);
    stroke(255);
    rect(cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1);
  }
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode4phase6displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase6back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode4phase6save")
      .setPosition((width-800)/2 + 50, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase6back()
{
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode4phase6save()
{
  println("button: cropped photo saved to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // save edited photo to photo list

  int temp;
  if(cropX1 > cropX2)
  {
    temp = cropX1;
    cropX1 = cropX2;
    cropX2 = temp;
  }
  if(cropY1 > cropY2)
  {
    temp = cropY1;
    cropY1 = cropY2;
    cropY2 = temp;
  }


  displayPhoto(currentPhotoIndex);
  PImage screenShot = get();

  int cropWidth = cropX2 - cropX1;
  int cropHeight = cropY2 - cropY1;
  cropWidth = (int)(cropWidth * 0.8f);
  cropHeight = (int)(cropHeight * 0.8f);

  editPhoto = createImage(cropWidth, cropHeight, RGB);
  editPhoto.copy(screenShot, cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1, 0, 0, cropWidth, cropHeight);
  Photos.add(editPhoto);
  numPhotos++;
}
// Edit Panel mode



//__________________________________________________________________________________________________________________________
public void displayPanel(int index)
{
	image(Panels.get(index), (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode5phase1displayButtons()
{
	if(displayButtons)
  	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    int left = (width-800)/2;
    	int offset = 0;

	    cp5.addButton("mode5phase1back")
	      .setPosition(left, 677)
	      .setCaptionLabel("<")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(40, 40)
	      .setColor(backButtonColor)
	      ;

	    offset += 40 + 10;

	    cp5.addButton("mode5phase1delete")
	      .setPosition(left + offset, 677)
	      .setCaptionLabel("Delete")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(90, 40)
	      ;

	    offset += 90 + 10;
	    
	    cp5.addButton("mode5phase1left")
	      .setPosition(left + offset, 677)
	      .setCaptionLabel("Left")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    offset += 80 + 10;

	    cp5.addButton("mode5phase1right")
	      .setPosition(left + offset, 677)
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
  	/*
  	for(int i = currentPanelIndex + 1; i < numPanels; i++)
  	{
  		Panels[i - 1] = Panels[i];
  	}*/
  	Panels.remove(currentPanelIndex);
  	PanelSizes.remove(currentPanelIndex);
  	numPanels--;
}



// Add text bubbles mode


//==========================================================================================================================
public void mode6phase1display()
{
	background(backgroundColor);
	mode6phase1displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	text("Click and drag to make the textbox size.", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(255);
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		stroke(255);
		strokeWeight(10);
		
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);

		noStroke();
	}
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode6phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;
      
   cp5.addButton("mode6phase1save")
      .setPosition((width-800)/2 + 50, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode6phase1back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase1save()
{
  	println("button: save text bubble");
  	if(rectX1 != 0)
	{
	  	phase = 2;
	  	cp5.hide();
	  	displayButtons = true;
	  	paint = color(255, 255, 255, 255);
	}
	else
		text("Please make a rectangle first.", 200, 40);

}


//__________________________________________________________________________________________________________________________
public void mode6mousePressed()
{
	if(phase == 1)
	{
		if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
		{
			rectX1 = mouseX;
			rectY1 = mouseY;
		}	
	}
}



//==========================================================================================================================
public void mode6phase2display()
{
	background(backgroundColor);
	mode6phase2displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	fill(255);
	text("Enter text", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(paint);
		noStroke();
		
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		strokeWeight(10);
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);
		noStroke();
	}
	
	fill(0,0,0);
	int bubbleBorder = 10;
	text(cp5.get(Textfield.class,"textBubble").getText(), 
		rectX1+bubbleBorder, rectY1+bubbleBorder, 
		rectX2 - rectX1 - bubbleBorder, rectY2 - rectY1 - bubbleBorder);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode6phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase2back")
		.setPosition((width-800)/2, 677)
		.setCaptionLabel("<")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(40, 40)
		.setColor(backButtonColor)
		;
      
   	cp5.addButton("mode6phase2save")
		.setPosition((width-800)/2 + 50, 677)
		.setCaptionLabel("Save")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(80, 40)
		;

	cp5.addTextfield("textBubble")
		.setCaptionLabel("Add text")
		.setPosition(width/2, 5)
		.setSize(200,40)
		.setFont(smallFont)
		.setFocus(true)
		.setColor(color(255,0,0))
		;

	cp = cp5.addColorPicker("colorPicker2")
  		.setPosition((width + 100)/2 + 220, 5)
  		.setColorValue(color(255, 255, 255, 255))
  		;

    displayButtons = false;
  }
}




//__________________________________________________________________________________________________________________________
public void mode6phase2back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase2save()
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
	Photos.add(editPhoto);
	numPhotos++;
}
// file: Mode7.pde
// description: Mode 7 allows the user to search flickr for a photo and add that photo to their project


//==========================================================================================================================
public void mode7phase1display()
{
	background(backgroundColor);
	mode7phase1displayButtons();
	text("Search flickr for a photo to add.", 20, 40);
	text("Use '+' to separate tags.", 20, 80);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase1displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    int left = (width-800)/2;

	    cp5.addButton("mode7phase1back")
			.setPosition(left, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

		cp5.addTextfield("flickrSearchQuery")
			.setCaptionLabel("Enter Query")
			.setPosition((width-290)/2, (height-40)/2)
			.setSize(200, 40)
			.setFont(smallFont)
			.setFocus(true)
			.setColor(color(255,0,0))
			;

		cp5.addButton("flickrSearchButton")
			.setPosition((width+200)/2 - 20, (height-40)/2)
			.setCaptionLabel("Search")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(90, 40)
	      	;

	    displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void flickrSearchButton()
{
	println("button: flickr search button");
	phase = 2;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = cp5.get(Textfield.class,"flickrSearchQuery").getText();
	getFlickrData();
}


//__________________________________________________________________________________________________________________________
public void mode7phase1back()
{
	println("button: back to add photo");
	mode = 2;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}


//==========================================================================================================================
public void mode7phase2display()
{
	background(backgroundColor);
	mode7phase2displayButtons();
	text("Click on a photo to add.", 20, 40);
	text("Showing results for \""+flickrSearchQuery+"\"", 20, 80);

	int x=0, y=0, size = flickrPhotoList.size();
	float flickrW, flickrH, compW = 640, compH = 480, compAspectRatio = compW/compH;

	for (int i = 0; i < size; i++)
	{
		flickrW = (float)flickrPhotoList.get(i).width;
		flickrH = (float)flickrPhotoList.get(i).height;
		float aspectRatio = flickrW/flickrH;

		if(aspectRatio >= compAspectRatio)
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 200, 200 * flickrH/flickrW);
		else
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 150 * flickrW/flickrH, 150);

		x++;
		if (x >= 5)
		{
		  	x = 0;
		  	y++;
		}
	}
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase2displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase2back")
			.setPosition(width-60, 20)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase2back()
{
	phase = 1;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = "";
	flickrPhotoList = new ArrayList <PImage> ();
}


//__________________________________________________________________________________________________________________________
public void getFlickrData() 
{
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
	String request = api + "&per_page=25&format=json&nojsoncallback=1&extras=geo";
	request += "&api_key=" + "b4b2de9fa436d4629313b6463f254d69"; 
	String tags = flickrSearchQuery;
	request += "&tags=" + tags;

	//String userId = "88935360@N05"; // 690 test
	//request += "&user_id=" + userId;
	//request += "&min_upload_date=2012-07-01";
	//request += "&bbox=-125,32,-120,40";
	//request += "&lat=38";
	//request += "&lon=-122";
	//request += "&tags=bill%20burroughs";

	println("Sent request: " + request);
	json = loadJSONObject(request);
	println("Received from flickr: " + json);

	JSONObject photos = json.getJSONObject("photos");
	JSONArray photo = photos.getJSONArray("photo");
	println("Found " + photo.size() + " photos");

	for (int i=0; i<photo.size (); i++) 
	{
		JSONObject pic = photo.getJSONObject(i);
		// get parameters to construct url
		int farm = pic.getInt("farm");
		String server = pic.getString("server");
		String id = pic.getString("id");
		String secret = pic.getString("secret");
		String url = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
		String title = pic.getString("title");
		println("Photo " + i + " " + title + " " + url);
		PImage img = loadImage(url);

		if(img.width/img.height >= 640/480)
			img.resize(640, 640 * img.height/img.width);
		else
			img.resize(480 * img.width/img.height, 480);

		flickrPhotoList.add(img);
	}
}


//__________________________________________________________________________________________________________________________
public void mode7mousePressed()
{
	if(phase == 2)
	{
		int x = 0, y = 0, 
			photoY, photoX, photoW, photoH, 
			size = flickrPhotoList.size();

		for(int i = 0; i < size; i++)
		{

			float compW = 640, 
				compH = 480, 
				compAspectRatio = compW/compH,
				flickrW = (float)flickrPhotoList.get(i).width,
				flickrH = (float)flickrPhotoList.get(i).height,
				aspectRatio = flickrW/flickrH
				;

			if(aspectRatio >= compAspectRatio)
			{
				photoW = 200;
				photoH = (int)(200 * flickrH/flickrW);
			}
			else
			{
				photoW = (int)(150 * flickrW/flickrH);
				photoH = 150;
			}


			photoX = 20 + x*210;
			photoY = 100+y*160;

			if(mouseY < photoY + photoH && mouseY > photoY 
				&& mouseX < photoX + photoW && mouseX > photoX)
			{
				flickrPhotoIndex = i;
				phase = 3;
				cp5.hide();
				displayButtons = true;
				break;
			}

			x++;
			if (x >= 5)
			{
			  	x = 0;
			  	y++;
			}
		}
	}
}


//==========================================================================================================================
public void mode7phase3display()
{
	background(backgroundColor);
	mode7phase3displayButtons();

	float flickrW = (float)flickrPhotoList.get(flickrPhotoIndex).width,
		flickrH = (float)flickrPhotoList.get(flickrPhotoIndex).height,
		aspectRatio = flickrW/flickrH,
		compW = 800,
		compH = 600,
		compAspectRatio = compW/compH
		;

	if(aspectRatio >= compAspectRatio)
		image(flickrPhotoList.get(flickrPhotoIndex), (width - 800)/2, 70, 800, 800/aspectRatio);
	else
		image(flickrPhotoList.get(flickrPhotoIndex), (width - 600 * aspectRatio)/2, 70, 600 * aspectRatio, 600);
	//println("aspectRatio: "+aspectRatio+"  compAspectRatio: "+compAspectRatio);
	//image(flickrPhotoList.get(flickrPhotoIndex), (width - 800)/2, 70);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode7phase3displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase3back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			.setColor(backButtonColor)
			;

	    cp5.addButton("mode7phase3save")
			.setPosition((width-800)/2 + 60, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase3back()
{
	phase = 2;
	cp5.hide();
	displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode7phase3save()
{
	mode = 1;
	phase = 1;
	flickrSearchQuery = "";
	cp5.hide();
	displayButtons = true;
	Photos.add(flickrPhotoList.get(flickrPhotoIndex));
	numPhotos++;
	flickrPhotoList = new ArrayList <PImage> ();
}
// Mode 0: Start Screen
// Mode 1: Overview

//__________________________________________________________________________________________________________________________
public void drawStartScreen()
{
  PImage startLogo = loadImage("logo.png");
  image(startLogo, (width - startLogo.width)/2, (height - startLogo.height)/2);  
}



//==========================================================================================================================
public void drawOverview()
{
  background(backgroundColor);
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
    image(Photos.get(i), 80 + i*90, 140, 80, 60);

    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= 140
      && mouseY <= 140 + 60)
      text("Edit", 80 + i*90, 140 + 35);
  }
  for(int i = 0; i < numPanels; i++) 
  {
    if(PanelSizes.get(i) == 1)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 80, 60);
    else if(PanelSizes.get(i) == 2)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 80, 30);
    else if(PanelSizes.get(i) == 3)
      image(Panels.get(i), 80 + i*90, (height/2 + 40), 40, 60);
    // show "Edit" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
      text("Edit", 80 + i*90, (height/2 + 40 + 35));
  }
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
    image(Photos.get(currentPhotoIndex + i), 80, (height/2 + 40) + i*70, 80, 60);
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
  phase = 2;
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
  displayIndex = 0;
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



//==========================================================================================================================
public void mode1phase2display()
{
  background(backgroundColor);
  text("Pick your export preference", 20, 40);
  mode1phase2buttons();

  if(displayExportedComic)
  {
    fill(0xff817575);
    float comicH = exportedComic.height,
      comicW = exportedComic.width,
      ratio = (comicH/comicW)
      ;
    image(exportedComic, (width-800)/2, 80, 800, 800*ratio);
    //text("Exported Comic: ", 80, (height/2 + 150));
  }
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void mode1phase2buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode2phase2back")
      .setPosition(60, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;

    cp5.addButton("mode1exportLong")
      .setPosition(width - 230, 10)
      .setCaptionLabel("Export as Strip")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(210, 40)
      ;
      
    cp5.addButton("mode1exportBlock")
      .setPosition(width - 230, 60)
      .setCaptionLabel("Export as Block")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(210, 40)
      ;

    cp5.addButton("mode1customExport")
      .setPosition(width - 220, 110)
      .setCaptionLabel("Custom Export")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(200, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode1exportLong()
{
  println("Export button pressed ");

  if(numPanels > 0)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 16;
    PImage comicStrip = createImage(border + (border + 640)*(numPanels - numHalfPanels/2), 480 + 2 * border, RGB);
    comicStrip.loadPixels();
    int Max = (border + (border + 640)*(numPanels - numHalfPanels/2));
    for (int i = 0; i < Max; i++)
      for (int j = 0; j < 480 + 2 * border; j++)
        comicStrip.pixels[j*(border + (border + 640)*(numPanels - numHalfPanels/2)) + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    int numBlocks = 0;

    for(int i = 0; i < numPanels; i++)
    {
      boolean written = false;
      boolean writtenVertical = false;
      println(i);
      int cX = border + (640 + border) * numBlocks;
      int cY = border;
      Panels.get(i).loadPixels();

      if(PanelSizes.get(i) == 1)
      {
        comicStrip.copy(Panels.get(i), 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes.get(i) == 2) 
      {
        if(i > 0)
        {
          int cX2 = border + (640 + border) * (numBlocks-1);
          int cY2 = border + 480/2 + 8;
          if(PanelSizes.get(i-1) == 2)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX2, cY2, 640, 480/2-8);
            numBlocks--;
            written = true;
          }
        }
        if(!written)
          comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX, cY, 640, 480/2-8);
      }
      else if(PanelSizes.get(i) == 3) 
      {
        if(i > 0)
        {
          int cX3 = border + (640 + border) * (numBlocks-1) + 640/2 + 8;
          int cY3 = border;
          if(PanelSizes.get(i-1) == 3)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX3, cY3, 640/2-8, 480);
            numBlocks--;
            writtenVertical = true;
          }
        }
        if(!writtenVertical)
          comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX, cY, 640/2-8, 480);
      }
      numBlocks++;
    }


    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }
}


//__________________________________________________________________________________________________________________________
public void mode1exportBlock()
{
  println("Export button pressed ");

  if(numPanels > 0)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 16;
    PImage comicStrip = createImage(border + (border + 640)*(numPanels - numHalfPanels/2), 480 + 2 * border, RGB);
    comicStrip.loadPixels();
    int Max = (border + (border + 640)*(numPanels - numHalfPanels/2));
    for (int i = 0; i < Max; i++)
      for (int j = 0; j < 480 + 2 * border; j++)
        comicStrip.pixels[j*(border + (border + 640)*(numPanels - numHalfPanels/2)) + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    int numBlocks = 0;

    for(int i = 0; i < numPanels; i++)
    {
      boolean written = false;
      boolean writtenVertical = false;
      println(i);
      int cX = border + (640 + border) * numBlocks;
      int cY = border;
      Panels.get(i).loadPixels();

      if(PanelSizes.get(i) == 1)
      {
        comicStrip.copy(Panels.get(i), 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes.get(i) == 2) 
      {
        if(i > 0)
        {
          int cX2 = border + (640 + border) * (numBlocks-1);
          int cY2 = border + 480/2 + 8;
          if(PanelSizes.get(i-1) == 2)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX2, cY2, 640, 480/2-8);
            numBlocks--;
            written = true;
          }
        }
        if(!written)
          comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX, cY, 640, 480/2-8);
      }
      else if(PanelSizes.get(i) == 3) 
      {
        if(i > 0)
        {
          int cX3 = border + (640 + border) * (numBlocks-1) + 640/2 + 8;
          int cY3 = border;
          if(PanelSizes.get(i-1) == 3)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX3, cY3, 640/2-8, 480);
            numBlocks--;
            writtenVertical = true;
          }
        }
        if(!writtenVertical)
          comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX, cY, 640/2-8, 480);
      }
      numBlocks++;
    }


    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }
}


public void mode1customExport(){
  mode = 8;
  Jassetup();
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
