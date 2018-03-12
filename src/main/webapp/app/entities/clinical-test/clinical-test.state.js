(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('clinical-test', {
            parent: 'entity',
            url: '/clinical-test?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClinicalTests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clinical-test/clinical-tests.html',
                    controller: 'ClinicalTestController',
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
        .state('clinical-test-detail', {
            parent: 'clinical-test',
            url: '/clinical-test/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClinicalTest'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clinical-test/clinical-test-detail.html',
                    controller: 'ClinicalTestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ClinicalTest', function($stateParams, ClinicalTest) {
                    return ClinicalTest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'clinical-test',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('clinical-test-detail.edit', {
            parent: 'clinical-test-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinical-test/clinical-test-dialog.html',
                    controller: 'ClinicalTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClinicalTest', function(ClinicalTest) {
                            return ClinicalTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('clinical-test.new', {
            parent: 'clinical-test',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinical-test/clinical-test-dialog.html',
                    controller: 'ClinicalTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name : null,
                                testType: null,
                                rate: null,
                                preRequisite: null,
                                caution: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('clinical-test', null, { reload: 'clinical-test' });
                }, function() {
                    $state.go('clinical-test');
                });
            }]
        })
        .state('clinical-test.edit', {
            parent: 'clinical-test',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinical-test/clinical-test-dialog.html',
                    controller: 'ClinicalTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClinicalTest', function(ClinicalTest) {
                            return ClinicalTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('clinical-test', null, { reload: 'clinical-test' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('clinical-test.delete', {
            parent: 'clinical-test',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinical-test/clinical-test-delete-dialog.html',
                    controller: 'ClinicalTestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClinicalTest', function(ClinicalTest) {
                            return ClinicalTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('clinical-test', null, { reload: 'clinical-test' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
