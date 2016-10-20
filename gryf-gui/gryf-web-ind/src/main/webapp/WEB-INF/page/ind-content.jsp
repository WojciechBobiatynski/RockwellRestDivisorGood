<%@page pageEncoding="UTF-8" %>
<toaster-container toaster-options="{'position-class': 'toast-top-center'}"></toaster-container>
<div>
    <section>
        <header id="header">
            <h1>Moje dane</h1>
        </header>
        <div class="grid-100">
            <div class="grid-30">
                <div>
                    <strong class="label">Imię</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
                <div>
                    <strong class="label">Nazwisko</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
                <div>
                    <strong class="label">Pesel</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
            </div>
            <div class="grid-70">
                <div>
                    <strong class="label">ID umowy</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
                <div>
                    <strong class="label">Data podpisania umowy</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
                <div>
                    <strong class="label">Przydzielona kategoria szkolenia</strong>
                    <input type="text" ng-model="entityObject.id" disabled="disabled">
                </div>
            </div>
        </div>
    </section>
    <section>
        <div class="grid-100">
            <h1>Moje bony</h1>
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th class="sortable" ng-class="getSortingTypeClass('id')">
                        <div>Data ważności bonów</div>
                    </th>

                    <th class="sortable" ng-class="getSortingTypeClass('status.id')">
                        <div>Liczba bonów przyznanych</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('enterprise.id')">
                        <div>Liczba bonów zarezerwowanych</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('basicData.enterpriseName')">
                        <div>
                            Liczba bonów wykorzystanych
                        </div>
                    </th>
                    <th class="sortable"
                        ng-class="getSortingTypeClass('basicData.vatRegNum')">
                        <div>
                            Liczba bonów dostępnych
                        </div>
                    </th>
                    <th class="sortable"
                        ng-class="getSortingTypeClass('basicData.addressInvoice')">
                        <div>
                            ID zamówienia na bony
                        </div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('basicData.zipCodeInvoice.zipCode')">
                        <div>
                            Data zamówienia na bony
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in searchDTO.searchResultList | limitTo: searchResultOptions.displayLimit">
                    <td>10-10-2017</td>
                    <td>180</td>
                    <td>100</td>
                    <td>20</td>
                    <td>80</td>
                    <td>1233</td>
                    <td>10-10-2016</td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
    <section>
        <div class="grid-100">
            <h1>Moje zarezerwowane szkolenia</h1>
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th class="sortable" ng-class="getSortingTypeClass('id')">
                        <div>Data rozpoczęcia szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('status.id')">
                        <div>Data zakończenia szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('enterprise.id')">
                        <div>Nazwa szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('basicData.enterpriseName')">
                        <div>
                            Nazwa Instytucji Szkoleniowej
                        </div>
                    </th>
                    <th class="sortable"
                        ng-class="getSortingTypeClass('basicData.vatRegNum')">
                        <div>
                            Liczba zarezerwowanych bonów
                        </div>
                    </th>
                    <th class="sortable"
                        ng-class="getSortingTypeClass('basicData.addressInvoice')">
                        <div>
                            Data rezerwacj bonów / zapisu na szkolenie
                        </div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('basicData.zipCodeInvoice.zipCode')">
                        <div>
                            Prześlij ponownie PIN DO ROZLICZENIA
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in searchDTO.searchResultList | limitTo: searchResultOptions.displayLimit">
                    <td>10-02-2017</td>
                    <td>30-02-2017</td>
                    <td>Cyberbezpieczeństwo</td>
                    <td>Hiabak</td>
                    <td>80</td>
                    <td>02-02-2017</td>
                    <td>
                        <button type="submit" class="button">Prześlij ponownie PIN DO ROZLICZENIA</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
    <section>
        <div class="grid-100">
            <h1>Moje rozliczone szkolenia</h1>
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th class="sortable" ng-class="getSortingTypeClass('id')">
                        <div>Data rozpoczęcia szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('status.id')">
                        <div>Data zakończenia szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('enterprise.id')">
                        <div>Nazwa szkolenia</div>
                    </th>
                    <th class="sortable" ng-class="getSortingTypeClass('basicData.enterpriseName')">
                        <div>
                            Nazwa Instytucji Szkoleniowej
                        </div>
                    </th>
                    <th class="sortable"
                        ng-class="getSortingTypeClass('basicData.vatRegNum')">
                        <div>
                            Liczba rozliczonych bonów
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in searchDTO.searchResultList | limitTo: searchResultOptions.displayLimit">
                    <td>10-02-2017</td>
                    <td>30-02-2017</td>
                    <td>Cyberbezpieczeństwo</td>
                    <td>Hiabak</td>
                    <td>80</td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
</div>