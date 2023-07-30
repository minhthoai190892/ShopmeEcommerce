dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function () {
  $("#shortDescription").richText();
  $("#fullDescription").richText();
  dropdownBrands.change(function () {
    dropdownCategories.empty();
    getCategories();
  });
  getCategoriesForNewForm();
});
function getCategoriesForNewForm() {
  catIdField = $("#categoryId");
  editMode = false;
  if (catIdField.length) {
    editMode = true;
  }
  if (!editMode) {
    getCategories();
  }
}
/**
 * Hàm liệt kê danh sách category
 */
function getCategories() {
  brandID = dropdownBrands.val(); //id
  //tạo đường dẫn giống với BrandRestController
  // /brands/id/categories
  url = brandModuleURL + "/" + brandID + "/categories";
  $.get(url, function (responseJson) {
    $.each(responseJson, function (index, category) {
      $("<option>")
        .val(category.id)
        .text(category.name)
        .appendTo(dropdownCategories);
    });
  });
}

/**
 * Hàm kiểm tra trùng nhau
 * @param {*} form
 * @returns
 */
function checkUnique(form) {
  productId = $("#id").val();
  productName = $("#name").val();
  csrfValue = $("input[name='_csrf']").val();

  params = { id: productId, name: productName, _csrf: csrfValue };
  $.post(checkUniqueUrl, params, function (response) {
    if (response == "Ok") {
      form.submit();
    } else if (response == "Duplicate") {
      showWarningModal(
        "There is another product having the name " + productName
      );
    } else {
      showErrorModal("Unknown response from server");
    }
  }).fail(function () {
    showErrorModal("could not connect to the server");
  });
  return false;
}
