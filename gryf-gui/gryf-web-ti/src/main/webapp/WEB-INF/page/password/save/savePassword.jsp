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
            <h1 itemprop="headline name" class="headline name">Tworzenie nowego hasła</h1>

            <p>Prosimy pamiętać, że hasła w obu polach muszą być identyczne</p>

            <c:if test="${error != null}">
                <div class="msg msg-error"><p><c:out value="${error.message}"/></p></div>
            </c:if>

            <section class="form form-password">
                <form name="passwordForm" action="${pageContext.request.contextPath}/password/save" method="POST">
                    <fieldset>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="new-password">Nowe hasło</label>
                            </div>
                            <div class="control">
                                <input id="new-password" type="password" name="password" value="" tabindex="1">
                            </div>
                        </div>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="old-password">Powtórz hasło</label>
                            </div>
                            <div class="control">
                                <input id="old-password" type="password" name="repeatedPassword" tabindex="2">
                            </div>
                        </div>
                    </fieldset>
                    <div class="submit submit-left">
                        <button type="submit" tabindex="3">Zapisz hasło</button>
                        <input type="hidden" name="token" value="${token}" />
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