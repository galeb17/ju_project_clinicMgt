(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FirojDeleteController',FirojDeleteController);

    FirojDeleteController.$inject = ['$uibModalInstance', 'entity', 'Firoj'];

    function FirojDeleteController($uibModalInstance, entity, Firoj) {
        var vm = this;

        vm.firoj = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Firoj.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
