(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('ClinicalTestDialogController', ClinicalTestDialogController);

    ClinicalTestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClinicalTest'];

    function ClinicalTestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClinicalTest) {
        var vm = this;

        vm.clinicalTest = entity;
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
            if (vm.clinicalTest.id !== null) {
                ClinicalTest.update(vm.clinicalTest, onSaveSuccess, onSaveError);
            } else {
                ClinicalTest.save(vm.clinicalTest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:clinicalTestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
