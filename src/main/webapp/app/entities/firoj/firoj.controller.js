(function() {
    'use strict';

    angular
        .module('clinicApp')
        .controller('FirojController', FirojController);

    FirojController.$inject = ['Firoj'];

    function FirojController(Firoj) {

        var vm = this;

        vm.firojs = [];

        loadAll();

        function loadAll() {
            Firoj.query(function(result) {
                vm.firojs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
