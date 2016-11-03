<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Sodexo Gryf</title>

    <link rel="stylesheet" href="${cdnUrl}gryf/css/init.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/angular-toastr.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gryf-custom.css">
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
        var resultLimit = {
            "grantApplications": 15
        };
        var sessionTimeoutInMs = ${sessionTimeout};
    </script>

    <script src="${pageContext.request.contextPath}/js/gryf-sessionStorage.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-route.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-animate.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-load.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-input-masks-standalone.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/angular-toastr.tpls.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf-components.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/ng-file-upload.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/components/gryf-menu.js"></script>
</head>
<body>
<section id="timeoutBox">
    <p>Twoja sesja wygaśnie za <span id="timerCounter">60</span> sekund.</p>

    <p><span id="prolongSession">Przedłuż sesję</span></p>
</section>
<div id="breadcrumbs">
    <div class="grid">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}" title="Sodexo Gryf">Sodexo Gryf</a>
            </li>
            <c:forEach items="${breadcrumbs}" var="item">
                <c:choose>
                    <c:when test="${not empty item.url}">
                        <li><a href="${pageContext.request.contextPath}/${item.url}"
                               title="${item.name}">${item.name}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><span>${item.name}</span></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
        <ol>
            <li class="welcome">Witaj <span>${login}</span></li>
            <li class=" "><a href="${pageContext.request.contextPath}/logout" title="Sodexo Group">wyloguj
                się</a></li>
        </ol>
    </div>
</div>

<nav>
    <div class="grid">
        <ul id="menu" ng-controller="MenuController" keypress-events>
            <li class="home"><a title="start" href="${pageContext.request.contextPath}/">start</a>
            </li>
            <li>
                <a>OBoSz</a>
                <ul>
                    <li class="submenu">
                        <a
                                ng-href="${pageContext.request.contextPath}/publicBenefits/enterprises/"
                                gryf-link-privilege="GRF_ENTERPRISES">MŚP</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/enterprises/"
                                   gryf-link-privilege="GRF_ENTERPRISES">Lista MŚP
                                </a></li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/enterprises/#modify"
                                   gryf-link-privilege="GRF_ENTERPRISE_MOD">Nowe MŚP</a></li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a
                                ng-href="${pageContext.request.contextPath}/publicBenefits/individuals/"
                                gryf-link-privilege="GRF_INDIVIDUALS">Osoby fizyczne</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/individuals/"
                                   gryf-link-privilege="GRF_ENTERPRISES">Lista osób fizycznych
                                </a></li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/individuals/#modify"
                                   gryf-link-privilege="GRF_ENTERPRISE_MOD">Nowa osoby fizyczna</a></li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a
                                ng-href="${pageContext.request.contextPath}/publicBenefits/trainingInstitutions/"
                                gryf-link-privilege="GRF_TRAINING_INSTITUTIONS">IS</a>
                        <ul>
                            <li>
                                <a
                                        ng-href="${pageContext.request.contextPath}/publicBenefits/trainingInstitutions/"
                                        gryf-link-privilege="GRF_TRAINING_INSTITUTIONS">
                                    Lista IS</small>
                                </a>
                            </li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/trainingInstitutions/#modify"
                                   gryf-link-privilege="GRF_TRAINING_INSTITUTION_MOD">
                                    Nowe IS
                                </a>
                            </li>
                            <li class="submenu">
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/training/"
                                   gryf-link-privilege="GRF_PBE_TI_TRAININGS">
                                    Katalog szkoleń
                                </a>
                                <ul>
                                    <li>
                                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/training/"
                                                gryf-link-privilege="GRF_PBE_TI_TRAININGS">Lista szkoleń</a>
                                    <li>
                                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/training/#/modify"
                                           gryf-link-privilege="GRF_PBE_TI_TRAININGS_MOD">Nowe szkolenie</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/grantApplications/"
                           gryf-link-privilege="GRF_PBE_APPLICATIONS">Wnioski</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/grantApplications/"
                                   gryf-link-privilege="GRF_PBE_APPLICATIONS">Lista wniosków</a>
                            </li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/grantApplications/#modify"
                                   gryf-link-privilege="GRF_PBE_APPLICATION_MOD">
                                    Nowy wniosek
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/agreements/"
                           gryf-link-privilege="GRF_PBE_APPLICATIONS">Umowy</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/agreements/"
                                   gryf-link-privilege="GRF_PBE_APPLICATIONS">Lista umów</a>
                            </li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/agreements/#modify"
                                   gryf-link-privilege="GRF_PBE_APPLICATION_MOD">
                                    Nowa umowa
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/orders/"
                           gryf-link-privilege="GRF_PBE_ORDERS">Zamówienia</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/orders/"
                                   gryf-link-privilege="GRF_PBE_ORDERS">Lista zamówień</a>
                            </li>
                        </ul>
                    </li>
                    <li class="submenu">
                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#searchReimbursements"
                           gryf-link-privilege="GRF_PBE_REIMB">Rozliczenia</a>
                        <ul>
                            <li class="submenu">
                                <a
                                        ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#searchDelivery"
                                        gryf-link-privilege="GRF_PBE_DELIVERIES">Dostawa</a>
                                <ul>
                                    <li>
                                        <a ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#searchDelivery"
                                           gryf-link-privilege="GRF_PBE_DELIVERIES">
                                            Lista dostaw
                                            <small class="right">(alt+d)</small>
                                        </a>
                                    </li>
                                    <li><a
                                            ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#registerDelivery"
                                            gryf-link-privilege="GRF_PBE_DELIVERIES_MOD">
                                        Rejestracja dost.
                                        <small class="right">(alt+r)</small>
                                    </a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#searchReimbursements"
                                   gryf-link-privilege="GRF_PBE_REIMB">Lista rozliczeń</a>
                            </li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/publicBenefits/reimbursements/#announceReimbursements"
                                   gryf-link-privilege="GRF_PBE_REIMB_ANON">Anonsuj rozliczenie</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li>
                <a title="słowniki">Słowniki</a>
                <ul>
                    <li class="submenu">
                        <a ng-href="${pageContext.request.contextPath}/dictionaries/zipCodes/#"
                           gryf-link-privilege="GRF_ZIP_CODES">Kody pocztowe</a>
                        <ul>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/dictionaries/zipCodes/"
                                   gryf-link-privilege="GRF_ZIP_CODES">Lista kodów</a></li>
                            <li>
                                <a ng-href="${pageContext.request.contextPath}/dictionaries/zipCodes/#zipCode"
                                   gryf-link-privilege="GRF_ZIP_CODES_MOD">Nowy kod</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a title="administracja">Administracja</a>
                <ul>
                    <li >
                        <%--TODO uprawnienia--%>
                        <a ng-href="${pageContext.request.contextPath}/administration/generatePrintNumbers/#"
                           gryf-link-privilege="GRF_ZIP_CODES">Generacja numerów dla bonów</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</nav>

<div id="content">
    <div class="grid">
        <br/><br/>
        <jsp:include page="${pageMainContent}"/>
    </div>
</div>


<footer>
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
</footer>

</body>
</html>