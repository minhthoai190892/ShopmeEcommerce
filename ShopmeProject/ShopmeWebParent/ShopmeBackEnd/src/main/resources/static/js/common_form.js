// $(document).ready(function(){
//   $("#buttonCancel").on("click",function(){
//     window.location = "[[@{/users}]]";
//   });
// });
$(document).ready(function () {
  // $("#buttonCancel").on("click",function(){
  //   window.location = "[[@{/users}]]";
  // });

  $("#fileImage").change(function (e) {
    if (!checkFileSize(this)) {
      return;
    } else {
      showImageThumbnail(this)
    }
  });
});
/**
 * Hàm kiểm tra kích thước của file hình ảnh
 * @param {*} fileInput  file hình ảnh 
 * @returns trả về true hoặc false
 */
function checkFileSize(fileInput) {

  //lấy size của hình ảnh
  fileSize = fileInput.files[0].size;

  //kiểm tra ddoojdaif file
  if (fileSize > MAX_FILE_SIZE) {
    fileInput.setCustomValidity("You must choose an image less than 1MB!");//hiện thông báo
    fileInput.reportValidity();//ngăn chuyển tới server
    return false
  } else {
    fileInput.setCustomValidity("");//hiện thông báo
    return true;
  }
}
/**
 * Hàm hiển thị hình ảnh 
 * @param {*} fileInput với file hình ảnh được chọn
 */
function showImageThumbnail(fileInput) {
  var file = fileInput.files[0];
  var reader = new FileReader();
  reader.onload = function (e) {
    $("#thumbnail").attr("src", e.target.result);
  };
  //đọc file hình ảnh
  reader.readAsDataURL(file);
}

// Modal Dialog
function showModalDialog(title, message) {
  $("#modalTitle").text(title);
  $("#modalBody").text(message);
  $("#modalDialog").modal('show');
}
function showWarningModal(message) {
  showModalDialog("Warning", message);
}
function showErrorModal(message) {
  showModalDialog("Error", message);
}

