(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('test-description', {
            parent: 'entity',
            url: '/test-description?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TestDescriptions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-description/test-descriptions.html',
                    controller: 'TestDescriptionController',
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
        .state('test-description-detail', {
            parent: 'test-description',
            url: '/test-description/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TestDescription'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-description/test-description-detail.html',
                    controller: 'TestDescriptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TestDescription', function($stateParams, TestDescription) {
                    return TestDescription.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'test-description',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('test-description-detail.edit', {
            parent: 'test-description-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-description/test-description-dialog.html',
                    controller: 'TestDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestDescription', function(TestDescription) {
                            return TestDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-description.new', {
            parent: 'test-description',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-description/test-description-dialog.html',
                    controller: 'TestDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                department: null,
                                treatment: null,
                                charge: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('test-description', null, { reload: 'test-description' });
                }, function() {
                    $state.go('test-description');
                });
            }]
        })
        .state('test-description.edit', {
            parent: 'test-description',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-description/test-description-dialog.html',
                    controller: 'TestDescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestDescription', function(TestDescription) {
                            return TestDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-description', null, { reload: 'test-description' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-description.delete', {
            parent: 'test-description',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-description/test-description-delete-dialog.html',
                    controller: 'TestDescriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TestDescription', function(TestDescription) {
                            return TestDescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-description', null, { reload: 'test-description' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
