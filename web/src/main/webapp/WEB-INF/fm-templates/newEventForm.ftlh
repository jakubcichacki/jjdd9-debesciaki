<!DOCTYPE html>
<html lang="pl-PL">
<#include "headSection.ftlh">
<style>
    #city {
        max-width: 50%;
    }
    #zipCode {
        max-width: 10%;
    }
</style>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
        <a class="navbar-brand " href="/">&nbsp;&nbsp;Planer kulturalny</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive"
                aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">Strona główna</a>
                </li>
                <li class="nav-item ">
                    <a class="nav-link" href="/show-events?action=showAll&page=1">Wydarzenia</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/show-organizers?page=1">Organizatorzy</a>
                </li>
                <#include "loginLogout.ftlh">
            </ul>
        </div>
    </div>
</nav>
<br><br>
<div class="container">
    <div class="jumbotron">
        <h1 class="display-4">Formularz dodawania nowego wydarzenia</h1><br>
        <form method="post" action="/add-event" enctype="multipart/form-data">
            <span class="font-weight-bold">Nazwa wydarzenia: <input type="text" name="name" class="m-2" required/></span><br>
            <span class="font-weight-bold">Organizator:
                <select class="m-2" name="organizersDesignation" id="organizersDesignation" required>
                    <#list organizers as organizer>
                        <option value="${organizer.designation}">${organizer.designation}</option>
                    </#list>
                </select>
                <br><h6><a data-toggle="modal" href="#myModal" class="text-primary font-italic">Dodaj organizatora</a></h6>
            </span><br>
            <span class="font-weight-bold">Główna nazwa miejsca: <input type="text" name="placeName" class="m-2" required/></span><br>
            <span class="font-weight-bold">Dodatkowa nazwa miejsca: <input type="text" name="placeSubname" class="m-2"/></span><br>
            <span class="font-weight-bold">Adres: <input type="text" name="street" class="m-2" required/>
            <nobr>Miasto: <input type="text" name="city" id="city" class="m-2" required/> Kod pocztowy: <input type="text" name="zipCode" id="zipCode" class="m-2" onblur="validateZipCode(this)" title="Format xx-xxx" maxlength="6" /></nobr></span><br>
            <span class="font-weight-bold">Kategoria: <input type="text" name="category" class="m-2" required/></span><br>
            <span class="font-weight-bold">Adres strony internetowej: <input type="url" name="url" class="m-2" onblur="checkURL(this)"/></span><br>
            <span class="font-weight-bold">Data rozpoczęcia: <input type="date" name="startDate" class="m-2" required/><input type="time" name="startTime" class="m-2" required/></span><br>
            <span class="font-weight-bold">Data zakończenia: <input type="date" name="endDate" class="m-2" required/><input type="time" name="endTime" class="m-2" required/></span></span><br>
            <nobr><span class="font-weight-bold">Typ biletów:
                <select class="m-2" onchange="freePay(this)" name="typeOfTicket">
                    <option value="free" name="free">bezpłatne</option>
                    <option value="tickets" name="tickets">płatne</option>
                </select></span></nobr>
            <br>
            <span class="font-weight-bold">Ilość dostępnych miejsc: <input type="number" name="numberOfTickets" class="m-2" required min="1" step="1"/></span><br>
            <div id="ifTickets" style="display: none;">
                <span class="font-weight-bold">Cena biletu normalnego: <input type="number" name="normalTicket" class="m-2"  min="1" step="1"/></span><br>
                <span class="font-weight-bold">Cena biletu ulgowego: <input type="number" name="reducedTicket" class="m-2" min="1" step="1"/></span><br>
            </div>
            <span class="font-weight-bold">Opis: <input type="text" name="descLong" class="m-2"/></span><br>
           <!-- <span class="font-weight-bold">Zdjęcie: <input type="file" name="image"/></span><br> -->
            <button type="submit" class="btn btn-primary btn-lg">Zapisz</button>
        </form>
    </div>
</div>
<#include "newOrganizerModal.ftlh">
<#include "footerWithScripts.ftlh">
<script type="text/javascript">
    function freePay(that) {
        if (that.value == "tickets") {
            document.getElementById("ifTickets").style.display = "block";
        } else {
            document.getElementById("ifTickets").style.display = "none";
        }
    }
    function validateZipCode(code) {
        if (!code.value.match(/^\d{2}-\d{3}$/)) {
            code.value="";
            alert("Wprowadzono błędny kod pocztowy");
        }
    }
    function checkURL (abc) {
        var string = abc.value;
        if (!~string.indexOf("http")) {
            string = "http://" + string;
        }
        abc.value = string;
        return abc
    }
</script>
</body>
</html>