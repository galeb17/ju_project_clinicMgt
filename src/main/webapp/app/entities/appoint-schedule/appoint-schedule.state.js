(function() {
    'use strict';

    angular
        .module('clinicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('appoint-schedule', {
            parent: 'entity',
            url: '/appoint-schedule?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AppointSchedules'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedules.html',
                    controller: 'AppointScheduleController',
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
        .state('appoint-schedule-detail', {
            parent: 'appoint-schedule',
            url: '/appoint-schedule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AppointSchedule'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedule-detail.html',
                    controller: 'AppointScheduleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AppointSchedule', function($stateParams, AppointSchedule) {
                    return AppointSchedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'appoint-schedule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('appoint-schedule-detail.edit', {
            parent: 'appoint-schedule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedule-dialog.html',
                    controller: 'AppointScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AppointSchedule', function(AppointSchedule) {
                            return AppointSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appoint-schedule.new', {
            parent: 'appoint-schedule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedule-dialog.html',
                    controller: 'AppointScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scheduleDate: null,
                                scheduleTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('appoint-schedule', null, { reload: 'appoint-schedule' });
                }, function() {
                    $state.go('appoint-schedule');
                });
            }]
        })
        .state('appoint-schedule.edit', {
            parent: 'appoint-schedule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedule-dialog.html',
                    controller: 'AppointScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AppointSchedule', function(AppointSchedule) {
                            return AppointSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appoint-schedule', null, { reload: 'appoint-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appoint-schedule.delete', {
            parent: 'appoint-schedule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appoint-schedule/appoint-schedule-delete-dialog.html',
                    controller: 'AppointScheduleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AppointSchedule', function(AppointSchedule) {
                            return AppointSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appoint-schedule', null, { reload: 'appoint-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
