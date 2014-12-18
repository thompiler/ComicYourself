// Mode 3: Add a panel
//		Phase 1: show horizontal list of photos to add
//		Phase 2: show clicked photo large '<' button goes back and 'S' button adds clicked photo to panels array


//__________________________________________________________________________________________________________________________
void mode3displayPhotos()
{
	background(#012E4B);
	for(int i = 0; i < numPhotos; i++)
		image(Photos[i], 80 + i*110, height/2, 100, 75);
}


//__________________________________________________________________________________________________________________________
void displayPhoto(int index)
{
	image(Photos[index], (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode3phase1displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3phase1back")
      .setPosition((width-800)/2, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase1back()
{
  println("button: back to photo list");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode3phase2displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase2back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    offset += 40 + 20;

    cp5.addButton("mode3phase2save")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(80, 40)
      ;

    offset += 80 + 10;

    cp5.addButton("mode3phase2makeHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Make Half")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(120, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase2back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2save()
{
  println("button: save panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newPanel = Photos[currentPhotoIndex];
  Panels[numPanels] = newPanel;
  PanelSizes[numPanels] = 1;
  numPanels++;
}


//__________________________________________________________________________________________________________________________
public void mode3phase2makeHalf()
{
  println("button: display half photo");
  phase = 3;
  cp5.hide();
  displayButtons = true;
  halfX = (width - 800)/2;
  halfY = 70;
}


//===========================================================================================================================
public void mode3phase3display()
{
  background(#012E4B);
  text("Move the rectangle to pick region to save", 20, 40);
  mode3phase3displayButtons();
  displayPhoto(currentPhotoIndex);
  stroke(255);
  noFill();
  strokeWeight(3);
  rect(halfX, halfY, 800, 300);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode3phase3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    int left = (width-800)/2;
    int offset = 0;

    cp5.addButton("mode3phase3back")
      .setPosition(left, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    offset += 40 + 20;

    cp5.addButton("mode3phase3saveHalf")
      .setPosition(left + offset, 677)
      .setCaptionLabel("Save Region")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(140, 40)
      ;

    displayButtons = false;
  }
}


//__________________________________________________________________________________________________________________________
public void mode3phase3back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 2;
  cp5.hide();
  displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode3phase3saveHalf()
{
  println("button: save half panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newHalfPanel = createImage(640, 480/2, RGB); 
  newHalfPanel.copy(Photos[currentPhotoIndex], 0, halfY-70, 640, 480/2, 0, 0, 640, 480/2);
  Panels[numPanels] = newHalfPanel;
  PanelSizes[numPanels] = 2;
  numPanels++;
  numHalfPanels++;
}


//__________________________________________________________________________________________________________________________
void mode3mousePressed()
{
	if(phase == 1)
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
				currentPhotoIndex = i;
				phase = 2;

        cp5.hide();
        displayButtons = true;
			}
		}
	}
}
