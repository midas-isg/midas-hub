app.controller('Report', function($scope, $rootScope, DTOptionsBuilder) {
    'use strict';
    $scope.users = users;
    $scope.usersGroupByAffiliation = _.groupBy($scope.users, function (user) {
        return user && user.app_metadata && user.app_metadata.affiliation && user.app_metadata.affiliation.name;
    });

    $scope.logs = logs;
    $scope.loginsGroupByAffiliation = groupBy('userAffiliation', 's'); // s = Success Login
    $scope.registersGroupByAffiliation = groupBy('userAffiliation', 'sapi'); // sapi = API Operation

    $scope.loginsGroupByApplication = groupBy('applicationName', 's'); // s = Success Login

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

    function groupBy(fieldName, code) {
        var filteredLogs = _.filter(logs, function (log) {
            return log.eventCode === code;
        });
        return _.groupBy(filteredLogs, function (log) {
            return log[fieldName];
        });
    }
});