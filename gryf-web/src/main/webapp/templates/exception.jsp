<%@page pageEncoding="UTF-8" %>

<!DOCTYPE html >

<script src="${pageContext.request.contextPath}/js/components/gryf-config.js"></script>

<div ng-app="gryf.config" ng-controller="ConfigController">
    <header id="header">
        <h1>Nieznany błąd!</h1>
        <h1>Żądanie spowodowało wewnętrzny błąd systemu!</h1>
    </header><br>

    <button class="button button-show" ng-click="setShowExceptionStackTrace()">Opis błędu</button>
    <br/><br/>
    <div ng-show="isShowExceptionStackTrace" style="overflow-y: scroll">
        <pre style="white-space: pre-wrap;">{{getLastExceptionStackTrace()}}</pre>
    </div>
</div>