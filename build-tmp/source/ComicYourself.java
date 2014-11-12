import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import java.awt.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ComicYourself extends PApplet {


// name:    Thomas Cassady
// ID:      910477066
// class:   CSC 690
// project: Final
// date:    11/13/2014
// file:	ComicYourself.pde







PImage [] Photos;
PImage [] Panels;


public void setup()
{
	size(1080, 720);
	
}


public void draw()
{
	background(255);	
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ComicYourself" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
