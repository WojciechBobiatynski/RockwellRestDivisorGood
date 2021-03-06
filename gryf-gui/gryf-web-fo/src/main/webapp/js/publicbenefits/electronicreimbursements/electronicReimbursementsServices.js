angular.module('gryf.electronicreimbursements').factory("electronicReimbursementSearchService",
    ['$http', 'GryfModals', 'GryfHelpers', 'GryfTables', function($http, GryfModals, GryfHelpers, GryfTables) {

        var FIND_RMBS_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/list";
        var FIND_RMBS_STATUSES_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/statuses";
        var FIND_RMBS_TYPES_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/types";
        var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/publicBenefits/grantPrograms";


        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var elctRmbsModel = new ElctRmbsModel();
        var dictionaries = {rmbsStatuses: null, rmbsTypes: null};

        function ElctRmbsCriteria() {
            this.rmbsNumber =  null,
            this.grantProgramId = null,
            this.rmbsType =  null,
            this.trainingName= null,
            this.pesel= null,
            this.participantName= null,
            this.participantSurname= null,
            this.rmbsDateFrom= null,
            this.rmbsDateTo= null,
            this.rmbsStatus= null,
            this.noteNumber = null,
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

        var getNewElctRmbsModel = function() {
            elctRmbsModel = new ElctRmbsCriteria();
            return elctRmbsModel;
        };

        var getNewElctRmbsCriteria = function() {
            elctRmbsCriteria = new ElctRmbsCriteria();
            return elctRmbsCriteria;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getElctRmbsModel = function () {
            return elctRmbsModel;
        };

        var getDictionaries = function() {
            loadReimbursementsStatuses();
            loadReimbursementsTypes();
            return dictionaries;
        };

        var find = function(findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(elctRmbsCriteria);
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

        var loadReimbursementsStatuses = function() {
            var promise = $http.get(FIND_RMBS_STATUSES_LIST_URL);
            promise.then(function (response) {
                dictionaries.rmbsStatuses = response.data;
            });
            return promise;
        };

        var loadReimbursementsTypes = function() {
            var promise = $http.get(FIND_RMBS_TYPES_LIST_URL);
            promise.then(function (response) {
                dictionaries.rmbsTypes = response.data;
            });
            return promise;
        };

        var getGrantProgramNames = function () {
            return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
        }

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(elctRmbsCriteria, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        return {
            getNewElctRmbsModel: getNewElctRmbsModel,
            getNewCriteria: getNewElctRmbsCriteria,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getSearchResultOptions: getSearchResultOptions,
            getElctRmbsModel: getElctRmbsModel,
            getDictionaries: getDictionaries,
            find: find,
            loadMore: loadMore,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            getGrantProgramNames: getGrantProgramNames
        };
    }]);

angular.module("gryf.electronicreimbursements").factory("AnnounceEReimbursementService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "GryfPopups", "Upload",
        function($http, $routeParams, GryfModals, GryfExceptionHandler, GryfPopups, Upload) {

            var FIND_RMBS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/";
            var CHANGE_STATUS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/status";
            var NEW_CORRECTION_DATE_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/correctionDate/";
            var FIND_RMBS_CORRECTIONS_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/correction/list/";
            var CREATE_DOCUMENTS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/createDocuments/";
            var CORR_ATTACHMENT = contextPath + "/rest/publicBenefits/electronic/reimbursements/downloadCorrAttachment";
            var PRINT_REPORTS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/printReports/";
            var CONFIRM_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/confirm/";
            var CANCEL_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/cancel/";
            var CREATE_EMAILS_FROM_TEMPLATE = contextPath + "/rest/publicBenefits/electronic/reimbursements/email/create/";
            var SEND_EMAILS = contextPath + "/rest/publicBenefits/electronic/reimbursements/email/send";
            var SAVE_ATT = contextPath + "/rest/publicBenefits/electronic/reimbursements/att/save";
            var REJECT_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/reject/";
            var AUTO_CONFIRM_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/automatic/confirm/";
            var ADD_INFO = contextPath + "/rest/publicBenefits/electronic/reimbursements/addInfo";

            var eReimbObject = new EReimbObject();
            var correctionObject = new CorrectionObject();
            var violations = {};

            var getNewModel = function() {
                eReimbObject = new EReimbObject();
                return eReimbObject;
            };

            var getViolations = function() {
                return violations;
            };

            var getNewViolations = function() {
                violations = {};
                return violations;
            };

            function EReimbObject() {
                this.entity = {
                    ermbsId: null,
                    trainingInstanceId: null,
                    products: null,
                    sxoIndAmountDueTotal: null,
                    indSxoAmountDueTotal: null,
                    attachments: null,
                    statusCode: null,
                    returnAccountPayment: null,
                    lastCorrectionDto: null,
                    automatic: false
                }
            }

            function CorrectionObject() {
                this.isNewCorrect = false;
                this.isCorrectionsLoaded = false;
                this.entity = {
                    ermbsId: null,
                    correctionReason: null,
                    requiredDate: null
                };
                this.loadedCorrections = [];
            }

            var sendToCorrect = function() {
                if(!correctionObject.isNewCorrect) {
                    GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
                        message: "Nie dodano nowej korekty!"
                    });
                    return;
                }
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuj?? dane"});
                var promise = $http.post(CHANGE_STATUS_URL + "/tocorrect", correctionObject.entity);
                promise.success(function() {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie wys??ano do korekty");
                    GryfPopups.showPopup();

                    correctionObject.isCorrectionsLoaded = false;
                    correctionObject.isNewCorrect = false;
                    findById();
                });
                promise.error(function(error) {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wys??a?? rozliczenia do korekty");
                    GryfPopups.showPopup();

                    var conflictCallbacksObject;
                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                });
                promise.finally(function () {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            };

            var findById = function(rmbsId) {
                if ($routeParams.id || rmbsId) {
                    var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuj?? dane"});
                    var promise = $http.get(FIND_RMBS_URL + ($routeParams.id ? $routeParams.id : rmbsId));
                    promise.then(function(response) {
                        eReimbObject.entity = response.data;
                        if(eReimbObject.entity.lastCorrectionDto) {
                            correctionObject.loadedCorrections = [eReimbObject.entity.lastCorrectionDto];
                        }
                    });
                    promise.finally(function() {
                        GryfModals.closeModal(modalInstance);
                    });
                    return promise;
                }
            };

            var getNewRequiredCorrectionDate = function() {
                return $http.get(NEW_CORRECTION_DATE_URL + eReimbObject.entity.ermbsId).success(function(date) {
                    correctionObject.isNewCorrect = true;
                    correctionObject.entity.requiredDate = date;
                    correctionObject.entity.ermbsId = eReimbObject.entity.ermbsId;
                });
            };

            var getCorrectionObject = function() {
                return correctionObject;
            };

            var findAllCorrections = function(rmbsId) {
                var rmbsId =  + ($routeParams.id ? $routeParams.id : rmbsId);
                return $http.get(FIND_RMBS_CORRECTIONS_LIST_URL + rmbsId).success(function(data) {
                    correctionObject.loadedCorrections = data;
                    correctionObject.isCorrectionsLoaded = true;
                });
            };

            var createDocuments = function() {
                var rmbsId =  + ($routeParams.id ? $routeParams.id : rmbsId);
                return $http.post(CREATE_DOCUMENTS_URL + rmbsId)
                    .success(function(response) {
                        GryfPopups.setPopup("success", "Sukces", "Wystawiono dokumenty");
                        eReimbObject.entity = response;
                    })
                    .error(function() {
                        GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wystawi?? dokument??w");
                    })
                    .finally(function() {
                        GryfPopups.showPopup();
                    });
            };

            var printReports = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(PRINT_REPORTS_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Wydrukowano dokumenty");
                    eReimbObject.entity = response;
                })
                .error(function() {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wydrukowa?? dokument??w");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var confirm = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(CONFIRM_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie zosta??o zatwierdzone");
                    eReimbObject.entity = response;
                })
                .error(function() {
                    GryfPopups.setPopup("success", "Sukces", "Nie uda??o si?? zatwierdzi?? rozliczenia");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var cancel = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(CANCEL_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Anulowano rozliczenie");
                    eReimbObject.entity = response;
                })
                .error(function() {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? anulowa?? rozliczenia");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var createEmailsFromTemplate = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(CREATE_EMAILS_FROM_TEMPLATE + rmbsId);
                promise.success(function(response) {
                    angular.forEach(response, function (mail) {
                        eReimbObject.entity.emails.push(mail);
                    });
                });
                return promise;
            };

            function findAllFileAttachments(mail) {
                var resultArray = [];
                for (var i = 0; i < mail.attachments.length; i++) {
                    var attachmentFileField = mail.attachments[i].file;
                    if (attachmentFileField) {
                        if (attachmentFileField.length) {
                            resultArray.push(mail.attachments[i].file[0]);
                            mail.attachments[i].fileIncluded = true;
                            mail.attachments[i].originalFilename=mail.attachments[i].file[0].name;
                        }
                    }
                }
                return resultArray;
            }

            var sendMail = function(mail) {
                var files = findAllFileAttachments(mail);
                var promise = Upload.upload({
                    url: SEND_EMAILS,
                    data: angular.toJson(mail),
                    file: files
                });
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Mail zosta?? wys??any");
                    var index = eReimbObject.entity.emails.indexOf(mail);
                    eReimbObject.entity.emails[index] = response;
                })
                .error(function() {
                GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wys??a?? maila");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var getSaveAttUrl = function () {
                return SAVE_ATT;
            };

            var reject = function() {
                var rejectionDto = new RejectionDto();
                var promise = $http.post(REJECT_URL, rejectionDto);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie zosta??o odrzucone");
                    eReimbObject.entity = response;
                })
                    .error(function(error) {
                        GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? odrzuci?? rozliczenia");
                        var conflictCallbacksObject;
                        GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                    })
                    .finally(function() {
                        GryfPopups.showPopup();
                    });
                return promise;
            };

            function RejectionDto() {
                this.ermbsId = eReimbObject.entity.ermbsId;
                this.rejectionReasonId = eReimbObject.entity.rejectionReasonId;
                this.rejectionDetails = eReimbObject.entity.rejectionDetails;
            };

            var automaticConfirm = function () {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(AUTO_CONFIRM_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie zosta??o zatwierdzone");
                    eReimbObject.entity = response;
                })
                    .error(function() {
                        GryfPopups.setPopup("success", "Sukces", "Nie uda??o si?? zatwierdzi?? rozliczenia");
                    })
                    .finally(function() {
                        GryfPopups.showPopup();
                    });
                return promise;
            };

            function ErmbsAdditionalInformationDto() {
                this.ermbsId = eReimbObject.entity.ermbsId;
                this.foComment = eReimbObject.entity.foComment;
            };

            var saveFoComment = function() {
                var addInfoDto = new ErmbsAdditionalInformationDto();
                var promise = $http.post(ADD_INFO, addInfoDto);
                promise
                    .success(function(response) {
                        GryfPopups.setPopup("success", "Sukces", "Komentarz zapisany");
                    })
                    .error(function() {
                        GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? zapisa?? komentarza");
                    })
                    .finally(function() {
                        GryfPopups.showPopup();
                    });
                return promise;
            };

            return {
                getNewModel: getNewModel,
                getViolation: getViolations,
                getNewViolations: getNewViolations,
                findById: findById,
                sendToCorrect: sendToCorrect,
                getCorrectionObject: getCorrectionObject,
                getNewRequiredCorrectionDate: getNewRequiredCorrectionDate,
                findAllCorrections: findAllCorrections,
                createDocuments: createDocuments,
                printReports: printReports,
                confirm: confirm,
                cancel: cancel,
                createEmailsFromTemplate: createEmailsFromTemplate,
                sendMail: sendMail,
                getSaveAttUrl: getSaveAttUrl,
                reject: reject,
                automaticConfirm: automaticConfirm,
                saveFoComment: saveFoComment
            };
        }]);

