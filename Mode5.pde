// Edit Panel mode



//__________________________________________________________________________________________________________________________
void displayPanel(int index)
{
	image(Panels[index], (width - 800)/2, 70, 800, 600);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode5phase1displayButtons()
{
	if(displayButtons)
  	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode5phase1back")
	      .setPosition((width-800)/2, 677)
	      .setCaptionLabel("<")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(40, 40)
	      ;

	    cp5.addButton("mode5phase1delete")
	      .setPosition(width/2 - 20, 677)
	      .setCaptionLabel("Delete")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(90, 40)
	      ;
	    
	    cp5.addButton("mode5phase1left")
	      .setPosition(width/2 + 100, 677)
	      .setCaptionLabel("Left")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    cp5.addButton("mode5phase1right")
	      .setPosition(width/2 + 200, 677)
	      .setCaptionLabel("Right")
	      .align(CENTER,CENTER,CENTER,CENTER)
	      .setSize(80, 40)
	      ;

	    displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode5phase1back()
{
	println("button: back to photo list");
	mode = 1;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}



//__________________________________________________________________________________________________________________________
public void mode5phase1delete()
{
  	println("button: deleted panel: " + currentPanelIndex);
  	mode = 1;
  	phase = 1;
  	cp5.hide();
  	displayButtons = true;

  	// delete panel and move panels behind it forward
  	for(int i = currentPanelIndex + 1; i < numPanels; i++)
  	{
  		Panels[i - 1] = Panels[i];
  	}
  	numPanels--;
}



