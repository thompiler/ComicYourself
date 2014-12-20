void mode8mouseReleased(){
  
  //box1
  
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=204 && !(vb1||hb1)){
    box1=false;
    if(imageSelected){
    boxImage[0]=draggedImage.get();
    drawBox1Image=true;
    }
  }
  //////////////vb1
  if(mouseX>=54 && mouseX<=152 && mouseY>=54 && mouseY<=204 && vb1){
    b1v1=false;
    fb1v1x=154-b1v1x;
    if(imageSelected){
      vboxImage[0]=draggedImage.get();
    db1v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=54 && mouseY<=204 && vb1){
    b1v2=false;
    fb1v2x=256-b1v2x;
    if(imageSelected){
      vboxImage[1]=draggedImage.get();
    db1v2=true;
    }
  }
  //////////////hb1
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=127 && hb1){
    b1h1=false;
    fb1h1y=129-b1h1y;
    if(imageSelected){
      hboxImage[0]=draggedImage.get();
    db1h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=131 && mouseY<=204 && hb1){
    b1h2=false;
    fb1h2y=206-b1h2y;
    if(imageSelected){
      hboxImage[1]=draggedImage.get();
    db1h2=true;
    }
  }
  
  
  
  //box2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=204 && !(vb2||hb2)){
    box2=false;
    if(imageSelected){
    boxImage[1]=draggedImage.get();
    drawBox2Image=true;
    }
  }
  //vb2
  if(mouseX>=258 && mouseX<=356 && mouseY>=54 && mouseY<=204 && vb2){
    b2v1=false;
    fb2v1x=358-b2v1x;
    if(imageSelected){
      vboxImage[2]=draggedImage.get();
    db2v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=54 && mouseY<=204 && vb2){
    b2v2=false;
    fb2v2x=460-b2v2x;
    if(imageSelected){
      vboxImage[3]=draggedImage.get();
    db2v2=true;
    }
  }
  //hb2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=127 && hb2){
    b2h1=false;
    fb1h1y=129-b1h1y;
    if(imageSelected){
      hboxImage[2]=draggedImage.get();
    db2h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=131 && mouseY<=204 && hb2){
    b2h2=false;
    fb1h2y=206-b1h2y;
    if(imageSelected){
      hboxImage[3]=draggedImage.get();
    db2h2=true;
    }
  }
  
  
  
  //box3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=204){
    box3=false;
    if(imageSelected){
    boxImage[2]=draggedImage.get();
    drawBox3Image=true;
    }
  }
  //vb3
  if(mouseX>=462 && mouseX<=560 && mouseY>=54 && mouseY<=204 && vb3){
    b3v1=false;
    fb3v1x=562-b3v1x;
    if(imageSelected){
      vboxImage[4]=draggedImage.get();
    db3v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=54 && mouseY<=204 && vb3){
    b3v2=false;
    fb3v2x=664-b3v2x;
    if(imageSelected){
      vboxImage[5]=draggedImage.get();
    db3v2=true;
    }
  }
  //hb3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=127 && hb3){
    b3h1=false;
    fb3h1y=129-b3h1y;
    if(imageSelected){
      hboxImage[4]=draggedImage.get();
    db3h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=131 && mouseY<=204 && hb3){
    b3h2=false;
    fb3h2y=206-b3h2y;
    if(imageSelected){
      hboxImage[5]=draggedImage.get();
    db3h2=true;
    }
  }
  
  
  //box4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=358){
    box4=false;
    if(imageSelected){
    boxImage[3]=draggedImage.get();
    drawBox4Image=true;
    }
  }
  //vb4
  if(mouseX>=54 && mouseX<=152 && mouseY>=208 && mouseY<=358 && vb4){
    b4v1=false;
    fb4v1x=154-b4v1x;
    if(imageSelected){
      vboxImage[6]=draggedImage.get();
    db4v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=208 && mouseY<=358 && vb4){
    b4v2=false;
    fb4v2x=256-b4v2x;
    if(imageSelected){
      vboxImage[7]=draggedImage.get();
    db4v2=true;
    }
  }
  //hb4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=281 && hb4){
    b4h1=false;
    fb4h1y=283-b4h1y;
    if(imageSelected){
      hboxImage[6]=draggedImage.get();
    db4h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=285 && mouseY<=358 && hb4){
    b4h2=false;
    fb4h2y=360-b4h2y;
    if(imageSelected){
      hboxImage[7]=draggedImage.get();
    db4h2=true;
    }
  }
  
  
  
  //box5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=358){
    box5=false;
    if(imageSelected){
    boxImage[4]=draggedImage.get();
    drawBox5Image=true;
    }
  }
  //vb5
  if(mouseX>=258 && mouseX<=356 && mouseY>=208 && mouseY<=358 && vb5){
    b5v1=false;
    fb5v1x=358-b5v1x;
    if(imageSelected){
      vboxImage[8]=draggedImage.get();
    db5v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=208 && mouseY<=358 && vb5){
    b5v2=false;
    fb5v2x=458-b5v2x;
    if(imageSelected){
      vboxImage[9]=draggedImage.get();
    db5v2=true;
    }
  }
  //hb5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=281 && hb5){
    b5h1=false;
    fb5h1y=283-b5h1y;
    if(imageSelected){
      hboxImage[8]=draggedImage.get();
    db5h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=285 && mouseY<=358 && hb5){
    b5h2=false;
    fb5h2y=360-b5h2y;
    if(imageSelected){
      hboxImage[9]=draggedImage.get();
    db5h2=true;
    }
  }
  
  
  
  //box6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=358){
    box6=false;
    if(imageSelected){
    boxImage[5]=draggedImage.get();
    drawBox6Image=true;
    }
  }
  //vb6
  if(mouseX>=462 && mouseX<=560 && mouseY>=208 && mouseY<=358 && vb6){
    b6v1=false;
    fb6v1x=562-b6v1x;
    if(imageSelected){
      vboxImage[10]=draggedImage.get();
    db6v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=208 && mouseY<=358 && vb6){
    b6v2=false;
    fb6v2x=664-b6v2x;
    if(imageSelected){
      vboxImage[11]=draggedImage.get();
    db6v2=true;
    }
  }
  //hb6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=281 && hb6){
    b6h1=false;
    fb6h1y=283-b6h1y;
    if(imageSelected){
      hboxImage[10]=draggedImage.get();
    db6h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=285 && mouseY<=358 && hb6){
    b6h2=false;
    fb6h2y=360-b6h2y;
    if(imageSelected){
      hboxImage[11]=draggedImage.get();
    db6h2=true;
    }
  }
  
  
  
  //box7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=512){
    box7=false;
    if(imageSelected){
    boxImage[6]=draggedImage.get();
    drawBox7Image=true;
    }
  }
  //vb7
  if(mouseX>=54 && mouseX<=152 && mouseY>=362 && mouseY<=512 && vb7){
    b7v1=false;
    fb7v1x=154-b7v1x;
    if(imageSelected){
      vboxImage[12]=draggedImage.get();
    db7v1=true;
    }
  }
  if(mouseX>=156 && mouseX<=254 && mouseY>=362 && mouseY<=512 && vb7){
    b7v2=false;
    fb7v2x=256-b7v2x;
    if(imageSelected){
      vboxImage[13]=draggedImage.get();
    db7v2=true;
    }
  }
  //hb7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=435 && hb7){
    b7h1=false;
    fb7h1y=437-b7h1y;
    if(imageSelected){
      hboxImage[12]=draggedImage.get();
    db7h1=true;
    }
  }
  if(mouseX>=54 && mouseX<=254 && mouseY>=439 && mouseY<=512 && hb7){
    b7h2=false;
    fb7h2y=514-b7h2y;
    if(imageSelected){
      hboxImage[13]=draggedImage.get();
    db7h2=true;
    }
  }
  
  
  
  //box8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=512){
    box8=false;
    if(imageSelected){
    boxImage[7]=draggedImage.get();
    drawBox8Image=true;
    }
  }
  //vb8
  if(mouseX>=258 && mouseX<=356 && mouseY>=362 && mouseY<=512 && vb8){
    b8v1=false;
    fb8v1x=358-b8v1x;
    if(imageSelected){
      vboxImage[14]=draggedImage.get();
    db8v1=true;
    }
  }
  if(mouseX>=360 && mouseX<=458 && mouseY>=362 && mouseY<=512 && vb8){
    b8v2=false;
    fb8v2x=460-b8v2x;
    if(imageSelected){
      vboxImage[15]=draggedImage.get();
    db8v2=true;
    }
  }
  //hb8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=435 && hb8){
    b8h1=false;
    fb8h1y=437-b8h1y;
    if(imageSelected){
      hboxImage[14]=draggedImage.get();
    db8h1=true;
    }
  }
  if(mouseX>=258 && mouseX<=458 && mouseY>=439 && mouseY<=512 && hb8){
    b8h2=false;
    fb8h2y=514-b8h2y;
    if(imageSelected){
      hboxImage[15]=draggedImage.get();
    db8h2=true;
    }
  }
  
  
  
  //box9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=512){
    box9=false;
    if(imageSelected){
    boxImage[8]=draggedImage.get();
    drawBox9Image=true;
    }
  }
  //vb9
  if(mouseX>=462 && mouseX<=560 && mouseY>=362 && mouseY<=512 && vb9){
    b9v1=false;
    fb9v1x=562-b9v1x;
    if(imageSelected){
      vboxImage[16]=draggedImage.get();
    db9v1=true;
    }
  }
  if(mouseX>=564 && mouseX<=662 && mouseY>=362 && mouseY<=512 && vb9){
    b9v2=false;
    fb9v2x=664-b9v2x;
    if(imageSelected){
      vboxImage[17]=draggedImage.get();
    db9v2=true;
    }
  }
  //hb9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=435 && hb9){
    b9h1=false;
    fb9h1y=437-b9h1y;
    if(imageSelected){
      hboxImage[16]=draggedImage.get();
    db9h1=true;
    }
  }
  if(mouseX>=462 && mouseX<=662 && mouseY>=439 && mouseY<=512 && hb9){
    b9h2=false;
    fb9h2y=514-b9h2y;
    if(imageSelected){
      hboxImage[17]=draggedImage.get();
    db9h2=true;
    }
  }
  
  
  
  
  drawDragImage = false;
  drawBoxImage = false;
  imageSelected = false;
}

