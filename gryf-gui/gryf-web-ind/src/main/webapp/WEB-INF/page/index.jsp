<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope>
<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/cdn/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/cdn/css/participant.css">
    <%-- odkomentowa� i podmieni�, gdy zasoby powy�ej b�d� na cdn --%>
    <%-- <link rel="stylesheet" href="${cdnUrl}gryf-ti/css/reset.css"> --%>
    <%-- <link rel="stylesheet" href="${cdnUrl}gryf-ti/css/participant.css"> --%>

    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,700&amp;subset=latin-ext" rel="stylesheet">

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

    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-animate.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-ui-router.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-resource.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vendor/angular/angular-toastr.tpls.min.js"></script>

    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-config.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-components.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-menu.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-sessionStorage.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/components/individualuser/gryf-individualUser.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/individualUserController.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/individualUserService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/modal/sendPinModalController.js"></script>

    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/assets/cdn/img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/cdn/img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/cdn/img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="${pageContext.request.contextPath}/assets/cdn/img/manifest.json">
    <link rel="mask-icon" href="${pageContext.request.contextPath}/assets/cdn/img/safari-pinned-tab.svg" color="#5bbad5">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/cdn/img/favicon.ico">
    <meta name="msapplication-config" content="${pageContext.request.contextPath}/assets/cdn/img/browserconfig.xml">
    <%-- odkomentowa� i podmieni�, gdy zasoby powy�ej b�d� na cdn --%>
    <%-- <link rel="apple-touch-icon" sizes="180x180" href="${cdnUrl}gryf-ti/img/apple-touch-icon.png"> --%>
    <%-- <link rel="icon" type="image/png" href="${cdnUrl}gryf-ti/img/favicon-32x32.png" sizes="32x32"> --%>
    <%-- <link rel="icon" type="image/png" href="${cdnUrl}gryf-ti/img/favicon-16x16.png" sizes="16x16"> --%>
    <%-- <link rel="manifest" href="${cdnUrl}gryf-ti/img/manifest.json"> --%>
    <%-- <link rel="mask-icon" href="${cdnUrl}gryf-ti/img/safari-pinned-tab.svg" color="#5bbad5"> --%>
    <%-- <link rel="shortcut icon" href="${cdnUrl}gryf-ti/img/favicon.ico"> --%>
    <%-- <meta name="msapplication-config" content="${cdnUrl}gryf-ti/img/browserconfig.xml"> --%>

    <meta name="theme-color" content="#ffffff">

<body id="ak" ng-app="gryf.ind">


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
                <a title="Instytucja Województwa Małopolskiego, Wojewódzki Urząd Pracy w Krakowie" rel="home" href="/" itemprop="name">Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</a>
            </h1>
        </div>
        <ol>
            <li class="ci ci-malopolska-v"><a href="http://www.malopolska.pl/" rel="external" title="Małopolska">Małopolska</a></li>
            <li class="ci ci-ue"><a href="http://europa.eu/european-union/index_pl" rel="external" title="Unia Europejska">Unia Europejska</a></li>
        </ol>
    </div></header>

    <nav><div class="grid">
        <ul>
            <li><a ng-href="${pageContext.request.contextPath}/" class="current" title="Kierunek Kariera">Kierunek Kariera</a></li>
            <li><a ng-href="${pageContext.request.contextPath}/logout" title="Wyloguj">Wyloguj</a></li>
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
            <li><span>Operator Finansowy: </span><a title="Sodexo Benefits and Rewards Services Polska Sp. z o.o. | ##BUILD_NUMBER" rel="external" href="http://www.sodexo.pl/">Sodexo</a></li>
        </ol>

    </div></footer>

</body>
</html>