// Add text bubbles mode


//==========================================================================================================================
public void mode6phase1display()
{
	background(backgroundColor);
	mode6phase1displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	text("Click and drag to make the textbox size.", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(255);
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		stroke(255);
		strokeWeight(10);
		
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);

		noStroke();
	}
}



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode6phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;
      
   cp5.addButton("mode6phase1save")
      .setPosition((width-800)/2 + 50, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode6phase1back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase1save()
{
  	println("button: save text bubble");
  	if(rectX1 != 0)
	{
	  	phase = 2;
	  	cp5.hide();
	  	displayButtons = true;
	  	paint = color(255, 255, 255, 255);
	}
	else
		text("Please make a rectangle first.", 200, 40);

}


//__________________________________________________________________________________________________________________________
public void mode6mousePressed()
{
	if(phase == 1)
	{
		if(mouseY < 670 && mouseY > 70 && mouseX > (width-800)/2 && mouseX < (width+800)/2)
		{
			rectX1 = mouseX;
			rectY1 = mouseY;
		}	
	}
}



//==========================================================================================================================
public void mode6phase2display()
{
	background(backgroundColor);
	mode6phase2displayButtons();
	displayPhoto(currentPhotoIndex);
	textFont(font);
	fill(255);
	text("Enter text", 20, 40);
	if(rectX1 != 0 && rectX2 != 0)
	{
		println("rectX1: "+rectX1+",  rectY1: "+rectY1+", rectX2: "+rectX2+", rectY2: "+rectY2);
		fill(paint);
		noStroke();
		
		rect(rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, 7);
		strokeWeight(10);
		int triX = rectX1 + (rectX2 - rectX1)/2;
		int triY = rectY2;
		triangle(triX, triY, triX - 15, triY + 25, triX  - 10, triY);
		noStroke();
	}
	
	fill(0,0,0);
	int bubbleBorder = 10;
	text(cp5.get(Textfield.class,"textBubble").getText(), 
		rectX1+bubbleBorder, rectY1+bubbleBorder, 
		rectX2 - rectX1 - bubbleBorder, rectY2 - rectY1 - bubbleBorder);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode6phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode6phase2back")
		.setPosition((width-800)/2, 677)
		.setCaptionLabel("<")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(40, 40)
		.setColor(backButtonColor)
		;
      
   	cp5.addButton("mode6phase2save")
		.setPosition((width-800)/2 + 50, 677)
		.setCaptionLabel("Save")
		.align(CENTER,CENTER,CENTER,CENTER)
		.setSize(80, 40)
		;

	cp5.addTextfield("textBubble")
		.setCaptionLabel("Add text")
		.setPosition(width/2, 5)
		.setSize(200,40)
		.setFont(smallFont)
		.setFocus(true)
		.setColor(color(255,0,0))
		;

	cp = cp5.addColorPicker("colorPicker2")
  		.setPosition((width + 100)/2 + 220, 5)
  		.setColorValue(color(255, 255, 255, 255))
  		;

    displayButtons = false;
  }
}




//__________________________________________________________________________________________________________________________
public void mode6phase2back()
{
  println("button: back to photo list");
  mode = 4;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode6phase2save()
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
	Photos.add(editPhoto);
	numPhotos++;
}