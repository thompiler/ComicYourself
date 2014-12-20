PImage[] photos;
PImage[] gallery;
PImage draggedImage;
PImage[] boxImage;
PImage[] hboxImage;
PImage[] vboxImage;
int selectedPicture;
int imageX, imageY;
boolean drawDragImage;
boolean drawBoxImage;
boolean vb1, vb2, vb3, vb4, vb5, vb6, vb7, vb8, vb9; 
boolean hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9; 
boolean box1, box2, box3, box4, box5, box6, box7, box8, box9;
//half horizontal boxes //half vertical boxes
boolean b1h1, b1h2, b1v1, b1v2, 
        b2h1, b2h2, b2v1, b2v2, 
        b3h1, b3h2, b3v1, b3v2, 
        b4h1, b4h2, b4v1, b4v2, 
        b5h1, b5h2, b5v1, b5v2, 
        b6h1, b6h2, b6v1, b6v2, 
        b7h1, b7h2, b7v1, b7v2, 
        b8h1, b8h2, b8v1, b8v2, 
        b9h1, b9h2, b9v1, b9v2;
        
int b1h1y, b1h2y, b1v1x, b1v2x, 
    b2h1y, b2h2y, b2v1x, b2v2x, 
    b3h1y, b3h2y, b3v1x, b3v2x, 
    b4h1y, b4h2y, b4v1x, b4v2x, 
    b5h1y, b5h2y, b5v1x, b5v2x, 
    b6h1y, b6h2y, b6v1x, b6v2x, 
    b7h1y, b7h2y, b7v1x, b7v2x, 
    b8h1y, b8h2y, b8v1x, b8v2x, 
    b9h1y, b9h2y, b9v1x, b9v2x;
    
int fb1h1y, fb1h2y, fb1v1x, fb1v2x, 
    fb2h1y, fb2h2y, fb2v1x, fb2v2x, 
    fb3h1y, fb3h2y, fb3v1x, fb3v2x, 
    fb4h1y, fb4h2y, fb4v1x, fb4v2x, 
    fb5h1y, fb5h2y, fb5v1x, fb5v2x, 
    fb6h1y, fb6h2y, fb6v1x, fb6v2x, 
    fb7h1y, fb7h2y, fb7v1x, fb7v2x, 
    fb8h1y, fb8h2y, fb8v1x, fb8v2x, 
    fb9h1y, fb9h2y, fb9v1x, fb9v2x;
        
boolean drawBox1Image, drawBox2Image, drawBox3Image,
        drawBox4Image, drawBox5Image, drawBox6Image, 
        drawBox7Image, drawBox8Image, drawBox9Image;
        
boolean db1h1, db1h2, db1v1, db1v2, 
        db2h1, db2h2, db2v1, db2v2, 
        db3h1, db3h2, db3v1, db3v2, 
        db4h1, db4h2, db4v1, db4v2, 
        db5h1, db5h2, db5v1, db5v2, 
        db6h1, db6h2, db6v1, db6v2, 
        db7h1, db7h2, db7v1, db7v2, 
        db8h1, db8h2, db8v1, db8v2, 
        db9h1, db9h2, db9v1, db9v2;
        
boolean imageSelected;

boolean b2l,b3l,b4l,b5l,b6l,b7l,b8l,b9l;


void Jassetup()
{
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  background(200);
  strokeCap(SQUARE);

  vb1=false; vb2=false; vb3=false;
  vb4=false; vb5=false; vb6=false;
  vb7=false; vb8=false; vb9=false;
  
  hb1=false; hb2=false; hb3=false;
  hb4=false; hb5=false; hb6=false;
  hb7=false; hb8=false; hb9=false;

  drawDragImage=false;
  imageX=0;
  imageY=0;
  
  boxImage = new PImage[9];
  hboxImage = new PImage[18];
  vboxImage = new PImage[18];
  
/*  
  String path = sketchPath+"/data/"; //folder of images rename as needed
  File[] files = listFiles(path);
  photos = new PImage[files.length];
  for(int i=0; i<files.length; i++){
    photos[i]=loadImage(files[i].getAbsolutePath());
  }
  */
}

