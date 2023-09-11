$(document).ready(function () {
    $("#buttonAdd2Cart").on("click", function (evt) {
        addToCart();
    });
});

function addToCart() {
    quantity = $("#quantity" + productId).val();
    url = contextPath + "cart/add/" + productId + "/" + quantity;
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }

    }).done(function (response) {
        showModalDialog("Shopping cart ", response);
    }).fail(function () {
        showErrorDialog("Error while adding product to shopping cart");
    });

    console.log(quantity + "\n" + url);
};