(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TestDescriptionDialogController', TestDescriptionDialogController);

    TestDescriptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TestDescription'];

    function TestDescriptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TestDescription) {
        var vm = this;

        vm.testDescription = entity;
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
            if (vm.testDescription.id !== null) {
                TestDescription.update(vm.testDescription, onSaveSuccess, onSaveError);
            } else {
                TestDescription.save(vm.testDescription, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:testDescriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
