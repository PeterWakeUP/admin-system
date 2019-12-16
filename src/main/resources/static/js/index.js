//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props: {item: {}, index: 0},
    template: [
        '<li :class="{active: (item.type===0 && index === 0)}">',
        '<a v-if="item.type === 0" href="javascript:;">',
        '<i v-if="item.icon != null" :class="item.icon"></i>',
        '<span>{{item.name}}</span>',
        '<i class="fa fa-angle-left pull-right"></i>',
        '</a>',
        '<ul v-if="item.type === 0" class="treeview-menu">',
        '<menu-item :item="item" :index="index" v-for="(item, index) in item.list"></menu-item>',
        '</ul>',
        '<a v-if="item.type === 1 && item.isTop === 0" :href="\'#\'+item.url">' +
        '<i v-if="item.icon != null" :class="item.icon"></i>' +
        '<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
        '</a>',
        '<a v-else-if="item.type === 1 && item.isTop === 1" :href="\'#\'+item.url" target="_blank">' +
        '<i v-if="item.icon != null" :class="item.icon"></i>' +
        '<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
        '</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function () {
    var $content = $('.content');
    $content.height($(this).height() - 120);
    $content.find('iframe').each(function () {
        $(this).height($content.height());
    });
}).resize();

//注册菜单组件
Vue.component('menuItem', menuItem);

var jstx_userList = null;

var vm = new Vue({
    el: '#rrapp',
    data: {
        user: {},
        menuList: {},
        roleList: {},
        main: "main.html",
        password: '',
        newPassword: '',
        navTitle: "欢迎页"
    },
    methods: {
        getMenuList: function () {
            $.getJSON(baseURL + "sys/menu/nav", function (r) {
                vm.menuList = r.menuList;
                window.permissions = r.permissions;

                //路由
                var router = new Router();
                routerList(router, vm.menuList);
                router.add('#particulars/postLoanDetails.html', function () {
                    var url = window.location.hash;
                    vm.main = url.replace('#', '');
                    vm.navTitle = '标的详情';
                });
                router.start();
            });
        },
        getUser: function () {
            $.getJSON(baseURL + "sys/user/info", function (r) {
                vm.user = r.user;
            });
        },
        updatePassword: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['550px', '270px'],
                shadeClose: false,
                content: jQuery("#passwordLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sys/user/password",
                        data: data,
                        dataType: "json",
                        success: function (r) {
                            if (r.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function () {
                                    location.reload();
                                });
                            } else {
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        logout: function () {
            var token = localStorage.getItem("token");
            $.ajax({
                type: "POST",
                url: baseURL + "sys/logout/logoutUpdateTime",
                data:{"token":token},
                dataType: "json",
                success:function (ret) {
                    localStorage.removeItem("token");
                    localStorage.removeItem("loginStatus");
                    var operatorValue = localStorage.getItem("operatorValue");
                    if (operatorValue == "TR") {
                        doLogout();   //推出天润账号
                    }
                    setTimeout(function () {
                        location.href = baseURL + 'login.html';
                    },500);
                }
            });
        },
        getRoleList: function () {
            $.get(baseURL + "sys/role/select", function (r) {
                vm.roleList = r.list;
            });
        },
        showUserInfo: function () {
            this.getRoleList();
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "用户信息",
                area: ['550px', '470px'],
                shadeClose: false,
                content: jQuery("#userInfoLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sys/user/updateBasicInfo",
                        data: JSON.stringify(vm.user),
                        dataType: "json",
                        contentType: "application/json",
                        success: function (r) {
                            if (r.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function () {
                                    location.reload();
                                });
                            } else {
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['', 'no']
            });
        }
    },
    created: function () {
        this.getMenuList();
        this.getUser();

    }

});

function routerList(router, menuList) {
    for (var key in menuList) {
        var menu = menuList[key];
        if (menu.type == 0) {
            routerList(router, menu.list);
        } else if (menu.type == 1) {
            router.add('#' + menu.url, function () {
                var url = window.location.hash;

                //替换iframe的url
                vm.main = url.replace('#', '');

                //导航菜单展开
                $(".treeview-menu li").removeClass("active");
                $(".sidebar-menu li").removeClass("active");
                $("a[href='" + url + "']").parents("li").addClass("active");
                vm.navTitle = $("a[href='" + url.split('?')[0] + "']").text();
            });
        }
    }
}








