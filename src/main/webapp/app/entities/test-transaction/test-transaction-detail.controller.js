(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TestTransactionDetailController', TestTransactionDetailController);

    TestTransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TestTransaction', 'Patient'];

    function TestTransactionDetailController($scope, $rootScope, $stateParams, previousState, entity, TestTransaction, Patient) {
        var vm = this;

        vm.testTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:testTransactionUpdate', function(event, result) {
            vm.testTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
