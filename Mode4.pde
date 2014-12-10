// File: Mode4.pde
// Mode 4 is the photo editing hub
//	Phase 1 = editing hub with buttons to different phases
//  Phase 2 = simple drawing mode (ie: ms paint)
//	Phase 3 = resize a photo


//__________________________________________________________________________________________________________________________
void mode4phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode4phase1draw")
      .setPosition(width/2 - 100, 677)
      .setCaptionLabel("Draw")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode4phase1select")
      .setPosition(width/2 - 200, 677)
      .setCaptionLabel("Select")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    cp5.addButton("mode4phase1text")
      .setPosition(width/2, 677)
      .setCaptionLabel("Text")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
    
    cp5.addButton("mode4phase1resize")
      .setPosition(width/2 + 100, 677)
      .setCaptionLabel("Resize")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(90, 40)
      ;

    cp5.addButton("mode4phase1layer")
      .setPosition(width/2 + 200, 677)
      .setCaptionLabel("Layer")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(90, 40)
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
public void mode4phase1select()
{
  phase = 6;
  cp5.hide();
  displayButtons = true;
  cropX1 = 0;
  cropY1 = 0;
  cropX2 = 0;
  cropY2 = 0;
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
  paint = color(255, 128, 0, 255);
}


//__________________________________________________________________________________________________________________________
public void mode4phase1text()
{
  println("button: add text to photo");
  mode = 6;
  phase = 1;
  rectX1 = 0;
  rectY1 = 0;
  cp5.hide();
  displayButtons = true;
}

