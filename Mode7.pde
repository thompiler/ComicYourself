// file: Mode7.pde
// description: Mode 7 allows the user to search flickr for a photo and add that photo to their project


//==========================================================================================================================
public void mode7phase1display()
{
	background(#012E4B);
	mode7phase1displayButtons();
	text("Search flickr for a photo to add.", 20, 40);
	text("Use '+' to separate tags.", 20, 80);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode7phase1displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    int left = (width-800)/2;

	    cp5.addButton("mode7phase1back")
			.setPosition(left, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		cp5.addTextfield("flickrSearchQuery")
			.setCaptionLabel("Enter Query")
			.setPosition((width-290)/2, (height-40)/2)
			.setSize(200, 40)
			.setFont(smallFont)
			.setFocus(true)
			.setColor(color(255,0,0))
			;

		cp5.addButton("flickrSearchButton")
			.setPosition((width+200)/2, (height-40)/2)
			.setCaptionLabel("Search")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
	      	;

	    displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void flickrSearchButton()
{
	println("button: flickr search button");
	phase = 2;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = cp5.get(Textfield.class,"flickrSearchQuery").getText();
	getFlickrData();
}


//__________________________________________________________________________________________________________________________
public void mode7phase1back()
{
	println("button: back to add photo");
	mode = 2;
	phase = 1;
	cp5.hide();
	displayButtons = true;
}


//==========================================================================================================================
public void mode7phase2display()
{
	background(#012E4B);
	mode7phase2displayButtons();
	text("Click on a photo to add.", 20, 40);
	text("Showing results for \""+flickrSearchQuery+"\"", 20, 80);

	int x=0, y=0, size = flickrPhotoList.size();
	float flickrW, flickrH, compW = 640, compH = 480, compAspectRatio = compW/compH;

	for (int i = 0; i < size; i++)
	{
		flickrW = (float)flickrPhotoList.get(i).width;
		flickrH = (float)flickrPhotoList.get(i).height;
		float aspectRatio = flickrW/flickrH;

		if(aspectRatio >= compAspectRatio)
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 200, 200 * flickrH/flickrW);
		else
			image(flickrPhotoList.get(i), 20 + x*210, 100+y*160, 150 * flickrW/flickrH, 150);

		x++;
		if (x >= 5)
		{
		  	x = 0;
		  	y++;
		}
	}
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode7phase2displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase2back")
			.setPosition(width-60, 20)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase2back()
{
	phase = 1;
	cp5.hide();
	displayButtons = true;
	flickrSearchQuery = "";
	flickrPhotoList = new ArrayList <PImage> ();
}


//__________________________________________________________________________________________________________________________
public void getFlickrData() 
{
	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
	String request = api + "&per_page=25&format=json&nojsoncallback=1&extras=geo";
	request += "&api_key=" + "b4b2de9fa436d4629313b6463f254d69"; 
	String tags = flickrSearchQuery;
	request += "&tags=" + tags;

	//String userId = "88935360@N05"; // 690 test
	//request += "&user_id=" + userId;
	//request += "&min_upload_date=2012-07-01";
	//request += "&bbox=-125,32,-120,40";
	//request += "&lat=38";
	//request += "&lon=-122";
	//request += "&tags=bill%20burroughs";

	println("Sent request: " + request);
	json = loadJSONObject(request);
	println("Received from flickr: " + json);

	JSONObject photos = json.getJSONObject("photos");
	JSONArray photo = photos.getJSONArray("photo");
	println("Found " + photo.size() + " photos");

	for (int i=0; i<photo.size (); i++) 
	{
		JSONObject pic = photo.getJSONObject(i);
		// get parameters to construct url
		int farm = pic.getInt("farm");
		String server = pic.getString("server");
		String id = pic.getString("id");
		String secret = pic.getString("secret");
		String url = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
		String title = pic.getString("title");
		println("Photo " + i + " " + title + " " + url);
		PImage img = loadImage(url);

		if(img.width/img.height >= 640/480)
			img.resize(640, 640 * img.height/img.width);
		else
			img.resize(480 * img.width/img.height, 480);

		flickrPhotoList.add(img);
	}
}


//__________________________________________________________________________________________________________________________
public void mode7mousePressed()
{
	if(phase == 2)
	{
		int x = 0, y = 0, 
			photoY, photoX, photoW, photoH, 
			size = flickrPhotoList.size();

		for(int i = 0; i < size; i++)
		{

			float compW = 640, 
				compH = 480, 
				compAspectRatio = compW/compH,
				flickrW = (float)flickrPhotoList.get(i).width,
				flickrH = (float)flickrPhotoList.get(i).height,
				aspectRatio = flickrW/flickrH
				;

			if(aspectRatio >= compAspectRatio)
			{
				photoW = 200;
				photoH = (int)(200 * flickrH/flickrW);
			}
			else
			{
				photoW = (int)(150 * flickrW/flickrH);
				photoH = 150;
			}


			photoX = 20 + x*210;
			photoY = 100+y*160;

			if(mouseY < photoY + photoH && mouseY > photoY 
				&& mouseX < photoX + photoW && mouseX > photoX)
			{
				flickrPhotoIndex = i;
				phase = 3;
				cp5.hide();
				displayButtons = true;
				break;
			}

			x++;
			if (x >= 5)
			{
			  	x = 0;
			  	y++;
			}
		}
	}
}


//==========================================================================================================================
public void mode7phase3display()
{
	background(#012E4B);
	mode7phase3displayButtons();
	image(flickrPhotoList.get(flickrPhotoIndex), (width - 800)/2, 70);
}


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void mode7phase3displayButtons()
{
	if(displayButtons)
	{
	    cp5 = new ControlP5(this);

	    cp5.setControlFont(buttonFont);

	    cp5.addButton("mode7phase3back")
			.setPosition((width-800)/2, 677)
			.setCaptionLabel("<")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(40, 40)
			;

	    cp5.addButton("mode7phase3save")
			.setPosition(width/2 - 40, 677)
			.setCaptionLabel("Save")
			.align(CENTER,CENTER,CENTER,CENTER)
			.setSize(80, 40)
			;

		displayButtons = false;
  	}
}


//__________________________________________________________________________________________________________________________
public void mode7phase3back()
{
	phase = 2;
	cp5.hide();
	displayButtons = true;
}


//__________________________________________________________________________________________________________________________
public void mode7phase3save()
{
	mode = 1;
	phase = 1;
	flickrSearchQuery = "";
	cp5.hide();
	displayButtons = true;
	Photos[numPhotos] = flickrPhotoList.get(flickrPhotoIndex);
	numPhotos++;
	flickrPhotoList = new ArrayList <PImage> ();
}