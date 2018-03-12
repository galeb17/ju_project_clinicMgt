(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('PrescriptionDetailController', PrescriptionDetailController);

    PrescriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prescription', 'Patient', 'Doctor', 'Medicine'];

    function PrescriptionDetailController($scope, $rootScope, $stateParams, previousState, entity, Prescription, Patient, Doctor, Medicine) {
        var vm = this;

        vm.prescription = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:prescriptionUpdate', function(event, result) {
            vm.prescription = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
