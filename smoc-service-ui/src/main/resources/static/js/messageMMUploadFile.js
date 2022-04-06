$(document).ready(function(){
	var contextPath =$("#path").val();
	var messageTypeInit = $("#messageType").val();
	var initFilePath =$("#originalAttachment").val();
	var fileSize = $("#originalAttachmentSize").val();
	var initFiles =[];

	if(initFilePath !="" && initFilePath!=null ){
		var type=initFilePath.substring(initFilePath.lastIndexOf(".")+1);
		var name = "号码文件."+type;
		initFiles = [{
			name: name,
			type: type,
			size:fileSize,
			file: ""
		}];
	}
	$("#filer_name").filer({
		limit: 1,
		maxSize: 10,
		extensions:["txt","xls","xlsx"],
		files: initFiles,
		changeInput: '<div class="jFiler-input-dragDrop"><div class="jFiler-input-inner"><div class="jFiler-input-icon"><i class="icon-jfi-cloud-up-o"></i></div><div class="jFiler-input-text"><h3>Drag & Drop files here</h3> <span style="display:inline-block; margin: 15px 0">or</span></div><a class="jFiler-input-choose-btn btn btn-custom waves-effect waves-light">上传附件</a></div></div>',
		showThumbs: true,
		theme: "dragdropbox",
		templates: {
			box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
			item: '<li class="jFiler-item">\
                        <div class="jFiler-item-container">\
                            <div class="jFiler-item-inner">\
                                <div class="jFiler-item-thumb">\
                                    <div class="jFiler-item-status"></div>\
                                    <div class="jFiler-item-info">\
                                        <span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
                                        <span class="jFiler-item-others">{{fi-size2}}</span>\
                                    </div>\
                                    {{fi-image}}\
                                </div>\
                                <div class="jFiler-item-assets jFiler-row">\
                                <ul class="list-inline pull-left">\
                                    <li>{{fi-progressBar}}</li>\
                                </ul>\
                                <ul class="list-inline pull-right">\
                                    <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
                                </ul>\
                            </div>\
                            </div>\
                        </div>\
                    </li>',
			itemAppend: '<li class="jFiler-item">\
                            <div class="jFiler-item-container">\
                                <div class="jFiler-item-inner">\
                                    <div class="jFiler-item-thumb">\
                                        <div class="jFiler-item-status"></div>\
                                        <div class="jFiler-item-info">\
                                            <span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
                                            <span class="jFiler-item-others">{{fi-size2}}</span>\
                                        </div>\
                                        {{fi-image}}\
                                    </div>\
                                    <div class="jFiler-item-assets jFiler-row">\
                                        <ul class="list-inline pull-left">\
											<li><span class="jFiler-item-others">{{fi-icon}}</span></li>\
										</ul>\
										<ul class="list-inline pull-right">\
											<li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
										</ul>\
                                    </div>\
                                </div>\
                            </div>\
                        </li>',
			progressBar: '<div class="bar"></div>',
			itemAppendToEnd: false,
			removeConfirmation: true,
			_selectors: {
				list: '.jFiler-items-list',
				item: '.jFiler-item',
				progressBar: '.bar',
				remove: '.jFiler-item-trash-action'
			}
		},
		dragDrop: {
			dragEnter: null,
			dragLeave: null,
			drop: null,
		},
		uploadFile: {
			url: contextPath+"/message/mm/uploadFile/"+messageTypeInit ,
			type: 'POST',
			contentType: false, //不设置内容类型
			processData: false, //不处理数据
			dataType:"json",
			success: function(data, el){
				//alert(data);
				var code = data.code;
				//alert("code:"+code);
				if(code!=null&&code.length>0){
					var filePath = data.filePath;
					var sendFilePath = data.sendFilePath;
					var errorFilePath = data.errorFilePath;
					$("#sendNumberId").val(data.sendNumber);
					$("#originalAttachmentSize").val(data.originalAttachmentSize);

					if(code=="-1"){
						$("#tip-div").find("#tip-content").html("号码文件必须为xlsx、xls、txt格式");
						$("#tip-div").modal();
						return;
					}

					if(errorFilePath&&errorFilePath!=null&&errorFilePath.length>0) {
						//alert("有异常号码");
						$("#tip-div").find("#tip-content").html("有异常号码");
						$("#tip-div").modal();

						var listr = '<li class="jFiler-item jFiler-no-thumbnail" data-jfiler-index="0" style="">\
								<div class="jFiler-item-container">\
								<div class="jFiler-item-inner">\
								<div class="jFiler-item-thumb">\
								<div class="jFiler-item-status"></div>\
								<div class="jFiler-item-thumb-image">\
								<span class="jFiler-icon-file f-file f-file-ext-txt" style="-webkit-box-shadow: #709c27 42px -55px 0px 0px inset; -moz-box-shadow: #709c27 42px -55px 0px 0px inset; box-shadow: #709c27 42px -55px 0px 0px inset;">.txt</span>\
							</div>\
							</div>\
							<div class="jFiler-item-assets jFiler-row">\
								<ul class="list-inline pull-left">\
								<li>\
								<div class="jFiler-jProgressBar" style="display: none;">\
								<div class="bar" style="width: 100%;"></div>\
								</div>\
								<div class="jFiler-item-others text-error" style="">\
								异常号码内容文件\
								</div>\
								</li>\
								</ul>\
								<ul class="list-inline pull-right">\
								<li>\
								<a class="on-default edit-row" target="_blank" href="/service/message/downloadErrorFile/errorFilePath" style="width:25px ;padding-right: 5px;color: #29b6f6;" title="下载">\
								<i style="font-size: 18px" class="mdi mdi-download"></i>\
								</a>\
								</li>\
								</ul>\
								</div>\
								</div>\
								</div>\
								</li>'

								$(".jFiler-items-grid").append(listr);
					}
					$("#originalAttachment").val(filePath);
					$("#sendFilePath").val(sendFilePath);


					var parent = el.find(".jFiler-jProgressBar").parent();
					el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
						$("<div class=\"jFiler-item-others text-success\"></div>").hide().appendTo(parent).fadeIn("slow");
					});
				}else{
					var parent = el.find(".jFiler-jProgressBar").parent();
					el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
						$("<div class=\"jFiler-item-others text-error\"><i class=\"icon-jfi-minus-circle\"></i> Error</div>").hide().appendTo(parent).fadeIn("slow");
					});
				}

			},
			error: function(el){
				var parent = el.find(".jFiler-jProgressBar").parent();
				el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
					$("<div class=\"jFiler-item-others text-error\"><i class=\"icon-jfi-minus-circle\"></i> Error</div>").hide().appendTo(parent).fadeIn("slow");
				});
			},
			statusCode: null,
			onProgress: null,
			onComplete: null
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
		onRemove: function(){$("#originalAttachment").val('');$("#sendNumberId").val(0);},
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
