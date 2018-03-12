(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('AppointScheduleDeleteController',AppointScheduleDeleteController);

    AppointScheduleDeleteController.$inject = ['$uibModalInstance', 'entity', 'AppointSchedule'];

    function AppointScheduleDeleteController($uibModalInstance, entity, AppointSchedule) {
        var vm = this;

        vm.appointSchedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AppointSchedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
