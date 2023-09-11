
// Modal Dialog
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");
}
function showWarningModal(message) {
    showModalDialog("Warning", message);
}
function showErrorModal(message) {
    showModalDialog("Error", message);
}