app.controller('Term', function($scope, $rootScope) {
    'use strict';
    $scope.stepNames = [
        'Terms',
        'Privacy',
        'Affiliation'
    ];
    $scope.completedSteps = _.map($scope.stepNames, function () {
        return false;
    });
    $scope.completeStep = function (n) {
        $scope.completedSteps[n] = true;
    };
    $scope.isReadyToSave = function () {
        return _.every($scope.completedSteps, function(el){
            return el == true;
        })
    }
});