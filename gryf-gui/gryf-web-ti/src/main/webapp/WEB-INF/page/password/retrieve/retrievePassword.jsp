<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <title>Sodexo Gryf</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${cdnUrl}gryf/css/init.css">
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
            <h1 itemprop="headline name" class="headline name">RESET HASŁA</h1>

            <p itemprop="description" class="description">Link umożliwiający zmianę hasła zostanie wysłany wyłącznie na adres email zarejestrowany w naszej bazie Instytucji Szkoleniowych</p>

            <c:if test="${error != null}">
                <div class="msg msg-error"><p><c:out value="${error.message}"/></p></div>
            </c:if>

            <c:if test="${unknowerror != null}">
                <div class="msg msg-error"><p>Wystąpił niespodziewany błąd po stronie serwera. Prosimy spróbować później</p></div>
            </c:if>

            <section class="form password-retrieve">
                <form name="verificationForm" action="${pageContext.request.contextPath}/retrieve/password" method="POST">
                    <fieldset>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="email">Email</label>
                            </div>
                            <div class="control">
                                <input id="email" type="email" name="email">
                            </div>
                        </div>
                    </fieldset>
                    <div class="submit submit-left">
                        <button type="submit">Resetuj hasło</button>
                        <p class="centered"><a href="${pageContext.request.contextPath}/login" title="login">Powrót na stronę logowania</a></p>
                    </div>
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