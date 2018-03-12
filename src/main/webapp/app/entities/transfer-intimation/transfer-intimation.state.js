(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer-intimation', {
            parent: 'entity',
            url: '/transfer-intimation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferIntimations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimations.html',
                    controller: 'TransferIntimationController',
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
        .state('transfer-intimation-detail', {
            parent: 'transfer-intimation',
            url: '/transfer-intimation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferIntimation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimation-detail.html',
                    controller: 'TransferIntimationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransferIntimation', function($stateParams, TransferIntimation) {
                    return TransferIntimation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer-intimation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-intimation-detail.edit', {
            parent: 'transfer-intimation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimation-dialog.html',
                    controller: 'TransferIntimationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferIntimation', function(TransferIntimation) {
                            return TransferIntimation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-intimation.new', {
            parent: 'transfer-intimation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimation-dialog.html',
                    controller: 'TransferIntimationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                refDoctor: null,
                                fromBedCode: null,
                                fromBedNo: null,
                                toBedCode: null,
                                toBedNo: null,
                                charge: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfer-intimation', null, { reload: 'transfer-intimation' });
                }, function() {
                    $state.go('transfer-intimation');
                });
            }]
        })
        .state('transfer-intimation.edit', {
            parent: 'transfer-intimation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimation-dialog.html',
                    controller: 'TransferIntimationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferIntimation', function(TransferIntimation) {
                            return TransferIntimation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-intimation', null, { reload: 'transfer-intimation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-intimation.delete', {
            parent: 'transfer-intimation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-intimation/transfer-intimation-delete-dialog.html',
                    controller: 'TransferIntimationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransferIntimation', function(TransferIntimation) {
                            return TransferIntimation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-intimation', null, { reload: 'transfer-intimation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
