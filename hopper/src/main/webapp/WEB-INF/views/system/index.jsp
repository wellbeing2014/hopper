<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/views/meta.jsp" %>



<%@ include file="/WEB-INF/views/header.jsp" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a> <span class="c-999 en">&gt;</span><span class="c-666">系统设置</span></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
		      <div id="tab_demo" class="HuiTab">
		        <div class="tabBar cl"> <span>WEB容器配置</span> <span>JDK配置</span> <span>其他配置</span> </div>
		        <div class="tabCon pd-20">
		        	
					<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"> <a href="javascript:;" onclick="addServerConfig('添加WEB容器','/system/addServer','','450')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加容器</a></span> </div>
					<div style="position: relative;">
					
					<table id="serverconftable" class="table table-border table-bordered table-bg table-hover">
						<thead>
							<tr>
								<th scope="col" colspan="7">容器列表</th>
							</tr>
							<tr class="text-c">
								<th width="300">容器名称</th>
								<th >容器路径</th>
								<th >附加参数</th>
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
		        <div class="tabCon">
		        
		        	<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"> <a href="javascript:;" onclick="addajdk()" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加JDK</a></span>  </div>
					<div style="position: relative;">
		        	<table id="jdktable" class="table table-border table-bordered " style="clear: both">
		        		
		                <tbody>
		                	<tr class="success"><td colspan="2"><span class="l"><strong>JDK1.7</strong> </span></td></tr>
						    <tr ><th width="30%">别名</th><td class="editable" data-type="input">JDK1.7</td></tr>
						    <tr ><th>JAVA_HOME</th><td class="editable" data-type="input">警告或出错</td></tr>
						    <tr ><th>CLASS_PATH</th><td class="editable" data-type="input">危险</td></tr>
						    <tr ><th>备注</th><td class="editable" data-type="textarea">危险</td></tr>
						    <tr ><td colspan="2"> <span class="r"> <a href="javascript:;" class="btn btn-success radius">保存</a>  <a href="javascript:;" class="btn btn-danger radius">删除</a></span></td></tr>
						    <tr class="success"><td colspan="2"><span class="l"><strong>JDK1.7</strong> </span></td></tr>
						    <tr ><th width="30%">别名</th><td>成功或积极</td></tr>
						    <tr ><th>JAVA_HOME</th><td>警告或出错</td></tr>
						    <tr ><th>CLASS_PATH</th><td>危险</td></tr>
						    <tr ><td colspan="2"> <span class="r"> <a href="javascript:;" class="btn btn-primary radius">删除</a></span></td></tr>
		                </tbody>
            		</table>
            		</div>
            		
		        </div>
		        <div class="tabCon">内容三</div>
		      </div>
		</article>
	</div>
</section>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<script type="text/javascript" src="/lib/mustache/2.3.0/mustache.js"></script> 
<script id="jdk-tpl" type="x-tmpl-mustache">
	{{ #. }}
		<tr class="success"><td colspan="2"><span class="l"><strong>{{jdkname}}</strong> </span></td></tr>
    	<tr><th width="30%">别名</th><td class="editable" data-type="input">{{jdkname}}</td></tr>
		<tr><th>JAVA_HOME</th><td class="editable" data-type="input">{{javahome}}</td></tr>
		<tr><th>CLASS_PATH</th><td class="editable" data-type="input">{{classpath}}</td></tr>
		<tr><th>备注</th><td class="editable" data-type="textarea">{{remark}}</td></tr>
		<tr><td colspan="2"> <span class="r"> <a href="javascript:;" onclick="saveJdk(this,'{{id}}')" class="btn btn-success radius">保存</a>  <a href="javascript:;" onclick="deletejdk('{{id}}')" class="btn btn-danger radius">删除</a></span></td></tr>
	{{ /. }}
</script>

<script id="server-container-tpl" type="x-tmpl-mustache">
	{{ #. }}
	<tr class="text-c">
		<td>{{name}}</td>
		<td>{{path}}</td>
		<td>{{args}}</td>
		<td>{{plat}}</td>
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
    	var template = $('#server-container-tpl').html();
	   	Mustache.parse(template);   // optional, speeds up future uses
	   	var rendered = Mustache.render(template, json);
	   	tbl_body.html(rendered);
    });
};

var jdks=[];
function getjdks() {
    $.getJSON("/system/getJdk.json",
    function (json) {
    	jdks=json;
    	initjdk();
    });
};

function initjdk(){
	var tbl_body = $("#jdktable>tbody");
	var template = $('#jdk-tpl').html();
   	Mustache.parse(template);   // optional, speeds up future uses
   	var rendered = Mustache.render(template, jdks);
   	tbl_body.html(rendered);
   	
   	$('.editable').click(function(e){  
   	    e.stopPropagation();      //<-------stop the bubbling of the event here
   	    var value = $(this).text();
   	    updateVal(this, value);
   	    
   	});
}

function saveJdk(ele,id) {
	var remark = $(ele).closest('tr').prev().find('td').text();
	var classpath = $(ele).closest('tr').prev().prev().find('td').text();
	var javahome = $(ele).closest('tr').prev().prev().prev().find('td').text();
	var jdkname = $(ele).closest('tr').prev().prev().prev().prev().find('td').text();
	var postdata = {'remark':remark,'classpath':classpath,'javahome':javahome,'jdkname':jdkname, 'id':id};
	$.post("/system/addJdk.json",postdata, function(data) {
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.msg(data.msg,{icon:1,time:1000});
		parent.layer.close(index);
		getjdks();
	});
	
};

function addajdk() {
    jdks.push({"jdkname":"新的jdk","classpath":"	.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar"});
    initjdk();
};

function deletejdk(id) {
	$.post("/system/delJdk.json",{'id':id}, function(data) {
		console.log(data);
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.msg(data.msg,{icon:1,time:1000});
		parent.layer.close(index);
		getjdks();
	});
	
};


function updateVal(currentEle, value) {
    var type=$(currentEle).attr('data-type');
    var inner = '';
    var child ;
    if(type=='input'){
    	inner = '<input class="input-text radius size-S" value="'+value+'" />';
    	$(currentEle).html(inner);
        child = $(currentEle).find(">:first-child");        
        $(child).click(function(e){  
            e.stopPropagation();
        });
        $(child).focus();
        $(child).focusout(function () { 
        		 $(currentEle).html($(child).val().trim());
        });
    	
    }
    if(type=='textarea')
    {
    	inner = '<textarea class="textarea radius " />';
    	$(currentEle).html(inner);
        child = $(currentEle).find(">:first-child");  
        $(child).val(value);
        $(child).click(function(e){  
            e.stopPropagation();
        });
        $(child).focus();
        $(child).focusout(function () { 
            	 $(currentEle).html('<pre style="padding:0;border:0;background-color:#ffffff;margin-bottom:0">'+$(child).val().trim()+'</pre>');
        });
    }
    
}
var loadConf=function(i) {
    switch (i) {
        case 0:
        	getServers();
            break;
        case 1:
        	getjdks();
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
<script src="/lib/x-editable/jquery-editable/js/jquery-editable-poshytip.js"></script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>