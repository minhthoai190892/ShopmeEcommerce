var buttonLoadCountriesForStates
var dropDownCountriesForStates
var dropDownStates
var labelStateName
var fieldStateName
var buttonAddState
var buttonUpdateState
var buttonDeleteState

$(document).ready(function () {
    buttonLoadCountriesForStates = $("#buttonLoadCountriesForStates");
    dropDownCountriesForStates = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    buttonLoadCountriesForStates.click(function () {
        loadCountriesForStates();
    });
    dropDownCountriesForStates.on("change", function () {
        dropDownCountries4States();
    });
    dropDownStates.on("change", function () {
        changeFormStateTeSelectedSate();
    });
    buttonAddState.click(function () {
        if (buttonAddState.val() == "Add") {
            addState();
        }
        changeFormStateToNew();
    });
    buttonUpdateState.click(function () {
        updateState();
    });
    buttonDeleteState.click(function () {
        deleteState();
    });
});

/**
 * Sài GETMAPPING();
 */
// function deleteState() {
//     stateId = dropDownStates.val();
//     url = contextPath + "states/delete/" + stateId;
//     $.get(url, function () {
//         $("#dropDownStates option[value=" + stateId + "]").remove();
//         changeFormStateToNew();
//     }).done(function () {
//         showToastMessage("The state has been deleted");
//     }).fail(function () {
//         showToastMessage("ERROR: Could not connect to the server or the server encountered an error");
//     });
// }

function deleteState() {
    stateId = dropDownStates.val();
    url = contextPath + "states/delete/" + stateId;
    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },

    }).done(function () {
        $("#dropDownStates option[value=" + stateId + "]").remove();
        changeFormStateToNew();
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to the server or the server encountered an error");
    });
}


function updateState() {
	    formStates = document.getElementById("formStates");
    if (!formStates.checkValidity()) {
        formStates.reportValidity();
        return;
    }
    url = contextPath + "states/save";

    stateId = dropDownStates.val();
    stateName = fieldStateName.val();
    selectedCountry = $("#dropDownCountriesForStates option:selected");

    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = { id: stateId, name: stateName, country: { id: countryId, name: countryName } };
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function () {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The new state has been updated successfully");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to the server or the server encountered an error");

    });
}

function addState() {

    formStates = document.getElementById("formStates");
    if (!formStates.checkValidity()) {
        formStates.reportValidity();
        return;
    }
    url = contextPath + "states/save";
    stateName = fieldStateName.val();
    stateId = fieldStateName.val();
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();
    jsonData = { name: stateName, country: { id: countryId, name: countryName } };
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function () {
        selectedNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to the server or the server encountered an error");

    });
}
/**
 * Hàm chọn state vừa thêm mới 
 * @param {*} stateId 
 * @param {*} stateName 
 */
function selectedNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
    $("#dropDownStates option[value='" + stateId + "']").prop('selected', true);
    fieldStateName.val("").focus();
}
/**
 * Hàm thay đổi tên nút khi click vào State (All States/Province)
 */
function changeFormStateTeSelectedSate() {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);
    labelStateName.text("Selected State/Province:")
    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}
/**
 * Click button load Country
 */
function loadCountriesForStates() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountriesForStates.empty();
        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
        });
    });
}
/**
 * Hiển thị danh sách State của Country
 */
function dropDownCountries4States() {
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dropDownStates.empty();
        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);

        });
    }).done(function () {
        changeFormStateToNew();
        showToastMessage("All states have been loaded successfully for country " + selectedCountry.text());
        fieldStateName.val("")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to the server or the server encountered an error");
    });
}
/**
 * Thay đổi tên nút
 */
function changeFormStateToNew() {

    //thay đổi nut
    buttonAddState.val("Add");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

}


