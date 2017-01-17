"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("ourTrainings", {
        url: "/ourTrainings",
        templateUrl: contextPath + "/templates/ourtrainings/ourTrainings.html",
        controller: "OurTrainingsController"
    }),
    $stateProvider.state('ourTrainings.trainingDetails', {
        parent: 'ourTrainings',
        url: '/trainingDetails/{trainingId}',
        onEnter: ['$state', '$modal', function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/sharedModals/trainingDetailsModal.html",
                size: "md",
                controller: "TrainingDetailsModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    });
}]);