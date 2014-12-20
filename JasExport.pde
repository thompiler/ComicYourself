public void mode8export()
{
  println("Export button pressed ");
    int wide = 0;
    int heigh = 0;
    // export panels as one combined pimage
    // 1. create white pimage with dimensions to fit all panels
    if(drawBox1Image || db1v1 || db1v2 || db1h1 || db1h2){
      wide = 672;
      heigh = 512;
    }
    
    if(drawBox2Image || db2v1 || db2v2 || db2h1 || db2h2){
      wide = 1328;
      heigh = 512;
    }
    
    if(drawBox3Image || db3v1 || db3v2 || db3h1 || db3h2){
      wide = 1984;
      heigh = 512;
    }
    
    if(drawBox4Image || db4v1 || db4v2 || db4h1 || db4h2){
      wide = 672;
      heigh = 1008;
    }
    
    if(drawBox5Image || db5v1 || db5v2 || db5h1 || db5h2){
      wide = 1328;
      heigh = 1008;
    }
    
    if(drawBox6Image || db6v1 || db6v2 || db6h1 || db6h2){
      wide = 1984;
      heigh = 1008;
    }
    
    if(drawBox7Image || db7v1 || db7v2 || db7h1 || db7h2){
      wide = 672;
      heigh = 1504;
    }
    
    if(drawBox8Image || db8v1 || db8v2 || db8h1 || db8h2){
      wide = 1328;
      heigh = 1504;
    }
    
    if(drawBox9Image || db9v1 || db9v2 || db9h1 || db9h2){
      wide = 1984;
      heigh = 1504;
    }
    
    
    
    int border = 16;
    
    PImage comicStrip = createImage(wide, heigh, RGB);
    
    comicStrip.loadPixels();
    
    for (int y = 0; y < heigh; y++){
      for (int x = 0; x < wide; x++){
        int loc = x + y * wide;
        comicStrip.pixels[loc] = color(255, 255, 255);
      }
    }
    // 2. loop through panels and write them to output pimage
    if(drawBox1Image || db1v1 || db1v2 || db1h1 || db1h2){
      if(drawBox1Image && !(vb1||hb1)){
        comicStrip.copy(boxImage[0], 0, 0, 640, 480, 16, 16, 640, 480);
      }
      if(db1v1 && vb1){
        comicStrip.copy(vboxImage[0], 0, 0, 312, 480, 16, 16, 312, 480);
      }
      if(db1v2 && vb1){
        comicStrip.copy(vboxImage[1], 0, 0, 312, 480, 336, 16, 312, 480);
      }
      if(db1h1 && hb1){
        comicStrip.copy(hboxImage[0], 0, 0, 480, 232, 16, 16, 640, 232);
      }
      if(db1h2 && hb1){
        comicStrip.copy(hboxImage[1], 0, 0, 480, 232, 264, 16, 640, 232);
      }
    }
    
    if(drawBox2Image || db2v1 || db2v2 || db2h1 || db2h2){
      if(drawBox2Image && !(vb2||hb2)){
        comicStrip.copy(boxImage[1], 0, 0, 640, 480, 672, 16, 640, 480);
      }
      if(db1v1 && vb1){
        comicStrip.copy(vboxImage[2], 0, 0, 312, 480, 672, 16, 312, 480);
      }
      if(db1v2 && vb1){
        comicStrip.copy(vboxImage[3], 0, 0, 312, 480, 1000, 16, 312, 480);
      }
      if(db1h1 && hb1){
        comicStrip.copy(hboxImage[2], 0, 0, 480, 232, 672, 16, 640, 232);
      }
      if(db1h2 && hb1){
        comicStrip.copy(hboxImage[3], 0, 0, 480, 232, 672, 264, 640, 232);
      }
      
    }
    
    if(drawBox3Image || db3v1 || db3v2 || db3h1 || db3h2){
      comicStrip.copy(boxImage[2], 0, 0, 640, 480, 1328, 16, 640, 480);
    }
    
    if(drawBox4Image || db4v1 || db4v2 || db4h1 || db4h2){
      comicStrip.copy(boxImage[3], 0, 0, 640, 480, 16, 512, 640, 480);
    }
    
    if(drawBox5Image || db5v1 || db5v2 || db5h1 || db5h2){
      comicStrip.copy(boxImage[4], 0, 0, 640, 480, 672, 512, 640, 480);
    }
    
    if(drawBox6Image || db6v1 || db6v2 || db6h1 || db6h2){
      comicStrip.copy(boxImage[5], 0, 0, 640, 480, 1328, 512, 640, 480);
    }
    
    if(drawBox7Image || db7v1 || db7v2 || db7h1 || db7h2){
      comicStrip.copy(boxImage[6], 0, 0, 640, 480, 16, 1008, 640, 480);
    }
    
    if(drawBox8Image || db8v1 || db8v2 || db8h1 || db8h2){
      comicStrip.copy(boxImage[7], 0, 0, 640, 480, 672, 1008, 640, 480);
    }
    
    if(drawBox9Image || db9v1 || db9v2 || db9h1 || db9h2){
      comicStrip.copy(boxImage[8], 0, 0, 640, 480, 1328, 1008, 640, 480);
    }
    


    comicStrip.updatePixels();
    println("Comic Strip Exported to File");
    comicStrip.save("comicStrip.png");
  }