angular.module("gryf.electronicreimbursements").factory("UnreservedPoolService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "GryfPopups", "Upload",
        function($http, $routeParams, GryfModals, GryfExceptionHandler, GryfPopups, Upload) {
            var FIND_UN_RMBS_URL = contextPath + "/rest/publicBenefits/unrsv/reimbursements/";
            var CREATE_DOCUMENTS_URL = contextPath + "/rest/publicBenefits/unrsv/reimbursements/createDocuments/";
            var PRINT_REPORTS_URL = contextPath + "/rest/publicBenefits/unrsv/reimbursements/printReports/";
            var EXPIRE_URL = contextPath + "/rest/publicBenefits/unrsv/reimbursements/expire/";
            var CREATE_EMAILS_FROM_TEMPLATE = contextPath + "/rest/publicBenefits/unrsv/reimbursements/email/create/";
            var SEND_EMAILS = contextPath + "/rest/publicBenefits/electronic/reimbursements/email/send";
            var ADD_INFO = contextPath + "/rest/publicBenefits/electronic/reimbursements/addInfo";

            var unReimbObject = new UnReimbObject();

            function UnReimbObject() {
                this.entity = {
                    ermbsId: null,
                    typeCode: null,
                    poolId: null,
                    grantProgramId: null,
                    pool: null,
                    sxoTiAmountDueTotal: null,
                    sxoIndAmountDueTotal: null,
                    indTiAmountDueTotal: null,
                    indOwnContributionUsed: null,
                    indSubsidyValue: null,
                    statusCode: null,
                    reimbursementDate: null,
                    reports: [],
                    emails: [],
                    individual: null,
                    foComment: null
                }
            }

            var getNewModel = function() {
                unReimbObject = new UnReimbObject();
                return unReimbObject;
            };

            var findById = function(rmbsId) {
                if ($routeParams.id || rmbsId) {
                    var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuj?? dane"});
                    var promise = $http.get(FIND_UN_RMBS_URL + ($routeParams.id ? $routeParams.id : rmbsId));
                    promise.then(function(response) {
                        unReimbObject.entity = response.data;
                    });
                    promise.finally(function() {
                        GryfModals.closeModal(modalInstance);
                    });
                    return promise;
                }
            };

            var createDocuments = function() {
                var rmbsId =  + ($routeParams.id ? $routeParams.id : rmbsId);
                return $http.post(CREATE_DOCUMENTS_URL + rmbsId)
                .success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Wystawiono dokumenty");
                    unReimbObject.entity = response;
                })
                .error(function(error) {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wystawi?? dokument??w");
                    var conflictCallbacksObject;
                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
            };

            var printReports = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(PRINT_REPORTS_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Wydrukowano dokumenty");
                    unReimbObject.entity = response;
                })
                .error(function(error) {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wydrukowa?? dokument??w");
                    var conflictCallbacksObject;
                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var expire = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(EXPIRE_URL + rmbsId);
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie zosta??o zatwierdzone");
                    unReimbObject.entity = response;
                })
                .error(function(error) {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? zatwierdzi?? rozliczenia");
                    var conflictCallbacksObject;
                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            var createEmailsFromTemplate = function() {
                var rmbsId =  $routeParams.id;
                var promise = $http.post(CREATE_EMAILS_FROM_TEMPLATE + rmbsId);
                promise.success(function(response) {
                    angular.forEach(response, function (mail) {
                        unReimbObject.entity.emails.push(mail);
                    });
                });
                return promise;
            };

            function findAllFileAttachments(mail) {
                var resultArray = [];
                for (var i = 0; i < mail.attachments.length; i++) {
                    var attachmentFileField = mail.attachments[i].file;
                    if (attachmentFileField) {
                        if (attachmentFileField.length) {
                            resultArray.push(mail.attachments[i].file[0]);
                            mail.attachments[i].fileIncluded = true;
                            mail.attachments[i].originalFilename=mail.attachments[i].file[0].name;
                        }
                    }
                }
                return resultArray;
            }

            var sendMail = function(mail) {
                var files = findAllFileAttachments(mail);
                var promise = Upload.upload({
                    url: SEND_EMAILS,
                    data: angular.toJson(mail),
                    file: files
                });
                promise.success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Mail zosta?? wys??any");
                    var index = unReimbObject.entity.emails.indexOf(mail);
                    unReimbObject.entity.emails[index] = response;
                })
                .error(function(error) {
                    GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wys??a?? maila");
                    var conflictCallbacksObject;
                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
                return promise;
            };

            function ErmbsAdditionalInformationDto() {
                this.ermbsId = unReimbObject.entity.ermbsId;
                this.foComment = unReimbObject.entity.foComment;
            };

            var saveFoComment = function() {
                var addInfoDto = new ErmbsAdditionalInformationDto();
                var promise = $http.post(ADD_INFO, addInfoDto);
                promise
                    .success(function(response) {
                        GryfPopups.setPopup("success", "Sukces", "Komentarz zapisany");
                    })
                    .error(function() {
                        GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? zapisa?? komentarza");
                    })
                    .finally(function() {
                        GryfPopups.showPopup();
                    });
                return promise;
            };

            return {
                getNewModel: getNewModel,
                findById: findById,
                createDocuments: createDocuments,
                printReports: printReports,
                expire: expire,
                createEmailsFromTemplate: createEmailsFromTemplate,
                sendMail: sendMail,
                saveFoComment: saveFoComment
            };
        }]);