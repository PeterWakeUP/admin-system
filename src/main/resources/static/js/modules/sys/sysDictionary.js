$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'dictionary/list',
        datatype: "json",
        colModel: [
            { label: '项目ID', name: 'id', index: "project_id", width: 25, key: true, hidden: true },
            { label: '序号', name: 'id',  width: 75 ,sortable: false },
            { label: '字典类型', name: 'dicKey', width: 75 ,sortable: false},
            { label: '字典名称', name: 'dicName', width: 45 ,sortable: false},
            { label: '备    注', name: 'remarks', width: 45 ,sortable: false},
            { label: '创建时间', width: 55 ,sortable: false ,formatter : formatDates}
        ],
        viewrecords: true,
        height: screen.height * 0.55,
        rowNum: 50,
        rowList : [15,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },

    });
});

//格式化时间
function formatDates(cellvalue, options, rowObject) {
    var createAt=rowObject.createAt;
    var date = new Date(createAt);
    return $.dateFormat(date,'yyyy-MM-dd hh:mm:ss');
}


//表单验证
function validate_required(field,alerttxt)
{
    with (field)
    {
        if (value==null||value=="")
        {alert(alerttxt);return false}
        else {return true}
    }
}

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};

//实例化vue对象
var vm = new Vue({
    el:'#rrapp',  //element 需要获取的元素，一定是html中的根容器元素
    data:{		   // data 用于数据的存储
        q:{
            dicName: null
        },
        showList: true,
        title:null,
        dictionary:{}
    },
    methods: {
        query:function () {
            vm.reload();
        },
        operate: function () {
            var dictionaryId = getSelectedRow();
            if(dictionaryId == null){
                return ;
            }
            window.location.href=baseURL+"modules/sys/sysDictionaryDetail.html?id="+dictionaryId;
        },
        add: function(){
            vm.showList = false;
            vm.dictionary = {dicName:null, dicKey:null, remarks:null};
            vm.title = "新增";
        },
        update: function () {
            var dictionaryId = getSelectedRow();
            if(dictionaryId == null){
                return ;
            }
            var rowData = $("#jqGrid").jqGrid("getRowData", dictionaryId);
            vm.showList = false;
            vm.title = "修改";
            vm.dictionary.id=dictionaryId;
            vm.dictionary.dicName=rowData.dicName;
            vm.dictionary.dicKey=rowData.dicKey;
            vm.dictionary.remarks=rowData.remarks;
        },
        del: function () {
            var projectIds = getSelectedRows();
            if(projectIds == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "dictionary/delete",
                    contentType: "application/json",
                    data: JSON.stringify(projectIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (thisform) {
            //项目表单验证
            with (thisform) {
                if (validate_required(dicName,"字典名称不能为空！")==false)
                {dicName.focus();return false}

                if (validate_required(dicKey,"字典类型不能为空！")==false)
                {dicKey.focus();return false}
            }
            var url = vm.dictionary.id == null ? "dictionary/save" : "dictionary/update";
            console.log(url);
            console.log(vm.dictionary);
            console.log(JSON.stringify(vm.dictionary));
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.dictionary),
                success: function(r){
                    if(r.code === 0){
                        //vm.saveOrUpdateTask(r.projectId);
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function () {
            vm.showList = true;
            // var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'dicName': vm.q.dicName},
                page:1
            }).trigger("reloadGrid");
        }

    }
})





