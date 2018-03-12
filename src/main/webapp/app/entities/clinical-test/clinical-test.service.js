(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('ClinicalTest', ClinicalTest);

    ClinicalTest.$inject = ['$resource'];

    function ClinicalTest ($resource) {
        var resourceUrl =  'api/clinical-tests/:id';

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
