<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl" itemtype="http://schema.org/WebPage" itemscope class="gryf-web-ti ti-login">

<head>
    <meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>${cdnTitle}</title>

    <%--<link rel="stylesheet" href="${cdnUrl}css/gryf.css">--%>
    <%-- GRYF DEV CSS--%>
    <link rel="stylesheet" href="${cdnUrl}css/gryf-dev.css?v=##CSS_VERSION">

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

    <div id="accessbility">
        <div class="grid">
            <ol class="font">
                <li><a href="#font-normal" title="ustaw małą czcionkę" rel="font" class="font-normal current">A</a></li>
                <li><a href="#font-medium" title="ustaw średnią czcionkę" rel="font" class="font-medium">A</a></li>
                <li><a href="#font-big" title="ustaw dużą czcionkę" rel="font" class="font-big">A</a></li>
                <li><a href="#high-contrast" title="zmień kontrast" rel="contrast" class="contrast-high">zmień kontrast</a></li>
            </ol>
        </div>
    </div>
    <div style="clear: both;"></div>
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

    <nav>
        <div class="grid">
        <ul>
                <li><a href="#" class="current" title="Panel Logowania">Panel Logowania</a></li>
                <li><a href="${pageContext.request.contextPath}/help" class="" title="Pomoc">Pomoc</a></li>
            </ul>
        </div>
    </nav>

    <div id="content" class="page-login">
        <div class="grid">

        <section class="form form-big form-login">

            <header>
                <h2>Kierunek Kariera</h2>
                <h2>Kierunek Kariera Zawodowa</h2>
            </header>

            <div class="content">

                <form name="loginForm" action="j_spring_security_check" method="POST">

                    <h3>Panel Logowania<br>Usługodawcy</h3>

                    <c:if test="${param.logout != null}">
                        <div class="msg"><p>Wylogowano pomyślnie.</p></div>
                    </c:if>

                    <%--TODO: docelowo ma być licznik dla nieaktywnego konta--%>
                    <c:if test="${param.error != null}">
                        <div class="msg msg-error"><p><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></p></div>
                    </c:if>

                    <div class="field field-string">
                        <div class="label">
                            <label for="username">Wprowadź login/email</label>
                        </div>
                        <div class="control">
                            <input type="text" id="username" name="username" tabindex="1" data-test="__login__username">
                        </div>
                    </div>

                    <div class="field field-string">
                        <div class="label">
                            <label for="password">Hasło</label>
                        </div>
                        <div class="control">
                            <input type="password" id="password" name="password" tabindex="2" maxlength="60" data-test="__login__password">
                        </div>
                    </div>

                    <div class="field field-submit">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <button type="submit" class="button" tabindex="3" data-test="__login__submit">Zaloguj się</button>
                    </div>

                    <div class="msg">
                        <a href="${pageContext.request.contextPath}/retrieve" title="Odzyskaj hasło">Odzyskaj hasło</a>
                    </div>

                </form>
            </div>
        </section>
    </div></div>

    <div>
        <div id="bottombarKKK">
            <div class="grid">
                <h2>Telefoniczne Biuro Obsługi Klienta</h2>
                <h2>Kierunek Kariera</h2>
                <h3 class="phone">+48 22 346 75 05</h3>
                <a class="mail" href="mailto:tbok.kk@sodexo.com" title="tbok.kk@sodexo.com">tbok.kk@sodexo.com</a>
                <p class="description">Wszystkie informacje o projekcie pn. „Kierunek Kariera” dostępne są na stronie<br><a target="_blank" href="http://www.pociagdokariery.pl" rel="external" title="Pociąg do Kariery">www.pociagdokariery.pl</a></p>
            </div>

            <div class="grid">
                <h2>Telefoniczne Biuro Obsługi Klienta</h2>
                <h2>Kierunek Kariera Zawodowa</h2>
                <h3 class="phone">+48 22 346 75 15</h3>
                <a class="mail" href="mailto:tbok.kk@sodexo.com" title="tbok.kk@sodexo.com">tbok.kk@sodexo.com</a>
                <p class="description">Wszystkie informacje o projekcie pn. „Kierunek Kariera Zawodowa” dostępne są na stronie<br><a target="_blank" href="http://www.pociagdokariery.pl" rel="external" title="Pociąg do Kariery">www.pociagdokariery.pl</a></p>
            </div>
        </div>
    </div>

    <div id="logobar">
        <div class="grid">
            <img src="${cdnUrl}img/fu_logo.gif" alt="Logo Programu Regionalnego Fundusze Europejskie z logiem przedstawiającym na niebieskim tle trzy gwiazdki w kolorze zółtym, czerwonym i białym">
            <img src="${cdnUrl}img/rp_flag.jpg" alt="Flaga Rzczypospolitej Polskiej, biało-czerwona flaga po lewej stronie, po prawej napis Rzeczypospolita Polska">
            <img src="${cdnUrl}img/malo_hor_logo.gif" alt="Logo województwa małopolskiego, znak gór wykonany z fioletowych, niebieskich zielonych i zółtych prostokątów nachodzących na siebie tworzących podobiznę górskich szczytów">
            <img src="${cdnUrl}img/eu_efs_logo.gif" alt="Napis Unia Europejska, Europejski Fundusz Społeczny po prawej, po lewej faga uni europejskiej - złote gwiazdki na ciemnoniebieskim tle">
        </div>
    </div>

    <footer>
        <div class="grid">

            <ul>
                <li>&copy; <span itemprop="copyrightYear">2017</span>&nbsp;<a target="_blank" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." rel="external" itemprop="copyrightHolder" href="http://www.sodexo.pl/">Sodexo Benefits and Rewards Services Polska Sp.&nbsp;z&nbsp;o.o.</a></li>
            </ul>

            <ol>
                <li><span>Operator Finansowy: </span><a title="Sodexo Benefits and Rewards Services Polska Sp. z o.o. | ##BUILD_NUMBER" rel="external" href="http://www.sodexo.pl/">Sodexo</a></li>
            </ol>

        </div>
    </footer>

    <script src="${cdnUrl}js/gryf.js"></script>

    <script>
        document.loginForm.username.focus();
        var hashFragment = window.location.hash;
        if (hashFragment ) {
            var action = document.loginForm.getAttribute('action');
            document.loginForm.setAttribute('action', action+hashFragment);
        }
    </script>

</body></html>