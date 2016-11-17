<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Sodexo Gryf</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/angular-toastr.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gryf-ti-ind.css">
    <link rel="shortcut icon" href="${cdnUrl}favicon.ico">

    <script>
        var contextPath = "${pageContext.request.contextPath}";
        var xsrf = "${_csrf.token}";
        var cdn = "${cdnUrl}";
        var resourcesUrl = "${resourcesUrl}";
        var jsUrl = "${jsUrl}";
        var templatesUrl = "${templatesUrl}";
        var privileges = ${privileges};
        var login = '${login}';
        var attachmentMaxSize = '${attachmentMaxSize}';
        var resultLimit = {"grantApplications": 15};
        var sessionTimeoutInMs = ${sessionTimeout};
    </script>

    <script src="${pageContext.request.contextPath}/js/gryf-sessionStorage.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-route.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-animate.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.3.2/angular-ui-router.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-load.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-input-masks-standalone.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-toastr.tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/ng-file-upload.min.js"></script>

    <script src="${pageContext.request.contextPath}/js/components/gryf-config.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf-components.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf-ti-menu.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/dictionaryService/dictionaryService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/userService/userService.js"></script>

    <script src="${pageContext.request.contextPath}/js/dashboard/gryf-dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard/dashboardServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard/dashboardControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingReservation/gryf-trainingReservation.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingReservation/trainingReservationControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingReservation/trainingReservationServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingReservation/reservationModal/reservationModalControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/reimbursement/gryf-reimbursement.js"></script>
    <script src="${pageContext.request.contextPath}/js/reimbursement/reimbursementControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/reimbursement/reimbursementServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingtoreimburse/gryf-trainingToReimburse.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingtoreimburse/trainingToReimburseControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingtoreimburse/trainingToReimburseServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/trainingSearch/trainingSearchService.js"></script>
    <script src="${pageContext.request.contextPath}/js/ourTrainings/gryf-ourTrainings.js"></script>
    <script src="${pageContext.request.contextPath}/js/ourTrainings/ourTrainingsController.js"></script>
</head>
<body ng-app="gryf.ti">
<div class="container">

<div class="header">
    <div class="grid">
        <image class="left" src="${pageContext.request.contextPath}/images/header_01.jpg" />
        <image class="right" src="${pageContext.request.contextPath}/images/header_02.jpg" />
    </div>
</div>

<ul id="menu" class="navbar grid" ng-controller="MenuController">
    <li ng-class="{'active': isActive('dashboard')}"><a ui-sref="dashboard">Pulpit</a></li>
    <li ng-class="{'active': isActive('trainingReservation')}"><a ui-sref="trainingReservation">Rezerwuj szkolenie</a></li>
    <li ng-class="{'active': isActive('/v3')}"><a ng-href="${pageContext.request.contextPath}/page2">Zatwierdź PIN Uczestnika</a></li>
    <li ng-class="{'active': isActive('reimbursements')}"><a ui-sref="reimbursements">Rozliczenia i korekty</a>
    <ul>
        <li ng-class="{'active': isActive('trainingToReimburse')}">
            <a ui-sref="trainingToReimburse">Szkolenia do rozliczenia</a>
        </li>
        <li ng-class="{'active': isActive('reimbursements')}">
            <a ui-sref="reimbursements">Rozliczenia</a>
        </li>
    </ul>
    </li>
    <li ng-class="{'active': isActive('/v5')}"><a ng-href="${pageContext.request.contextPath}/page4">Anuluj rezerwację</a></li>
    <li ng-class="{'active': isActive('ourTrainings')}"><a ui-sref="ourTrainings">Nasze szkolenia</a></li>
    <li><a ng-href="${pageContext.request.contextPath}/logout">Wyloguj</a></li>
</ul>
<hr class="grid navbar-separator"/>

<div class="content grid" ui-view></div>

<div class="footer">
    <div class="grid">
        <hr class="footer-separator"/>
        <span class="footer-elem">
            <image class="left" src="${pageContext.request.contextPath}/images/logo_01.jpg" />
        </span>
        <span class="footer-elem">
            <image src="${pageContext.request.contextPath}/images/logo_02.jpg" />
        </span>
        <span class="footer-elem">
            <image class="right" src="${pageContext.request.contextPath}/images/logo_03.jpg" />
        </span>
    </div>
</div>

</div>
</body>
</html>