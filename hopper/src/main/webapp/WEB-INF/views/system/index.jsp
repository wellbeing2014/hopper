<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/views/meta.jsp" %>

<title>H-ui.admin v3.0</title>
<meta name="keywords" content="H-ui.admin v3.0,H-ui网站后台模版,后台模版下载,后台管理系统模版,HTML后台模版下载">
<meta name="description" content="H-ui.admin v3.0，是一款由国人开发的轻量级扁平化网站后台模板，完全免费开源的网站后台管理系统模版，适合中小型CMS后台系统。">
</head>
<body>

<%@ include file="/WEB-INF/views/header.jsp" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a> <span class="c-999 en">&gt;</span><span class="c-666">系统设置</span></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
		      <div id="tab_demo" class="HuiTab">
		        <div class="tabBar cl"> <span>WEB容器配置</span> <span>JDK配置</span> <span>其他配置</span> </div>
		        <div class="tabCon pd-20">
		        	
					<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"> <a href="javascript:;" onclick="addServerConfig('添加WEB容器','/system/addServer','','450')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加容器</a></span> <span class="r">共有数据：<strong>54</strong> 条</span> </div>
					<div style="position: relative;">
					
					<table id="serverconftable" class="table table-border table-bordered table-bg table-hover">
						<thead>
							<tr>
								<th scope="col" colspan="7">容器列表</th>
							</tr>
							<tr class="text-c">
								<th width="300">容器名称</th>
								<th >容器路径</th>
								<th width="100">容器类型</th>
								<th width="50">适用平台</th>
								<th width="100">操作</th>
							</tr>
						</thead>
						<tbody />
					</table>
					<div class="item load9" style="position: absolute;margin-left: 50%">
								<div class="loader">加载中...</div>
					</div>
		        	</div>
		        </div>
		        <div class="tabCon">内容二</div>
		        <div class="tabCon">内容三</div>
		      </div>
		</article>
	</div>
</section>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<script type="text/javascript" src="/lib/mustache/2.3.0/mustache.js"></script> 
<script id="template" type="x-tmpl-mustache">
	{{ #. }}
	<tr class="text-c">
		<td>{{name}}</td>
		<td>{{path}}</td>
		<td>{{name}}</td>
		<td></td>
		<td><a title="编辑" href="javascript:;" onclick="editServerConfig('编辑WEB容器','/system/editServer','','450','{{id}}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> <a title="删除" href="javascript:;" onclick="delServerConfig(this,'{{id}}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
	</tr>
	{{ /. }}
</script>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript">
function getServers() {
    $.getJSON("/system/getTomcat.json",
    function (json) {
    	var tbl_body = $("#serverconftable>tbody");
    	var template = $('#template').html();
	   	Mustache.parse(template);   // optional, speeds up future uses
	   	var rendered = Mustache.render(template, json);
	   	tbl_body.html(rendered);
    });
};
var loadConf=function(i) {
    switch (i) {
        case 0:
        	getServers();
            break;
        case 1:
            break;
        case 2:
            break;
        default:
            break;
    }
}
$.Huitab_ext("#tab_demo .tabBar span","#tab_demo .tabCon","current","click",0,loadConf);

/*
参数解释：
title	标题
url		请求的url
id		需要操作的数据id
w		弹出层宽度（缺省调默认值）
h		弹出层高度（缺省调默认值）
*/
/*管理员-权限-添加*/
function addServerConfig(title,url,w,h){
	layer_show_ext(title,url,w,h,getServers);
}

function editServerConfig(title,url,w,h,sid){
	layer_show_ext(title,url+'?sid='+sid,w,h,getServers);
}

/*管理员-权限-删除*/
function delServerConfig(obj,sid){
	layer.confirm('删除容器须谨慎，确认要删除吗？',function(index){
		$.post("/system/delServer.json",
			  {
			    id:sid
			  },
			  function(data,status){
				  $(obj).parents("tr").remove();
				  layer.msg('已删除!',{icon:1,time:1000});
			  });
		
	});
}



</script>
<script type="text/javascript" src="/static/hopper/common.js"></script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>