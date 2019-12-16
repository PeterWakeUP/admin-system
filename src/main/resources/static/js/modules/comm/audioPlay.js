var filePath = $.getUrlParam("url");
var name = $.getUrlParam("name");
var author = $.getUrlParam("author");
var photo = $.getUrlParam("photo");

$(function () {
    $("#adownload").attr("href",filePath);
    var zp1 = new zplayer({
        element: document.getElementById("player1"),
        autoPlay: true,
        listFolded: true,
        musics: [{
            title: name,
            author: author,
            url: filePath,
            pic: photo
        }]
    })
    zp1.init();
});
