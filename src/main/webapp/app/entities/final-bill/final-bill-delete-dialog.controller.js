(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FinalBillDeleteController',FinalBillDeleteController);

    FinalBillDeleteController.$inject = ['$uibModalInstance', 'entity', 'FinalBill'];

    function FinalBillDeleteController($uibModalInstance, entity, FinalBill) {
        var vm = this;

        vm.finalBill = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FinalBill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
