$(document).ready(function(){
	var contextPath =$("#path").val();
	if(resourceFileSizeLimit==null||resourceFileSizeLimit.length==0){
		resourceFileSizeLimit = "80";
	}
	var fileSizeLimit_m = parseFloat(resourceFileSizeLimit)/1024;
	fileSizeLimit_m = Math.floor(fileSizeLimit_m*100)/100;

	var fileTypes = [];
	var fileFormat = imgTypes+","+audioTypes+","+videoTypes;
	if(fileFormat!=null&&fileFormat.length>0){
		fileTypes = fileFormat.split(",");
	}

	var initFiles = [];
	var resourceAttchmentSize = $("#resourceAttchmentSize").val();
	var resourceAttchmentType = $("#resourceAttchmentType").val();
	var resourceTitle = $("#resourceTitle").val();

	if(imgTypes.indexOf(resourceAttchmentType)!=-1){
		resourceAttchmentType = "image/"+resourceAttchmentType;
	}
	var fileUrl = contextPath+"/resource/download/"+$("#id").val();

	if(resourceAttchmentSize>0){
		initFiles = [{
			name: resourceTitle,
			size: resourceAttchmentSize,
			type: resourceAttchmentType,
			file: fileUrl
		}];
	}

	var drugFileName = "";
	var drugFileSize = "";
	var drugFileType = "";

	$("#filer_name1").filer({
		limit: 1,
		maxSize: fileSizeLimit_m,
		files: initFiles,
		extensions: fileTypes,
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
			drop: function (e) {
				drugFileName = e[0].name;
				drugFileType = e[0].type;
				drugFileSize = e[0].size;
			},
		},
		uploadFile: {
			url: contextPath+"/resource/upload",
			type: 'POST',
			contentType: false, //不设置内容类型
			processData: false, //不处理数据
			dataType:"json",
			success: function(data, el){
				if(data!=null&&data.length>0){
					if(data.toString().startsWith("上传失败")){
						$("#alert-modal").find("#alert-message").html(data.toString());
						$("#alert-modal").modal();
						return;
					}

					var fileUpload = null;
					var fileName = null;
					var fileType = null;
					var fileSize = 0;
					var resourceHeight = 0;
					var resourceWidth = 0;
					if($('#filer_name1')[0].files.length>0){
						fileUpload = $('#filer_name1')[0].files[0];
						fileName = fileUpload.name;
						fileSize = fileUpload.size;
						fileType = fileUpload.type;
						if(fileType&&fileType!=null&&fileType.startsWith("image")){
							var imgUpload = $(".jFiler-row").find("img").eq(0);
							resourceWidth = imgUpload.width();
							resourceHeight = imgUpload.height();
						}
					}else{
						fileName = drugFileName;
						fileSize = drugFileSize;
						fileType = drugFileType;

						fileUpload = $(".jFiler-item-thumb-image").eq(0).find("img");
						resourceWidth = fileUpload.width();
						resourceHeight = fileUpload.height();
					}
					var suffixName = fileName.split("\.")[1];

					$("#space_id").hide();
					$("#resourceAttchment").val(data.toString());
					$("#resourceAttchmentSize").val(fileSize);
					$("#resourceAttchmentType").val(suffixName);
					$("#resourceHeight").val(resourceHeight);
					$("#resourceWidth").val(resourceWidth);

					var parent = el.find(".jFiler-jProgressBar").parent();
					el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
						$("<div class=\"jFiler-item-others text-success\"><i class=\"icon-jfi-check-circle\"></i> Success</div>").hide().appendTo(parent).fadeIn("slow");
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
		onRemove: function(){$("#resourceAttchment").val('');},
		options: null,
		captions: {
			button: "Choose Files",
			feedback: "Choose files To Upload",
			feedback2: "files were chosen",
			removeConfirmation: "确定移除该文件?",
			errors: {
				filesLimit: "只能上传 {{fi-limit}} 个资源文件",
				filesType: "资源文件不符合格式，必须为"+fileFormat+"文件",
				filesSize: "资源文件大小不能超过"+resourceFileSizeLimit+"KB.",
				filesSizeAll: "Files you've choosed are too large! Please upload files up to {{fi-maxSize}} MB."
			}
		}
	});
});