(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FirojDialogController', FirojDialogController);

    FirojDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Firoj'];

    function FirojDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Firoj) {
        var vm = this;

        vm.firoj = entity;
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
            if (vm.firoj.id !== null) {
                Firoj.update(vm.firoj, onSaveSuccess, onSaveError);
            } else {
                Firoj.save(vm.firoj, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clinicApp:firojUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
