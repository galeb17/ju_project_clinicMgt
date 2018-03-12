(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('AvailabilityDeleteController',AvailabilityDeleteController);

    AvailabilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Availability'];

    function AvailabilityDeleteController($uibModalInstance, entity, Availability) {
        var vm = this;

        vm.availability = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Availability.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
