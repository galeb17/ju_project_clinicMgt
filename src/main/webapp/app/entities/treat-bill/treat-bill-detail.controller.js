(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TreatBillDetailController', TreatBillDetailController);

    TreatBillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TreatBill', 'Patient'];

    function TreatBillDetailController($scope, $rootScope, $stateParams, previousState, entity, TreatBill, Patient) {
        var vm = this;

        vm.treatBill = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:treatBillUpdate', function(event, result) {
            vm.treatBill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
