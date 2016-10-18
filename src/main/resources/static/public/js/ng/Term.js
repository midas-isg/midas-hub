app.controller('Term', function($scope, $rootScope) {
    'use strict';
    $scope.completedSteps = [false, false, false];
    $scope.completeStep = function (n) {
        $scope.completedSteps[n] = true;
    };
    $scope.isReadyToSave = function () {
        return _.every($scope.completedSteps, function(el){
            return el == true;
        })
    }
});