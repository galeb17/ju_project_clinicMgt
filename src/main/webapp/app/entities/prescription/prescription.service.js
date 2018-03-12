(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('Prescription', Prescription);

    Prescription.$inject = ['$resource'];

    function Prescription ($resource) {
        var resourceUrl =  'api/prescriptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
