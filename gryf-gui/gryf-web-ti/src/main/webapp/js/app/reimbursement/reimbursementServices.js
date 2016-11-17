angular.module("gryf.ti").factory("ReimbursementsService",
    ['$http','GryfModals', 'GryfPopups', 'GryfExceptionHandler','GryfHelpers', function($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers) {
    var FIND_RMBS_LIST_URL = contextPath + "/rest/reimbursements/list";
    var FIND_RMBS_STATUSES_LIST_URL = contextPath + "/rest/reimbursements/statuses";


    var elctRmbsCriteria = new ElctRmbsCriteria();
    var searchResultOptions = new SearchResultOptions();
    var elctRmbsModel = new ElctRmbsModel();

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

    function ElctRmbsModel() {
        this.rmbsStatuses = [];
        this.foundRmbs = [];

    }

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

    var getElctRmbsModel = function () {
        return elctRmbsModel;
    };

    var find = function(findUrl) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

        GryfHelpers.transformDatesToString(elctRmbsModel.foundRmbs);
        if (!findUrl) {
            findUrl = FIND_RMBS_LIST_URL;
        }
        var promise = $http.get(findUrl, {params: elctRmbsCriteria});
        promise.then(function(response) {
            elctRmbsModel.foundRmbs = response.data;
            searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
        }, function() {
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

    return {
        getNewCriteria: getNewElctRmbsCriteria,
        getSearchResultOptions: getSearchResultOptions,
        getNewSearchResultOptions: getNewSearchResultOptions,
        getElctRmbsModel: getElctRmbsModel,
        find: find,
        loadMore: loadMore
    };
}]);