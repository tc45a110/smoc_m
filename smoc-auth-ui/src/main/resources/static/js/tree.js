/**
 * 树状结构
 */
$(document).ready(
    function () {
        $.ajax({
            type: "GET",
            url: "/menu/getTreeJson/root",
            dataType: "json",
            success: function (result) {
                $('#menuTree').treeview({
                    data: result,         // 数据源
                    showCheckbox: true,   //是否显示复选框
                    highlightSelected: true,    //是否高亮选中
                    nodeIcon: 'glyphicon glyphicon-globe',
                    emptyIcon: '',    //没有子节点的节点图标
                    multiSelect: false,    //多选
                    onNodeChecked: function (event, data) {
                        alert(data.nodeId);
                    },
                    onNodeSelected: function (event, data) {
                        alert(data.nodeId);
                    }
                });
            },
            error: function () {
                alert("树形结构加载失败！")
            }
        });
    });
