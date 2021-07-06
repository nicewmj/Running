////弹出层
///* 获取首页、下一页、上一页、尾页、页码按钮等 */
//var pageList_2 = document.getElementById("page-num-2");
//var first_2 = document.getElementById("first-page-2");
//var last_2 = document.getElementById("last-page-2");
//var previous_2 = document.getElementById("pre-page-2");
//var next_2 = document.getElementById("next-page-2");
//var list_2 = pageList_2.getElementsByTagName("li");
//
//function getPage() {
//  /* 获取分页信息 */
//  var pages_2 = $("#pages").val();
//  var pageNum_2 = $("#pageNum").val();
//  var firstPage_2 = $("#firstPage").val();
//  var lastPage_2 = $("#lastPage").val();
//  var prePage_2 = $("#prePage").val();
//  var nextPage_2 = $("#nextPage").val();
//  var isFirstPage_2 = $("#isFirstPage").val();
//  var isLastPage_2 = $("#isLastPage").val();
//  pages_2 = lastPage_2;
//  /*动态增加页码按钮*/
//  if (pageNum_2 >= 5) {
//      removeElements(5);
//      var number = parseInt(pageNum_2);
//      for (var k = number - 3; k < number + 2; k++) {
//          creatElements(k, pageList_2);
//      }
//      setStyle(pageNum_2, list_2);
//  } else {
//      removeElements(5);
//      if (pages_2 > 5) {
//          for (var i = 0; i < 5; i++) {
//              creatElements(i, pageList_2);
//          }
//      } else {
//          for (var j = 0; j < pages_2; j++) {
//              creatElements(j, pageList_2);
//          }
//      }
//      setStyle(pageNum_2, list_2);
//  }
//
//  if (isFirstPage_2 == true) {
//      previous_2.className = "disabled";
//      first_2.className = "disabled";
//  } else {
//      previous_2.className = "";
//      first_2.className = "";
//  }
//  /*末页，下一页禁用*/
//  if (isLastPage_2 == true) {
//      next_2.className = "disabled";
//      last_2.className = "disabled";
//  } else {
//      next_2.className = "";
//      last_2.className = "";
//  }
//}
//
////  /*首页，上一页禁用*/
//first_2.onclick = function () {
//  queryList(1);
//};
//
//previous_2.onclick = function () {
//  queryList($("#pageNum").val() - 1);
//};
//
//next_2.onclick = function () {
//  queryList(parseInt($("#pageNum").val()) + 1);
//};
//
//last_2.onclick = function () {
//  queryList($("#pages").val());
//};
//
//
//function setStyle(cur, obj) {
//  /*移除所有样式*/
//  //for (var i = 0; i < cur; i++) {
//      //obj[i].className = null;
//  //}
//  /*为当前按钮添加样式*/
//  //obj[cur - 1].className = "active";
//}
//
///*动态增加按钮函数*/
//function creatElements(x, obj) {
//  var liObj = document.createElement("li");
//  var aObj = document.createElement("a");
//  aObj.innerText = x + 1;
//  obj.appendChild(liObj);
//  liObj.appendChild(aObj);
//  aObj.onclick = function () {
//      queryList(this.innerText);
//  };
//}
//
///*动态删除*/
//function removeElements() {
//  var elementById = document.getElementById("page-num-2");
//  var lis = elementById.getElementsByTagName("li");
//  var len = $("#page-num-2").children().length;
//  if (len > 0) {
//      for (var i = 0; i < len; i++) {
//          elementById.removeChild(lis[0]);
//      }
//  }
//}

function page(data) {
    layui.use(['laypage', 'layer'], function () {
        var laypage = layui.laypage
            , layer = layui.layer;

        var pages = $("#pages").val();
        laypage.render({
            elem: 'demo5'
            , count: pages
            , curr: data.pageNum
            , limit: 10
            , limits: [100, 300, 500]
            , jump: function (obj, first) {
                data.pageNum = obj.curr; //获取到当前的页数
                if (!first) {
                    queryList(obj.curr);
                }
            }
        });

    });
}


/*$(function () {
	
      setTimeout(function(){	
	page();
}, 1000);

   });	*/
	
