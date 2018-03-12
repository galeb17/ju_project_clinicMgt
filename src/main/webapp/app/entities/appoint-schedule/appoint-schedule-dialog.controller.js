(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('AppointScheduleDialogController', AppointScheduleDialogController);

    AppointScheduleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AppointSchedule', 'Patient', 'Doctor'];

    function AppointScheduleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AppointSchedule, Patient, Doctor) {
        var vm = this;

        vm.appointSchedule = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.patients = Patient.query();
        vm.doctors = Doctor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.appointSchedule.id !== null) {
                AppointSchedule.update(vm.appointSchedule, onSaveSuccess, onSaveError);
            } else {
                AppointSchedule.save(vm.appointSchedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:appointScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.scheduleDate = false;
        vm.datePickerOpenStatus.scheduleTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
