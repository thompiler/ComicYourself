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

  // display rectangles to show where photos are displayed
  for(int i = 0; i < 5; i++)
  {
    noStroke();
    fill(#558CAD);
    rect(80 + i*90, 140, 80, 60);
    rect(80 + i*90, (height/2 + 40), 80, 60);
  }

  // display photos and panels created
  for(int i = 0; i < numPhotos; i++)
    image(Photos[i], 80 + i*90, 140, 80, 60);
  for(int i = 0; i < numPanels; i++) 
  {
    image(Panels[i], 80 + i*90, (height/2 + 40), 80, 60);

    // show "X" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
    {
      text("X", 80 + i*90, (height/2 + 40 + 35));
    }
  }


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
public void mode1export()
{
  println("Export button pressed ");

  if(numPanels > 0)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 15;
    PImage comicStrip = createImage(border + (border + 640)*numPanels, 480 + 2 * border, RGB);
    comicStrip.loadPixels();

    for (int i = 0; i < (border + (border + 640)*numPanels); i++)
      for (int j = 0; j < 480 + 2 * border; j++)
        comicStrip.pixels[j*(border + (border + 640)*numPanels) + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    for(int i = 0; i < numPanels; i++)
    {
      int cX = border + (640 + border) * i;
      int cY = border;
      Panels[i].loadPixels();
      for(int x = 0; x < 640; x++)
      {
        for(int y = 0; y < 480; y++)
        {
          comicStrip.pixels[y*comicStrip.width + cY*comicStrip.width + cX + x] = Panels[i].pixels[y*640 + x];
        }
      }
    }
    comicStrip.updatePixels();
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }
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

