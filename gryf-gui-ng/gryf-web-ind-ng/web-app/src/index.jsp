<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope class="gryf-web-ind ind-index">
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

    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/fangular-animate.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap-tpls.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-ui-router.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-resource.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-toastr.tpls.min.ts"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-locale_pl-pl.min.ts"></script>

    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-resizeTable.ts"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-config.ts"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-components.ts"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-sessionStorage.ts"></script>
    <script src="${pageContext.request.contextPath}/js/app.ts"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-menu.ts"></script>

    <script src="${pageContext.request.contextPath}/js/app/dashboard/gryf-dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/js/app/dashboard/individualUserController.ts"></script>
    <script src="${pageContext.request.contextPath}/js/app/dashboard/individualUserService.ts"></script>
    <script src="${pageContext.request.contextPath}/js/app/dashboard/modal/sendPinModalController.ts"></script>
    <script src="${pageContext.request.contextPath}/js/app/help/gryf-help.ts"></script>

    <link rel="apple-touch-icon" sizes="180x180" href="${cdnUrl}img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${cdnUrl}img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="${cdnUrl}img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="${cdnUrl}img/manifest.json">
    <link rel="mask-icon" href="${cdnUrl}img/safari-pinned-tab.svg" color="#5bbad5">
    <link rel="shortcut icon" href="${cdnUrl}img/favicon.ico">
    <meta name="msapplication-config" content="${cdnUrl}img/browserconfig.xml">
    <meta name="theme-color" content="#ffffff">
</head>

<body id="ak" ng-app="gryf.ind" ng-strict-di>
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
                <li><a role="button" tabindex="4" href="#font-normal" title="ustaw małą czcionkę" rel="font" class="font-normal current">A</a></li>
                <li><a role="button" tabindex="5" href="#font-medium" title="ustaw średnią czcionkę" rel="font" class="font-medium">A</a></li>
                <li><a role="button" tabindex="6" href="#font-big" title="ustaw dużą czcionkę" rel="font" class="font-big">A</a></li>
                <li><a role="button" tabindex="7" href="#high-contrast" title="zmień kontrast" rel="contrast" class="contrast-high">zmień kontrast</a></li>
            </ol>
        </div>
    </div>
    <div style="clear:both;"></div>
    <header>
        <span class="description">System bonów szkoleniowych realizowany jest na zlecenie</span>
        <div class="top-logos">
            <a href="${pageContext.request.contextPath}/" target="_blank"    title="Logo wojewódzkiego Urzędu Pracy w Krakowie">
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

    <nav>
        <div class="grid">
            <ul id="menu" ng-controller="MenuController">
                <li tabindex="1" role="button" ng-class="{'current': isActive('dashboard')}"><a ui-sref="dashboard" title="Kierunek Kariera">Kierunek Kariera</a></li>
                <li tabindex="2" role="button" ng-class="{'current': isActive('help')}"><a ui-sref="help" title="Pomoc">Pomoc</a></li>
                <li tabindex="3" role="button" ><a onclick="document.getElementById('logoutForm').submit();" title="Pomoc">Wyloguj</a>
                    <form id="logoutForm" action="${pageContext.request.contextPath}/logout" method="POST">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </li>
            </ul>
        </div>
    </nav>

    <div id="content" class="content grid" ui-view></div>

    <div id="logobar">
        <div class="grid">

            <%--<a href="http://www.funduszeeuropejskie.gov.pl/" rel="external" title="Fundusze Europejskie - Program Regionalny">--%>
                <img src="${cdnUrl}img/fu_logo.gif" alt="Logo Programu Regionalnego Fundusze Europejskie z logiem przedstawiającym na niebieskim tle trzy gwiazdki w kolorze zółtym, czerwonym i białym">
            <%--</a>--%>

            <%--<a href="http://www.malopolska.pl/" rel="external" title="Rzeczpospolita Polska">--%>
                <img src="${cdnUrl}img/rp_flag.jpg" alt="Flaga Rzeczpospolitej Polskiej, biało-czerwona flaga po lewej stronie, po prawej napis Rzeczpospolita Polska">
            <%--</a>--%>

            <%--<a href="http://www.malopolska.pl/" rel="external" title="Małopolska">--%>
                <img src="${cdnUrl}img/malo_hor_logo.gif" alt="Logo województwa małopolskiego, znak gór wykonany z fioletowych, niebieskich zielonych i zółtych prostokątów nachodzących na siebie tworzących podobiznę górskich szczytów">
            <%--</a>--%>

            <%--<a href="http://www.funduszeeuropejskie.gov.pl/efs/" rel="external" title="Unia Europejska - Europejski Fundusz Społeczny">--%>
                <img src="${cdnUrl}img/eu_efs_logo.gif" alt="Napis Unia Europejska, Europejski Fundusz Społeczny po prawej, po lewej flaga uni europejskiej - złote gwiazdki na ciemnoniebieskim tle">
            <%--</a>--%>

        </div>
    </div>

    <footer>
        <div class="grid">
            <ul>
                <li>&copy; <span itemprop="copyrightYear">2017</span>&nbsp;<a target="_blank" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external" itemprop="copyrightHolder" href="http://www.sodexo.pl/">Sodexo Benefits and Rewards Services Polska Sp.&nbsp;z&nbsp;o.o.</a></li>
            </ul>

            <ol>
                <li><span>Operator Finansowy: </span><a target="_blank" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o. | ##BUILD_NUMBER" rel="external" href="http://www.sodexo.pl/">Sodexo</a></li>
            </ol>

        </div>
    </footer>

    <script src="${cdnUrl}js/gryf.js"></script>
    <script src="//code.angularjs.org/1.0.8/i18n/angular-locale_pl-pl.js"></script>

</body></html>