void drawCam()
{
  
  
  frame = webcam;
   
  pushMatrix();

  //flip across x axis
  scale(-1,1);
  image(frame, -700, 50, 640, 480);
  popMatrix();
    
}

void mode2phase1Buttons(){
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    println("  Add Buttons");

    cp5.addButton("takePhoto")
      .setPosition(800, 200)
      .setCaptionLabel("Cap")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("backButton")
      .setPosition(800, 300)
      .setCaptionLabel("<-")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
  

}

public void takePhoto(){
  try{
       mode2Capture = frame.get();
     } catch(NullPointerException e){
       }
  phase = 2;
  cp5.hide();
  mirror(mode2Capture);
  displayButtons = true;
}

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


public void backButton(){
  mode = 1;
  cp5.hide();  
  displayButtons = true;
}

public void mode2phase2Image(){
  
  image(mode2Capture, 60,50, 640,480);
  
  
}

public void mode2phase2Buttons(){
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);

    println("  Add Buttons");

    cp5.addButton("savePhoto")
      .setPosition(800, 200)
      .setCaptionLabel("S")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    cp5.addButton("back2Button")
      .setPosition(800, 300)
      .setCaptionLabel("<-")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      ;

    displayButtons = false;
  }
}

public void savePhoto(){
  Photos[numPhotos] = mode2Capture.get(); 
  mode = 1;
  phase = 1;
  cp5.hide();
  displayButtons = true; 
}

public void back2Button(){
  phase = 1;
  cp5.hide();  
  displayButtons = true;
}
