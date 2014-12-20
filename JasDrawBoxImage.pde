void drawBoxImage(){
  //box1
  if(drawBox1Image && !(vb1||hb1)){
    imageMode(CENTER);
    image(boxImage[0], 154, 129);
  }
  if(db1v1 && vb1){
    imageMode(CENTER);
    image(vboxImage[0].get((fb1v1x),0,98,150), 103, 129);
  }
  if(db1v2 && vb1){
    imageMode(CENTER);
    image(vboxImage[1].get((fb1v2x),0,98,150), 205, 129);
  }
  if(db1h1 && hb1){
    imageMode(CENTER);
    image(hboxImage[0].get(0,fb1h1y,200,73), 154, 90);
  }
  if(db1h2 && hb1){
    imageMode(CENTER);
    image(hboxImage[1].get(0,fb1h2y,200,73), 154, 167);
  }
  
  //box2
  if(drawBox2Image && !(vb2||hb2)){
    imageMode(CENTER);
    image(boxImage[1], 358, 129);
  }
  if(db2v1 && vb2){
    imageMode(CENTER);
    image(vboxImage[2].get((fb2v1x),0,98,150), 307, 129);
  }
  if(db2v2 && vb2){
    imageMode(CENTER);
    image(vboxImage[3].get((fb2v2x),0,98,150), 409, 129);
  }
  if(db2h1 && hb2){
    imageMode(CENTER);
    image(hboxImage[2].get(0,fb2h1y,200,73), 358, 90);
  }
  if(db2h2 && hb2){
    imageMode(CENTER);
    image(hboxImage[3].get(0,fb2h2y,200,73), 358, 167);
  }
  
  //box3
  if(drawBox3Image && !(vb3||hb3)){
    imageMode(CENTER);
    image(boxImage[2], 562, 129);
  }
  if(db3v1 && vb3){
    imageMode(CENTER);
    image(vboxImage[4].get((fb3v1x),0,98,150), 511, 129);
  }
  if(db3v2 && vb3){
    imageMode(CENTER);
    image(vboxImage[5].get((fb3v2x),0,98,150), 613, 129);
  }
  if(db3h1 && hb3){
    imageMode(CENTER);
    image(hboxImage[4].get(0,fb3h1y,200,73), 562, 90);
  }
  if(db3h2 && hb3){
    imageMode(CENTER);
    image(hboxImage[5].get(0,fb3h2y,200,73), 562, 167);
  }
  
  
  
  //box4
  if(drawBox4Image && !(vb4||hb4)){
    imageMode(CENTER);
    image(boxImage[3], 154, 283);
  }
  if(db4v1 && vb4){
    imageMode(CENTER);
    image(vboxImage[6].get((fb4v1x),0,98,150), 103, 283);
  }
  if(db4v2 && vb4){
    imageMode(CENTER);
    image(vboxImage[7].get((fb4v2x),0,98,150), 205, 283);
  }
  if(db4h1 && hb4){
    imageMode(CENTER);
    image(hboxImage[6].get(0,fb4h1y,200,73), 154, 244);
  }
  if(db4h2 && hb4){
    imageMode(CENTER);
    image(hboxImage[7].get(0,fb4h2y,200,73), 154, 321);
  }
  
  
  //box5
  if(drawBox5Image && !(vb5||hb5)){
    imageMode(CENTER);
    image(boxImage[4], 358, 283);
  }
  if(db5v1 && vb5){
    imageMode(CENTER);
    image(vboxImage[8].get((fb5v1x),0,98,150), 307, 283);
  }
  if(db5v2 && vb5){
    imageMode(CENTER);
    image(vboxImage[9].get((fb5v2x),0,98,150), 409, 283);
  }
  if(db5h1 && hb5){
    imageMode(CENTER);
    image(hboxImage[8].get(0,fb5h1y,200,73), 358, 244);
  }
  if(db5h2 && hb5){
    imageMode(CENTER);
    image(hboxImage[9].get(0,fb5h2y,200,73), 358, 321);
  }
  
  
  
  
  //box6
  if(drawBox6Image && !(vb6||hb6)){
    imageMode(CENTER);
    image(boxImage[5], 562, 283);
  }
  if(db6v1 && vb6){
    imageMode(CENTER);
    image(vboxImage[10].get((fb6v1x),0,98,150), 511, 283);
  }
  if(db6v2 && vb6){
    imageMode(CENTER);
    image(vboxImage[11].get((fb6v2x),0,98,150), 613, 283);
  }
  if(db6h1 && hb6){
    imageMode(CENTER);
    image(hboxImage[10].get(0,fb6h1y,200,73), 562, 244);
  }
  if(db6h2 && hb6){
    imageMode(CENTER);
    image(hboxImage[11].get(0,fb6h2y,200,73), 562, 321);
  }
  
  
  
  
  //box7
  if(drawBox7Image && !(vb7||hb7)){
    imageMode(CENTER);
    image(boxImage[6], 154, 437);
  }
  if(db7v1 && vb7){
    imageMode(CENTER);
    image(vboxImage[12].get((fb7v1x),0,98,150), 103, 437);
  }
  if(db7v2 && vb7){
    imageMode(CENTER);
    image(vboxImage[13].get((fb7v2x),0,98,150), 205, 437);
  }
  if(db7h1 && hb7){
    imageMode(CENTER);
    image(hboxImage[12].get(0,fb7h1y,200,73), 154, 398);
  }
  if(db7h2 & hb7){
    imageMode(CENTER);
    image(hboxImage[13].get(0,fb7h2y,200,73), 154, 475);
  }
  
  
  
  //box8
  if(drawBox8Image && !(vb8||hb8)){
    imageMode(CENTER);
    image(boxImage[7], 358, 437);
  }
  if(db8v1 && vb8){
    imageMode(CENTER);
    image(vboxImage[14].get((fb8v1x),0,98,150), 307, 437);
  }
  if(db8v2 && vb8){
    imageMode(CENTER);
    image(vboxImage[15].get((fb8v2x),0,98,150), 409, 437);
  }
  if(db8h1 && hb8){
    imageMode(CENTER);
    image(hboxImage[14].get(0,fb8h1y,200,73), 358, 398);
  }
  if(db8h2 && hb8){
    imageMode(CENTER);
    image(hboxImage[15].get(0,fb8h2y,200,73), 358, 475);
  }
  
  
  
  //box9
  if(drawBox9Image && !(vb9||hb9)){
    imageMode(CENTER);
    image(boxImage[8], 562, 437);
  }
  if(db9v1 && vb9){
    imageMode(CENTER);
    image(vboxImage[16].get((fb9v1x),0,98,150), 511, 437);
  }
  if(db9v2 && vb9){
    imageMode(CENTER);
    image(vboxImage[17].get((fb9v2x),0,98,150), 613, 437);
  }
  if(db9h1 && hb9){
    imageMode(CENTER);
    image(hboxImage[16].get(0,fb9h1y,200,73), 562, 398);
  }
  if(db9h2 && hb9){
    imageMode(CENTER);
    image(hboxImage[17].get(0,fb9h2y,200,73), 562, 475);
  }
  
  
}

