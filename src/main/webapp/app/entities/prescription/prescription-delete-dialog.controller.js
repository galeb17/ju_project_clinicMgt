(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('PrescriptionDeleteController',PrescriptionDeleteController);

    PrescriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prescription'];

    function PrescriptionDeleteController($uibModalInstance, entity, Prescription) {
        var vm = this;

        vm.prescription = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prescription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
