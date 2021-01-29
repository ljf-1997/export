/**
 * 查询数据
 * @param params
 */
function submitData(params) {
    $.ajax({
        url: 'export/export/export',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        beforeSend: function () {
            commonUtils.loading('loading...');
        },
        success: function (res) {
            alert("导出成功！")
        },

    });
}
