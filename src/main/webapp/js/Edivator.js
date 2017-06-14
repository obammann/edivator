//Globale Variablen
var picture_obj = {
    width: 100, 				//Prozentualer Wert in relation zur Orginalgröße, 10 bis 1000
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
var picture;					//Enthält das Bild


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

function PictureFactory_UploadDialog($http){
    var img;
    var file;

    var uploadButton = {
        label: 'Upload',
        className: "Button-Upload",
        callback: function() {
            var reader = new FileReader();

            reader.onloadend = function(e) {
                var data = e.target.result;

                // https://stackoverflow.com/questions/13963022/angularjs-how-to-implement-a-simple-file-upload-with-multipart-form
                var uploadUrl = "/image"
                $http.post(uploadUrl, data, {
                    withCredentials: true,
                    headers: {'Content-Type': undefined },
                    transformRequest: angular.identity
                }).then(function (response) {
                    console.log('successfull uploaded')
                }).catch(function (data) {
                    console.log(data)
                });
            }

            reader.readAsArrayBuffer(file);


            picture = img;
            picture.style.width = "auto";
            dialog.modal('hide');
            document.getElementById('PictureArea').innerHTML = "";
            document.getElementById('PictureArea').appendChild(picture);
        }
    };
    var cancelButton = {
        label: 'Cancel',
        className: "Button-Cancel",
        callback: function() {dialog.modal('hide');}
    };
    var options = {
        message: ''+
        '<div class="container-fluid">'+
        '<div class="row">'+
        '<div class="col-sm-7">'+
        '<div>'+
        'Select an image file from Disk: '+
        '<input type="file" id="fileInput">'+
        '</div>'+
        '<br>'+
        '<div>'+
        'Select an image file from url: '+
        '<input type="text" id="fileInputUrl">'+
        '</div>'+
        '</div>'+
        '<div class="col-sm-5">'+
        '<div id="fileDisplayArea"></div>'+
        '</div>'+
        '</div>'+
        '</div>',
        title: "Upload",
        callback: function(){},
        onEscape: false,
        backdrop: true,
        closeButton: true,
        animate: true,
        size: "medium",
        buttons: {uploadButton, cancelButton}
    };
    var dialog = bootbox.dialog(options);

    var fileInput = document.getElementById('fileInput');
    var fileDisplayArea = document.getElementById('fileDisplayArea');

    fileInput.addEventListener('change', function(e) {

        file = fileInput.files[0];
        var imageType = /image.*/;

        if (file.type.match(imageType)) {
            var reader = new FileReader();

            reader.onload = function(e) {
                fileDisplayArea.innerHTML = "Preview:";

                // Create a new image.
                img = new Image();
                // Set the img src property using the data URL.
                img.src = reader.result;
                img.style.width = "100%";

                // Add the image to the page.
                fileDisplayArea.appendChild(img);
            }

            reader.readAsDataURL(file);
        } else {
            fileDisplayArea.innerHTML = "File not supported!";
        }
    });
}

function PictureFactory_ExportDialog(){
    var exportButton = {
        label: 'export',
        className: "Export-Upload",
        callback: function() {}
    };
    var cancelButton = {
        label: 'Cancel',
        className: "Button-Cancel",
        callback: function() {dialog.modal('hide');}
    };
    var options = {
        message: ''+
        '<div>'+
        'Type: <br>'+
        '<select class="selectpicker show-tick" data-style="btn-primary">'+
        '<option>PNG</option>'+
        '<option>JPG</option>'+
        '<option>WEBM</option>'+
        '</select>'+
        '</div>'+
        '<br>'+
        '<div>'+
        'Target: <br>'+
        '<select class="selectpicker show-tick" data-style="btn-primary">'+
        '<option>Download</option>'+
        '<option>E-Mail</option>'+
        '<option>Cloud-Storage</option>'+
        '</select>'+
        '</div>',
        title: "Export",
        callback: function(){},
        onEscape: false,
        backdrop: true,
        closeButton: true,
        animate: true,
        size: "small",
        buttons: {exportButton, cancelButton}
    };

    var dialog = bootbox.dialog(options);
    $('.selectpicker').selectpicker();
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

//Wird momentan nicht benutzt
function getDataUri(url, callback) {
    var image = new Image();

    image.onload = function () {
        var canvas = document.createElement('canvas');
        canvas.width = this.naturalWidth; // or 'width' if you want a special/scaled size
        canvas.height = this.naturalHeight; // or 'height' if you want a special/scaled size

        canvas.getContext('2d').drawImage(this, 0, 0);

        // Get raw image data
        callback(canvas.toDataURL('image/png').replace(/^data:image\/(png|jpg);base64,/, ''));

        // ... or get as Data URI
        callback(canvas.toDataURL('image/png'));
    };


    image.src = url;
}

function ShowLoading(){
    var html=''+
        '<div class="loading">'+
        'Loading...<br>'+
        '<i class="fa fa-spinner fa-spin" aria-hidden="true"></i>'+
        '</div>';

    var ImageSection = document.getElementById("PictureArea");
    ImageSection.innerHTML = html;
}

function ShowPicture(url){
    var html='<img src="'+url+'">';

    var ImageSection = document.getElementById("PictureArea");
    ImageSection.innerHTML = html;
}