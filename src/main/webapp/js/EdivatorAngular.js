//Angular Code
'use strict';

var EdivatorModul = angular.module('Edivator', []);

EdivatorModul.factory('Picture', function() {
    return {
        back: function() {PictureFactory_changeVersion(-1);},
        forward: function() {PictureFactory_changeVersion(1);},
        rotateLeft: function() {PictureFactory_rotate(-90);},
        rotateRight: function() {PictureFactory_rotate(90);},
        flipHorizontal: function() {PictureFactory_flip("h");},
        flipVertical: function() {PictureFactory_flip("v");},
        cropHeight: function() {PictureFactory_crop(10, 10, 0, 0);},
        cropWidth: function() {PictureFactory_crop(0, 0, 10, 10);},
        feelingLucky: function() {PictureFactory_Filter("FeelingLucky");},
        bigger: function() {PictureFactory_resize(10, 10);},
        smaler: function() {PictureFactory_resize(-10, -10);},
        resetPic: function() {PictureFactory_reset();},
        UploadDialog: function() {},
        ExportDialog: function() {PictureFactory_ExportDialog();},
        getPictureObject: function() {return picture_obj;}
    };
});

EdivatorModul.controller('PictureCtrl', function($scope, Picture, $http){
    $scope.picture = Picture;
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
                    var uploadUrl = "/image"
                    $http.post(uploadUrl, data, {
                        withCredentials: true,
                        headers: {'Content-Type': undefined },
                        transformRequest: angular.identity
                    }).then(function (response) {
                        console.log(response)
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
});