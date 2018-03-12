(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('AppointScheduleDetailController', AppointScheduleDetailController);

    AppointScheduleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AppointSchedule', 'Patient', 'Doctor'];

    function AppointScheduleDetailController($scope, $rootScope, $stateParams, previousState, entity, AppointSchedule, Patient, Doctor) {
        var vm = this;

        vm.appointSchedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:appointScheduleUpdate', function(event, result) {
            vm.appointSchedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
