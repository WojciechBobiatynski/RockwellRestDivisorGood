<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope class="gryf-web-ti ti-login">

<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</title>

    <link rel="stylesheet" href="${cdnUrl}css/gryf.css">

    <link href="//fonts.googleapis.com/css?family=Roboto:300,400,700&amp;subset=latin-ext" rel="stylesheet">

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

<body id="ak">

<div id="accessbility"><div class="grid">
    <ol class="font">
        <li><a href="#font-normal" title="ustaw małą czcionkę" rel="font" class="font-normal current">A</a></li>
        <li><a href="#font-medium" title="ustaw średnią czcionkę" rel="font" class="font-medium">A</a></li>
        <li><a href="#font-big" title="ustaw dużą czcionkę" rel="font" class="font-big">A</a></li>
        <li><a href="#high-contrast" title="zmień kontrast" rel="contrast" class="contrast-high">zmień kontrast</a></li>
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

<nav>

        <c:choose>
            <c:when test="${loggedIn}">
                <div class="grid">
                    <ul id="menu">
                        <li><a href="${pageContext.request.contextPath}/#/dashboard" ui-sref="dashboard" title="Pulpit">Pulpit</a></li>
                        <li><a href="${pageContext.request.contextPath}/#/trainingReservation" ui-sref="trainingReservation" title="Rezerwuj usługę">Rezerwuj usługę</a></li>
                        <li><a href="${pageContext.request.contextPath}/#/confirmPin" ui-sref="confirmPin" title="Zatwierdź PIN Uczestnika">Zatwierdź PIN Uczestnika</a></li>
                        <li class="submenu">
                            <a ui-sref="trainingToReimburse" title="Rozliczenia">Rozliczenia</a>
                            <ul>
                                <li>
                                    <a href="${pageContext.request.contextPath}/#/trainingToReimburse" ui-sref="trainingToReimburse" title="Usługi do rozliczenia">Usługi do rozliczenia</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/#/reimbursements" ui-sref="reimbursements" title="Rozliczenia i korekty">Rozliczenia i korekty</a>
                                </li>
                            </ul>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/#/cancelReservation" ui-sref="cancelReservation" title="Anuluj rezerwację">Anuluj rezerwację</a></li>
                        <li><a href="${pageContext.request.contextPath}/#/ourTrainings" ui-sref="ourTrainings" title="Nasze usługi">Nasze usługi</a></li>
                        <li><a href="${pageContext.request.contextPath}/#/help" ui-sref="help" title="Pomoc">Pomoc</a></li>
                        <li><a href="${pageContext.request.contextPath}/logout" title="Wyloguj">Wyloguj</a></li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid">
                    <ul>
                        <li><a href="${pageContext.request.contextPath}" title="Panel Logowania">Panel Logowania</a></li>
                        <li><a href="${pageContext.request.contextPath}/help" class="current" title="Pomoc">Pomoc</a></li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<div id="content" class="page-error"><div class="grid">
    <p>
        <c:choose>
            <c:when test="${param['code'] == 403}">Dostęp do strony jest zabroniony</c:when>
            <c:when test="${param['code'] == 404}">Nie znaleziono danej strony</c:when>
            <c:when test="${param['code'] == 500}">Wystapił krytyczny błąd</c:when>
            <c:otherwise>Wystąpił nieznany błąd</c:otherwise>
        </c:choose>
    </p>
    <ul>
      <li><a class="button" href="${pageContext.request.contextPath}" title="wróć do strony głównej">wróć do strony głównej</a><li>
      <li><a class="button" href="javascript:window.history.back();" title="wróć do poprzedniej strony">wróć do poprzedniej strony</a></li>
    </ul>
</div></div>

<div id="bottombar"><div class="grid">
    <h2>Telefoniczne Biuro Obsługi Klienta</h2>
    <h3 class="phone">+48 22 346 75 05</h3>
    <a class="mail" href="mailto:tbok.kk@sodexo.com" title="tbok.kk@sodexo.com">tbok.kk@sodexo.com</a>
    <p class="description">Wszystkie informacje o projekcie pn. „Kierunek Kariera” dostępne są na stronie<br><a href="http://www.pociagdokariery.pl" rel="external" title="Pociąg do Kariery">www.pociagdokariery.pl</a></p>
</div></div>

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