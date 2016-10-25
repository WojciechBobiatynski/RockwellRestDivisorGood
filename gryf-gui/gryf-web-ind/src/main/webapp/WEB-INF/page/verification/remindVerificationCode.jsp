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
            <h1 itemprop="headline name" class="headline name">Prześlij ponownie kod weryfikacyjny</h1>

            <p itemprop="description" class="description">Prosimy, wprowadź dane uwierzytelniające.</p>

            <c:if test="${error != null}">
                <div class="msg msg-error"><p><c:out value="${error.message}"/></p></div>
            </c:if>

            <c:if test="${unknowerror != null}">
                <div class="msg msg-error"><p>Wystąpił niespodziewany błąd po stronie serwera. Prosimy spróbować później</p></div>
            </c:if>

            <section class="form verification-code-reminder">
                <form name="verificationForm" action="${pageContext.request.contextPath}/verification/resend" method="POST" onsubmit="return validate()">
                    <fieldset>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="pesel">PESEL</label>
                            </div>
                            <div id="peselControl" class="control">
                                <input id="pesel" type="text" name="pesel" value="" tabindex="1">
                            </div>
                        </div>
                        <div class="field input input-short required">
                            <div class="label">
                                <label for="email">Email</label>
                            </div>
                            <div id="emailControl" class="control">
                                <input id="email" type="text" name="email" tabindex="2">
                            </div>
                        </div>
                    </fieldset>
                    <div class="submit submit-left">
                        <button type="submit" tabindex="3">Wyślij</button>
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