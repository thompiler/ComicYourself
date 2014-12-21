// Mode 0: Start Screen
// Mode 1: Overview

//__________________________________________________________________________________________________________________________
void drawStartScreen()
{
  PImage startLogo = loadImage("logo.png");
  image(startLogo, (width - startLogo.width)/2, (height - startLogo.height)/2);  
}



//==========================================================================================================================
void drawOverview()
{
  background(backgroundColor);
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
  displayExportedComic = false;
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



//==========================================================================================================================
public void mode1phase2display()
{
  background(backgroundColor);
  text("Pick your export preference", 20, 40);
  mode1phase2buttons();

  if(displayExportedComic)
  {
    fill(#817575);
    float comicH = exportedComic.height,
      comicW = exportedComic.width,
      ratio = (comicH/comicW)
      ;
    image(exportedComic, (width-800)/2, 80, 800, 800*ratio);
    //text("Exported Comic: ", 80, (height/2 + 150));
  }
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode1phase2buttons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode1phase2back")
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
/*
    cp5.addButton("mode1customExport")
      .setPosition(width - 220, 110)
      .setCaptionLabel("Custom Export")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(200, 40)
      ;
*/
    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode1phase2back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
  //Photos.remove(numPhotos);
  println("button back to overview ");
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
  println("Export button pressed as 3 wide strip");

  if(numPanels > 3)
  {
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels

    int border = 16;
    int totalBlocks = numPanels - numHalfPanels/2;
    int rowBlocks = totalBlocks/3;

    println("totalBlocks: "+totalBlocks+"   rowBlocks: "+rowBlocks);

    //PImage comicStrip = createImage(border + (border + 640)*(totalBlocks), 480 + 2 * border, RGB);

    PImage comicStrip = createImage(border + (border + 640)*3, border + (480 + border)*rowBlocks, RGB);

    comicStrip.loadPixels();
    //int Max = (border + (border + 640)*(numPanels - numHalfPanels/2));
    int MaxX = comicStrip.width;
    int MaxY = comicStrip.height;
    for (int i = 0; i < MaxX; i++)
      for (int j = 0; j < MaxY; j++)
        comicStrip.pixels[j*MaxX + i] = color(255, 255, 255);

    // 2. loop through panels and write them to output pimage
    int numBlocks = 0;

    for(int i = 0; i < numPanels; i++)
    {
      boolean written = false;
      boolean writtenVertical = false;
      println("num: "+i);
      int cX = border + (640 + border) * (numBlocks%3);
      int cY = border + (numBlocks/3)*(480+border);
/*
      if(numBlocks == 3 || numBlocks == 6 || numBlocks == 9)
      {
        cY = border + (numBlocks/3)*(480+border);
      }
      else
      {
        cY = border;
      }
*/
      Panels.get(i).loadPixels();

      if(PanelSizes.get(i) == 1)
      {
        println("cX: "+cX+"   cY: "+cY);
        comicStrip.copy(Panels.get(i), 0, 0, 640, 480, cX, cY, 640, 480);
      }
      else if(PanelSizes.get(i) == 2) 
      {
          int cX2 = border + (640 + border) * ((numBlocks)%3);
          // int cY2 = border + 480/2 + 8;
          int cY2 = border + ((numBlocks-1)/3)*(480+border) + 480/2 + 8;
        if(i > 0 && i%2 == 1)
        {
          //int cX2 = border + (640 + border) * ((numBlocks)%3);
          // int cY2 = border + 480/2 + 8;
          //int cY2 = border + ((numBlocks-1)/3)*(480+border) + 480/2 + 8;

          //if(PanelSizes.get(i-1) == 2)
          //{
            comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX, cY, 640, 480/2-8);
            numBlocks--;
            written = true;
            //println("h1 cX2: "+cX2+"   cY2: "+cY2+"  cY: "+cY);
          //}
        }
        else //if(!written)
        {
          comicStrip.copy(Panels.get(i), 0, 0, 640, 480/2-8, cX2, cY2, 640, 480/2-8);
          println("h2 cX: "+cX+"   cY: "+cY);
        }
      }
      else if(PanelSizes.get(i) == 3) 
      {
        if(i > 0 && i%2 == 1)
        {
          int cX3 = border + (640 + border) * ((numBlocks-1)%3) + 640/2 + 8;
          int cY3 = border + ((numBlocks-1)/3)*(480+border);
          //int cY3 = border;
          if(PanelSizes.get(i-1) == 3 && i%2 == 0)
          {
            comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX3, cY, 640/2-8, 480);
            numBlocks--;
            writtenVertical = true;
            println("v cX: "+cX3+"   cY: "+cY);
          }
        }
        else //if(!writtenVertical)
        {
          comicStrip.copy(Panels.get(i), 0, 0, 640/2-8, 480, cX, cY, 640/2-8, 480);
          println("v cX: "+cX+"   cY: "+cY);
        }
      }
      numBlocks++;
    }


    displayExportedComic = true;
    comicStrip.updatePixels();
    exportedComic = comicStrip;
    println("Comic Strip Block Exported to File");
    comicStrip.save("comicStripBlock.png");
  }
}


public void mode1customExport(){
  mode = 8;
  Jassetup();
  cp5.hide();
  displayButtons = true;
}