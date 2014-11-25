// File: Mode4.pde
// Mode4 is the photo editing hub
//	Phase 1 = simple drawing mode (ie: ms paint)
//	Phase 2 = selection and removal

// variable used:


//__________________________________________________________________________________________________________________________
void mode4phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase1back")
      .setPosition(width/2 - 50, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode4phase1draw")
      .setPosition(width/2 + 10, 677)
      .setCaptionLabel("Draw")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
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
public void mode4phase1draw()
{
  println("button: edit photo");
  phase = 2;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
}



//__________________________________________________________________________________________________________________________
void mode4phase2draw()
{
	mode4phase2displayButtons();

	if(displayPhoto)
	{
		displayPhoto(photoIndex);
		displayPhoto = false;
	}

	noStroke();
	fill(255, 255, 255);
	ellipse((width - 200)/2, 20, 50, 50);

	fill(paint);
	ellipse((width - 200)/2, 20, strokeWt, strokeWt);

	stroke(paint);
	strokeWeight(strokeWt);
	if(flag == 1
		&& mouseX >= (width - 800)/2
		&& mouseX <= (width - 800)/2 + 800
		&& mouseY >= 70
		&& mouseY <= 70 + 600)
		line(mouseX, mouseY, pmouseX, pmouseY);
}




//__________________________________________________________________________________________________________________________
void mode4phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase2back")
		.setPosition(width/2 - 50, 677)
		.setCaptionLabel("<")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(40, 40)
		;

    cp5.addButton("mode4phase2save")
		.setPosition(width/2 + 10, 677)
		.setCaptionLabel("Save")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(80, 40)
		;

    cp5.addSlider("brushSize")
    	.setCaptionLabel("Brush Size")
    	.setPosition((width - 100)/2, 20)
    	.setSize(100, 20)
    	.setRange(0, 50)
    	.setNumberOfTickMarks(10)
    	;

    cp5.getController("brushSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);


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
	Photos[numPhotos] = editPhoto;
	numPhotos++;
}


void brushSize(int theBrushSize)
{
	strokeWt = theBrushSize;
}