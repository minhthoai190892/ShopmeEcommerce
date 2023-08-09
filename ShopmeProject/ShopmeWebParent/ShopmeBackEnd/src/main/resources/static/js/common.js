$(document).ready(function () {
  $("#logoutLink").on("click", function (e) {
    //Phương thức preventDefault() của đối tượng event được sử dụng để ngăn chặn cách xử lý mặc định của trình duyệt khi xảy ra sự kiện.
    e.preventDefault();
    document.logoutForm.submit();
  });
  customizetabs();
  //gọi lại hàm
  // customizeDropDownMenu();
});

// function customizeDropDownMenu() {
//   $(".dropdown>a").hover(function () {
//           // over
//           $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
//       }, function () {
//           // out
//           $(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
//       }
//   );
//   //chọn thẻ a trong class dropdown và ghi đè hàm click
//   $(".dropdown>a").click(function (e) { 
//       e.preventDefault();
//       location.href=this.href;
//   });
// }


function clearFilter() {
  window.location = moduleURL;
}
function showDeleteConfirmModal(link, entityName) {
  entityId = link.attr("entityId");
  $("#yesButton").attr("href", link.attr("href"));
  $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + " ?");
  $("#confirmModal").modal('show');
}
function customizetabs() {
  var url = document.location.toString();
  if (url.match('#')) {
   $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
    console.log(url.split('#')[1] );
  }
  $('.nav-tabs button').on('shown.bs.tab', function(){
   window.location.hash =e.target.hash;
  });
}