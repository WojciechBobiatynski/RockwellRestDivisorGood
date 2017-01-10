angular.module("gryf.config").factory("AttachmentService", ["$http", "Upload", "GryfPopups", function ($http, Upload, GryfPopups) {
    var PATH_REST_ATTACHMENTS = '/rest/attachments';
    var PATH_ATT_TYPES = contextPath + PATH_REST_ATTACHMENTS + "/types/";

    var loadAttachmentsTypes = function (grantProgramId) {
        return promise = $http.get(PATH_ATT_TYPES + grantProgramId);
    };

    var saveAttachments = function(model, saveUrl) {
        var files = findAllFileAttachments(model);
        var promise = Upload.upload({
            url: saveUrl,
            data: angular.toJson(model),
            file: files
        });
        promise.success(function(response) {
            GryfPopups.setPopup("success", "Sukces", "Pliki zostały zapisane");
        })
        .error(function() {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać plików");
        })
        .finally(function() {
            GryfPopups.showPopup();
        });
        return promise;
    };

    function findAllFileAttachments(model) {
        var resultArray = [];
        for (var i = 0; i < model.attachments.length; i++) {
            var attachmentFileField = model.attachments[i].file;
            if (attachmentFileField) {
                if (attachmentFileField.length) {
                    // var file = angular.copy(attachments[i].file[0]);
                    resultArray.push(model.attachments[i].file[0]);
                    delete model.attachments[i].file;
                }
            }
        }
        return resultArray;
    }

    return {
        loadAttachmentsTypes: loadAttachmentsTypes,
        saveAttachments: saveAttachments
    }
}]);

