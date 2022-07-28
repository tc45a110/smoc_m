/**
 * 控制登录按钮，及提交按钮
 */
var publicKey = null;
$(document).ready(
	function() {
		/*$.ajax({
			url: "/admin/getPublicKey" ,
			success: function (data) {
				if(data){
					publicKey = data;
				};
				if(publicKey==null){
					return;
				};

			}, error: function (data) {
				console.log("error");
			}
		});*/

		$('#login_submit').on(
			'click',
			function() {
				if (null == $("#userName").val()
					|| "" == $("#userName").val()) {
					return false;
				}
				if (null == $("#password").val()
					|| "" == $("#password").val()) {
					return false;
				}

				if($("#password").val().length <6){
					return false;
				}


				var verify = $("#verify").val();
				if(null !=verify && "" != verify){
					if (null == $("#verifyCode").val()
						|| "" == $("#verifyCode").val()) {
						return false;
					}

					if($("#verifyCode").val().length !=4){
						return false;
					}
				}
				$("#login_submit").attr('disabled', 'disabled').html(
					'登录中...');
				$('#preloader').css('display', 'block');
				saveUserInfo();
				setTimeout(function() {
					if(""!=publicKey && null!=publicKey && "null"!=publicKey){
						var encrypt = new JSEncrypt();
						encrypt.setPublicKey(publicKey);
						var username = $("#userName").val()
						var password = $("#password").val()
						$("#userName").val(encrypt.encrypt(username));
						$("#password").val(encrypt.encrypt(password));
					}

					$("#login-form").submit();
				}, 2000);
			});
	});

/**
 * 按下回车键
 */
$(function() {
	document.onkeydown = function(e) {
		var ev = document.all ? window.event : e;

		if (ev.keyCode == 13) {
			if (null == $("#userName").val()
				|| "" == $("#userName").val()) {
				return false;
			}
			if (null == $("#password").val()
				|| "" == $("#password").val()) {
				return false;
			}

			if($("#password").val().length <6){
				return false;
			}

			var verify = $("#verify").val();
			if(null !=verify && "" != verify){
				if (null == $("#verifyCode").val()
					|| "" == $("#verifyCode").val()) {
					return false;
				}

				if($("#verifyCode").val().length !=4){
					return false;
				}
			}
			$("#login_submit").attr('disabled', 'disabled').html('登录中...');
			$('#preloader').css('display', 'block');
			saveUserInfo();
			setTimeout(function() {
				if(""!=publicKey && null!=publicKey && "null"!=publicKey){
					var encrypt = new JSEncrypt();
					encrypt.setPublicKey(publicKey);
					var username = $("#userName").val()
					var password = $("#password").val()
					$("#userName").val(encrypt.encrypt(username));
					$("#password").val(encrypt.encrypt(password));
				}
				$("#login-form").submit();
			}, 2000);
		}
	}
});

/**
 * 处理cookie 读取cookie值
 */
$(document).ready(function() {

	if ($.cookie("rmbAuth") == "true") {
		$("#rmbAuth").attr("checked", true);
		$("#userName").val($.cookie("authUserName"));
	}
});

/**
 * 保存或删除cookie
 *
 * @returns
 */
function saveUserInfo() {

	var isChecked = $('#rmbAuth').is(":checked");
	if (isChecked) {
		var userName = $("#userName").val();
		$.cookie('rmbAuth', 'true', {
			expires : 7
		});
		$.cookie("authUserName", userName, {
			expires : 7
		}); // 存储一个带7天期限的 cookie
	} else {
		$.cookie("rmbAuth", "false", {
			expires : -1
		});// 删除 cookie
		$.cookie("authUserName", '', {
			expires : -1
		});
	}
}

//隐藏进度项
$('#preloader').css('display', 'none');