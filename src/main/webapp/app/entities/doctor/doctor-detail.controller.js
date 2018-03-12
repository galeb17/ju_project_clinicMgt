(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('DoctorDetailController', DoctorDetailController);

    DoctorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Doctor'];

    function DoctorDetailController($scope, $rootScope, $stateParams, previousState, entity, Doctor) {
        var vm = this;

        vm.doctor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('clinicApp:doctorUpdate', function(event, result) {
            vm.doctor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