//__________________________________________________________________________________________________________________________
public void mode4phase1resize()
{
  println("button: resize photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
  //displayResizePhoto = true;
  resizeValue = 100;
}


//__________________________________________________________________________________________________________________________
public void mode4phase1layer()
{
  println("button: layer photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
  displayPhoto = true;
  background(255);
}


//__________________________________________________________________________________________________________________________
void mode4phase2draw()
{
  fill(#909090);
  textFont(smallFont);
  text("Brush size:", (width - 100)/2 - 30, 15);
  textFont(buttonFont);
        
	mode4phase2displayButtons();

	if(displayPhoto)
	{
		displayPhoto(currentPhotoIndex);
		displayPhoto = false;
	}

	stroke(255, 255, 255);
	fill(255, 255, 255);
	ellipse((width - 200)/2 - 60, 20, 60, 60);

	fill(paint);
  stroke(paint);
	ellipse((width - 200)/2 - 60, 20, strokeWt, strokeWt);

	stroke(paint);
	strokeWeight(strokeWt);

	if(flag == 1
		&& mouseX >= (width - 800)/2
		&& mouseX <= (width - 800)/2 + 800
		&& mouseY >= 70
		&& mouseY <= 70 + 600
    )
		  line(mouseX, mouseY, pmouseX, pmouseY);
}


//__________________________________________________________________________________________________________________________
void displayResizedPhoto(int index, float resize)
{
  image(Photos[index], (width - (800 * (resize/100)))/2, 70 + (300 - (600 * (resize/100)/2)), 800 * (resize/100), 600 * (resize/100));
}


//__________________________________________________________________________________________________________________________
void mode4phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);
    fill(#909090);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase2back")
  		.setPosition((width-800)/2, 677)
  		.setCaptionLabel("<")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(40, 40)
  		;

    cp5.addButton("mode4phase2save")
  		.setPosition(width/2, 677)
  		.setCaptionLabel("Save")
  		.align(CENTER,CENTER,CENTER,CENTER)
  		.setSize(80, 40)
  		;

    cp5.addSlider("brushSize")
    	.setCaptionLabel("")
    	.setPosition((width - 100)/2 - 30, 20)
    	.setSize(100, 20)
    	.setRange(1, 50)
        .setValue(5)
    	.setNumberOfTickMarks(10)
    	;

    cp5.getController("brushSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    cp5.setControlFont(smallFont);

  	cp = cp5.addColorPicker("colorPicker")
  		.setPosition((width + 100)/2 + 20, 5)
  		.setColorValue(color(255, 128, 0, 255))
  		;

    cp5.setControlFont(buttonFont);

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


//__________________________________________________________________________________________________________________________
void brushSize(int theBrushSize) { strokeWt = theBrushSize; }


//__________________________________________________________________________________________________________________________
void picker(int col)
{
  println("picker\talpha:"
    +alpha(col)
    +"\tred:"+red(col)
    +"\tgreen:"+green(col)
    +"\tblue:"+blue(col)
    +"\tcol"+col)
    ;
}


//__________________________________________________________________________________________________________________________
void mode4phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase2back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;
      
   cp5.addButton("mode4phase3save")
      .setPosition(width/2, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
      
    cp5.addSlider("imageSize")
      .setCaptionLabel("")
      .setPosition((width - 100)/2 - 30, 20)
      .setSize(100, 20)
      .setRange(1, 100)
      //.setDefaultValue(100)
      .setValue(100)
      //.setNumberOfTickMarks(50)
      ;

    cp5.getController("imageSize").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase3save()
{
  println("button: save resized photo to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true; 
            
  // save edited photo to photo list
  int resizedHeight = (int)(480 * (resizeValue/100));
  int resizedWidth = (int)(640 * (resizeValue/100));
  int resizedHeight2 = (int)(600 * (resizeValue/100));
  int resizedWidth2 = (int)(800 * (resizeValue/100));
  int resizedX = (int)((width - (800 * (resizeValue/100)))/2);
  int resizedY = (int)(70 + (300 - (600 * (resizeValue/100)/2)));
  PImage screenShot = get();
  println("Created resized photo with dimensions: "+resizedWidth+", "+resizedHeight);
  editPhoto = createImage(resizedWidth, resizedHeight, RGB);
  //editPhoto.copy(screenShot, (width - 800)/2, 70, 800, 600, 0, 0, resizedWidth, resizedHeight);
  editPhoto.copy(screenShot, resizedX, resizedY, resizedWidth2, resizedHeight2, 0, 0, resizedWidth, resizedHeight);
  Photos[numPhotos] = editPhoto;
  numPhotos++;
}


//__________________________________________________________________________________________________________________________
void imageSize(float value)
{
  println(value);
  resizeValue = value;
}



//__________________________________________________________________________________________________________________________
void mode4phase4display()
{
  textFont(font);
  fill(#817575);
  background(#012E4B);
  displayPhoto(currentPhotoIndex);

  for(int i = 0; i < numLayers; i++)
  {
    int layerWidth = (int)((1.25)*Photos[Layers[i]].width);
    int layerHeight = (int)((1.25)*Photos[Layers[i]].height);
    println("("+LayersX[i]+", "+LayersY[i]+") dims: "+layerWidth+", "+layerHeight);
    image(Photos[Layers[i]], LayersX[i], LayersY[i], layerWidth, layerHeight);
  }

  noStroke();
  fill(#012E4B);
  rect(0, 0, width, 70);    //top
  rect(0, 0, (width-800)/2, height); // left
  rect(0, 70+600, width, height-670); // bottom
  rect((width+800)/2, 0, width-(width+800)/2, height); // right
  text("Layer", 20, 40);
  mode4phase4displayButtons();
}


//__________________________________________________________________________________________________________________________
void mode4phase4displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase4back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode4phase4addPhoto")
      .setPosition(width/2, 677)
      .setCaptionLabel("+Photo")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;
    
    cp5.addButton("mode4phase4save")
      .setPosition(width/2 + 100, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase4back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
  numLayers = 0;
}


//__________________________________________________________________________________________________________________________
public void mode4phase4save()
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
  numLayers = 0;

}


//__________________________________________________________________________________________________________________________
public void mode4phase4addPhoto()
{
  textFont(font);
  fill(#817575);
  println("button: pick a photo from photo list");
  phase = 5;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
void mode4phase5display()
{
  textFont(font);
  fill(#817575);
  background(#012E4B);
  mode3displayPhotos();
  text("Pick a photo to add as a layer", 20, 40);
  mode4phase5displayButtons();
}



//__________________________________________________________________________________________________________________________
void mode4phase5displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase5back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode4phase5back()
{
  println("button: back to photo");
  phase = 4;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
void mode4mousePressed()
{
  if(phase == 5)
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
        Layers[numLayers] = i;
        LayersX[numLayers] = (width - 800)/2;
        LayersY[numLayers] = 70;
        numLayers++;
        phase = 4;
        println("-- added photo as layer");


        cp5.hide();
        displayButtons = true;
      }
    }
  }
  else if(phase == 6)
  {
    if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
    {
      cropX1 = mouseX;
      cropY1 = mouseY;
    }
  }
}



//__________________________________________________________________________________________________________________________
void mode4phase6display() // Simple selection and crop mode
{
  textFont(font);
  fill(#817575);
  background(#012E4B);
  displayPhoto(currentPhotoIndex);
  text("Click and drag crop selection", 20, 40);
  mode4phase6displayButtons();
  if(cropX1 != 0 && cropX2 != 0)
  {
    noFill();
    strokeWeight(1);
    stroke(255);
    rect(cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1);
  }
}



//__________________________________________________________________________________________________________________________
void mode4phase6displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode4phase6back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode4phase6save")
      .setPosition((width)/2, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode4phase6back()
{
  phase = 1;
  cp5.hide();
  displayButtons = true;
}




//__________________________________________________________________________________________________________________________
public void mode4phase6save()
{
  println("button: cropped photo saved to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // save edited photo to photo list

  int temp;
  if(cropX1 > cropX2)
  {
    temp = cropX1;
    cropX1 = cropX2;
    cropX2 = temp;
  }
  if(cropY1 > cropY2)
  {
    temp = cropY1;
    cropY1 = cropY2;
    cropY2 = temp;
  }


  displayPhoto(currentPhotoIndex);
  PImage screenShot = get();

  int cropWidth = cropX2 - cropX1;
  int cropHeight = cropY2 - cropY1;
  cropWidth = (int)(cropWidth * 0.8);
  cropHeight = (int)(cropHeight * 0.8);

  editPhoto = createImage(cropWidth, cropHeight, RGB);
  editPhoto.copy(screenShot, cropX1, cropY1, cropX2 - cropX1, cropY2 - cropY1, 0, 0, cropWidth, cropHeight);
  Photos[numPhotos] = editPhoto;
  numPhotos++;
}