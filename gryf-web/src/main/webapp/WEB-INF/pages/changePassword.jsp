<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="pl">
<head>
    <title>Sodexo Gryf</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${cdnUrl}gryf/css/init.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gryf-custom.css">
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
            <h1 itemprop="headline name" class="headline name">Zmiana hasła</h1>

            <p itemprop="description" class="description">Prosimy, wprowadź nowe hasło.</p>
            <p>Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę oraz wielkie i małe litery.</p>
            <section class="form form-change-password">
                <form name="loginForm" action="j_spring_security_check" method="POST">
                    <fieldset>
                        <div class="field input input-short required">
                            <div class="label-change-password">
                                <label for="oldPassword">Stare hasło:</label>
                            </div>
                            <div class="control">
                                <input id="oldPassword" type="password" name="oldPassword" value="" tabindex="1">
                            </div>
                        </div>
                        <div class="field input input-short required">
                            <div class="label-change-password">
                                <label for="newPassword">Nowe hasło:</label>
                            </div>
                            <div class="control">
                                <input id="newPassword" type="password" name="newPassword" tabindex="2">
                            </div>
                        </div>
                        <div class="field input input-short required">
                            <div class="label-change-password">
                                <label for="newPasswordRepeat">Powtórz nowe hasło:</label>
                            </div>
                            <div class="control">
                                <input id="newPasswordRepeat" type="password" name="newPasswordRepeat" tabindex="2">
                            </div>
                        </div>
                    </fieldset>
                    <div class="submit submit-new-password">
                        <button type="submit" tabindex="3">Zapisz nowe hasło</button>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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