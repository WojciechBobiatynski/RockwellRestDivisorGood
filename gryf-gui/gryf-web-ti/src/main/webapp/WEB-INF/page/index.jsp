<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope class="gryf-web-ti ti-index">

<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</title>

    <!--deployed-->

    <link rel="stylesheet" href="${cdnUrl}css/gryf.css">

    <link href="//fonts.googleapis.com/css?family=Roboto:300,400,700&amp;subset=latin-ext" rel="stylesheet">

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

    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-sessionStorage.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-animate.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-ui-router.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-resource.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-load.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-input-masks-standalone.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-toastr.tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/ng-file-upload.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/mask.min.js"></script>

    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-config.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-components.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-ti-menu.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/dictionaryservice/dictionaryService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/userservice/userService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/attachment/attachmentService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/directives/attachmentDirective.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/traininginstancesearchservice/trainingInstanceSearchService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/trainingsearchservice/trainingSearchService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/sharedModals/trainingInstanceDetailsModalController.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/sharedModals/trainingDetailsModalController.js"></script>

    <script src="${pageContext.request.contextPath}/js/app/dashboard/gryf-dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/dashboard/dashboardServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/dashboard/dashboardControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingreservation/gryf-trainingReservation.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingreservation/trainingReservationControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingreservation/trainingReservationServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingreservation/modal/reservationModalControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/reimbursement/gryf-reimbursement.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/reimbursement/reimbursementControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/reimbursement/reimbursementServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingtoreimburse/gryf-trainingToReimburse.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingtoreimburse/trainingToReimburseControllers.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/trainingtoreimburse/trainingToReimburseServices.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/ourtrainings/gryf-ourTrainings.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/ourtrainings/ourTrainingsController.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/canceltrainingreservation/gryf-cancelTrainingReservation.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/canceltrainingreservation/cancelTrainingReservationController.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/canceltrainingreservation/cancelTrainingReservationService.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/canceltrainingreservation/modal/cancelTrainingReservationModalController.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/confirmpin/gryf-confirmPin.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/confirmpin/confirmPinController.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/confirmpin/confirmPinService.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/confirmpin/modal/confirmPinModalController.js"></script>
</head>
<body id="ak" ng-app="gryf.ti" ng-strict-di>

    <div id="accessbility"><div class="grid">
        <ol class="font">
            <li><a href="#font-normal" title="ustaw małą czcionkę" class="current normal">A</a></li>
            <li><a href="#font-medium" title="ustaw średnią czcionkę" class="medium">A</a></li>
            <li><a href="#font-big" title="ustaw dużą czcionkę" class="big">A</a></li>
            <li><a href="#high-contrast" title="zmień kontrast" class="high-contrast">zmień kontrast</a></li>
        </ol>
    </div></div>

    <header><div class="grid">
        <div>
            <span class="description">System bonów szkoleniowych realizowany jest na zlecenie</span>
            <h1 class="ci ci-wup">
                <a title="Instytucja Województwa Małopolskiego, Wojewódzki Urząd Pracy w Krakowie" rel="home" href="${pageContext.request.contextPath}" itemprop="name">Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</a>
            </h1>
        </div>
        <ol>
            <li class="ci ci-malopolska-v"><a href="http://www.malopolska.pl/" rel="external" title="Małopolska">Małopolska</a></li>
            <li class="ci ci-ue"><a href="http://europa.eu/european-union/index_pl" rel="external" title="Unia Europejska">Unia Europejska</a></li>
        </ol>
    </div></header>

    <nav><div class="grid">
        <ul id="menu" ng-controller="MenuController">
            <li ng-class="{'current': isActive('dashboard')}"><a ui-sref="dashboard">Pulpit</a></li>
            <li ng-class="{'current': isActive('trainingReservation')}"><a ui-sref="trainingReservation">Rezerwuj szkolenie</a></li>
            <li ng-class="{'current': isActive('confirmPin')}"><a ui-sref="confirmPin">Zatwierdź PIN Uczestnika</a></li>
            <li class="submenu" ng-class="{'current': (isActive('trainingToReimburse') || isActive('reimbursements'))}">
                <a href="#">Rozliczenia</a>
                <ul>
                    <li ng-class="{'current': isActive('trainingToReimburse')}">
                        <a ui-sref="trainingToReimburse">Szkolenia do rozliczenia</a>
                    </li>
                    <li ng-class="{'current': isActive('reimbursements')}">
                        <a ui-sref="reimbursements">Rozliczenia i korekty</a>
                    </li>
                </ul>
            </li>
            <li ng-class="{'current': isActive('cancelReservation')}"><a ui-sref="cancelReservation">Anuluj rezerwację</a></li>
            <li ng-class="{'current': isActive('ourTrainings')}"><a ui-sref="ourTrainings">Nasze szkolenia</a></li>
            <li><a ng-href="${pageContext.request.contextPath}/logout">Wyloguj</a></li>
        </ul>
    </div></nav>

    <div id="content" class="content grid" ui-view></div>

    <div id="logobar"><div class="grid">
        <ul>
            <li class="ci ci-fepr"><a href="http://www.funduszeeuropejskie.gov.pl/" rel="external" title="Fundusze Europejskie - Program Regionalny">Fundusze Europejskie - Program Regionalny</a></li>
            <li class="ci ci-malopolska-h"><a href="http://www.malopolska.pl/" rel="external" title="Małopolska">Małopolska</a></li>
            <li class="ci ci-ueefs"><a href="http://www.funduszeeuropejskie.gov.pl/efs/" rel="external" title="Unia Europejska - Europejski Fundusz Społeczny">Unia Europejska - Europejski Fundusz Społeczny</a></li>
        </ul>
    </div></div>

    <footer><div class="grid">

        <ul>
            <li>&copy; <span itemprop="copyrightYear">2017</span>&nbsp;<a title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external" itemprop="copyrightHolder" href="http://www.sodexo.pl/">Sodexo Benefits and Rewards Services Polska Sp.&nbsp;z&nbsp;o.o.</a></li>
        </ul>

        <ol>
            <li><span>Operator Finansowy: </span><a title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external" href="http://www.sodexo.pl/">Sodexo</a></li>
        </ol>

    </div></footer>

    <script src="${cdnUrl}js/gryf.js"></script>

</body></html>