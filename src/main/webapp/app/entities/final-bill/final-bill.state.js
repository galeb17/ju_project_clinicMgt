(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('final-bill', {
            parent: 'entity',
            url: '/final-bill?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FinalBills'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/final-bill/final-bills.html',
                    controller: 'FinalBillController',
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
        .state('final-bill-detail', {
            parent: 'final-bill',
            url: '/final-bill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FinalBill'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/final-bill/final-bill-detail.html',
                    controller: 'FinalBillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FinalBill', function($stateParams, FinalBill) {
                    return FinalBill.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'final-bill',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('final-bill-detail.edit', {
            parent: 'final-bill-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/final-bill/final-bill-dialog.html',
                    controller: 'FinalBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FinalBill', function(FinalBill) {
                            return FinalBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('final-bill.new', {
            parent: 'final-bill',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/final-bill/final-bill-dialog.html',
                    controller: 'FinalBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                billNo: null,
                                treatmentCharge: null,
                                treatmentReceived: null,
                                treatmentBalance: null,
                                bedCharge: null,
                                nursingCharge: null,
                                medicineCharge: null,
                                doctorVisit: null,
                                operation: null,
                                Date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('final-bill', null, { reload: 'final-bill' });
                }, function() {
                    $state.go('final-bill');
                });
            }]
        })
        .state('final-bill.edit', {
            parent: 'final-bill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/final-bill/final-bill-dialog.html',
                    controller: 'FinalBillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FinalBill', function(FinalBill) {
                            return FinalBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('final-bill', null, { reload: 'final-bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('final-bill.delete', {
            parent: 'final-bill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/final-bill/final-bill-delete-dialog.html',
                    controller: 'FinalBillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FinalBill', function(FinalBill) {
                            return FinalBill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('final-bill', null, { reload: 'final-bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
