// Mode 2: Take a picture
// Author: Jason

//==========================================================================================================================
void drawCam()
{  
  textFont(font);
  text("Capture Mode", 20, 40);

  frame = webcam;
  
  //if(removeBackground)
  //	removeBackground(frame);
	
	pushMatrix();

	//flip across x axis
	scale(-1,1);

	if(removeBackground)
		image(removeBackground(frame.get()), -(width - 800)/2 -800, 70, 800, 600);

  else if(changeBackground)
    image(changeBackground(removeBackground(frame.get())), -(width - 800)/2 -800, 70, 800, 600);

	else
		image(frame, -(width - 800)/2 -800, 70, 800, 600);	
  
	popMatrix(); 
}

PImage removeBackground(PImage frame)
{       
        
    mode2Calibration.loadPixels();
    frame.loadPixels();
    for (int y=0; y<frame.height; y++) {
      for (int x=0; x<frame.width; x++) {
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

		cp5.addButton("takePhoto")
			.setPosition(width/2 - 30, 677)
			.setCaptionLabel("Capture")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(100, 40)
			;

		cp5.addButton("backButton")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;
                
    cp5.addButton("goToCalibrationPhase")
      .setPosition(width/2 + 100, 677)
      .setCaptionLabel("Calibrate")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(110, 40)
      ;

    cp5.addButton("mode2phase1flickr")
      .setPosition((width+800)/2+10, 70)
      .setCaptionLabel("flickr")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(100, 40)
      ;  

    if(removeBackground){
      cp5.addButton("backgroundSelection")
        .setPosition(width/2 + 250, 677)
        .setCaptionLabel("Background")
        .align(CENTER,CENTER,CENTER,CENTER)
        .setSize(200, 40)
        ;

      cp5.addSlider("thresholdSize")
        .setCaptionLabel("Replace threshold")
        .setPosition((width - 100)/2 - 30, 20)
        .setSize(100, 20)
        .setRange(20, 150)
        .setValue(70)
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
  showFlickrResults = false;
}

//__________________________________________________________________________________________________________________________
void thresholdSize(int value)
{
  println(value);
  threshold = value;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode2phase2Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("mode2phase2save")
			.setPosition(width/2 + 40, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
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

    cp5.addButton("takeCalibrationPhoto")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("C")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode2phase3back")
      .setPosition((width-800)/20, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
  
}


//__________________________________________________________________________________________________________________________
public void takeCalibrationPhoto(){
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
  for(int i=0; i<files.length; i++){
    stockBackground[i]=loadImage(files[i].getAbsolutePath());
    imageResize(stockBackground[i]);
  }
}


//__________________________________________________________________________________________________________________________
public void imageResize(PImage img){
  img.resize(640,480);
}


//__________________________________________________________________________________________________________________________
public void backgroundSelection()
{
  loadStockBackground();
  changeBackground = true;
  removeBackground = false;  
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
