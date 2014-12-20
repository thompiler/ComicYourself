void mode8mousePressed(){
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=50 && mouseY<=100){
    draggedImage = photos[0].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=50 && mouseY<=100){
    draggedImage = photos[1].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=125 && mouseY<=175){
    draggedImage = photos[2].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=125 && mouseY<=175){
    draggedImage = photos[3].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=200 && mouseY<=250){
    draggedImage = photos[4].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=200 && mouseY<=250){
    draggedImage = photos[5].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=750 && mouseX<=800 && mouseY>=275 && mouseY<=325){
    draggedImage = photos[6].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  
  if(mouseX>=825 && mouseX<=875 && mouseY>=275 && mouseY<=325){
    draggedImage = photos[7].get();
    imageX = mouseX;
    imageY = mouseY;
    drawDragImage=true;
    drawBoxImage=true;
    imageSelected=true;
  }
  if(drawDragImage){
    if(draggedImage.height > 150)
      draggedImage.resize(0,150);
    if(draggedImage.width > 200)
      draggedImage.resize(50,0);
  }
  
  
}

