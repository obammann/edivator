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
		bigger: function() {PictureFactory_resize(10, 10)},
		smaler: function() {PictureFactory_resize(-10, -10)},
		resetPic: function() {PictureFactory_reset()},
		getPictureObject: function() {return picture_obj;}
    };
});

EdivatorModul.controller('PictureCtrl', function($scope, Picture, $http){
    $scope.picture = Picture;

    $scope.uploadState = "No Image loaded.";
    this.uploadPicture = function () {

    	// https://stackoverflow.com/questions/18571001/file-upload-using-angularjs
    	$scope.uploadState = "Loading...";

        var f = document.getElementById('file').files[0],
            r = new FileReader();

        r.onloadend = function(e) {
            var data = e.target.result;


			//send your binary data via $http or $resource or do anything else with it
			// https://stackoverflow.com/questions/25152700/angularjs-put-binary-data-from-arraybuffer-to-the-server
			// Url fehlr
			// $http({
			// 	method: 'PUT',
			// 	headers: {'Content-Type': 'undefined'},
			// 	data: new Uint8Array(r.result),
			// 	transformRequest: []
			// });


			// Other Solution:
			// https://stackoverflow.com/questions/13963022/angularjs-how-to-implement-a-simple-file-upload-with-multipart-form
            var uploadUrl = "http://localhost:8080/image"
			$http.post(uploadUrl, data, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            }).success(
                $scope.uploadState = "Uploaded Picture succesfully."
			).error(
                $scope.uploadState = "Uploaded Picture failed."
			);
        }

        r.readAsArrayBuffer(f);
		// $scope.uploadState = "Image is uploaded.";
    }


});