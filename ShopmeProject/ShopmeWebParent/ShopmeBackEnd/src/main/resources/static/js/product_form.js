
dropdownBrands = $("#brand");
dropdownCategories = $("#category");
var extraImagesCount = 0;
$(document).ready(function () {
    $("#shortDescription").richText();
    $("#fullDescription").richText();
    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategoies();
    });
    getCategoies();

    $("input[name='extraImage']").each(function (index) {
        extraImagesCount++;
        $(this).change(function () {
            //gọi hàm hiển thị hình ảnh extra
            showExtraImageThumbnail(this, index)
        })
    });

});
/**
 * hàm hiển thị hình ảnh
 * @param {*} fileInput file hình ảnh (id của img)
 */
function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
    if (index >= extraImagesCount-1) {
        // gọi hàm thêm extra image

        addNextExtraImageSection(index + 1);
    }
}
/**
 * Thêm extra image
 */
function addNextExtraImageSection(index) {
    htmlExtraImage = `
     <div class="col border m-3 p-2 " id="divExtraImage${index}">
        <div id="extraImageHeader${index}">
            <label for="">Extra Image #${index + 1}: </label>
        </div> 
        <div>
            <img src="${defaultImageThumbnailSrc}" style="width: 200px; height: 200px;"
             alt="Extra Image #${index + 1} preview" class="img-fluid"
             id="extraThumbnail${index}">
        </div>
        <div>
            <input type="file" name="extraImage" 
            onchange = "showExtraImageThumbnail(this,${index})"
            accept="image/png,image/jpeg">
        </div>
    </div>
    `;
    htmlLinkRemove = ` <a class="fas fa-times-circle fa-2x icon-dark " style
    href="javascript:removeExtraImage(${index - 1})"
    title="Remove this image" ></a>`;
    $("#divProductImages").append(htmlExtraImage);
    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImagesCount++;

}
/**
 * Hàm xóa hình ảnh extra
 */
function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
   
}
/**
 * Hàm liệt kê danh sách category
 */
function getCategoies() {
    brandID = dropdownBrands.val();//id
    //tạo đường dẫn giống với BrandRestController
    // /brands/id/categories
    url = brandModuleURL + "/" + brandID + "/categories";
    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
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
            showWarningModal("There is another product having the name " + productName)
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(function () {
        showErrorModal("could not connect to the server");
    });
    return false;
}