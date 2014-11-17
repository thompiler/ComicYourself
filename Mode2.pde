// Mode 2: Take a picture

//__________________________________________________________________________________________________________________________
void drawCam()
{
	frame = webcam;

	pushMatrix();

	//flip across x axis
	scale(-1,1);
	image(frame, -700, 50, 640, 480);
	popMatrix(); 
}



//__________________________________________________________________________________________________________________________
void mode2phase1Buttons()
{
	if(displayButtons)
	{
		cp5 = new ControlP5(this);

		cp5.setControlFont(buttonFont);

		cp5.addButton("takePhoto")
			.setPosition(800, 200)
			.setCaptionLabel("C")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addButton("backButton")
			.setPosition(800, 300)
			.setCaptionLabel("<")
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