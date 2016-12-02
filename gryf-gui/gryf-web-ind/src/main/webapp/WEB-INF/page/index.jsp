<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Sodexo Gryf</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/angular-toastr.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/gryf-ti-ind.css">
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

    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-animate.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.3.2/angular-ui-router.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.js"></script>

    <script src="${pageContext.request.contextPath}/assets/libraries/angular-toastr.tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-config.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-components.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-menu.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf/gryf-sessionStorage.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/components/individualuser/gryf-individualUser.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/individualUserController.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/individualUserService.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/individualuser/modal/sendPinModalController.js"></script>

<body ng-app="gryf.ind">
<div class="container">
    <div class="header">
        <div class="grid">
            <image class="left" src="${pageContext.request.contextPath}/assets/images/header_01.jpg" />
            <image class="right" src="${pageContext.request.contextPath}/assets/images/header_02.jpg" />
        </div>
    </div>

    <ul class="navbar grid">
        <li></li>
        <li class="blue"><a ng-href="${pageContext.request.contextPath}/logout">Wyloguj</a></li>
    </ul>

    <hr class="grid navbar-separator blue"/>
    <div class="content grid" ui-view></div>

    <div class="footer">
        <div class="grid">
            <hr class="footer-separator"/>
        <span class="footer-elem">
            <image class="left" src="${pageContext.request.contextPath}/assets/images/logo_01.jpg" />
        </span>
        <span class="footer-elem">
            <image src="${pageContext.request.contextPath}/assets/images/logo_02.jpg" />
        </span>
        <span class="footer-elem">
            <image class="right" src="${pageContext.request.contextPath}/assets/images/logo_03.jpg" />
        </span>
        </div>
        <div class="grid">
            <div class="copyright">
                <p>
                    &copy;
                    <span itemprop="copyrightYear">2015</span>
                    &nbsp;
                    <a title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external"
                       itemprop="copyrightHolder" href="http://www.sodexo.pl/">
                        Sodexo Benefits and Rewards Services Polska Sp.&nbsp;z&nbsp;o.o.
                    </a>
                    <br/>##BUILD_NUMBER
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>