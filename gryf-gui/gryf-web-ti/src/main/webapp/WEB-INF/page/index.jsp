<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope class="gryf-web-ti ti-index">

<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</title>

    <!--deployed-->

    <%--<link rel="stylesheet" href="${cdnUrl}css/gryf.css">--%>
    <%-- GRYF DEV CSS--%>
    <link rel="stylesheet" href="${cdnUrl}css/gryf-dev.css?v=##CSS_VERSION">

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
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-resizeTable.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular.js"></script>
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
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-locale_pl-pl.min.js"></script>

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
    <script src="${pageContext.request.contextPath}/js/app/help/gryf-help.js"></script>

    <meta name="description" content="Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie">
    <meta property="og:description" content="Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie">
    <meta name="keywords" content="Kariera, Małopolska, Kraków, Urząd Pracy, Praca">
    <meta property="og:title" content="System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie">
    <meta name="application-name" content="System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie">
    <meta property="og:site_name" content="System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie">
    <meta property="og:type" content="website">
    <meta name="author" content="Sodexo Benefits and Rewards Services Polska Sp. z o.o.">
    <meta property="og:image" content="${cdnUrl}img/favicon.png">
    <link rel="apple-touch-icon" sizes="180x180" href="${cdnUrl}img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${cdnUrl}img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="${cdnUrl}img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="${cdnUrl}img/manifest.json">
    <link rel="mask-icon" href="${cdnUrl}img/safari-pinned-tab.svg" color="#5bbad5">
    <link rel="shortcut icon" href="${cdnUrl}img/favicon.ico">
    <meta name="msapplication-config" content="${cdnUrl}img/browserconfig.xml">
    <meta name="theme-color" content="#ffffff">

