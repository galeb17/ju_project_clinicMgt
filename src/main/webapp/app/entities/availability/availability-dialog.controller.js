(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('AvailabilityDialogController', AvailabilityDialogController);

    AvailabilityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Availability'];

    function AvailabilityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Availability) {
        var vm = this;

        vm.availability = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.availability.id !== null) {
                Availability.update(vm.availability, onSaveSuccess, onSaveError);
            } else {
                Availability.save(vm.availability, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:availabilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
