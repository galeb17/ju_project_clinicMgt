(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('ClinicalTestDetailController', ClinicalTestDetailController);

    ClinicalTestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClinicalTest'];

    function ClinicalTestDetailController($scope, $rootScope, $stateParams, previousState, entity, ClinicalTest) {
        var vm = this;

        vm.clinicalTest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:clinicalTestUpdate', function(event, result) {
            vm.clinicalTest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
