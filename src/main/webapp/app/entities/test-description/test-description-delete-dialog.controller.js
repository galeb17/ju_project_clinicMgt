(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TestDescriptionDeleteController',TestDescriptionDeleteController);

    TestDescriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TestDescription'];

    function TestDescriptionDeleteController($uibModalInstance, entity, TestDescription) {
        var vm = this;

        vm.testDescription = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TestDescription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
