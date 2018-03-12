(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('firoj', {
            parent: 'entity',
            url: '/firoj',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Firojs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/firoj/firojs.html',
                    controller: 'FirojController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('firoj-detail', {
            parent: 'firoj',
            url: '/firoj/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Firoj'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/firoj/firoj-detail.html',
                    controller: 'FirojDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Firoj', function($stateParams, Firoj) {
                    return Firoj.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'firoj',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('firoj-detail.edit', {
            parent: 'firoj-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/firoj/firoj-dialog.html',
                    controller: 'FirojDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Firoj', function(Firoj) {
                            return Firoj.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('firoj.new', {
            parent: 'firoj',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/firoj/firoj-dialog.html',
                    controller: 'FirojDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('firoj', null, { reload: 'firoj' });
                }, function() {
                    $state.go('firoj');
                });
            }]
        })
        .state('firoj.edit', {
            parent: 'firoj',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/firoj/firoj-dialog.html',
                    controller: 'FirojDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Firoj', function(Firoj) {
                            return Firoj.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('firoj', null, { reload: 'firoj' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('firoj.delete', {
            parent: 'firoj',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/firoj/firoj-delete-dialog.html',
                    controller: 'FirojDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Firoj', function(Firoj) {
                            return Firoj.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('firoj', null, { reload: 'firoj' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
