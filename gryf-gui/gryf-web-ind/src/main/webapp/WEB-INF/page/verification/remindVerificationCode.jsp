<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope>

<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>System bonów szkoleniowych - Kierunek Kariera - Wojewódzki Urząd Pracy w Krakowie</title>

    <link rel="stylesheet" href="${cdnUrl}css/gryf.css">

    <link href="//fonts.googleapis.com/css?family=Roboto:300,400,700&amp;subset=latin-ext" rel="stylesheet">

    <meta property="og:url" content="http://">
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

<script>
    //TODO: dołączyć inne biblioteki i z nich skorzystać
    function validateEmail(email) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }

    function validatePesel(pesel) {
        var reg = /^[0-9]{11}$/;
        if (reg.test(pesel) == false) {
            return false;
        }
        else {
            var dig = ("" + pesel).split("");
            var checksum = (1 * parseInt(dig[0]) + 3 * parseInt(dig[1]) + 7 * parseInt(dig[2]) + 9 * parseInt(dig[3]) + 1 * parseInt(dig[4]) + 3 * parseInt(dig[5]) + 7 * parseInt(dig[6]) + 9 * parseInt(dig[7]) + 1 * parseInt(dig[8]) + 3 * parseInt(dig[9])) % 10;
            if (checksum == 0) checksum = 10;
            checksum = 10 - checksum;
            if (parseInt(dig[10]) == checksum) {
                return true;
            } else {
                return false;
            }
        }

    }

    function createErrorMessageAsChildNode(message, parentNodeId){
        if(document.getElementById(parentNodeId + 'Val') == null){
            var validationNode = document.createElement("span");
            var textNode = document.createTextNode(message);
            validationNode.style.color = 'red';
            validationNode.id = parentNodeId + 'Val';
            validationNode.appendChild(textNode);
            var parentNode = document.getElementById(parentNodeId);
            parentNode.appendChild(validationNode);
        }
    }

    function disableErrorMessage(parentNodeId){
        var childNode = document.getElementById(parentNodeId + 'Val');
        if(childNode != null){
            var parentNode = document.getElementById(parentNodeId);
            parentNode.removeChild();
        }
    }

    function validate() {
        var email = document.getElementById("email");
        var pesel = document.getElementById("pesel");
        var formValid = true;

        if (!validatePesel(pesel.value)) {
            createErrorMessageAsChildNode('Niepoprawna wartość pesel', 'peselControl');
            pesel.style.borderColor = 'red';
            formValid = false;
        } else {
            disableErrorMessage('peselControl');
            pesel.style.borderColor = '';
        }

        if (!validateEmail(email.value)) {
            createErrorMessageAsChildNode('Niepoprawna wartość email', 'emailControl');
            email.style.borderColor = 'red';
            formValid = false;
        } else {
            disableErrorMessage('emailControl');
            email.style.borderColor = '';
        }

        return formValid;
    }
</script>

<body>
<body id="ak">

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
    <ul>
        <li><a href="${pageContext.request.contextPath}" title="Panel Logowania">Panel Logowania</a></li>
    </ul>
</div></nav>

<div id="content" class="page-login"><div class="grid">

    <section class="form form-big form-login">

        <div class="content">

            <form name="verificationForm" action="${pageContext.request.contextPath}/verification/resend" method="POST" onsubmit="return validate()">

                <h3 itemprop="headline name" class="headline name">Prześlij ponownie kod weryfikacyjny</h3>

                <p itemprop="description" class="description">Prosimy, wprowadź dane uwierzytelniające.</p>

                <c:if test="${error != null}">
                    <div class="msg msg-error"><p><c:out value="${error.message}"/></p></div>
                </c:if>

                <c:if test="${unknownerror != null}">
                    <div class="msg msg-error"><p>Wystąpił niespodziewany błąd po stronie serwera. Prosimy spróbować później</p></div>
                </c:if>

                <div class="field field-string">
                    <div class="label">
                        <label for="pesel">Wprowadź PESEL</label>
                    </div>
                    <div id="peselControl" class="control">
                        <input id="pesel" type="text" name="pesel" value="" tabindex="1">
                    </div>
                </div>

                <div class="field field-string">
                    <div class="label">
                        <label for="email">oraz e-mail</label>
                    </div>
                    <div id="emailControl" class="control">
                        <input id="email" type="text" name="email" tabindex="2">
                    </div>
                </div>

                <div class="field field-submit">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="button" tabindex="3" data-test="__login__submit">Prześlij kod</button>
                </div>

            </form>
        </div>
    </section>
</div></div>

<div id="bottombar"><div class="grid">
    <h2>Telefoniczne Biuro Obsługi Klienta</h2>
    <h3 class="phone">+48 00 000 00 00</h3>
    <a class="mail" href="mailto:szkolenia@wup-krakow.pl" title="szkolenia@wup-krakow.pl">szkolenia@wup-krakow.pl</a>
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

</body></html>