$(document).ready(function () {
    $(".linkMinus").on("click", function (evt) {
        evt.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) - 1;
        if (newQuantity > 0) {
            quantityInput.val(newQuantity);
            updateQuantity(productId, newQuantity);
        } else {
            showModalDialog("Warning", "Minimum quantity is 1");
        }
    });
    $(".linkPlus").on("click", function (evt) {
        evt.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) + 1;
        console.log(newQuantity);
        if (newQuantity <= 5) {
            quantityInput.val(newQuantity);
            updateQuantity(productId, newQuantity);
        } else {
            showModalDialog("Warning", "Maximum quantity is 5");
        }
    });
});
function updateQuantity(productId, quantity) {
    // quantity = $("#quantity" + productId).val();
    url = contextPath + "cart/update/" + productId + "/" + quantity;
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }

    }).done(function (updatedSubtotal) {

        updateSubtotal(updatedSubtotal, productId);
        updateTotal();
    }).fail(function () {
        showErrorModal("Error while updating product quantity");
        // showModalDialog();
    });

    console.log(quantity + "\n" + url);
};
function updateSubtotal(updatedSubtotal, productId) {
    formattedSubtotal = $.number(updatedSubtotal, 2);
    $("#subtotal" + productId).text(formattedSubtotal);
}
function updateTotal() {
    total = 0.0;
    $(".subtotal").each(function (indexInArray, valueOfElement) {
        total+=parseFloat(valueOfElement.innerHTML.replaceAll(",",""))
    });
    formattedTotal=$.number(total,8);
    $("#total").text(formattedTotal);
}