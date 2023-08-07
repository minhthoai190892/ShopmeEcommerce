var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;
var message;

$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");
    message = $("#message");

    // button load data
    buttonLoad.click(function () {
        loadCountries();
    });
    dropDownCountry.on("change", function () {
        changeFormStateToSelectedCountry();
    });
    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() == "Add") {
                addCountry();
        } else {
            changeFormStateToNew();
        }
    })
    buttonUpdateCountry.click(function () {
        updateCountry();
    });
    buttonDeleteCountry.click(function () {
        deleteCountry();
    });

});
function deleteCountry() {
    // optionValue = dropDownCountry.val();
    countryId = dropDownCountry.val().split("-")[0];
    url = contextPath + "countries/delete/" + countryId;
    jsonData = { id: countryId };
    $.get(url, function () {
        $("#dropDownCountries option[value = '" + optionValue + "']").remove();
        changeFormStateToNew();
        loadCountries() ;
    })
        .done(function () {
            showToastMessage("The country has been deleted");
        })
        .fail(function () {
            showToastMessage(
                "ERROR: Could not connect to server or server encountered an error"
            );
        });
}
function updateCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountry.val().split("-")[0];
    jsonData = { id: countryId, name: countryName, code: countryCode };
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {
        showToastMessage("The new country has been updated successfully");
        changeFormStateToNew();
        loadCountries();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}
function addCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val().trim();
    countryCode = fieldCountryCode.val().trim();
    if (countryCode === ""&& countryName ==="") {
        message.html("Please enter a country")
    }else{
        jsonData = { name: countryName, code: countryCode };
        $.ajax({
            type: 'POST',
            url: url,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeaderName, csrfValue);
            },
            data: JSON.stringify(jsonData),
            contentType: 'application/json'
        }).done(function (countryId) {
            selectNewlyAddedCountry(countryId, countryCode, countryName);
            showToastMessage("The new country has been added successfully");
            loadCountries();
        }).fail(function () {
            showToastMessage("ERROR: Could not connect to server or server encountered an error");
        });
        message.html("")
    }
    
}
/**
 * Hàm chọn quốc gia mới thêm vào
 * @param {*} countryId 
 * @param {*} countryCode 
 * @param {*} countryName 
 */
function selectNewlyAddedCountry(countryId, countryCode, countryName) {
    console.log("selectNewlyAddedCountry");
    //thêm dữ liệu vào option 
    $("<option>")
        .val(optionValue)
        .text(countryName)
        .appendTo(dropDownCountry);
        //chọn vào dữ liệu mới thêm vào
    $("#dropDownCountries option[value = '" + optionValue + "']").prop("selected", true)
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}
/**
 * Hàm click vào button new
 */
function changeFormStateToNew() {
  
    //thay đổi nut
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name: ")
    //thay đổi disabled
    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);
    //thay đổi tên label và focus vào input
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}
/**
 * Hàm thay đổi tên label và tên nut khi lick vào dữ liệu
 */
function changeFormStateToSelectedCountry() {
    //thay đổi value của nút khi click vào data
    buttonAddCountry.prop("value", "New");
    //thay đổi disabled
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country:")
    //lấy giá trị khi lick vào
    selectedCountryName = $("#dropDownCountries option:selected").text();
    // alert(selectedCountryName)
    //gán giá trị vào input
    fieldCountryName.val(selectedCountryName);
    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
    // alert(countryCode);
}
/**
 * Hàm load country
 */
function loadCountries() {
    url = contextPath + "countries/list";
     //alert("About loading countries..."+url);
    //lấy data từ RestController
    $.get(url, function (responseJSON) {
        dropDownCountry.empty();
      
        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>")
                .val(optionValue)
                .text(country.name)
                .appendTo(dropDownCountry);
        });
    })
        .done(function () {
            //thay đổi tên nút khi đã load country
            buttonLoad.val("Refresh Country List");
            showToastMessage("All countries have been load");
        })
        .fail(function () {
            showToastMessage(
                "ERROR: Could not connect to server or server encountered an error"
            );
        });
}
/**
 * Hàm hiển thị Toast Message
 * @param {*} message nhận một chuổi message
 */
function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast("show");
}



