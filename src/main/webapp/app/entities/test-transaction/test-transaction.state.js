(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('test-transaction', {
            parent: 'entity',
            url: '/test-transaction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TestTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-transaction/test-transactions.html',
                    controller: 'TestTransactionController',
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
        .state('test-transaction-detail', {
            parent: 'test-transaction',
            url: '/test-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TestTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-transaction/test-transaction-detail.html',
                    controller: 'TestTransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TestTransaction', function($stateParams, TestTransaction) {
                    return TestTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'test-transaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('test-transaction-detail.edit', {
            parent: 'test-transaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-transaction/test-transaction-dialog.html',
                    controller: 'TestTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestTransaction', function(TestTransaction) {
                            return TestTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-transaction.new', {
            parent: 'test-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-transaction/test-transaction-dialog.html',
                    controller: 'TestTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                treatment : null,
                                cause: null,
                                charge: null,
                                received: null,
                                balance: null,
                                refDoctor: null,
                                treatNo: null,
                                testReport: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('test-transaction', null, { reload: 'test-transaction' });
                }, function() {
                    $state.go('test-transaction');
                });
            }]
        })
        .state('test-transaction.edit', {
            parent: 'test-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-transaction/test-transaction-dialog.html',
                    controller: 'TestTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestTransaction', function(TestTransaction) {
                            return TestTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-transaction', null, { reload: 'test-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-transaction.delete', {
            parent: 'test-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-transaction/test-transaction-delete-dialog.html',
                    controller: 'TestTransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TestTransaction', function(TestTransaction) {
                            return TestTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-transaction', null, { reload: 'test-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
