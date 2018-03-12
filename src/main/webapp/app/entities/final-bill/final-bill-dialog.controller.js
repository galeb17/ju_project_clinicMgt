(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FinalBillDialogController', FinalBillDialogController);

    FinalBillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FinalBill', 'Patient'];

    function FinalBillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FinalBill, Patient) {
        var vm = this;

        vm.finalBill = entity;
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
            if (vm.finalBill.id !== null) {
                FinalBill.update(vm.finalBill, onSaveSuccess, onSaveError);
            } else {
                FinalBill.save(vm.finalBill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:finalBillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.Date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
