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
  displayButtons = true;
}

public void backButton(){
  mode = 1;
  cp5.hide();  
  displayButtons = true;
}
