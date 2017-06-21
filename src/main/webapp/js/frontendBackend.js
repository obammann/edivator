//Angular Code
'use strict';

var EdivatorModul = angular.module('Edivator', []);

EdivatorModul.factory('Picture', function() {
    return {
        ExportDialog: function() {ExportDialog();},
        getPictureObject: function() {
            switch(true){
                case (frontendObj.flippedHorizontal == true && frontendObj.flippedVertical == false):
                    frontendObj.flippedString = "horizontal";
                    break;
                case (frontendObj.flippedHorizontal == false && frontendObj.flippedVertical == true):
                    frontendObj.flippedString = "vertical";
                    break;
                case (frontendObj.flippedHorizontal == true && frontendObj.flippedVertical == true):
                    frontendObj.flippedString = "horizontal + vertical";
                    break;
                default:
                    frontendObj.flippedString = "";
            }
            return frontendObj;
        }
    };
});

EdivatorModul.controller('PictureCtrl', function($scope, Picture, $http){
    $scope.picture = Picture;
    $scope.imgId = 0;
    this.OpenUploadDialog = function () {
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
                    var uploadUrl = "/imageUpload"
                    $http.post(uploadUrl, data, {
                        withCredentials: true,
                        headers: {'Content-Type': undefined },
                        transformRequest: angular.identity
                    }).then(function (response) {
                        console.log(response);
                        console.log('successfull uploaded');
                        $scope.imgId = response.data.id;
                        console.log($scope.imgId);
                        ShowPicture(response.data.url);
                        BtnSetObjState();
                    }).catch(function (data) {
                        console.log(data)
                    });
                };

                reader.readAsArrayBuffer(file);

                dialog.modal('hide');
                ShowLoading();
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
            closeButton: false,
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
                    $scope.imgActualWidth = img.width;
                    $scope.imgActualHeight = img.height;
                    // Add the image to the page.
                    fileDisplayArea.appendChild(img);
                }

                reader.readAsDataURL(file);
            } else {
                fileDisplayArea.innerHTML = "File not supported!";
            }
        });
    };

    $scope.rotateLeft = function() {
        console.log("rotated Left");
        console.log($scope.imgId);
        var url = "/image/" + $scope.imgId + "/rotate?left=true";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_rotate(-90);
    };

    $scope.rotateRight = function() {
        console.log("rotated Right");
        var url = "/image/" + $scope.imgId + "/rotate?left=false";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_rotate(90);
    };

    $scope.flipHorizontal = function() {
        console.log("horizontal flipped");
        var url = "/image/" + $scope.imgId + "/flip?horizontal=true";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_flip("h");
    }

    $scope.flipVertical = function() {
        console.log("verticly flipped");
        var url = "/image/" + $scope.imgId + "/flip?horizontal=false";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_flip("v");
    };

    $scope.cropHeight = function() {
        console.log("height cropped");
        var crop = 10;
        //TODO --> crop anpassen
        var url = "/image/" + $scope.imgId + "/crop?cropHeight=true&crop=" + crop;
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_crop(0, 10);
    };

    $scope.cropWidth = function() {
        console.log("width cropped");
        var crop = 10;
        //TODO --> Crop anpassen
        var url = "/image/" + $scope.imgId + "/crop?cropHeight=false&crop=" + crop;
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_crop(10, 0);
    };

    $scope.feelingLucky = function() {
        console.log("luckyFilterApplied");
        var url = "/image/" + $scope.imgId + "/filter/feelinglucky";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_Filter("FeelingLucky");
    };

    $scope.bigger = function () {
        console.log("img gets bigger");
        var url = "/image/" + $scope.imgId + "/resize?percentage=10";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_resize(10);
    };

    $scope.smaler = function () {
        console.log("image gets smaller");
        var url = "/image/" + $scope.imgId + "/resize?wishedWidth";
        ShowLoading();
        this.callRouteAndActualize(url);
        FrontendObj_resize(-10);
    };

    $scope.changeSize = function () {
        console.log("imagesize is changed");
        var newWidth = 0;
        var newHeight = 0;
        //TODO --> neue größen anpassen und smaller und bigger löschen
        var url = "/image/" + $scope.imgId + "/resize?wishedWidth=" + newWidth + "&wishedHeight=" +newHeight;
        ShowLoading();
        this.callRouteAndActualize(url);
    }

    $scope.callRouteAndActualize = function(url) {
        $http.put(url, {
            withCredentials: true,
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function (response) {
            console.log(response);
            $scope.imgId = response.data.id;
            ShowPicture(response.data.url);
        }).catch(function (data) {
            console.log(data)
        });
    }

});



function ShowPicture(url){
    BtnSetObjState();
    var html='<img id= "CurrentImage" src="'+url+'">';

    var ImageSection = document.getElementById("PictureArea");
    ImageSection.innerHTML = html;
}
