(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TransferIntimationDetailController', TransferIntimationDetailController);

    TransferIntimationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransferIntimation', 'Patient'];

    function TransferIntimationDetailController($scope, $rootScope, $stateParams, previousState, entity, TransferIntimation, Patient) {
        var vm = this;

        vm.transferIntimation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:transferIntimationUpdate', function(event, result) {
            vm.transferIntimation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
