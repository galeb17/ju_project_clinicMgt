(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('AppointSchedule', AppointSchedule);

    AppointSchedule.$inject = ['$resource', 'DateUtils'];

    function AppointSchedule ($resource, DateUtils) {
        var resourceUrl =  'api/appoint-schedules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.scheduleDate = DateUtils.convertDateTimeFromServer(data.scheduleDate);
                        data.scheduleTime = DateUtils.convertDateTimeFromServer(data.scheduleTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
