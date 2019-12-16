//获取URL参数
$.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

//初始化表格高度 = 屏幕高度 - 条件查询高度 - 顶部条高度
function initHeight(x){
    var temp = $("#condition").height();
    var height = $(window).height() - (x+temp);
    jQuery("#jqGrid").jqGrid('setGridHeight',height);
}

var folder = {
    userAvatar: "userAvatar",
    topicPhoto: "topicPhoto",
    musicPhoto: "musicPhoto",
    musicFile: "musicFile"
}
var fileUrlPrefix = "https://tcshipin-1257933730.cos.ap-guangzhou.myqcloud.com/";
function uploadFile(elementId, folder) {
    var fileObj = document.getElementById(elementId).files[0];
    var formFile = new FormData();
    formFile.append("action", "UploadVMKImagePath");
    formFile.append("file", fileObj);
    formFile.append("folder", folder);
    var url = "";
    $.ajax({
        type: 'post',
        url: baseURL+"api/upload/file",
        data: formFile,
        async: false,
        cache: false,
        processData: false,
        contentType: false,
        success:function(ret){
            if(ret.code == 0){
                url = fileUrlPrefix + ret.data.pathRelative;
            }
        },
        error:function(){
            console.log("上传失败");
        }
    });
    return url;
}

function musicPlayer(url, name, author, photo) {
    layer.open({
        type: 2,
        fixed: false,
        title: false,
        area: ['510px', '120px'],
        shade: 0.8,
        closeBtn: 0,
        shadeClose: true,
        content:"../../modules/comm/audioPlay.html?url="+url+"&name="+name+"&author="+author+"&photo="+photo
    });
}


function videoPlayer(url) {
    layer.open({
        type: 2,
        fixed: false,
        title: false,
        area: ['370px', '650px'],
        skin:"background-color:transparent",
        shade: 0.8,
        closeBtn: 0,
        shadeClose: true,
        content:"../../modules/comm/videoPlayer.html?url="+url
    });
}
