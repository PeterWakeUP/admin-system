$(function () {
    var bResetPwdRole=hasPermission('sys:user:resetpwd');
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
			{ label: '用户名', name: 'username', width: 75 },
            { label: '姓名', name: 'nickname', width: 60 },
            { label: '所属部门', name: 'deptName', width: 75 },
            { label: '工号', name: 'jobNum', width: 50 },
			{ label: '邮箱', name: 'email', width: 90 },
			{ label: '手机号', name: 'mobile', width: 80 },
            { label: '登录次数', name: 'tgNum', width: 80 },
			{ label: '状态', name: 'status', width: 50, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTimeStr', index: "createTimeStr", width: 90},
            { label: '操作', name: 'opt', width: 90,
                formatter: function (cellvalue, options, rowObject) {
                    var detail = '';
                    if (bResetPwdRole) {
                        detail = detail+' <a class="btn-link" style="text-decoration: none" href="javascript:;" title="密码将会被重置为：888888" onclick="vm.resetPwd('+rowObject.userId+')"><i class="fa fa-pencil-square-o"></i>&nbsp;重置密码</a>';
                    }
                    //var detail="<a  onclick='btn_detail(\""+ rowObject.clid + "\")'' title='详细信息' src='../../Content/Images/Icon16/application_view_detail.png' style='padding:0px 10px'>";
                    return detail;
                },},
        ],
		viewrecords: true,
        height: 'auto',
        rowNum: 15,
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
        loadComplete: function () {
            initHeight();
        }
    });

    $('body').click(function (e) {
        if (e.target.id != 'txt_department')
            if ($('#treeview').is(':visible')) {
                $('#treeview').hide();
            }
    })

    $("#txt_department").one("click", function () {
        $.get(baseURL + "sys/dept/deptTreeData", function (r) {
            var treedata = buildDomTree([r])
            var options = {
                bootstrap2: false,
                showTags: true,
                levels: 5,
                showCheckbox: false,
                selectedBackColor: 'rgb(243,243,243)',
                selectedColor: '#000',
                checkedIcon: "glyphicon glyphicon-check",
                data: treedata,
                onNodeSelected: function (event, data) {
                    vm.q.deptName = data.text;
                    vm.q.deptID = data.id;
                    var deptIds = getChildNodeIdArr(data);
                    deptIds.push(data.id);
                    $("#txt_department").attr("jihuodeptids", JSON.stringify(deptIds));
                    if (deptIds[0] == "") {
                        $("#txt_department").attr("jihuodeptids", "");
                    }
                    getUserDropDownList(deptIds);
                    $("#treeview").hide();
                }
            };
            $('#treeview').treeview(options);
        });
    });
});

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
var ztree;
var vedittip;
var vaddtip;
var vm = new Vue({
	el:'#rrapp',
	data:{
        userSelected: "",
		q:{
			username: null,
            deptName:"",
            uid:"",
            operator:""
		},
		showList: true,
		title:null,
		roleList:{},
        dicList: {},
        dicUser: [],
		user:{
			status:1,
			deptId:null,
            deptName:null,
			roleIdList:[]
		},
        dicOperatorValue:[]
	},
    created:function(){

    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			//vm.showList = false;
            var htmladd = $('.panel-default');
            vm.title = "增加";
            vaddtip=layer.open({
                type: 1,
                title: "增加",
                area:  ['50%','70%'],
                content:htmladd
            });
			vm.roleList = {};
			vm.user = {deptName:null, deptId:null, status:1, roleIdList:[]};



			//获取角色信息
			this.getRoleList();

			vm.getDept();
		},
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
				}
            })
        },
		update: function () {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
            //var userId = getSelectedRow();
			//vm.showList = false;
            var htmledit = $('.panel-default');
            vm.title = "修改";
            vedittip=layer.open({
                type: 1,
                title: "修改",
                area:  ['50%','70%'],
                content:htmledit
            });
            //获取用户数据
            if(userId != -1){
                vm.getUser(userId);
            }

			//获取角色信息
			this.getRoleList();

		},
        resetPwd: function (userId) {
            var vconfirm=layer.confirm('确定要将该用户的密码重置为：888888吗？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                var url = "sys/user/resetPwd/"+userId;
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    success: function(r){
                        if(r.code === 0){
                            layer.close(vconfirm);
                            layer.msg('操作成功', {icon: 1,time:2000,shade : [0.3 , '#000' , true]});
                        }else{
                            layer.msg(r.msg, {icon: 2,time:2000,shade : [0.3 , '#000' , true]});
                        }
                    }
                });
            }, function(){
            });
        },
		del: function () {
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
				    data: JSON.stringify(userIds),
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
        untieUser: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要解绑用户二次验证？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/untieUser",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
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
			var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";


			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.user),
			    success: function(r){
			    	if(r.code === 0){
                        layer.msg('操作成功', {icon: 1});
                        if(vm.user.userId != null)
                           {
                               layer.close(vedittip);
                               vm.updateOverAllFrame();
                           }
                           else
                           {
                               layer.close(vaddtip);
                           }
                        	vm.reload();

					}else{
						alert(r.msg);
					}
				}
			});
		},
		getUser: function(userId){
			$.get(baseURL + "sys/user/info/"+userId, function(r){
				vm.user = r.user;
				vm.user.password = null;
                if(vm.user.operatorValue == null || vm.user.operatorValue == ""){
                    vm.user.operatorValue = -1;
                }
                vm.getDept();
			});
		},
		getRoleList: function(){
			$.get(baseURL + "sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},


        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },


    reload: function () {
			vm.showList = true;
			// var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{
                    'username': vm.q.username,
                    'deptId':$("#txt_department").attr("jihuodeptids"),
                    'uid':vm.q.uid,
                    'operator':vm.q.operator
                },
                page:1
            }).trigger("reloadGrid");
		}
	}
});



//组别明细隐藏
function buildDomTree(r) {
    var data = [{"text": "请选择组别", "id": ''}];
    var root = "请选择组别";

    function walk(nodes, data) {
        if (!nodes) {
            return;
        }
        $.each(nodes, function (idx, item) {
            var obj = {
                id: item.deptId,
                text: item.name != null ? item.name : root
            };
            if (item.list.length > 0) {
                obj.nodes = [];
                walk(item.list, obj.nodes);
            }
            data.push(obj);
        });
    }

    walk(r, data);
    return data;
}

function getUserDropDownList(deptIds) {
    $.ajax({
        type: "POST",
        url: baseURL + "sys/user/getAllUserByDeptId",
        contentType: "application/json",
        data: JSON.stringify(deptIds),
        success: function (data) {
            if (data.code == 0) {
                vm.dicUser = data.userList;
            } else {
                alert(data.msg);
            }
        }
    });
}

function getChildNodeIdArr(node) {
    var ts = [];
    if (node.nodes) {
        for (var x in node.nodes) {
            ts.push(node.nodes[x].id);
            if (node.nodes[x].nodes) {
                var getNodeDieDai = getChildNodeIdArr(node.nodes[x]);
                for (var j in getNodeDieDai) {
                    ts.push(getNodeDieDai[j]);
                }
            }
        }
    } else {
        ts.push(node.id);
    }
    return ts;
}
$(window).resize(function() {
    initHeight();
});

function initHeight(){
    var temp = $("#jobapp").height();
    jQuery("#jqGrid").jqGrid('setGridHeight',$(window).height() - (140+temp));
}