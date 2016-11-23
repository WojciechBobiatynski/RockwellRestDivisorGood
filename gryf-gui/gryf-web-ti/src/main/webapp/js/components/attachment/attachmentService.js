angular.module("gryf.ti").factory("AttachmentService", function ($http) {
    var PATH_REST_ATTACHMENTS = '/rest/attachments';
    var PATH_ATT_TYPES = contextPath + PATH_REST_ATTACHMENTS + "/types/";

    var loadAttachmentsTypes = function (grantProgramId) {
        return promise = $http.get(PATH_ATT_TYPES + grantProgramId);
    };

    return {
        loadAttachmentsTypes: loadAttachmentsTypes
    }
});

