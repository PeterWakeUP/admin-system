var filePath = $.getUrlParam("url");
var videoWrap = document.getElementById('testVideo')
var fullScreen = document.getElementById('getFullScreen')
var video = new Dvideo ({
    ele: '#testVideo',
    title: '视频详情',
    showNext: true,
    width: '365px',
    height: '640px',
    src: filePath,
    autoplay: true,
    showNext: false,
    showVideoDefinition: false,
})

// 全屏
function setFullScreen () {
    video.launchFullScreen(videoWrap)
}

// 播放
function play () {
    video.videoPlay()
}

// 暂停
function pause () {
    video.videoPause()
}

// 播放暂停
function playpause () {
    video.videoPlayPause()
}

function setVolume (v) {
    video.updateVolume(v)
}

function setBackRate (index) {
    video.setPlayBackRate(index)
}

function setVideoForward () {
    video.videoForward(10)
}

function setVideoRewind () {
    video.videoRewind(10)
}

function showLoading () {
    video.showLoading(true, '视频清晰度切换中，请稍等')
}

function showTopBottomCtrlNotClose () {
    video.showTopBottomCtrl()
}

function hideTopBottomCtrlLi () {
    video.hideTopBottomCtrl(true)
}

function showTopBottomCtrl () {
    video.showTopBottomCtrl(true)
}

function hideTopBottomCtrl () {
    video.hideTopBottomCtrl()
}

function setVideoSize () {
    video.updateVideoSize(720,480)
}
