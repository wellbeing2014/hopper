layui.define(['jquery', 'layer'], function(exports){
    var $       =   layui.jquery,
        layer   =   layui.layer;

    exports('request', function(options){
        var settings = {
            url: '',
            data: {},
            method: 'post',
            timeout: 10000,
            msg: {
                shade: [0.3, '#000'],
                time: 2000
            },
           
            async:true,
            failure:function(resp){
            	layer.msg(resp.msg, {icon: 5,anim: 6});
            }
        }
        $.extend(settings, options);
        
        
        
        $.ajax({
            url: settings.url,
            data: settings.data,
            method: settings.method,
            //timeout: settings.timeout,
            async:settings.async,
            //dataType: 'json',
            dataType:settings.dataType? settings.dataType:'',
            contentType:settings.contentType? settings.contentType:'',
            beforeSend: function ()
            {
                settings.loader = layer.msg('数据加载中～', {
                    shade: settings.msg.shade,
                    time: 0
                });
                return true;//  settings.beforeSend && typeof settings.beforeSend == 'function' && settings.beforeSend();
            },
            success: function (resp)
            {
                resp.success? settings.success && typeof settings.success == 'function' && settings.success(resp):settings.failure && typeof settings.failure == 'function' && settings.failure(resp)
            },
            error: function ()
            {
                settings.error && typeof settings.error == 'function' && settings.error();
                layer.msg('请求失败', {
                    icon: 5,
                    time: settings.msg.time
                });
            },
            complete: function (http, stat)
            {
                settings.complete && typeof settings.complete == 'function' && settings.complete(http, stat);
                if (stat == 'timeout') {
                    layer.msg('网络连接超时', {
                        icon: 5
                    });
                }
                layer.close(settings.loader);
            }
        });
    });
});