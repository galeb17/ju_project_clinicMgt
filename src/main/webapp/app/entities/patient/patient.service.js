(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('Patient', Patient);

    Patient.$inject = ['$resource', 'DateUtils'];

    function Patient ($resource, DateUtils) {
        var resourceUrl =  'api/patients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.discharge_Date = DateUtils.convertDateTimeFromServer(data.discharge_Date);
                        data.admission_Date = DateUtils.convertDateTimeFromServer(data.admission_Date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