void mode8dispayButon(){
  if(displayButtons)
  {
    cp5 = new ControlP5(this);

    cp5.setControlFont(buttonFont);
  
  cp5.addButton("mode2phase1back")
      .setPosition(10, 677)
      .setCaptionLabel("<")
      .align(CENTER,CENTER,CENTER,CENTER)
      .setSize(40, 40)
      .setColor(backButtonColor)
      ;
      
      displayButtons = false;
  }

}
/*
File[] listFiles(String directory){
  File file = new File(directory);
  if(file.isDirectory()){
    File[] files = file.listFiles();
    return files;
  } else {
    return null;
  }
}

void drawImageGallery(){
  //loading and resizing gallery
  PImage[] gallery = new PImage[photos.length];
  for(int i=0; i<photos.length; i++){
    gallery[i] = photos[i].get();
    if(gallery[i].height > 50)
      gallery[i].resize(0,50);
    if(gallery[i].width > 50)
      gallery[i].resize(50,0);
  }
  
  
  //gallery display on
  int i = 0;
  for(int col = 0; col < 6; col++){
    for(int row = 0; row < 2; row++){      
      imageMode(CORNER);
      image(gallery[i], (750+(row*75)), (50+(col*75)));
      i++;
    }
  }
  
}
*/



void mode8draw()
{  
  background(200);
  drawBoxImage();
  comicPage();
  //drawImageGallery();
  drawImageDragging();
  
}

void comicPage(){
  
  
  //topline
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,52,258,52);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,52,462,52);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,52,666,52);
  
/////////////////////////////////////////////////////////////////// 
//1
///////////////////////////////////////////////////////////////////
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,50,52,208);             //|                                        
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,50,256,208);                     //|                              
  //first row vertical
  //left third line                                    //|                    
//  if(b2l){
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,50,460,208);                                //|                    
//  } else {
  
//  }
  //first row vertical
  //left fourth line                                             //|          
//  if(b3l){
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,50,664,208);                                          //|        
//  } else {
  
//  }
  
  //first row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,206,258,206);
  //center line                               2_________3
//  if(b2l){
  strokeWeight(4);
  stroke(255);
  line(254,206,462,206);
//  } else {
  
//  }
  //right line                                          3_________4
//  if(b3l){
  strokeWeight(4);
  stroke(255);
  line(458,206,666,206);
//  } else {
//  }
/////////////////////////////////////////////////////////////////// 
//2
///////////////////////////////////////////////////////////////////
  //second row vertical           //|
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,204,52,362);            //|                                        
  
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,204,256,362);                    //|                              
  //first row vertical
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,204,460,362);                               //|                    
  //first row vertical
  //left fourth line                                             //|          
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,204,664,362);                                         //|        
  
  //second row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,360,258,360);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,360,462,360);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,360,666,360);
  
/////////////////////////////////////////////////////////////////// 
//3
///////////////////////////////////////////////////////////////////
  //third row vertical            //|
  strokeWeight(4);                //|                                        
  stroke(255);                    //|                                        
  line(52,358,52,516);            //|                                        
  //first row vertical
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  stroke(255);                              //|                              
  line(256,358,256,516);                    //|                              
  //first row vertical
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  stroke(255);                                         //|                    
  line(460,358,460,516);                               //|                    
  //first row vertical
  //left fourth line                                             //|          
  strokeWeight(4);                                               //|        
  stroke(255);                                                   //|        
  line(664,358,664,516);                                         //|        
  
  //third row horizontal
  //left line                       1_________2
  strokeWeight(4);
  stroke(255);
  line(50,514,258,514);
  //center line                               2_________3
  strokeWeight(4);
  stroke(255);
  line(254,514,462,514);
  //right line                                          3_________4
  strokeWeight(4);
  stroke(255);
  line(458,514,666,514);
  
