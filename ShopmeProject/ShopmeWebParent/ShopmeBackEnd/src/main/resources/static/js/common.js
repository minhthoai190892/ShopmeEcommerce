$(document).ready(function () {
    $("#logoutLink").on("click",function (e) {
      //Phương thức preventDefault() của đối tượng event được sử dụng để ngăn chặn cách xử lý mặc định của trình duyệt khi xảy ra sự kiện.
      e.preventDefault();
      document.logoutForm.submit();
    })
  });