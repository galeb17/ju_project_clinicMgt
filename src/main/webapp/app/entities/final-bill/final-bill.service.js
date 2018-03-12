(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('FinalBill', FinalBill);

    FinalBill.$inject = ['$resource', 'DateUtils'];

    function FinalBill ($resource, DateUtils) {
        var resourceUrl =  'api/final-bills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.Date = DateUtils.convertDateTimeFromServer(data.Date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
