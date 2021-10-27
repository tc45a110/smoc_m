
function exp(){
	var reg = /^[0-9]*$/;
	var minValue = $("#minValue").val();
	var maxValue = $("#maxValue").val();
	var pages = $("#pages").val();
	if(minValue == "" || minValue == null || minValue == undefined || maxValue == "" || maxValue == null || maxValue == undefined){
		alert("请输入页数");
		return false;
	}
	if (!reg.test(minValue) || !reg.test(maxValue)) {
		alert("页数格式有误");
		return false;
	}
	if(Number(minValue)<=0 || Number(maxValue)<=0){
		alert("页数必须大于0");
		return false;
	}
	if(Number(minValue)>pages || Number(maxValue)>pages){
		alert("输入页数不能大于总页数");
		return false;
	}
	if(Number(minValue)>Number(maxValue)){
		alert("起始页不能大于结束页");
		return false;
	}

	if((Number(maxValue)-Number(minValue))>=10000){
		alert("导出数据不能大于10万条")
		return false;
	}

	document.getElementById('submit_form').submit();
}