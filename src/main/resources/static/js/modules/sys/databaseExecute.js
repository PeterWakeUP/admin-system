
var sql;
var names=[];
var model=[];
var rownum = 0;
var temp = 0;
var jqdata;
var TYPE_NAME = {
    json: 'JSON',
    csv: 'CSV',
    sql: 'SQL',
    doc: 'Word',
    excel: 'Excel'
};
$(function () {

    var format = function(time, format){
        var t = new Date(time);
        var tf = function(i){return (i < 10 ? '0' : '') + i};
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
            switch(a){
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            }
        })
    }

    var vm = new Vue({
        el: '#rrOauthClient',
        data: {
            isShow: false,//新增的菜单栏
            titleShow: true//标题栏
        },
        methods: {
            query: function () {
                var flag = getsql();
                if (flag != false){
                    sqlExecute(sql);
                    temp = 1;
                }
            },
            clear : function () {
                $('#sql').val('');
            },
            bsql :function () {
                bsql();
            }

        }
    })

    addExport();

    function sqlExecute(sql) {
        var dbtype = $("#dbtype").val();

        var url = encodeURI(baseURL + 'dataBase/dataBaseExecute?sql=' + sql +'&type='+2);

        if(dbtype == "dataCenter"){
            url = encodeURI(baseURL + 'dataBase/dataCenterSQL?sql=' + sql);
        }

        $.ajax({
            type: "get",
            url: url,
            dataType: "json",
            async: false,
            success: function (r) {
                jqdata = r.data;
                rownum = (r.data == undefined ? 0 : r.data.length);
                if (jqdata == undefined || jqdata.length == 0) {
                    if (temp == 1) {
                        $.jgrid.gridUnload("jqGrid");
                    }
                    $('#jqGrid').html('<h4 class="modal-title">暂无数据</h4>');
                } else {
                    $('#jqGrid').html('');
                    $.each(jqdata[0], function (key, value) {
                        var f1 = key.includes('date');
                        var f2 = key.includes('time');
                        if(f1||f2){
                            names.push(key);
                            model.push({
                                name: key,
                                index: value,
                                width: value == '' ? 100 : value.length * 10,
                                formatter:function(value,options,row){
                                    return format(value, 'yyyy-MM-dd HH:mm:ss');}
                            });
                        }else{
                            names.push(key);
                            model.push({
                                name: key,
                                index: value,
                                width: value == '' ? 100 : value.length * 10
                            });
                        };
                    });
                    if (temp == 1) {
                        $.jgrid.gridUnload("jqGrid");
                    }
                    var url1 = encodeURI(baseURL + 'dataBase/dataBaseExecute?sql=' + sql +'&type='+1);

                    if(dbtype == "dataCenter"){
                        url1 = encodeURI(baseURL + 'dataBase/dataCenterSQL?sql=' + sql);
                    }

                    $("#jqGrid").jqGrid({
                        url: url1,
                        datatype: "json",
                        colNames: names,
                        colModel: model,
                        formatoptions:{srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'},
                        height: screen.height * 0.35,
                        autowidth: true,
                        rowNum: -1,
                        rownumbers: true,
                        shrinkToFit: false,
                        jsonReader: {
                            root: "data"
                        },
                        loadComplete: function () {
                            initHeight();
                        },
                        autowidth: true
                    })
                }
            },
            error:function (data) {
                $('#jqGrid').html('<h4 class="modal-title">' + data.responseJSON.message + '</h4>');
            }
        })
    }
    function addExport() {
        var $menu = $('<div class="export btn-group" style="margin-left:2px;"><button class="btn btn-default dropdown-toggle" title="导出数据" data-toggle="dropdown" type="button" aria-expanded="false"><i class="glyphicon glyphicon-export icon-share"></i> <span class="caret"></span>导出</button><ul class="dropdown-menu" role="menu"></ul></div>');
        var exportTypes = ['excel','doc', 'json', 'csv', 'sql'];
        var tempStr = "";
        for (var i = 0; i < exportTypes.length; i++) {
            tempStr += '<li data-type="' + exportTypes[i] + '"><a href="javascript:void(0)">' + TYPE_NAME[exportTypes[i]] + '</a></li>';
        }
        $menu.find("ul.dropdown-menu").html(tempStr);
        $menu.find("li").click(function () {
            var type = $(this).data('type');
            var doExport = function () {
                $('#tempTable').html("<table id='cg_table'></table>")
                var dd = $('#gbox_jqGrid .ui-jqgrid-htable thead').clone();//找到<thead>
                var ths = dd.find('th');
                for (var i = 0; i < ths.length; i++) {
                    var temp = $(ths[i]);
                    temp.html(temp.text());
                }
                var re_records = $("#jqGrid").jqGrid('getGridParam', 'records'); //获取数据总条数
                if(re_records == undefined || re_records==0){
                    alert('没有可导出的数据');
                    return;
                }

                confirm('确定要导出'+re_records+'条数据？', function () {
                    var ee = $('#jqGrid').clone();
                    ee.find('tbody').before(dd);//合并表头和表数据
                    ee.find('.jqgfirstrow').remove();//去掉多余的无效行
                    ee.find('tr.ui-search-toolbar').remove();//去掉搜索框
                    $('#cg_table').html(ee.html());
                    $('#cg_table').data("tableexport-display", "always");
                    var date = new Date();
                    $('#cg_table').tableExport({
                        type: type,
                        escape: 'false',
                        fileName: 'activity_sql_data_' + date.toLocaleDateString() + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds()
                    });
                    alert('操作成功');
                });
            };
            doExport();
        });
        $('.export').replaceWith($menu);
    }


    $('#sql').contextmenu(function(e){
        var from = document.getElementById('sql').selectionStart;
        var to = document.getElementById('sql').selectionEnd;
        if(from<to){
            rightmenu.style.display = 'block';
            rightmenu.style.left = e.pageX+'px';
            rightmenu.style.top = e.pageY+'px';
            rightmenu.focus();
            e.preventDefault()
        }else if(from == to){
            infoMessag.style.display = 'block';
            infoMessag.style.left = e.pageX + 'px';
            infoMessag.style.top = e.pageY + 'px';
            infoMessag.focus();
            e.preventDefault();
            setTimeout(function () {
                infoMessag.style.display = 'none';
            },1000);
        }
    });
    $('#clicklist').click(function (e) {
        var lists = document.getElementById('clicklist').querySelectorAll('li');
        switch(e.target){
            case lists[0]:
                getsql();
                sqlExecute(sql);
                temp = 1;
                break;
            default:
                break;
        }
        $('#rightmenu').attr('style','display:none');
    })
    $('#rightmenu').blur(function () {
        $('#rightmenu').attr('style','display:none');
    })

 })

