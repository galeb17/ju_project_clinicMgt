(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FirojDetailController', FirojDetailController);

    FirojDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Firoj'];

    function FirojDetailController($scope, $rootScope, $stateParams, previousState, entity, Firoj) {
        var vm = this;

        vm.firoj = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:firojUpdate', function(event, result) {
            vm.firoj = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
