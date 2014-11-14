// Mode 0: Start Screen
// Mode 1: Overview

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
void displayPhotos()
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

