'use strict';

describe('Controller Tests', function() {

    describe('AppointSchedule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAppointSchedule, MockPatient, MockDoctor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAppointSchedule = jasmine.createSpy('MockAppointSchedule');
            MockPatient = jasmine.createSpy('MockPatient');
            MockDoctor = jasmine.createSpy('MockDoctor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AppointSchedule': MockAppointSchedule,
                'Patient': MockPatient,
                'Doctor': MockDoctor
            };
            createController = function() {
                $injector.get('$controller')("AppointScheduleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clinicApp:appointScheduleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
