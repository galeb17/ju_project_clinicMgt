(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TransferIntimationDeleteController',TransferIntimationDeleteController);

    TransferIntimationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransferIntimation'];

    function TransferIntimationDeleteController($uibModalInstance, entity, TransferIntimation) {
        var vm = this;

        vm.transferIntimation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransferIntimation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
