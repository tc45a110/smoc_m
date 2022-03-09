//平台、设备和操作系统
  var system = {
   win: false,
   mac: false,
   xll: false,
   ipad: false
  };
  //当前访问路径
  var url =  window.location.pathname;
  var mobile = url.indexOf("mobile")
//  alert(mobile);
  //检测平台
  var p = navigator.platform;
  system.win = p.indexOf("Win") == 0;
  system.mac = p.indexOf("Mac") == 0;
  system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
  system.ipad = (navigator.userAgent.match(/iPad/i) != null) ? true : false;
  //跳转语句，如果是手机访问就自动跳转到wap.baidu.com页面
  if (system.win || system.mac || system.xll) {
     //包含mobile字符串
     if(mobile != -1){
        window.location.href = "/index.html";
     }
  } else {
     //不包含mobile字符串
      if(mobile == -1){
         window.location.href = "/mobile/mobile.html";
      }
//   var ua = navigator.userAgent.toLowerCase();
//   if(ua.match(/MicroMessenger/i)=="micromessenger") {
//    alert("在手机端微信上打开的");
//   } else {
//    alert("在手机上非微信上打开的");
//   }
  }