/////////////////////////////////////////////////////////////////// 
//1
///////////////////////////////////////////////////////////////////  
  //vertical half row1 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb1){
    stroke(255);
  } else {  
    stroke(210);                 //|                                        
  }
  line(154,50,154,208);           //|                                          
  
  //vertical half row1 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb2){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,50,358,208);                     //|                                
  
  //vertical half row1 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb3){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,50,562,208);                                //|                    
  
  //horizontal half row1 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb1){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(50,129,258,129);
  //horizontal half row1 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb2){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(254,129,462,129);
  //horizontal half row1 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb3){
    stroke(255);
  } else { 
    stroke(210);
  }
  line(458,129,666,129);

/////////////////////////////////////////////////////////////////// 
//2
///////////////////////////////////////////////////////////////////  
  //vertical half row2 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb4){
    stroke(255);
  } else {
    stroke(210);                 //|                                        
  }
  line(154,204,154,362);          //|                                          
  
  //vertical half row2 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb5){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,204,358,362);                    //|                                
  
  //vertical half row2 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb6){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,204,562,362);                               //|                    
  
  //horizontal half row2 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb4){
    stroke(255);
  } else {
    stroke(210);
  }
  line(50,283,258,283);
  //horizontal half row2 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb5){
    stroke(255);
  } else {
    stroke(210);
  }
  line(254,283,462,283);
  //horizontal half row2 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb6){
    stroke(255);
  } else {
    stroke(210);
  }
  line(458,283,666,283);

/////////////////////////////////////////////////////////////////// 
//3
///////////////////////////////////////////////////////////////////
  //vertical half row3 box1
  //left first line               //|                                        
  strokeWeight(4);                //|                                        
  if(vb7){
    stroke(255);
  } else {
    stroke(210);                 //|                                        
  }
  line(154,358,154,516);          //|                                          
  
  //vertical half row3 box2
  //left second line                        //|                              
  strokeWeight(4);                          //|                               
  if(vb8){
    stroke(255);
  } else {
    stroke(210);                           //|                              
  }
  line(358,358,358,516);                    //|                                
  
  //vertical half row3 box3
  //left third line                                    //|                    
  strokeWeight(4);                                     //|                    
  if(vb9){
    stroke(255);
  } else {
    stroke(210);                                      //|                    
  }
  line(562,358,562,516);                               //|                    
  
  //horizontal half row3 box1
  //left line                       1_________2
  strokeWeight(4);
  if(hb7){
    stroke(255);
  } else {
    stroke(210);
  }
  line(50,437,258,437);
  //horizontal half row3 box2
  //center line                               2_________3
  strokeWeight(4);
  if(hb8){
    stroke(255);
  } else {
    stroke(210);
  }
  line(254,437,462,437);
  //horizontal half row3 box3
  //right line                                          3_________4
  strokeWeight(4);
  if(hb9){
    stroke(255);
  } else {
    stroke(210);
  }
  line(458,437,666,437);  
}







