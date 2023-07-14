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
    e.preventDefault();
    //lấy size của hình ảnh
    fileSize = this.files[0].size;

    //kiểm tra ddoojdaif file
    if (fileSize > MAX_FILE_SIZE) { 
      this.setCustomValidity("You must choose an image less than 1MB!");//hiện thông báo
      this.reportValidity();//ngăn chuyển tới server
    } else {
      this.setCustomValidity("");//hiện thông báo
      showImageThumbnail(this);
    }
  });
});

function showImageThumbnail(fileInput) {
  var file = fileInput.files[0];
  var reader = new FileReader();
  reader.onload = function (e) {
    $("#thumbnail").attr("src", e.target.result);
  };
  reader.readAsDataURL(file);
}

// Modal Dialog
function showModalDialog(title,message) {
  $("#modalTitle").text(title);
  $("#modalBody").text(message);
  $("#modalDialog").modal('show');
}
function showWarningModal(message) {
  showModalDialog("Warning",message);
}
function showErrorModal(message) {
  showModalDialog("Error",message);
}