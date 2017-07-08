<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/views/meta.jsp" %>

<%@ include file="/WEB-INF/views/header.jsp" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>

<section class="Hui-article-box" >
	 <form action="/user/submitUserList_1" method="post">
        ID:<input type="text" name="id"><br/>
        Username:<input type="text" name="name"><br/>
        Password:<input type="text" name="pwd"><br/><br/>

        ID:<input type="text" name="id"><br/>
        Username:<input type="text" name="name"><br/>
        Password:<input type="text" name="pwd"><br/><br/>
        <input type="submit" value="submit">
    </form>
</section>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<script id="template" type="x-tmpl-mustache">
	{{ #. }}
	
	<tr class="text-c">
		<td>{{mainport}}</td>
		<td>{{name}}</td>
		<td>{{loalpath}}</td>
		<td>{{desc}}</td>
		<td>{{count}}</td>
		<td>{{lasttime}}</td>
		<td class="td-status"><span class="label radius">{{status}}</span></td>
		<td class="td-manage"><a style="text-decoration:none" onClick="admin_start(this,'10001')" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe615;</i></a> <a title="编辑" href="javascript:;" onclick="admin_edit('管理员编辑','admin-add.html','2','800','500')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> <a title="删除" href="javascript:;" onclick="admin_del(this,'1')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
	</tr>
	{{ /. }}
</script>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/mustache/2.3.0/mustache.js"></script> 
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script> 
<script type="text/javascript">
$(document).ready(function() {
	 getServers();
});


function getServers() {
    $.getJSON("/server/allserver.json",
    function (json) {
    	var tbl_body = $("#servertable>tbody");
    	var template = $('#template').html();
	   	Mustache.parse(template);   // optional, speeds up future uses
	   	var rendered = Mustache.render(template, json);
	   	tbl_body.html(rendered);
    });
};

window.setInterval(function(){getServers()},1000);

/*
	参数解释：
	title	标题
	url		请求的url
	id		需要操作的数据id
	w		弹出层宽度（缺省调默认值）
	h		弹出层高度（缺省调默认值）
*/
/*管理员-增加*/
function admin_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*管理员-删除*/
function admin_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		
		$(obj).parents("tr").remove();
		layer.msg('已删除!',{icon:1,time:1000});
	});
}
/*管理员-编辑*/
function admin_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*管理员-停用*/
function admin_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		
		$(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_start(this,id)" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-default radius">已禁用</span>');
		$(obj).remove();
		layer.msg('已停用!',{icon: 5,time:1000});
	});
}

/*管理员-启用*/
function admin_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		
		$(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_stop(this,id)" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
		$(obj).remove();
		layer.msg('已启用!', {icon: 6,time:1000});
	});
}
</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>