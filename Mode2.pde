// Mode 2: Take a picture

//__________________________________________________________________________________________________________________________
void drawCam()
{  
    textFont(font);
    text("Capture Mode", 20, 40);

    frame = webcam;
    
    if(removeBackground)
    	removeBackground(frame);
	
	pushMatrix();

	//flip across x axis
	scale(-1,1);
	image(frame, -(width - 800)/2 -800, 70, 800, 600);
	popMatrix(); 
}

void removeBackground(PImage frame)
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
        
        if(diff < threshold){
              frame.pixels[loc] = color(255);
        }
        
      }
    }
    frame.updatePixels();        
    
}



//__________________________________________________________________________________________________________________________
void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("takePhoto")
			.setPosition(width/2 + 10, 677)
			.setCaptionLabel("C")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addButton("backButton")
			.setPosition(width/2 - 50, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;
                
                cp5.addButton("goToCalibrationPhase")
                        .setPosition(width/2 + 60, 677)
                        .setCaptionLabel("Cal")
                        .align(CENTER,CENTER,CENTER,CENTER)
                        .setSize(40, 40)
                        ;

		displayButtons = false;
	}
}



//__________________________________________________________________________________________________________________________
void mode2phase2Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("mode2phase2save")
			.setPosition(width/2 + 10, 677)
			.setCaptionLabel("S")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addButton("mode2phase2back")
			.setPosition(width/2 - 50, 677)
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
	Snap.play();
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
	mirror(mode2Capture);
	Photos[numPhotos] = mode2Capture;
	numPhotos++;
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
public void backButton()
{
	mode = 1;
	cp5.hide();  
	displayButtons = true;
}

public void goToCalibrationPhase()
{
        phase = 3;
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

//_________________________________________________________________
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
//-----------------------------
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
      .setPosition(width/2 - 50, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
  
}

//----------------------------------
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
//-----------------------------
public void mode2phase3back()
{
  phase = 1;
  cp5.hide();  
  displayButtons = true;
}