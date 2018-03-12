(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('treat-bill', {
            parent: 'entity',
            url: '/treat-bill?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TreatBills'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/treat-bill/treat-bills.html',
                    controller: 'TreatBillController',
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
        .state('treat-bill-detail', {
            parent: 'treat-bill',
            url: '/treat-bill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TreatBill'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/treat-bill/treat-bill-detail.html',
                    controller: 'TreatBillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TreatBill', function($stateParams, TreatBill) {
                    return TreatBill.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'treat-bill',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('treat-bill-detail.edit', {
            parent: 'treat-bill-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/treat-bill/treat-bill-dialog.html',
                    controller: 'TreatBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TreatBill', function(TreatBill) {
                            return TreatBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('treat-bill.new', {
            parent: 'treat-bill',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/treat-bill/treat-bill-dialog.html',
                    controller: 'TreatBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                charge: null,
                                received: null,
                                balance: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('treat-bill', null, { reload: 'treat-bill' });
                }, function() {
                    $state.go('treat-bill');
                });
            }]
        })
        .state('treat-bill.edit', {
            parent: 'treat-bill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/treat-bill/treat-bill-dialog.html',
                    controller: 'TreatBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TreatBill', function(TreatBill) {
                            return TreatBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('treat-bill', null, { reload: 'treat-bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('treat-bill.delete', {
            parent: 'treat-bill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/treat-bill/treat-bill-delete-dialog.html',
                    controller: 'TreatBillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TreatBill', function(TreatBill) {
                            return TreatBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('treat-bill', null, { reload: 'treat-bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
