$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'dictionary/dictionaryDetail/list',
        postData:{'pid':$.getUrlParam("id")},
        datatype: "json",
        colModel: [
            { label: '项目ID', name: 'id', index: "project_id", width: 25, key: true, hidden: true },
            { label: '序号', name: 'id',  width: 75 ,sortable: false },
            { label: '字典值', name: 'dicValue', width: 75 ,sortable: false},
            { label: '字典名称', name: 'dicName', width: 45 ,sortable: false},
            { label: '备    注', name: 'remarks', width: 45 ,sortable: false},
            { label: '排序', name: 'dicSort', width: 45 ,sortable: false}
        ],
        viewrecords: true,
        height: screen.height * 0.55,
        rowNum: 30,
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
        dictionary:{

        },
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.dictionary = {dicName:null, dicValue:null, remarks:null,pid:$.getUrlParam("id")};
            vm.title = "新增";
            console.log($.getUrlParam("id"));
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
            vm.dictionary.dicValue=rowData.dicValue;
            vm.dictionary.remarks=rowData.remarks;
        },
        del: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "dictionary/dictionaryDetail/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
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
                if (validate_required(dicValue,"字典值不能为空！")==false)
                {dicValue.focus();return false}

                if (validate_required(dicName,"字典名称不能为空！")==false)
                {dicName.focus();return false}
            }
            var url = vm.dictionary.id == null ? "dictionary/dictionaryDetail/save" : "dictionary/dictionaryDetail/update";
            console.log(url);
            console.log(vm.dictionary);
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
                postData:{'pid': $.getUrlParam("id")},
                page:1
            }).trigger("reloadGrid");
        },
        goBack: function () {
            window.location.href=baseURL+"modules/sys/sysDictionary.html";
        },

    }
})





