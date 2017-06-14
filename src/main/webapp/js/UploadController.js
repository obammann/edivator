'use strict';

var mainApp=angular.module('upload', []);

mainApp.controller('FileUploadController', function($scope, $http) {

    $scope.document = {};

    $scope.setTitle = function(fileInput) {

        var file=fileInput.value;
        var filename = file.replace(/^.*[\\\/]/, '');
        var title = filename.substr(0, filename.lastIndexOf('.'));
        $("#title").val(title);
        $("#title").focus();
        $scope.document.title=title;
    };

    $scope.uploadFile=function(){
        var formData=new FormData();
        formData.append("file",file.files[0]);
        $http({
            method: 'POST',
            url: '/newDocument',
            headers: { 'Content-Type': undefined},
            data:  formData
        })
            .success(function(data, status) {
                alert("Success ... " + status);
            })
            .error(function(data, status) {
                alert("Error ... " + status);
            });
    };
});