</head>
<body id="ak" ng-app="gryf.ti" ng-strict-di>
    <style>
        #timeoutBox {
            position: fixed;
            top: 0;
            right: 40%;
            padding: 30px;
            width: 350px;
            background-color: #FF9400;
            color: #ffffff;
            text-align: center;
            z-index: 100000;
            font-size: 14px;
            margin: 0;
            opacity: 0;
            visibility: hidden;
            -webkit-transition: all 0.5s;
            -moz-transition: all 0.5s;
            -ms-transition: all 0.5s;
            -o-transition: all 0.5s;
            transition: all 0.5s;
            -webkit-box-shadow: 0 0 10px 1px rgba(148,148,148,1);
            -moz-box-shadow: 0 0 10px 1px rgba(148,148,148,1);
            box-shadow: 0 0 10px 1px rgba(148,148,148,1);
        }
    </style>
    <section id="timeoutBox">
        <p>Twoja sesja wygaśnie za <span id="timerCounter">60</span> sekund.</p>

        <p><span id="prolongSession">Przedłuż sesję</span></p>
    </section>
    <div id="accessbility">
        <div class="grid">
            <ol class="font">
                <li><a role="button" href="#font-normal" title="ustaw małą czcionkę" rel="font" class="font-normal current">A</a></li>
                <li><a role="button" href="#font-medium" title="ustaw średnią czcionkę" rel="font" class="font-medium">A</a></li>
                <li><a role="button" href="#font-big" title="ustaw dużą czcionkę" rel="font" class="font-big">A</a></li>
                <li><a role="button" href="#high-contrast" title="zmień kontrast" rel="contrast" class="contrast-high">zmień kontrast</a></li>
            </ol>
        </div>
    </div>
    <div style="clear:both"></div>
    <header>
        <span class="description">System bonów szkoleniowych realizowany jest na zlecenie</span>
        <div class="top-logos">
            <a href="${pageContext.request.contextPath}/" target="_blank" title="Logo wojewódzkiego Urzędu Pracy w Krakowie">
                <img src="${cdnUrl}img/wup_logo.gif" alt="Logo wojewódzkiego Urzędu Pracy w Krakowie z szarym konturem człowiek i zieloną strzałką pod nim, wskaruzującym na napisz Wojewódzki Urząd Pracy w Krakowie, po lewej napis Instytucja Województwa Małopolskiego">
            </a>
            <a href="https://www.malopolska.pl/" target="_blank" title="Logo województwa małopolskiego">
                <img src="${cdnUrl}img/malopolska_logo.gif" alt="Logo województwa małopolskiego, znak gór wykonany z fioletowych, niebieskich zielonych i zółtych prostokątów nachodzących na siebie tworzących podobiznę górskich szczytów">
            </a>
            <a href="http://europa.eu/european-union/index_pl" target="_blank" title="Flaga Uni Europejskiej">
                <img src="${cdnUrl}img/eu_flag.gif" alt="Flaga Uni Europejskiej, ciemnoniebieskie tło z żółtymi gwiadkami i podpsiem podspodem Unia Europejska">
            </a>
        </div>
    </header>

    <nav><div class="grid">
        <ul id="menu" ng-controller="MenuController">
            <li  ng-class="{'current': isActive('dashboard')}"><a ui-sref="dashboard" title="Pulpit">Pulpit</a></li>
            <li  ng-class="{'current': isActive('trainingReservation')}"><a ui-sref="trainingReservation" title="Rezerwuj usługę">Rezerwuj usługę</a></li>
            <li  ng-class="{'current': isActive('confirmPin')}"><a ui-sref="confirmPin" title="Zatwierdź PIN Uczestnika">Zatwierdź PIN Uczestnika</a></li>
            <li  class="submenu" ng-class="{'current': (isActive('trainingToReimburse') || isActive('reimbursements'))}">
                <a ui-sref="trainingToReimburse" title="Rozliczenia">Rozliczenia</a>
                <ul>
                    <li  ng-class="{'current': isActive('trainingToReimburse')}">
                        <a ui-sref="trainingToReimburse" title="Usługi do rozliczenia">Usługi do rozliczenia</a>
                    </li>
                    <li  ng-class="{'current': isActive('reimbursements')}">
                        <a ui-sref="reimbursements" title="Rozliczenia i korekty">Rozliczenia i korekty</a>
                    </li>
                </ul>
            </li>
            <li  ng-class="{'current': isActive('cancelReservation')}"><a ui-sref="cancelReservation" title="Anuluj rezerwację">Anuluj rezerwację</a></li>
            <li  ng-class="{'current': isActive('ourTrainings')}"><a ui-sref="ourTrainings" title="Nasze usługi">Nasze usługi</a></li>
            <li  ng-class="{'current': isActive('help')}"><a ui-sref="help" title="Pomoc">Pomoc</a></li>
            <li  ><a onclick="document.getElementById('logoutForm').submit();" title="Pomoc">Wyloguj</a>
                <form id="logoutForm" action="${pageContext.request.contextPath}/logout" method="POST">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </li>
        </ul>
    </div></nav>

    <div id="content" class="content grid" ui-view></div>

    <div id="logobar">
        <div class="grid">
            <img src="${cdnUrl}img/fu_logo.gif" alt="Logo Programu Regionalnego Fundusze Europejskie z logiem przedstawiającym na niebieskim tle trzy gwiazdki w kolorze zółtym, czerwonym i białym">
            <img src="${cdnUrl}img/rp_flag.jpg" alt="Flaga Rzczypospolitej Polskiej, biało-czerwona flaga po lewej stronie, po prawej napis Rzeczypospolita Polska">
            <img src="${cdnUrl}img/malo_hor_logo.gif" alt="Logo województwa małopolskiego, znak gór wykonany z fioletowych, niebieskich zielonych i zółtych prostokątów nachodzących na siebie tworzących podobiznę górskich szczytów">
            <img src="${cdnUrl}img/eu_efs_logo.gif" alt="Napis Unia Europejska, Europejski Fundusz Społeczny po prawej, po lewej faga uni europejskiej - złote gwiazdki na ciemnoniebieskim tle">
        </div>
    </div>

    <footer><div class="grid">

        <ul>
            <li>&copy; <span itemprop="copyrightYear">2017</span>&nbsp;<a target="_blank" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external" itemprop="copyrightHolder" href="http://www.sodexo.pl/">Sodexo Benefits and Rewards Services Polska Sp.&nbsp;z&nbsp;o.o.</a></li>
        </ul>

        <ol>
            <li><span>Operator Finansowy: </span><a target="_blank" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o. | ##BUILD_NUMBER" rel="external" href="http://www.sodexo.pl/">Sodexo</a></li>
        </ol>

    </div></footer>

    <script src="${cdnUrl}js/gryf.js"></script>

</body></html>