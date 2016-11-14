angular.module('gryf.electronicreimbursements').factory("electronicReimbursementsService",
    ['$http', function($http) {

        var FIND_REIMBURSEMENTS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/reimbursements";

        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var foundRmbs = {};

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

        var loadMore = function() {
            elctRmbsCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getNewCriteria: getNewElctRmbsCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getFoundRmbs: getFoundRmbs,
            loadMore: loadMore
        };
    }]);