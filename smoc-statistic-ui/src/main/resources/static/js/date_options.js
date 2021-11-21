function init_daterange_picker(id, startPickerDate, endPickerDate) {
	
	if (null == startPickerDate || '' == startPickerDate) {
		startPickerDate = moment().subtract(29, 'days');
	}
	if (null == endPickerDate || '' == endPickerDate) {
		endPickerDate = moment();
	}
	
	id.daterangepicker({
				format : 'YYYY-MM-DD',
				startDate : startPickerDate,
				endDate : endPickerDate,
				dateLimit : {
					days : 365
				},
				showDropdowns : true,
				showWeekNumbers : true,
				timePicker : false,
				timePickerIncrement : 1,
				timePicker12Hour : false,
				ranges : {
					'今天' : [ moment(), moment() ],
					'昨天' : [ moment().subtract(1, 'days'),
							moment().subtract(1, 'days') ],
					'最近7天' : [ moment().subtract(6, 'days'), moment() ],
					'最近30天' : [ moment().subtract(29, 'days'), moment() ],
					'本月' : [ moment().startOf('month'), moment().endOf('month') ],
					'上个月' : [ moment().subtract(1, 'month').startOf('month'),
							moment().subtract(1, 'month').endOf('month') ]
				},
				opens : 'right',
				buttonClasses : [ 'btn btn-default' ],
				applyClass : 'btn-small btn-primary',
				cancelClass : 'btn-small',
				format : 'YYYY-MM-DD',
				separator : ' - ',
				locale : {
					applyLabel : '确定',
					cancelLabel : '关闭',
					fromLabel : '从',
					toLabel : '到',
					customRangeLabel : '自定义',
					daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
					monthNames : [ '01月', '02月', '03月', '04月', '05月', '06月',
							'07月', '08月', '09月', '10月', '11月', '12月' ],
					firstDay : 1
				}
			});
}


function init_select_month_picker(id, startPickerDate) {
	id.daterangepicker({  
		opens : 'right',
		showDropdowns: true,
		singleDatePicker: true,
		
		locale : {
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '01月', '02月', '03月', '04月', '05月', '06月',
					'07月', '08月', '09月', '10月', '11月', '12月' ],
			format : 'YYYY-MM' 
		}	
    }); 
}

function init_select_riqi_picker(id) {
	id.daterangepicker({  
		opens : 'right',
		showDropdowns: true,
		singleDatePicker: true,
		
		locale : {
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '01月', '02月', '03月', '04月', '05月', '06月',
					'07月', '08月', '09月', '10月', '11月', '12月' ],
			format : 'YYYY-MM-DD' 
		}	
    }); 
}

function init_sales_detail_picker(startPickerDate, endPickerDate) {
	if ("undefined" != typeof $.fn.daterangepicker) {
		
		var a = function(a, b, c) {
			$("#reportrange span").html(a.format("YYYY-MM-DD") + " - " + b.format("YYYY-MM-DD"))
		},
		b = {
			startDate : startPickerDate,
			endDate : endPickerDate,
			dateLimit : {
				days : 365
			},
			showDropdowns : true,
			showWeekNumbers : true,
			timePicker : false,
			timePickerIncrement : 1,
			timePicker12Hour : true,
			ranges : {
				'今天' : [ moment(), moment() ],
				'昨天' : [ moment().subtract(1, 'days'),
						moment().subtract(1, 'days') ],
				'最近7天' : [ moment().subtract(6, 'days'), moment() ],
				'最近30天' : [ moment().subtract(29, 'days'), moment() ],
				'本月' : [ moment().startOf('month'), moment().endOf('month') ],
				'上个月' : [ moment().subtract(1, 'month').startOf('month'),
						moment().subtract(1, 'month').endOf('month') ]
			},
			opens : 'right',
			buttonClasses : [ 'btn btn-default' ],
			applyClass : 'btn-small btn-primary',
			cancelClass : 'btn-small',
			format : 'YYYY-MM-DD',
			separator : ' - ',
			locale : {
				applyLabel : '确定',
				cancelLabel : '关闭',
				fromLabel : '从',
				toLabel : '到',
				customRangeLabel : '自定义',
				daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
				monthNames : [ '01月', '02月', '03月', '04月', '05月', '06月',
						'07月', '08月', '09月', '10月', '11月', '12月' ],
				firstDay : 1
			}
		};
		$("#reportrange span").html(moment().subtract(29, "days").format("YYYY-MM-DD") + " - " + moment().format("YYYY-MM-DD")),
		$("#reportrange").daterangepicker(b, a)
	}
	
}

