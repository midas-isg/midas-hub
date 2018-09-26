app.controller('Report', function($scope, $rootScope, DTOptionsBuilder) {
    'use strict';
    $scope.users = users;
    $scope.nilText = 'N/A';
    $scope.usersGroupByAffiliation = _.groupBy($scope.users, function (user) {
        var aff = user && user.app_metadata && user.app_metadata.affiliation && user.app_metadata.affiliation.name;
        return aff || $scope.nilText;
    });
    $scope.logs = logs;
    $scope.loginsGroupByAffiliation = groupBy('userAffiliation', ['s', 'ssa']); // s = Success Login; ssa = Success Silent Auth
    $scope.registersGroupByAffiliation = groupBy('userAffiliation', ['sapi']); // sapi = API Operation
    $scope.loginsGroupByApplication = groupBy('applicationName', ['s', 'ssa']); // s = Success Login

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('bLengthChange', false);
    $scope.dtOptionsWithButtons = DTOptionsBuilder.newOptions()
        .withOption('bLengthChange', false)
        .withButtons([
            // 'columnsToggle',
            // 'colvis',
            // 'copy',
            'print',
            'pdf',
            'excel'
        ]);

    function groupBy(fieldName, codes) {
        var filteredLogs = _.filter(logs, function (log) {
            return _.contains(codes, log.eventCode);
        });
        return _.groupBy(filteredLogs, function (log) {
            var nullText = $scope.nilText;
            var value = log[fieldName] || nullText;
            return value === 'null' ? nullText : value;
        });
    }
});