function bsql() {
    var sql3 = encodeURI($('#sql').val());
    if (null == sql3 || sql3 == ""){
        alert("查询语句不能为空");
        return false;
    }
    $.ajax({
        type: "get",
        url: baseURL + 'dataBase/getBeautifySql?str=' + sql3+'&a=1',
        dataType: "json",
        async: false,
        success: function (result) {
            if(result.code!='FAIL'){
                $('#sql').val(result.data.re);
                alert('~~~~~~~~~~~~格式化成功~~~~~~~~~~~~');
            }else{
                alert('~~~~~~~~~~~~'+result.message+'~~~~~~~~~~~~');
            }
        },
        error:function () {
            alert('内部服务错误,请联系管理人员');;
        }
    })
};

function getsql() {
    names = [];
    model = [];
    var content = $("#sql").val();
    if (null == content || content == ""){
        $('#jqGrid').jqGrid("clearGridData");
        alert("查询语句不能为空");
        return false;
    }
    var from = document.getElementById('sql').selectionStart,to = document.getElementById('sql').selectionEnd;
    if (to - from > 0){
        if (to - from < 8){
            $('#jqGrid').html('<h4 class="modal-title">请输入正确的SQL语句</h4>');
            return ;
        }
        sql = $('#sql').val().substring(from,to);
    }else{
        sql = $('#sql').val();
    }
    var annotationIndex = sql.indexOf('--');
    if (annotationIndex >= 0){
        var tempA = sql.substr(annotationIndex);
        var feedIndex = tempA.indexOf('\n');
        var sqlTemp = sql;
        sql = sqlTemp.substr(0,annotationIndex) + sqlTemp.substr(feedIndex+1);
    }
};


$(window).resize(function() {
    initHeight();
});

function initHeight(){
    var titleShowHeight = $("#titleShow").height();
    var sqlviewIdHeight = $("#sqlviewId").height();
    var temp = titleShowHeight + sqlviewIdHeight;
    jQuery("#jqGrid").jqGrid('setGridHeight',$(window).height() - (100+temp));
}

