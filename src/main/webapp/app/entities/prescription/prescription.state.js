(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prescription', {
            parent: 'entity',
            url: '/prescription?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Prescriptions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescription/prescriptions.html',
                    controller: 'PrescriptionController',
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
        .state('prescription-detail', {
            parent: 'prescription',
            url: '/prescription/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Prescription'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescription/prescription-detail.html',
                    controller: 'PrescriptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Prescription', function($stateParams, Prescription) {
                    return Prescription.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prescription',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prescription-detail.edit', {
            parent: 'prescription-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-dialog.html',
                    controller: 'PrescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescription.new', {
            parent: 'prescription',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-dialog.html',
                    controller: 'PrescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                days: null,
                                time: null,
                                remarks: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prescription', null, { reload: 'prescription' });
                }, function() {
                    $state.go('prescription');
                });
            }]
        })
        .state('prescription.edit', {
            parent: 'prescription',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-dialog.html',
                    controller: 'PrescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescription', null, { reload: 'prescription' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescription.delete', {
            parent: 'prescription',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-delete-dialog.html',
                    controller: 'PrescriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescription', null, { reload: 'prescription' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
