'use strict';

describe('Controller Tests', function() {

    describe('Prescription Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPrescription, MockPatient, MockDoctor, MockMedicine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPrescription = jasmine.createSpy('MockPrescription');
            MockPatient = jasmine.createSpy('MockPatient');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockMedicine = jasmine.createSpy('MockMedicine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Prescription': MockPrescription,
                'Patient': MockPatient,
                'Doctor': MockDoctor,
                'Medicine': MockMedicine
            };
            createController = function() {
                $injector.get('$controller')("PrescriptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clinicApp:prescriptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
