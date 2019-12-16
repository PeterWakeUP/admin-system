$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/task/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', width: 20, key: true },
			{ label: '任务Key', name: 'taskKey', width: 80},
			{ label: '任务名', name: 'mc', width: 70 },
			{ label: '规则', name: 'rule', width: 50 },
			{ label: '描述', name: 'bewrite', width: 180 },
			{ label: '状态', name: 'status', width: 30 ,formatter:formatterStatus},
			{ label: '操作', name: 'opt', width: 80 , formatter: function (cellvalue, options, rowObject) {
                var detail = '';
                if(rowObject.status==1){
                    detail = detail+' <a class="btn-link" style="text-decoration: none" href="javascript:;" title="任务状态" onclick="vm.optFun('+rowObject.id+',2)">&nbsp;关闭</a>';
				}else{
                    detail = detail+' <a class="btn-link" style="text-decoration: none" href="javascript:;" title="任务状态" onclick="vm.optFun('+rowObject.id+',1)">&nbsp;开启</a>';
				}
				if(rowObject.taskKey == 'syncCustomerPerfomanceTask'){
                    detail += ' <a class="btn-link" style="text-decoration: none" href="javascript:;" title="手动触发" onclick="vm.taskManual('+rowObject.id+',\''+rowObject.taskKey+'\')">&nbsp;手动触发</a>';
                }
              return detail;
            }}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.dataList",
            page: "page.pageNum",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "pageNum",
            rows: "limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        },
        loadComplete: function () {
            initHeight();
        }
    });
});

//格式化跟进状态
function formatterStatus(cellvalue, options, rowObject) {
    var statuss = rowObject.status;
    if(statuss==1){
        return "<font color='green'>已开启</font>";;
    }
    return "<font color='red'>已关闭</font>";
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null
		},
		showList: true,
		title: null,
		taskConfig: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.taskConfig = {};
            vm.taskConfig.status = 1;
            $("#taskKeyId").removeAttr("readonly");
		},
		update: function () {

			var id = getSelectedRow();
			if(id == null){
				return ;
			}

			$.get(baseURL + "sys/task/queryByTaskId?id="+id, function(r){
                vm.showList = false;
                vm.title = "修改";
                $("#taskKeyId").attr("readonly","readonly");
                vm.taskConfig = r.taskConfig;
            });
		},
		del: function (event) {
			var ids = getSelectedRow();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/task/del",
				    data:{"id":ids},
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
		saveOrUpdate: function () {
			var url = vm.taskConfig.id == null ? "sys/task/save" : "sys/task/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
			    data:vm.taskConfig,
                dataType: "json",
			    success: function(r){
			    	if(r.code === 0){
                        alert("操作成功");
					}else{
						alert(r.msg);
					}
                    vm.reload();
				}
			});
		},
		optFun:function (objId,status) {
			var str = "开启";
			if(status==2){
                var str = "关闭";
			}
            var vconfirm=layer.confirm('确定要'+str+'该定时任务？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/task/update",
                    data: {"id":objId,"status":status},
                    success: function(r){
                        if(r.code === 0){
                            vm.reload();
                            layer.msg("操作成功");
                        }else{
                            layer.msg(r.msg);
                        }
                        layer.close(index);
                    }
                });
            }, function(index){
                layer.close(index);
            });

        },
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'mc': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        taskManual: function (objId,taskKey) {
            var vconfirm=layer.confirm('确定要手动调用定时任务？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                $.ajax({
                    type: "GET",
                    url: baseURL + "task/sendTask",
                    data: {"taskType":"synCustomerPref"},
                    success: function(r){
                        layer.msg(r.msg);
                        layer.close(index);
                    }
                });
            }, function(index){
                layer.close(index);
            });


        }
	}
});

$(window).resize(function() {
    initHeight();
});

function initHeight(){
    var temp = $("#jobapp").height();
    jQuery("#jqGrid").jqGrid('setGridHeight',$(window).height() - (110+temp));
}