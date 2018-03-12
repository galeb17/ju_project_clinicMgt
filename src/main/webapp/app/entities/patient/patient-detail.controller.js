(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('PatientDetailController', PatientDetailController);

    PatientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Patient'];

    function PatientDetailController($scope, $rootScope, $stateParams, previousState, entity, Patient) {
        var vm = this;

        vm.patient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:patientUpdate', function(event, result) {
            vm.patient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
