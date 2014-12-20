void mode8mouseDragged(){  
  imageX = mouseX;
  imageY = mouseY;
  
  //box1  
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=204 && !(vb1||hb1)){
    box1=true;
    
  }else{
    box1=false;
  }
  //////////////vb1
  if(mouseX>=54 && mouseX<=152 && mouseY>=54 && mouseY<=204 && vb1){
    b1v1=true;
    b1v1x=mouseX;
  }else{b1v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=54 && mouseY<=204 && vb1){
    b1v2=true;
    b1v2x=mouseX;
  }else{b1v2=false;}
  //////////////hb1
  if(mouseX>=54 && mouseX<=254 && mouseY>=54 && mouseY<=127 && hb1){
    b1h1=true;
    b1h1y=mouseY;
  }else{b1h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=131 && mouseY<=204 && hb1){
    b1h2=true;
    b1h2y=mouseY;
  }else{b1h2=false;}
  //box2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=204 && !(vb2||hb2)){
    box2=true;
  }else{
    box2=false;
  }
  //vb2
  if(mouseX>=258 && mouseX<=356 && mouseY>=54 && mouseY<=204 && vb2){
    b2v1=true;
    b2v1x=mouseX;
  }else{b2v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=54 && mouseY<=204 && vb2){
    b2v2=true;
    b2v2x=mouseX;
  }else{b2v2=false;}
  //hb2
  if(mouseX>=258 && mouseX<=458 && mouseY>=54 && mouseY<=127 && hb2){
    b2h1=true;
    b2h1y=mouseY;
  }else{b2h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=131 && mouseY<=204 && hb2){
    b2h2=true;
    b2h2y=mouseY;
  }else{b2h2=false;}
  //box3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=204 && !(vb3||hb3)){
    box3=true;
  }else{
    box3=false;
  }
  //vb3
  if(mouseX>=462 && mouseX<=560 && mouseY>=54 && mouseY<=204 && vb3){
    b3v1=true;
    b3v1x=mouseX;
  }else{b3v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=54 && mouseY<=204 && vb3){
    b3v2=true;
    b3v2x=mouseX;
  }else{b3v2=false;}
  //hb3
  if(mouseX>=462 && mouseX<=662 && mouseY>=54 && mouseY<=127 && hb3){
    b3h1=true;
    b3h1y=mouseY;
  }else{b3h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=131 && mouseY<=204 && hb3){
    b3h2=true;
    b3h2y=mouseY;
  }else{b3h2=false;}
  //box4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=358 && !(vb4||hb4)){
    box4=true;
  }else{
    box4=false;
  }
  //vb4
  if(mouseX>=54 && mouseX<=152 && mouseY>=208 && mouseY<=358 && vb4){
    b4v1=true;
    b4v1x=mouseX;
  }else{b4v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=208 && mouseY<=358 && vb4){
    b4v2=true;
    b4v2x=mouseX;
  }else{b4v2=false;}
  //hb4
  if(mouseX>=54 && mouseX<=254 && mouseY>=208 && mouseY<=281 && hb4){
    b4h1=true;
    b4h1y=mouseY;
  }else{b4h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=285 && mouseY<=358 && hb4){
    b4h2=true;
    b4h2y=mouseY;
  }else{b4h2=false;}
  //box5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=358 && !(vb5||hb5)){
    box5=true;
  }else{
    box5=false;
  }
  //vb5
  if(mouseX>=258 && mouseX<=356 && mouseY>=208 && mouseY<=358 && vb5){
    b5v1=true;
    b5v1x=mouseX;
  }else{b5v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=208 && mouseY<=358 && vb5){
    b5v2=true;
    b5v2x=mouseX;
  }else{b5v2=false;}
  //hb5
  if(mouseX>=258 && mouseX<=458 && mouseY>=208 && mouseY<=281 && hb5){
    b5h1=true;
    b5h1y=mouseY;
  }else{b5h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=285 && mouseY<=358 && hb5){
    b5h2=true;
    b5h2y=mouseY;
  }else{b5h2=false;}
  //box6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=358 && !(vb6||hb6)){
    box6=true;
  }else{
    box6=false;
  }
  //vb6
  if(mouseX>=462 && mouseX<=560 && mouseY>=208 && mouseY<=358 && vb6){
    b6v1=true;
    b6v1x=mouseX;
  }else{b6v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=208 && mouseY<=358 && vb6){
    b6v2=true;
    b6v2x=mouseX;
  }else{b6v2=false;}
  //hb6
  if(mouseX>=462 && mouseX<=662 && mouseY>=208 && mouseY<=281 && hb6){
    b6h1=true;
    b6h1y=mouseY;
  }else{b6h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=285 && mouseY<=358 && hb6){
    b6h2=true;
    b6h2y=mouseY;
  }else{b6h2=false;}
  //box7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=512 && !(vb7||hb7)){
    box7=true;
  }else{
    box7=false;
  }
  //vb7
  if(mouseX>=54 && mouseX<=152 && mouseY>=362 && mouseY<=512 && vb7){
    b7v1=true;
    b7v1x=mouseX;
  }else{b7v1=false;}
  if(mouseX>=156 && mouseX<=254 && mouseY>=362 && mouseY<=512 && vb7){
    b7v2=true;
    b7v2x=mouseX;
  }else{b7v2=false;}
  //hb7
  if(mouseX>=54 && mouseX<=254 && mouseY>=362 && mouseY<=435 && hb7){
    b7h1=true;
    b7h1y=mouseY;
  }else{b7h1=false;}
  if(mouseX>=54 && mouseX<=254 && mouseY>=439 && mouseY<=512 && hb7){
    b7h2=true;
    b7h2y=mouseY;
  }else{b7h2=false;}
  //box8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=512 && !(vb8||hb8)){
    box8=true;
  }else{
    box8=false;
  }
  //vb8
  if(mouseX>=258 && mouseX<=356 && mouseY>=362 && mouseY<=512 && vb8){
    b8v1=true;
    b8v1x=mouseX;
  }else{b8v1=false;}
  if(mouseX>=360 && mouseX<=458 && mouseY>=362 && mouseY<=512 && vb8){
    b8v2=true;
    b8v2x=mouseX;
  }else{b8v2=false;}
  //hb8
  if(mouseX>=258 && mouseX<=458 && mouseY>=362 && mouseY<=435 && hb8){
    b8h1=true;
    b8h1y=mouseY;
  }else{b8h1=false;}
  if(mouseX>=258 && mouseX<=458 && mouseY>=439 && mouseY<=512 && hb8){
    b8h2=true;
    b8h2y=mouseY;
  }else{b8h2=false;}
  //box9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=512 && !(vb9||hb9)){
    box9=true;
  }else{
    box9=false;
  }
  //vb9
  if(mouseX>=462 && mouseX<=560 && mouseY>=362 && mouseY<=512 && vb9){
    b9v1=true;
    b9v1x=mouseX;
  }else{b9v1=false;}
  if(mouseX>=564 && mouseX<=662 && mouseY>=362 && mouseY<=512 && vb9){
    b9v2=true;
    b9v2x=mouseX;
  }else{b9v2=false;}
  //hb9
  if(mouseX>=462 && mouseX<=662 && mouseY>=362 && mouseY<=435 && hb9){
    b9h1=true;
    b9h1y=mouseY;
  }else{b9h1=false;}
  if(mouseX>=462 && mouseX<=662 && mouseY>=439 && mouseY<=512 && hb9){
    b9h2=true;
    b9h2y=mouseY;
  }else{b9h2=false;}
  ////////////////////////////////////////////////////////////
  if(mouseX<=666 && mouseX>=50 && mouseY<=516 && mouseY>=50){
    drawDragImage=false;
  } else {
    drawDragImage=true;
  }
  
}

