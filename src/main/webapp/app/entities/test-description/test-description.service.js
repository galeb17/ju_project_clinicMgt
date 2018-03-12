(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('TestDescription', TestDescription);

    TestDescription.$inject = ['$resource'];

    function TestDescription ($resource) {
        var resourceUrl =  'api/test-descriptions/:id';

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
