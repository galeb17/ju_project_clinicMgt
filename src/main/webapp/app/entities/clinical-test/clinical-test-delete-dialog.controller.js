(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('ClinicalTestDeleteController',ClinicalTestDeleteController);

    ClinicalTestDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClinicalTest'];

    function ClinicalTestDeleteController($uibModalInstance, entity, ClinicalTest) {
        var vm = this;

        vm.clinicalTest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClinicalTest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
