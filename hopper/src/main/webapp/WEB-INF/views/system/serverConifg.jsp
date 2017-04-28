<%@ include file="/WEB-INF/views/meta.jsp" %>

<title>添加TOMCAT容器</title>
</head>
<body>
<article class="cl pd-20">
	<form action="/system/add.json" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>别名：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${server.name}" placeholder="" id="servername" name="servername">
				<input type="hidden"  class="input-text" value="${server.id}" placeholder="" id="sid" name="sid">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>环境：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				<div class="radio-box">
					<input name="platform" type="radio" value="1" id="plat-1" checked>
					<label for="plat-1">X64</label>
				</div>
				<div class="radio-box">
					<input name="platform" type="radio" value="2" id="plat-2" >
					<label for="plat-2">X86</label>
				</div>
				<div class="radio-box">
					<input name="platform" type="radio" value="9" id="plat-9" >
					<label for="plat-9">其他</label>
				</div>
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务器路径：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${server.path}" placeholder="" id="localpath" name="localpath">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>附加参数：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="" name="args" id="args" value="${server.args}" />
			</div>
		</div>
		
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea name="beizhu" cols="" rows="" class="textarea"  placeholder="简要描述" ></textarea>
				<p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</div>
	</form>

</article>



<%@ include file="/WEB-INF/views/footer.jsp" %>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript">

$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	function showResponse(responseText,statusText) {
		    console.log(responseText);
		}
	
	 $("#form-member-add").validate({
		rules:{
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
						}
	 }
	 $("#form-member-add").ajaxForm(options);
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->

</body>
</html>