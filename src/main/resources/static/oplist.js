layui.use('table', function(){
    var table = layui.table;

    //第一个实例
    table.render({
        elem: '#oplist'
        ,url: '/operation/list.json'
        ,page: true
        ,cols: [[
            {field: 'id', title: 'ID', width:80, fixed: 'left'}
            ,{field: 'kind', title: '类型', width:80}
            ,{field: 'time', title: '发生时间', width:220}
            ,{field: 'comment', title: '备注信息'}
        ]]
    });

});