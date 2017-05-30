angular.module('demo', [])
    .controller('testController', function($scope, $http) {


        $http.get('/greeting').
        then(function(response) {
            $scope.helloworld = response.data;
            console.log($scope.helloworld);
            // $scope.helloworld = "Test Hello World";
        });

        // $scope.start = function() {
        //     $scope.helloworld = "Init";
        // }

    });
