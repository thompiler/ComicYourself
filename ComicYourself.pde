
// group members:    Thomas Cassady & Jason Huang
// class:    CSC 690
// project: Final
// date:    11/13/2014
// file:  ComicYourself.pde

//__________________________________________________________________________________________________________________________
import processing.video.*;
import java.awt.*;
import controlP5.*;
//import gab.opencv.*;



//__________________________________________________________________________________________________________________________
Capture webcam;
PImage [] Photos = new PImage[20];
PImage [] Panels;
int numPhotos = 0;
int numPanels = 0;
PImage frame, mode2Capture;
int mode = 0;
int phase = 1;
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
//_______________________________________________________________________________________________
//*****************THINGS JASON DID!!!!***************************
//__________________________________________________________________
      drawCam();
      mode2phase1Buttons();
//_______________________________________________________________
    }
    else if(phase == 2)
    {
      // show picture taken as freeze frame
      mode2phase2Image();
      mode2phase2Buttons();
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

  for(int i = 0; i < numPhotos; i++)
    image(Photos[i], 80, 140 + i*70, 80, 60);

  for(int i = 0; i < numPanels; i++)
    image(Panels[i], 80, (height/2 + 40) + i*70, 80, 60);
}



//__________________________________________________________________________________________________________________________
void displayStartButton()
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
void displayAddButtons()
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
public void startButton()
{
  println("Start button pressed ");
  mode = 1;
  cp5.hide();
}

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
}

//__________________________________________________________________________________________________________________________
void captureEvent(Capture webcam) 
{
  webcam.read();
  
}


//_______________________________________________________________________________________________
//*****************THINGS JASON DID!!!!***************************
//__________________________________________________________________
void keyPressed(){
 if (key == ' '){
   if( mode == 2 && phase == 1){
     
     try{
       mode2Capture = frame.get();      //takes the PImage frame and saves it to PImage mode2Capture
     } catch(NullPointerException e){
       }
     phase = 2;
     displayButtons = true;
   }
 }
  
}
