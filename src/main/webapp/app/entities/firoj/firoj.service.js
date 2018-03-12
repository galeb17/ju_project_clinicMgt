(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('Firoj', Firoj);

    Firoj.$inject = ['$resource'];

    function Firoj ($resource) {
        var resourceUrl =  'api/firojs/:id';

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
