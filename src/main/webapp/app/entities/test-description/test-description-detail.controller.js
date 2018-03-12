(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('TestDescriptionDetailController', TestDescriptionDetailController);

    TestDescriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TestDescription'];

    function TestDescriptionDetailController($scope, $rootScope, $stateParams, previousState, entity, TestDescription) {
        var vm = this;

        vm.testDescription = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:testDescriptionUpdate', function(event, result) {
            vm.testDescription = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
