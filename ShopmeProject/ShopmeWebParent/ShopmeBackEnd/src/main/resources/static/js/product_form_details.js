function addNextDetailSection() {
    //lấy đối tượng
    allDivDetails = $("[id^='divDetail']")
    //
    divDetailsCount = allDivDetails.length;
    // alert(divDetailsCount)
    htmlDetailSection =`
     <div style="display:  flex;" class="m-3"  id="divDetail${divDetailsCount}">
            <label class="m-3" for="" >Name:</label> 
            <input type="text" class="form-control w-25" name="detailNames" maxlength="255">
            <label class="m-3" for="" >Value:</label> 
            <input type="text" class="form-control w-25" name="detailValues" maxlength="255">
        </div>
    `;
    previousDivDetailSection = allDivDetails.last();
    previousDivDetailID = previousDivDetailSection.attr("id");
      
    $("#divProductDetails").append(htmlDetailSection);


    htmlLinkRemove = ` <a style="padding: 10px;" class="fas fa-times-circle fa-2x icon-dark " 
    href="javascript:removeDetailSectionById('${previousDivDetailID}')"
    title="Remove this detail" ></a>`;
    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='detailNames']").last().focus();
}
function removeDetailSectionById(id) {
    $("#"+id).remove();
}