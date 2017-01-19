<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <title>Sodexo Gryf</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${cdnUrl}css/init.css">
    <link rel="shortcut icon" href="${cdnUrl}favicon.ico">
</head>

<body>
<div id="breadcrumbs">
    <div class="grid">
        <ul>
            <li><a href="${pageContext.request.contextPath}" title="Sodexo Gryf">Sodexo Gryf</a></li>
        </ul>
    </div>
</div>
<nav>
    
</nav>

<div id="content">
    <div class="grid">
        <article itemprop="text" class="text centered">
            <h1 itemprop="headline name" class="headline name">Panel Logowania Instytucji Szkoleniowej</h1>

            <p itemprop="description" class="description">Prosimy, wprowadź dane uwierzytelniające.</p>

            <p>Życzymy miłego dnia.</p>

            <c:if test="${param.logout != null}">
                <div class="msg"><p>Wylogowano pomyślnie.</p></div>
            </c:if>

            <%--TODO: docelowo ma być licznik dla nieaktywnego konta--%>
            <c:if test="${param.error != null}">
                <div class="msg msg-error"><p><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></p></div>
            </c:if>

            <section class="form form-login">
                <form name="loginForm" action="j_spring_security_check" method="POST">
                    <fieldset>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="username">Login</label>
                            </div>
                            <div class="control">
                                <input id="username" type="text" name="username" value="" tabindex="1">
                            </div>
                        </div>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="password">Hasło</label>
                            </div>
                            <div class="control">
                                <input id="password" type="password" name="password" tabindex="2">
                            </div>
                        </div>
                    </fieldset>
                    <div class="submit submit-left">
                        <button type="submit" tabindex="3">Zaloguj się</button>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <p class="code-remimnder-label">W celu odzyskania hasła prosimy użyć poniższego łącza</p>
                        <a class="code-reminder" href="${pageContext.request.contextPath}/retrieve" title="retrieve"
                           style="float:left; margin:10px 0px 0px 40px">Odzyskaj hasło</a>
                    </div>
                    <script>
                        document.loginForm.username.focus();
                        var hashFragment = window.location.hash;
                        if (hashFragment ) {                            
                            var action = document.loginForm.getAttribute('action');
                            document.loginForm.setAttribute('action', action+hashFragment);
                        }
                    </script>
                </form>
            </section>
        </article>
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