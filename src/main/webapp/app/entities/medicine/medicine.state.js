(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medicine', {
            parent: 'entity',
            url: '/medicine?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Medicines'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicine/medicines.html',
                    controller: 'MedicineController',
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
        .state('medicine-detail', {
            parent: 'medicine',
            url: '/medicine/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Medicine'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicine/medicine-detail.html',
                    controller: 'MedicineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Medicine', function($stateParams, Medicine) {
                    return Medicine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medicine',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medicine-detail.edit', {
            parent: 'medicine-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-dialog.html',
                    controller: 'MedicineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicine.new', {
            parent: 'medicine',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-dialog.html',
                    controller: 'MedicineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                medicineType: null,
                                unitOfMeasurement: null,
                                generic: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medicine', null, { reload: 'medicine' });
                }, function() {
                    $state.go('medicine');
                });
            }]
        })
        .state('medicine.edit', {
            parent: 'medicine',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-dialog.html',
                    controller: 'MedicineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicine', null, { reload: 'medicine' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicine.delete', {
            parent: 'medicine',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-delete-dialog.html',
                    controller: 'MedicineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicine', null, { reload: 'medicine' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
