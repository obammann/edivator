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

EdivatorModul.controller('PictureCtrl', function($scope, Picture){
    $scope.picture = Picture;
});