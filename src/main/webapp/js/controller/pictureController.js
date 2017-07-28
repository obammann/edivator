//Angular Code
'use strict';

var EdivatorModul = angular.module('Edivator', []);

EdivatorModul.factory('Picture', function() {
    return {
        ExportDialog: function () {
            exportDialog();
        },
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
    $scope.imgId = null;
    $scope.imgIdDiv = document.getElementById("imgIdDiv");

    $scope.imageWidth = 0;
    $scope.imageHeight = 0;

    $scope.openUploadDialog = function () {
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
                        $scope.setNewId(response.data.id, response.data.url);
                        $scope.imgIdDiv.setAttribute("value",response.data.id);
                        console.log($scope.imgId);
                        $scope.showPicture(response.data.url);
                        btnSetObjState();
                    }).catch(function (data) {
                        console.log(data)
                    });
                };

                reader.readAsArrayBuffer(file);

                dialog.modal('hide');
                showLoading();
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
                };

                reader.readAsDataURL(file);
            } else {
                fileDisplayArea.innerHTML = "File not supported!";
            }
        });
    };

    $scope.rotateLeft = function() {
        console.log("rotated Left");
        console.log($scope.imgId);
        var url = "/image/" + $scope.imgId.myId + "/rotate?left=true";
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_rotate(-90);
    };

    $scope.rotateRight = function() {
        console.log("rotated Right");
        var url = "/image/" + $scope.imgId.myId + "/rotate?left=false";
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_rotate(90);
    };

    $scope.flipHorizontal = function() {
        console.log("horizontal flipped");
        var url = "/image/" + $scope.imgId.myId + "/flip?horizontal=true";
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_flip("h");
    };

    $scope.flipVertical = function() {
        console.log("verticly flipped");
        var url = "/image/" + $scope.imgId.myId + "/flip?horizontal=false";
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_flip("v");
    };

    $scope.cropHeight = function() {
        console.log("height cropped");
        var imgWidth = document.getElementById("CurrentImage").naturalWidth;
        var imgHeight= document.getElementById("CurrentImage").naturalHeight;
        var url = "/image/" + $scope.imgId.myId + "/crop?leftBorder=" + 0 + "&rightBorder=" + 1 + "&topBorder=" + 0.1 + "&bottomBorder=" + 0.9;
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_crop(0, 10);
    };

    $scope.cropWidth = function() {
        console.log("width cropped");
        var url = "/image/" + $scope.imgId.myId + "/crop?leftBorder=" + 0.1 + "&rightBorder=" + 0.9 + "&topBorder=" + 0 + "&bottomBorder=" + 1;
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_crop(10, 0);
        $scope.setImageMeasures();
    };

    $scope.feelingLucky = function() {
        console.log("luckyFilterApplied");
        var url = "/image/" + $scope.imgId.myId + "/filter/feelinglucky";
        showLoading();
        this.callRouteAndActualize(url);
        frontendObj_Filter("FeelingLucky");
    };

    $scope.undo = function() {
        console.log("Undo clicked.");
        $scope.setToLastId();
        $scope.showPicture($scope.imgId.myURL);
    };

    $scope.redo = function () {
        console.log("Redo clicked");
        $scope.setToNextId();
        $scope.showPicture($scope.imgId.myURL);
    };

    $scope.changeSize = function () {
        console.log("imagesize is changed");
        var newWidth = $scope.inputSizeWidth;
        var newHeight = $scope.inputSizeHeight;
        if (!newWidth) {
            var scale = $scope.imageWidth / $scope.imageHeight;
            newWidth = scale * newHeight;
        }
        if (!newHeight) {
            var scale = $scope.imageWidth / $scope.imageHeight;
            newHeight = newWidth / scale;
        }
        var url = "/image/" + $scope.imgId.myId + "/resize?wishedWidth=" + newWidth + "&wishedHeight=" + newHeight;
        showLoading();
        this.callRouteAndActualize(url);
    };

    $scope.callRouteAndActualize = function(url) {
        $http.put(url, {
            withCredentials: true,
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function (response) {
            console.log(response);
            $scope.setNewId(response.data.id, response.data.url);
            $scope.showPicture(response.data.url);
        }).catch(function (data) {
            console.log(data)
        });
    };

    $scope.setImageMeasures = function () {
        $scope.imageWidth = document.getElementById("CurrentImage").naturalWidth;
        $scope.imageHeight = document.getElementById("CurrentImage").naturalHeight;
    };

    $scope.showPicture = function (url) {
        btnSetObjState();
        // document.getElementById("CurrentImage").src = url;
        var html = '<img id= "CurrentImage" src="' + url + '">';
        var ImageSection = document.getElementById("PictureArea");
        ImageSection.innerHTML = html;
        $scope.currentImage = document.querySelector("#CurrentImage");
        $scope.currentImage.onload = function () {
            $scope.$apply(function () {
                $scope.setImageMeasures();
            });
        };
        $scope.setImageMeasuresCss();
    };

    $scope.setImageMeasuresCss = function () {
        //Window height - navigation bar height
        var imageHeightCss = $(window).height() - 250;
        document.getElementById("CurrentImage").style.maxHeight = imageHeightCss + "px";
    };

    $scope.refreshUndoRedoButtonStatus = function () {
        if($scope.imgId != null) {
            btnChangeState(Buttons.undo, $scope.imgId.last == null);
            btnChangeState(Buttons.repeat, $scope.imgId.next == null);
        } else {
            btnChangeState(Buttons.undo, false);
            btnChangeState(Buttons.repeat, false);
        }
    };

    $scope.setNewId = function (id, url) {
      var newNode={
          myId:id,
          myURL:url,
          last:$scope.imgId,
          next:null
      };
      if($scope.imgId == null) {
          $scope.imgId = newNode;
      } else {
          $scope.imgId.next = newNode;
          $scope.setToNextId();
      }
      $scope.refreshUndoRedoButtonStatus();
    };

    $scope.setToNextId = function () {
        if($scope.imgId.next != null) {
            $scope.imgId = $scope.imgId.next;
        }
        $scope.refreshUndoRedoButtonStatus();
    };

    $scope.setToLastId = function () {
        if($scope.imgId.last != null) {
            $scope.imgId = $scope.imgId.last;
        }
        $scope.refreshUndoRedoButtonStatus();
    };
});