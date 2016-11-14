angular.module('gryf.electronicreimbursements').factory("electronicReimbursementsService",
    ['$http', 'GryfModals', 'GryfHelpers', 'GryfTables', function($http, GryfModals, GryfHelpers, GryfTables) {

        var FIND_RMBS_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/list";
        var FIND_RMBS_STATUSES_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/statuses";


        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var rmbsStatuses = [];
        var foundRmbs = [];

        function ElctRmbsCriteria() {
            this.rmbsNumber =  null,
            this.trainingName= null,
            this.pesel= null,
            this.participantName= null,
            this.participantSurname= null,
            this.rmbsDateFrom= null,
            this.rmbsDateTo= null,
            this.rmbsStatus= null,
            this.sortTypes= [],
            this.sortColumns= [],
            this.limit= 10
        };

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        };

        var getElctRmbsCriteria = function () {
            return elctRmbsCriteria;
        };

        var getNewElctRmbsCriteria = function() {
            elctRmbsCriteria = new ElctRmbsCriteria();
            return elctRmbsCriteria;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getFoundRmbs = function () {
            return foundRmbs;
        };

        var find = function(findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(foundRmbs);
            if (!findUrl) {
                findUrl = FIND_RMBS_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: elctRmbsCriteria});
            promise.then(function(response) {
                //success
                foundRmbs = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function() {
                //error
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var loadMore = function() {
            elctRmbsCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var getReimbursementsStatuses = function() {
            return $http.get(FIND_RMBS_STATUSES_LIST_URL);
        };

        return {
            getNewCriteria: getNewElctRmbsCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getFoundRmbs: getFoundRmbs,
            find: find,
            loadMore: loadMore,
            getReimbursementsStatuses: getReimbursementsStatuses
        };
    }]);