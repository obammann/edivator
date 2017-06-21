//Globale Variablen
var frontendObj = {             //Picture Objekt für das Frontend
    width: 100, 				//Prozentualer Wert in relation zur Orginalgröße, 10 bis 1000
    height: 100, 				//Prozentualer Wert in relation zur Orginalgröße, 10 bis 1000
    rotation: 0,				//Grad kann negativ oder positiv sein
    flippedHorizontal: false,	//Bool Wert: false = nicht gespiegelt, true= gespiegelt
    flippedVertical: false,		//Bool Wert: false = nicht gespiegelt, true= gespiegelt
    flippedString: "",
    offsetLeftRight: 0,			//Prozentangabe, wieviel Prozent vom Bild in der Breite abgeschnitten wird
    offsetTopBottom: 0,			//Prozentangabe, wieviel Prozent vom Bild in der Höhe abgeschnitten wird
    feelingLucky: 0		        //Zähler, welcher angibt, wie häufig der Feeling Lucky Filter verwendet wurde
};

Buttons = {                     //Objekt welches alle Buttons mit ID und Zustand (disabled oder nicht disabled) enthält
    reset:{
        id:"btn_reset",
        disabled:true
    },
    undo:{
        id:"btn_undo",
        disabled:true
    },
    repeat:{
        id:"btn_repeat",
        disabled:true
    },
    import:{
        id:"btn_import",
        disabled:false
    },
    export:{
        id:"btn_export",
        disabled:false
    },
    sizePlus:{
        id:"btn_sizePlus",
        disabled:false
    },
    sizeMinus:{
        id:"btn_sizeMinus",
        disabled:false
    },
    rotateLeft:{
        id:"btn_rotateLeft",
        disabled:false
    },
    rotateRight:{
        id:"btn_rotateRight",
        disabled:false
    },
    flipHorizontal:{
        id:"btn_flipHorizontal",
        disabled:false
    },
    flipVertical:{
        id:"btn_flipVertical",
        disabled:false
    },
    cropWidth:{
        id:"btn_cropWidth",
        disabled:false
    },
    cropHeight:{
        id:"btn_cropHeight",
        disabled:false
    },
    filterLucky:{
        id:"btn_filterLucky",
        disabled:false
    }
};

//Initialfunktion
$( document ).ready(function() {
    BtnDiasbaleAll();                       //Alle Buttons ausblenden
    BtnChangeState(Buttons.import, false);  //Import Button einblenden
});

//Zustandsänderung des Bildes im FrontendObj speichern
function FrontendObj_rotate(angle){
    frontendObj.rotation = frontendObj.rotation + angle;
	if(frontendObj.rotation == 360 || frontendObj.rotation == -360){
        frontendObj.rotation = 0;
    }
}

function FrontendObj_flip(oriantation){
    switch (oriantation) {
        case ("h")://horizontal
            frontendObj.flippedHorizontal = !frontendObj.flippedHorizontal;
            break;
        case ("v")://vertical
            frontendObj.flippedVertical = !frontendObj.flippedVertical;
            break;
        default:
            alert("Error");
    }
}

function FrontendObj_crop(leftRight, topBottom){
    var maxCrop = 90; //Prozent
    var offsetLeftRight = frontendObj.offsetLeftRight + leftRight;
    var offsetTopBottom = frontendObj.offsetTopBottom + topBottom;

    switch (true) {
        case (offsetLeftRight > maxCrop):
            BtnChangeState(Buttons.cropWidth, true);
            break;
        case (offsetTopBottom > maxCrop):
            BtnChangeState(Buttons.cropHeight, true);
            break;
        default:
            frontendObj.offsetLeftRight = offsetLeftRight;
            frontendObj.offsetTopBottom = offsetTopBottom;
    }
}

function FrontendObj_Filter(name){
    switch (name) {
        case ("FeelingLucky"):
            frontendObj.feelingLucky = frontendObj.feelingLucky + 1;
            break;
        default:
            alert("Error");
    }
}

