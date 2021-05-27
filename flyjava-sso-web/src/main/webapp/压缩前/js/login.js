/**
 * 登陆 注册
 * 20180109
 * 彭皖成
 */

/*  登陆*/
/*var redirectUrl = "${redirectUrl}";*/
var LOGIN = {
		checkInput:function() {
			if ($("#loginname").val() == "") {
				alert("用户名不能为空");
				$("#loginname").focus();
				return false;
			}
			if ($("#nloginpwd").val() == "") {
				alert("密码不能为空");
				$("#nloginpwd").focus();
				return false;
			}
			return true;
		},
		doLogin:function() {
			$.ajax({
				url: "/user/login", 
			    contentType: "application/json; charset=utf-8",
				data:$("#formlogin").serialize(),
				type: "post",
	            dataType: 'json',
	            success: function (data) {
	            	if (data.status == 200) {
						alert("登录成功！");
						if (redirectUrl == "") {
							location.href = "http://www.flyjava.com";
						} else {
							location.href = redirectUrl;
						}
					} else {
						alert("登录失败，原因是：" + data.msg);
						$("#loginname").select();
					}  
	            }
			});
		},
		/*doLogin:function() {
			$.post("/user/login", $("#formlogin").serialize(),function(data){
				if (data.status == 200) {
					alert("登录成功！");
					if (redirectUrl == "") {
						location.href = "http://www.flyjava.com";
					} else {
						location.href = redirectUrl;
					}
				} else {
					alert("登录失败，原因是：" + data.msg);
					$("#loginname").select();
				}
			});
		},*/
		login:function() {
			if (this.checkInput()) {
				this.doLogin();
			}
		}
	
};
$(function(){
	$("#loginsubmit").click(function(){
		LOGIN.login();
	});
});


/*注册*/
var REGISTER={
	param:{
		//单点登录系统的url
		surl:""
	},
	inputcheck:function(){
			//不能为空检查
			if ($("#regName").val() == "") {
				alert("用户名不能为空");
				$("#regName").focus();
				return false;
			}
			if ($("#pwd").val() == "") {
				alert("密码不能为空");
				$("#pwd").focus();
				return false;
			}
			if ($("#phone").val() == "") {
				alert("手机号不能为空");
				$("#phone").focus();
				return false;
			}
			//密码检查
			if ($("#pwd").val() != $("#pwdRepeat").val()) {
				alert("确认密码和密码不一致，请重新输入！");
				$("#pwdRepeat").select();
				$("#pwdRepeat").focus();
				return false;
			}
			/* 手机号有限性正则表达式验证 */
			var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
			if(!myreg.test($("#phone").val())) 
			{ 
			    alert('请输入有效的手机号码！'); 
			    return false; 
			} 
			return true;
	},
	beforeSubmit:function() {
			//检查用户是否已经被占用
			$.ajax({
            	url : REGISTER.param.surl + "/user/check/"+escape($("#regName").val())+"/1?r=" + Math.random(),
            	contentType: "application/json; charset=utf-8",
            	success : function(data) {
            		if (data.data) {
            			//检查手机号是否存在
            			$.ajax({
            				url : REGISTER.param.surl + "/user/check/"+$("#phone").val()+"/2?r=" + Math.random(),
			            	success : function(data) {
			            		if (data.data) {
				            		REGISTER.doSubmit();
			            		} else {
			            			alert("此手机号已经被注册！");
			            			$("#phone").select();
			            		}
			            	}
            			});
            		} else {
            			alert("此用户名已经被占用，请选择其他用户名");
            			$("#regName").select();
            		}	
            	}
			});
            	
	},
	doSubmit:function() {
		$.post("/user/register",$("#personRegForm").serialize(), function(data){
			if(data.status == 200){
				alert('用户注册成功，请登录！');
				REGISTER.login();
			} else {
				alert("注册失败！");
			}
		});
	},
	login:function() {
		 location.href = "/user/login";
		 return false;
	},
	reg:function() {
		if (this.inputcheck()) {
			this.beforeSubmit();
		}
	}
};