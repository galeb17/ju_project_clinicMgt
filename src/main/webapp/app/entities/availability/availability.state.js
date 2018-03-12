(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('availability', {
            parent: 'entity',
            url: '/availability?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Availabilities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/availability/availabilities.html',
                    controller: 'AvailabilityController',
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
        .state('availability-detail', {
            parent: 'availability',
            url: '/availability/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Availability'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/availability/availability-detail.html',
                    controller: 'AvailabilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Availability', function($stateParams, Availability) {
                    return Availability.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'availability',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('availability-detail.edit', {
            parent: 'availability-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-dialog.html',
                    controller: 'AvailabilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Availability', function(Availability) {
                            return Availability.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('availability.new', {
            parent: 'availability',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-dialog.html',
                    controller: 'AvailabilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                wards: null,
                                bedCode: null,
                                bedNo: null,
                                reserved: null,
                                Charge: null,
                                gender: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: 'availability' });
                }, function() {
                    $state.go('availability');
                });
            }]
        })
        .state('availability.edit', {
            parent: 'availability',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-dialog.html',
                    controller: 'AvailabilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Availability', function(Availability) {
                            return Availability.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: 'availability' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('availability.delete', {
            parent: 'availability',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-delete-dialog.html',
                    controller: 'AvailabilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Availability', function(Availability) {
                            return Availability.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: 'availability' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
