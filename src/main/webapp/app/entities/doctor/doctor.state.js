(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('doctor', {
            parent: 'entity',
            url: '/doctor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Doctors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor/doctors.html',
                    controller: 'DoctorController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('doctor-detail', {
            parent: 'doctor',
            url: '/doctor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Doctor'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor/doctor-detail.html',
                    controller: 'DoctorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Doctor', function($stateParams, Doctor) {
                    return Doctor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'doctor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('doctor-detail.edit', {
            parent: 'doctor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-dialog.html',
                    controller: 'DoctorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor.new', {
            parent: 'doctor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-dialog.html',
                    controller: 'DoctorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                gender: null,
                                age: null,
                                contactNo: null,
                                department: null,
                                consult_fee: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('doctor', null, { reload: 'doctor' });
                }, function() {
                    $state.go('doctor');
                });
            }]
        })
        .state('doctor.edit', {
            parent: 'doctor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-dialog.html',
                    controller: 'DoctorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor', null, { reload: 'doctor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor.delete', {
            parent: 'doctor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-delete-dialog.html',
                    controller: 'DoctorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor', null, { reload: 'doctor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
