// Mode 3: Add a panel
//		Phase 1: show horizontal list of photos to add
//		Phase 2: show clicked photo large '<' button goes back and 'S' button adds clicked photo to panels array


//__________________________________________________________________________________________________________________________
void mode3displayPhotos()
{
	background(#012E4B);
	for(int i = 0; i < numPhotos; i++)
		image(Photos[i], 80 + i*110, (height/2 + 40), 100, 75);
}



//__________________________________________________________________________________________________________________________
void displayPhoto(int index)
{
	image(Photos[index], 100, 100, 880, 660);
}



//__________________________________________________________________________________________________________________________
void mode3displayButtons()
{
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    cp5.addButton("mode3back")
      .setPosition(200, 100 - 32)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("mode3save")
      .setPosition(200, height/2 - 32)
      .setCaptionLabel("S")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}



//__________________________________________________________________________________________________________________________
public void mode3back()
{
  println("button: back to photo list");
  mode = 3;
  phase = 1;
  cp5.hide();
  displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode3save()
{
  println("button: save panel");
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true;

  // Save copy of selected photo in panel array
  PImage newPanel = Photos[photoIndex];
  Panels[numPanels] = newPanel;
}



//__________________________________________________________________________________________________________________________
void mode3mousePressed()
{
	if(phase == 1)
	{
		for(int i = 0; i < numPhotos; i++)
		{
			int x = Photos[i].width;
			int y = Photos[i].height;
			int photoX = 80 + i*110;
			int photoY = height/2 + 40;

			if(mouseX >= photoX 
				&& mouseX <= photoX + x 
				&& mouseY >= photoY 
				&& mouseY <= photoY + y)
				photoIndex = i;
		}
	}
}
