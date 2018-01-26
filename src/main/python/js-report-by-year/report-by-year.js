$scope = init();

(function () {
    //console.log(JSON.stringify($scope.loginsGroupByApplication))
    //printJson('loginsGroupByAffiliation');
    printJson('usersGroupByAffiliation');

    // printLoginLogs($scope.loginsGroupByApplication);
    // printLoginLogs($scope.loginsGroupByAffiliation);
    // printUserLogs($scope.usersGroupByAffiliation);

    function printJson(key) {
        console.log(JSON.stringify($scope[key]))
        console.log(key + '.json')
    }

    function printLoginLogs(groupedLogs) {
        var keys = _.keys(groupedLogs);
        var key2year2numLogs = {};

        for (var i = 0; i < keys.length; i++){
            var key = keys[i];
            key2year2numLogs[key] = _.countBy(groupedLogs[key], toLoginYear)
        }
        console.log(key2year2numLogs);
        // years = _.range(2015, 2019);
    }

    function printUserLogs(groupedLogs) {
        var keys = _.keys(groupedLogs);
        var key2year2numLogs = {};

        for (var i = 0; i < keys.length; i++){
            var key = keys[i];
            key2year2numLogs[key] = _.countBy(groupedLogs[key], toUserYear)
        }
        console.log(key2year2numLogs);
        // years = _.range(2015, 2019);
    }

    function toUserYear(log) {
        return log.created_at.substring(0, 4);
    }

    function toLoginYear(log) {
        return log.timestamp.substring(0, 4);
    }
})();
