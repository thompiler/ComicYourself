// Mode 2: Take a picture

//==========================================================================================================================
void drawCam()
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
PImage removeBackground(PImage frame)
{       
        
    mode2Calibration.loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        

        if(inHSBmode)
        {
          colorMode(HSB, 255);
          
          int loc = x + y * frame.width;
          color display = frame.pixels[loc];
          color comparison = mode2Calibration.pixels[loc];
          
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
          color display = frame.pixels[loc];
          color comparison = mode2Calibration.pixels[loc];

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
void mode2phase1Buttons()
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
      mode2Capture = calibratedFrame.get();
    else if(changeBackground)
      mode2Capture = editedFrame.get();                
    else
      mode2Capture = frame.get();
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
void thresholdSize(int value)
{
  threshold = value;
}

//__________________________________________________________________________________________________________________________
void hueThresholdSize(int value)
{
  hueThreshold = value;
}

//__________________________________________________________________________________________________________________________
void satThresholdSize(int value)
{
  satThreshold = value;
}

//__________________________________________________________________________________________________________________________
void brtThresholdSize(int value)
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
void mode2phase2Buttons()
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
void mirror(PImage capImg) {
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
PImage changeBackground(PImage frame)
{       
    stockBackground[0].loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
        int loc = x + y * frame.width;
        color display = frame.pixels[loc];        
        
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
PImage displayCustomBackground(PImage frame)
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
      color display = frame.pixels[loc];        
      
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
void mode2mousePressed()
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