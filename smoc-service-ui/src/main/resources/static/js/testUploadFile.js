$(document).ready(function(){

	$("#filer_name").filer({
		limit: 1,
		maxSize: 10,
		extensions:["txt","xls","xlsx"],

		changeInput: '<a class="jFiler-input-choose-btn btn btn-custom waves-effect waves-light">上传附件</a>',
		showThumbs: true,

		uploadFile: {
			url: contextPath+"/message/uploadFile/"+messageTypeInit ,
			type: 'POST',
			contentType: false, //不设置内容类型
			processData: false, //不处理数据
			dataType:"json",
			success: function(data, el){


			},

		},
		addMore: false,
		clipBoardPaste: true,
		excludeName: null,
		beforeRender: null,
		afterRender: null,
		beforeShow: null,
		beforeSelect: null,
		onSelect: null,
		afterShow: null,
		onEmpty: null,
		options: null,
		captions: {
			button: "Choose Files",
			feedback: "Choose files To Upload",
			feedback2: "files were chosen",
			removeConfirmation: "确定移除该文件?",
			errors: {
				//filesLimit: "只能上传 {{fi-limit}} 个号码文件",
				filesLimit: "请先删除旧号码文件再上传",
				filesType: "号码文件必须为xlsx、xls、txt格式",
				filesSize: "号码文件大小不能超过 {{fi-maxSize}} MB.",
				filesSizeAll: "Files you've choosed are too large! Please upload files up to {{fi-maxSize}} MB."
			}
		}
	});
});

