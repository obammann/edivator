//Globale Variablen
var picture_obj = {
    "width": 100, 				//Prozentualer Wert in relation zur Orginalgröße, 10 bis 1000
    height: 100, 				//Prozentualer Wert in relation zur Orginalgröße, 10 bis 1000
    rotation: 0,				//Grad kann negativ oder positiv sein
    flippedHorizontal: false,	//Bool Wert: false = nicht gespiegelt, true= gespiegelt
    flippedVertical: false,		//Bool Wert: false = nicht gespiegelt, true= gespiegelt
    offsetLeft: 0,				//Prozentangabe, die Links vom Bild abgeschnitten wird 0-90
    offsetRight: 0,				//Prozentangabe, die Rechts vom Bild abgeschnitten wird 0-90
    offsetTop: 0,				//Prozentangabe, die Oben vom Bild abgeschnitten wird 0-90
    offsetBottom: 0,			//Prozentangabe, die Unten vom Bild abgeschnitten wird 0-90
    feelingLucky: false,		//Bool Wert: false = feeling Lucky Filter nicht aktiv, true = feeling Lucky Filter aktiv
    format: "png",				//png, jpg oder webm
    target: "preview"			//preview = Vorschaubild, email = per E-Mail senden, download = Bild herunterladen
};

var versions = new Array;		//Array der alle alten Versionen des Picture Objektes enthält
versions.push(clone(picture_obj));
var currentVersion = 0;			//Zählervariable mit welcher alte Versionen des Picture Objectes im versions Array referenziert werden können


//Funktionen die direkt von den Services aufgerufen werden-------------------------------------------------------------------------------
function PictureFactory_changeVersion(count){
	var newVersion = currentVersion + count;
	var minVersion = 0;
	var maxVersion = versions.length - 1;
	
	switch (true) {
		case (newVersion < minVersion):
			alert("Error");
			break;
		case (newVersion > maxVersion):
			alert("Error");
			break;
		default:
			currentVersion = newVersion;
			picture_obj = versions[currentVersion];
	}
}

function PictureFactory_rotate(angle){
	picture_obj.rotation = picture_obj.rotation + angle;
	/*if(picture_obj.rotation == 360 || picture_obj.rotation == -360){
		picture_obj.rotation = 0;
	}*/
	newVersion();
}

function PictureFactory_flip(oriantation){
	switch (oriantation) {
		case ("h"):
			picture_obj.flippedHorizontal = !picture_obj.flippedHorizontal;
			newVersion();
			break;
		case ("v"):
			picture_obj.flippedVertical = !picture_obj.flippedVertical;
			newVersion();
			break;
		default:
			alert("Error");
	}
}

function PictureFactory_crop(left, right, top_, bottom){
	var maxCrop = 90; //Prozent
	var offsetLeft = picture_obj.offsetLeft + left
	var offsetRight = picture_obj.offsetRight + right
	var offsetTop = picture_obj.offsetTop + top_
	var offsetBottom = picture_obj.offsetBottom + bottom
	
	switch (true) {
		case (offsetLeft + offsetRight > maxCrop):
			alert("Error");
			break;
		case (offsetTop + offsetBottom > maxCrop):
			alert("Error");
			break;
		default:
			picture_obj.offsetLeft = offsetLeft;
			picture_obj.offsetRight = offsetRight;
			picture_obj.offsetTop = offsetTop;
			picture_obj.offsetBottom = offsetBottom;
			newVersion();
	}
}

function PictureFactory_Filter(name){
	switch (name) {
		case ("FeelingLucky"):
			picture_obj.feelingLucky = !picture_obj.feelingLucky;
			newVersion();
			break;
		default:
			alert("Error");
	}
}

function PictureFactory_resize(width, height){
	var minSize = 10;
	var maxSize = 1000;
	
	var newWidth = picture_obj.width + width;
	var newHeight = picture_obj.height + height;
	
	switch (true) {
		case (newWidth < minSize):
			alert("Error");
			break;
		case (newWidth > maxSize):
			alert("Error");
			break;
		case (newHeight < minSize):
			alert("Error");
			break;
		case (newHeight > maxSize):
			alert("Error");
			break;
		default:
			picture_obj.width = newWidth;
			picture_obj.height = newHeight;
			newVersion();
	}	
}

function PictureFactory_reset(){
	picture_obj = clone(versions[0]);
	newVersion();
}

//Andere Funtkionen---------------------------------------------------------------
function newVersion(){
	currentVersion = versions.length - 1;	
	versions.push(clone(picture_obj));	
}

function clone(obj){
	var clone = {
		width: obj.width, 				
		height: obj.height, 				
		rotation: obj.rotation,				
		flippedHorizontal: obj.flippedHorizontal,	
		flippedVertical: obj.flippedVertical,		
		offsetLeft: obj.offsetLeft,				
		offsetRight: obj.offsetRight,				
		offsetTop: obj.offsetTop,				
		offsetBottom: obj.offsetBottom,			
		feelingLucky: obj.feelingLucky,		
		format: obj.format,				
		target: obj.target			
	};
	return clone;
}