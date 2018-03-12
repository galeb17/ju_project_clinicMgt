(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TreatBillDeleteController',TreatBillDeleteController);

    TreatBillDeleteController.$inject = ['$uibModalInstance', 'entity', 'TreatBill'];

    function TreatBillDeleteController($uibModalInstance, entity, TreatBill) {
        var vm = this;

        vm.treatBill = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TreatBill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
