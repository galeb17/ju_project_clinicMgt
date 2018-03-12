(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('PrescriptionDialogController', PrescriptionDialogController);

    PrescriptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prescription', 'Patient', 'Doctor', 'Medicine'];

    function PrescriptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prescription, Patient, Doctor, Medicine) {
        var vm = this;

        vm.prescription = entity;
        vm.clear = clear;
        vm.save = save;
        vm.patients = Patient.query();
        vm.doctors = Doctor.query();
        vm.medicines = Medicine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prescription.id !== null) {
                Prescription.update(vm.prescription, onSaveSuccess, onSaveError);
            } else {
                Prescription.save(vm.prescription, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:prescriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
