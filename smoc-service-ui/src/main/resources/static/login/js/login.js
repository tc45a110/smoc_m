/**
 * 控制登录按钮，及提交按钮
 */
var publicKey = null;
$(document).ready(
	function() {

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

				$("#login_submit").attr('disabled', 'disabled').html(
					'登录中...');
				saveUserInfo();
				setTimeout(function() {
					$("#form-login").submit();
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

			$("#login_submit").attr('disabled', 'disabled').html('登录中...');
			saveUserInfo();
			setTimeout(function() {
				$("#form-login").submit();
			}, 2000);
		}
	}
});

/**
 * 处理cookie 读取cookie值
 */
$(document).ready(function() {
	if ($.cookie("login-remember-me") == "true") {
		$("#login-remember-me").attr("checked", true);
		$("#userName").val($.cookie("userName"));
	}
});

/**
 * 保存或删除cookie
 *
 * @returns
 */
function saveUserInfo() {

	var isChecked = $('#login-remember-me').is(":checked");
	if (isChecked) {
		var userName = $("#userName").val();
		$.cookie('login-remember-me', 'true', {
			expires : 7
		});
		$.cookie("userName", userName, {
			expires : 7
		}); // 存储一个带7天期限的 cookie
	} else {
		$.cookie("login-remember-me", "false", {
			expires : -1
		});// 删除 cookie
		$.cookie("userName", '', {
			expires : -1
		});
	}
}
