<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section>
    <header id="header">
        <h1>Brak uprawnień do modułu</h1>
    </header>

    <c:choose>
        <c:when test="${fn:length(privilegesRequired) == 1}">
            <p>Do uruchomienia tego formularza wymagane jest uprawnienie
                <c:forEach items="${privilegesRequired}" var="p">
                    <em>${p}</em>
                </c:forEach>
            </p>
        </c:when>
        <c:otherwise>
            <p>Do uruchomienia tego formularza wymagane jest jedno z następujących uprawnień
                <c:forEach items="${privilegesRequired}" var="p">
                    <em>${p}, </em>
                </c:forEach>
            </p>
        </c:otherwise>
    </c:choose>

</section>