function FrontendObj_resize(value){
    var minSize = 10;
    var maxSize = 1000;

    var newSize = frontendObj.width + value;

    switch (true) {
        case (newSize == minSize):
            BtnChangeState(Buttons.sizeMinus, true);
            break;
        case (newSize == maxSize):
            BtnChangeState(Buttons.sizePlus, true);
            break;
        default:
            BtnChangeState(Buttons.sizeMinus, false);
            BtnChangeState(Buttons.sizePlus, false);
    }
    frontendObj.width = newSize;
    frontendObj.height = newSize;
}

//Export-Dialog erzeugen
function ExportDialog(){
    var exportButton = {
        label: 'export',
        className: "Export-Upload",
        callback: function(){DownloadImage();}
    };
    var cancelButton = {
        label: 'Cancel',
        className: "Button-Cancel",
        callback: function() {dialog.modal('hide');}
    };
    var options = {
        message: ''+
        '<div>'+
            'Name: <br>'+
            '<input id="ChoosenFilename" type="text" value="Output">'+
        '</div>'+
        '<br>'+
        '<div>'+
            'Type: <br>'+
            '<select id="ChoosenTyp" class="selectpicker show-tick" data-style="btn-primary">'+
                '<option value="jpg">JPG</option>'+
                '<option value="png">PNG</option>'+
                '<option value="bmp">BMP</option>'+
            '</select>'+
        '</div>'+
        '<br>'+
        '<div>'+
            'Target: <br>'+
            '<select class="selectpicker show-tick" data-style="btn-primary">'+
                '<option>Download</option>'+
                '<option disabled>E-Mail</option>'+
                '<option disabled>Cloud-Storage</option>'+
            '</select>'+
        '</div>',
        title: "Export",
        callback: function(){},
        onEscape: false,
        backdrop: true,
        closeButton: false,
        animate: true,
        size: "small",
        buttons: {exportButton, cancelButton}
    };

    var dialog = bootbox.dialog(options);
    $('.selectpicker').selectpicker();
}

//Ladeanzeige einblenden
function ShowLoading(){
    BtnDiasbaleAll();
    var image = document.getElementById("CurrentImage");
    var imageHeight = "";
    if(image !== null){
        imageHeight = image.naturalHeight;
    }

    var html=''+
        '<div class="loading" style="height:'+imageHeight+'">'+
        'Loading...<br>'+
        '<i class="fa fa-spinner fa-spin" aria-hidden="true"></i>'+
        '</div>';

    var ImageSection = document.getElementById("PictureArea");
    ImageSection.innerHTML = html;
}

//Bild herunterladen
function DownloadImage(){
    var url, name, typ, DropbdownType;
    url = document.getElementById("CurrentImage").src;
    name = document.getElementById("ChoosenFilename").value;
    DropbdownType = document.getElementById("ChoosenTyp");
    typ = DropbdownType.options[DropbdownType.selectedIndex].value;

    var xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';
    xhr.onload = function() {
        var a = document.createElement('a');
        a.href = window.URL.createObjectURL(xhr.response); // xhr.response is a blob
        a.download = name+"."+typ; // Set the file name.
        a.style.display = 'none';
        document.body.appendChild(a);
        a.click();
        delete a;
    };
    xhr.open('GET', url);
    xhr.send();
}

//Alle Buttons disablen
function BtnDiasbaleAll(){
    $('button').prop('disabled', true);
}

//Status eines Buttons ändern (disbaled oder nicht disabled
function BtnChangeState(obj, state){
    obj.disabled = state;
    $('#' + obj.id).prop('disabled', obj.disabled);
}

//Alle Buttons auf den Zustand setzen, welche im Buttons obj hinterlegt ist
function BtnSetObjState(){
    for (var Botton in Buttons) {
        if (Buttons.hasOwnProperty(Botton)) {
            var obj = Buttons[Botton];
            $('#'+obj.id).prop('disabled', obj.disabled);
        }
    }
}