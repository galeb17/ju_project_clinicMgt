(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TestTransactionDeleteController',TestTransactionDeleteController);

    TestTransactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TestTransaction'];

    function TestTransactionDeleteController($uibModalInstance, entity, TestTransaction) {
        var vm = this;

        vm.testTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TestTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
