(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FinalBillDetailController', FinalBillDetailController);

    FinalBillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FinalBill', 'Patient'];

    function FinalBillDetailController($scope, $rootScope, $stateParams, previousState, entity, FinalBill, Patient) {
        var vm = this;

        vm.finalBill = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:finalBillUpdate', function(event, result) {
            vm.finalBill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
