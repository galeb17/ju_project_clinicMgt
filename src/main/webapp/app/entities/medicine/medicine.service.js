(function() {
    'use strict';
    angular
        .module('clinicApp')
        .factory('Medicine', Medicine);

    Medicine.$inject = ['$resource'];

    function Medicine ($resource) {
        var resourceUrl =  'api/medicines/:id';

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
