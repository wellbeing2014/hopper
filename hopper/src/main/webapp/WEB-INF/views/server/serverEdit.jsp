<%@ include file="/WEB-INF/views/meta.jsp" %>


<article class="cl pd-20">
	<form action="/server/add.json" method="post" class="form form-horizontal" id="form-server-add">
		<div class="row cl">
			<input type="hidden"  class="input-text" value="${server.serverid}" placeholder="" id="serverid" name="serverid">
			<label class="form-label col-xs-3 "><span class="c-red">*</span>端口标识：</label>
			<div class="formControls col-xs-2 ">
				<input type="text" class="input-text" value="${server.mainport}" placeholder="" id="mainport" name="mainport">
			</div>
			
			
			<label class="form-label col-xs-2 "><span class="c-red">*</span>关闭端口：</label>
			<div class="formControls col-xs-2 ">
				<input type="text" class="input-text" value="${server.shutport}" placeholder="" id="shutport" name="shutport">
			</div>
			
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${server.servername}" placeholder="" id="servername" name="servername">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">WEB容器选择：</label>
			<div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
				<select id="tomcatid" name="tomcatid" class="select">
					
				</select>
				</span> 
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务器路径：</label>
			<div class="col-xs-8 col-sm-9">
				<a style="text-decoration:none" onClick="add()" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe600;</i></a>
				
			</div>
			<div id="pathcash" class="col-xs-offset-r col-sm-offset-3 col-xs-8 col-sm-9 " >
			
			
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">JDK选择：</label>
			<div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
				<select name="jdkid" class="select">
					<option value="0">jdk1.5.0_22</option>
					<option value="1">jdk1.6.0_45</option>
					<option value="2">jdk1.7.0_71</option>
					<option value="3">jdk1.8.0_73</option>
				</select>
				</span> 
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">JAVA_OPTS参数：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="" name="opts" id="opts" value="${server.opts}" />
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea name="desc" cols="" rows="" value="${server.desc}" class="textarea"  placeholder="简要描述" ></textarea>
				<p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				<input id="but" class="btn btn-primary radius" type="button" value="&nbsp;&nbsp;提交1&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>

<script id="template" type="x-tmpl-mustache">
	{{ #. }}
	<div class="row cl">
		<input type="hidden"  class="input-text" value="" placeholder="" id="pathid" name="tomcatpaths[{{count}}].pathid">
		<div class="col-sm-5">
			<input name="tomcatpaths[{{count}}].contextpath" type="text" class="input-text"  placeholder="相对路径" />
		</div>
		<div class="col-sm-5">
			<input name="tomcatpaths[{{count}}].docbase" type="text" class="input-text"  placeholder="绝对路径" />
		</div>
		
		<div class="col-sm-1">
			<a style="text-decoration:none" onClick="cancel({{count}})" href="javascript:;" title="取消"><i class="Hui-iconfont">&#xe6a6;</i></a>
		</div>
	</div>
	{{ /. }}
</script>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="/lib/jquery.tabledit.1.2.3/jquery.tabledit.js"></script> 
<script type="text/javascript" src="/lib/mustache/2.3.0/mustache.js"></script> 
<script type="text/javascript">

bindTomcatversion();

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
    	if(this.name.includes('localpath1'))
    	{
    		return;
    	}
    	
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    
    var cash = $('#pathcash>div');
    $.each(cash, function(index,obj) {
    	console.log($(obj).find('input[name="localpath1"]').val());
    	console.log($(obj).find('input[name="localpath1"]').val());
    })
    return o;
};
var path=[];
function add(){
	console.log("增加");
	path.push({count:1});
	save();
}


function cancel(nno) {
	path.splice(nno,1);
	save();
}

function bindTomcatversion(){
	console.log('bind start tomcat');
	$.getJSON("/system/getTomcat.json",
    function (json) {
		var tomver ="{{#.}}<option value=\"{{id}}\">{{name}}</option>{{/.}}";
		var nn = Mustache.render(tomver, json);
		$('#tomcatid').html(nn);
    });
}
	


function save() {
	for (i in path)
		path[i].count = i;
	var template = $('#template').html();
   	Mustache.parse(template);   // optional, speeds up future uses
   	var rendered = Mustache.render(template, path);
   	$('#pathcash').html(rendered);	
}

$(function(){
	$("#but").click(function () {
		var values = $("input[name='pname[]']")
        .map(function(){return $(this).val();}).get();
		var obj = $('#form-server-add').serializeObject();
		console.log(obj);
	});
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	function showResponse(responseText,statusText) {
		    console.log(responseText);
		}
	
	 $("#form-server-add").validate({
		rules:{
			mainport:{
				required:true,
				number:true
			},
			shutport:{
				required:true,
				number:true
			},
			servername:{
				required:true,
				minlength:3,
				maxlength:50
			},
			platform:{
				required:true
			},
			args:{
				required:true
			},
			localpath:{
				required:true,
				minlength:3,
				maxlength:200
			}
			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		//submitHandler:submit
	});
	
	 var options = {
				success:function(data){
							var index = parent.layer.getFrameIndex(window.name);
							parent.layer.msg('添加成功!',{icon:1,time:1000});
							parent.layer.close(index);
						},
				error:  function(data){
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.msg('添加失败!',{icon:1,time:1000});
				}       
	 }
	 $("#form-server-add").ajaxForm(options);
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->

</body>
</html>