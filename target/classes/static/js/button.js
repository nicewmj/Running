function back() {
    setTimeout(function () {
        base.back();
    }, 1000);
}


//function datetimeFormat_1(longTypeDate){
//  var datetimeType = "";
//  var date = new Date();
//  date.setTime(longTypeDate);
//  datetimeType+= date.getFullYear();   //年
//  datetimeType+= "-" + getMonth(date); //月
//  datetimeType += "-" + getDay(date);   //日
//  datetimeType+= "&nbsp;&nbsp;" + getHours(date);   //时
//  datetimeType+= ":" + getMinutes(date);      //分
//  datetimeType+= ":" + getSeconds(date);      //分
//  return datetimeType;
//}
////返回 01-12 的月份值
//function getMonth(date){
//  var month = "";
//  month = date.getMonth() + 1; //getMonth()得到的月份是0-11
//  if(month<10){
//      month = "0" + month;
//  }
//  return month;
//}
////返回01-30的日期
//function getDay(date){
//  var day = "";
//  day = date.getDate();
//  if(day<10){
//      day = "0" + day;
//  }
//  return day;
//}
////返回小时
//function getHours(date){
//  var hours = "";
//  hours = date.getHours();
//  if(hours<10){
//      hours = "0" + hours;
//  }
//  return hours;
//}
////返回分
//function getMinutes(date){
//  var minute = "";
//  minute = date.getMinutes();
//  if(minute<10){
//      minute = "0" + minute;
//  }
//  return minute;
//}
////返回秒
//function getSeconds(date){
//  var second = "";
//  second = date.getSeconds();
//  if(second<10){
//      second = "0" + second;
//  }
//  return second;
//}

function page(data) {

    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage
            , layer = layui.layer;

        laypage.render({
            elem: 'demo5'
            , count: data.COUNT
            , curr: data.PAGE_NUM
            , limit: 10
            , limits: [100, 300, 500]
            , jump: function (obj, first) {
                data.PAGE_NUM = obj.curr; //获取到当前的页数
                if (!first) {
                    initPayment(obj.curr);

                }
            }
        });

    });
}

function currentPage(data, count, pageNum) {
    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage
            , layer = layui.layer;
        var pages = $("#pages").val();
        laypage.render({
            elem: 'demo5'
            , count: count
            , curr: pageNum
            , limit: 10
            , limits: [100, 300, 500]
            , jump: function (obj, first) {
                data.pageNum = obj.curr; //获取到当前的页数
                if (!first) {
                    initPayment(obj.curr);

                }
            }
        });

    });
}


function getnowPage() {
    var his = sessionStorage.getItem("his");
    var test = sessionStorage.getItem("history");
    var historys = test.split(',');
    var url = historys[historys.length - 1];
    if (his == url) {
        var nowPage = sessionStorage.getItem("nowPage");
        sessionStorage.removeItem("nowPage");
        initPayment(nowPage);
    } else {
        sessionStorage.removeItem("nowPage");
        initPayment(1);
    }
}

function setnowPage() {
    var test = sessionStorage.getItem("history");
    var historys = test.split(',');
    var url = historys[historys.length - 1];
    sessionStorage.setItem("his", url);
    sessionStorage.setItem("nowPage", $('#curr').val());
}


function page2(a, b) {

    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage
            , layer = layui.layer;

        laypage.render({
            elem: 'demo5'
            , count: a
            , curr: b
            , limit: 8
            , limits: [100, 300, 500]
            , jump: function (obj, first) {
                b = obj.curr; //获取到当前的页数
                if (!first) {
                    initPayment(obj.curr);
                }
            }
        });

    });
}

function pages() {
    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage
            , layer = layui.layer;
        var pages = $("#pages").val();
        laypage.render({
            elem: 'demo5'
            , count: pages
            , limit: 10
            , limits: [100, 300, 500]
            , jump: function (obj, first) {
                if (!first) {
                    initPayment(obj.curr);
                }
            }
        });

    });
}


//$(function () {
//
//    setTimeout(function(){
//	page();
//}, 1000);
//
// });

