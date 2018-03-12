(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TransferIntimationDialogController', TransferIntimationDialogController);

    TransferIntimationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransferIntimation', 'Patient'];

    function TransferIntimationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransferIntimation, Patient) {
        var vm = this;

        vm.transferIntimation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.patients = Patient.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transferIntimation.id !== null) {
                TransferIntimation.update(vm.transferIntimation, onSaveSuccess, onSaveError);
            } else {
                TransferIntimation.save(vm.transferIntimation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:transferIntimationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
