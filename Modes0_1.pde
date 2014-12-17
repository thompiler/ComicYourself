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



//==========================================================================================================================
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
  fill(#CE235F);
  for(int i = 0; i < numPhotos; i++)
  {
    image(Photos[i], 80 + i*90, 140, 80, 60);

    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= 140
      && mouseY <= 140 + 60)
      text("Edit", 80 + i*90, 140 + 35);
  }
  for(int i = 0; i < numPanels; i++) 
  {
    if(PanelSizes[i] == 1)
      image(Panels[i], 80 + i*90, (height/2 + 40), 80, 60);
    else if(PanelSizes[i] == 2)
      image(Panels[i], 80 + i*90, (height/2 + 40), 80, 30);
    // show "Edit" on panel when mouse over
    if(mouseX >= 80 + i*90
      && mouseX <= 80 + i*90 + 80
      && mouseY >= height/2 + 40
      && mouseY <= height/2 + 40 + 60)
      text("Edit", 80 + i*90, (height/2 + 40 + 35));
  }
  if(displayExportedComic)
  {
    fill(#817575);
    float ratio = (exportedComic.width/exportedComic.height);
    image(exportedComic, 80, (height/2 + 190), 100*ratio, 100);
    text("Exported Comic: ", 80, (height/2 + 150));
  }
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
  for(int i = 0; i < numPhotos - currentPhotoIndex; i++)
  {
    image(Photos[currentPhotoIndex + i], 80, (height/2 + 40) + i*70, 80, 60);
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
      println(i);
      int cX = border + (640 + border) * numBlocks;
      int cY = border;
      Panels[i].loadPixels();

      if(PanelSizes[i] == 1)
      {
        comicStrip.copy(Panels[i], 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes[i] == 2) 
      {
        if(i > 0)
        {
          int cX2 = border + (640 + border) * (numBlocks-1);
          int cY2 = border + 480/2;
          if(PanelSizes[i-1] == 2)
          {
            comicStrip.copy(Panels[i], 0, 0, 640, 480/2, cX2, cY2, 640, 480/2);
            numBlocks--;
            written = true;
          }
        }
        if(!written)
          comicStrip.copy(Panels[i], 0, 0, 640, 480/2, cX, cY, 640, 480/2);
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
public void addPanel()
{
  println("+panel button pressed");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
void mode1mousePressed()
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

