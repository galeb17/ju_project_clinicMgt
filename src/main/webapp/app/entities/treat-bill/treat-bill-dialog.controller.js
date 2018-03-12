(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TreatBillDialogController', TreatBillDialogController);

    TreatBillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TreatBill', 'Patient'];

    function TreatBillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TreatBill, Patient) {
        var vm = this;

        vm.treatBill = entity;
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
            if (vm.treatBill.id !== null) {
                TreatBill.update(vm.treatBill, onSaveSuccess, onSaveError);
            } else {
                TreatBill.save(vm.treatBill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:treatBillUpdate', result);
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