void drawImageDragging(){
  if(imageSelected){
  if(drawDragImage){
    imageMode(CENTER);
    image(draggedImage, imageX, imageY);
  }
  
  if(drawBoxImage){
  if(box1){
    imageMode(CENTER);
    image(draggedImage, 154, 129);
  }
  if(b1v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b1v1x),0,98,150), 103, 129);
  }
  if(b1v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b1v2x),0,98,150), 205, 129);
  }
  if(b1h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b1h1y,200,73), 154, 90);
  }
  if(b1h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b1h2y,200,73), 154, 167);
  }
  
  if(box2){
    imageMode(CENTER);
    image(draggedImage, 358, 129);
  }
  if(b2v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b2v1x),0,98,150), 307, 129);
  }
  if(b2v2){
    imageMode(CENTER);
    image(draggedImage.get((460-b2v2x),0,98,150), 409, 129);
  }
  if(b2h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b2h1y,200,73), 358, 90);
  }
  if(b2h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b2h2y,200,73), 358, 167);
  }
  
  if(box3){
    imageMode(CENTER);
    image(draggedImage, 562, 129);
  }
  if(b3v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b3v1x),0,98,150), 511, 129);
  }
  if(b3v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b3v2x),0,98,150), 613, 129);
  }
  if(b3h1){
    imageMode(CENTER);
    image(draggedImage.get(0,129-b3h1y,200,73), 562, 90);
  }
  if(b3h2){
    imageMode(CENTER);
    image(draggedImage.get(0,206-b3h2y,200,73), 562, 167);
  }
  
  
  
  if(box4){
    imageMode(CENTER);
    image(draggedImage, 154, 283);
  }
  if(b4v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b4v1x),0,98,150), 103, 283);
  }
  if(b4v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b4v2x),0,98,150), 205, 283);
  }
  if(b4h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b4h1y,200,73), 154, 244);
  }
  if(b4h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b4h2y,200,73), 154, 321);
  }
  
  
  
  if(box5){
    imageMode(CENTER);
    image(draggedImage, 358, 283);
  }
  if(b5v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b5v1x),0,98,150), 307, 283);
  }
  if(b5v2){
    imageMode(CENTER);
    image(draggedImage.get((458-b5v2x),0,98,150), 409, 283);
  }
  if(b5h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b5h1y,200,73), 358, 244);
  }
  if(b5h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b5h2y,200,73), 358, 321);
  }
  
  
  
  if(box6){
    imageMode(CENTER);
    image(draggedImage, 562, 283);
  }
  if(b6v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b6v1x),0,98,150), 511, 283);
  }
  if(b6v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b6v2x),0,98,150), 613, 283);
  }
  if(b6h1){
    imageMode(CENTER);
    image(draggedImage.get(0,283-b6h1y,200,73), 562, 244);
  }
  if(b6h2){
    imageMode(CENTER);
    image(draggedImage.get(0,360-b6h2y,200,73), 562, 321);
  }
  
  
  if(box7){
    imageMode(CENTER);
    image(draggedImage, 154, 437);
  }
  if(b7v1){
    imageMode(CENTER);
    image(draggedImage.get((154-b7v1x),0,98,150), 103, 437);
  }
  if(b7v2){
    imageMode(CENTER);
    image(draggedImage.get((256-b7v2x),0,98,150), 205, 437);
  }
  if(b7h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b7h1y,200,73), 154, 398);
  }
  if(b7h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b7h2y,200,73), 154, 475);
  }
  
  
  
  if(box8){
    imageMode(CENTER);
    image(draggedImage, 358, 437);
  }
  if(b8v1){
    imageMode(CENTER);
    image(draggedImage.get((358-b8v1x),0,98,150), 307, 437);
  }
  if(b8v2){
    imageMode(CENTER);
    image(draggedImage.get((460-b8v2x),0,98,150), 409, 437);
  }
  if(b8h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b8h1y,200,73), 358, 398);
  }
  if(b8h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b8h2y,200,73), 358, 475);
  }
  
  
  if(box9){
    imageMode(CENTER);
    image(draggedImage, 562, 437);
  }
  if(b9v1){
    imageMode(CENTER);
    image(draggedImage.get((562-b9v1x),0,98,150), 511, 437);
  }
  if(b9v2){
    imageMode(CENTER);
    image(draggedImage.get((664-b9v2x),0,98,150), 613, 437);
  }
  if(b9h1){
    imageMode(CENTER);
    image(draggedImage.get(0,437-b9h1y,200,73), 562, 398);
  }
  if(b9h2){
    imageMode(CENTER);
    image(draggedImage.get(0,514-b9h2y,200,73), 562, 475);
  }
    
  }
  }
}



