/*
 * require：jquery.js,sweet-alert.min.js
 * by wangchao 2011/10/10
 */
(function($){
	$.exAjax = function(options){
		options = options || {};
		options.type = options.type || "POST";
		options.cache = false;
		options.dataType = options.dataType || "json";
//		options.success = function(res){
//						
//		};
//		options.error = options.error || function(XMLHttpRequest, textStatus, errorThrown){
//			alert(errorThrown);
//		}
		$.ajax(options);
	};
	$.fn.serializeObject = function() {  
	   var o = {};  
	   var a = this.serializeArray();  
	   $.each(a, function() {  
	       if (o[this.name]) {  
	           if (!o[this.name].push) {  
	               o[this.name] = [o[this.name]];  
	           }  
	           o[this.name].push(this.value || '');  
	       } else {  
	           o[this.name] = this.value || '';  
	       }  
	   });  
	   return o;  
	};
	$.fn.getFilters = function(){
		var str = "";
		var order = "";
		var or = "";
		var $searchs = $(":input",this).not(":button,:submit,:checkbox,:radio");
		$searchs = $searchs.add(":checked",this);
		for ( var i = 0; i < $searchs.length; i++) {
			var tmpvalue = $.trim($searchs.eq(i).get(0).value);
			tmpvalue = tmpvalue.replace(/'/g,"''");
			var rule = $searchs.eq(i).attr("data-rule");
			if (tmpvalue != "") {
				if (rule == "like") {
					str += " and " + $searchs.eq(i).attr("id")
							+ " like '%" + tmpvalue + "%'";
				}
				if (rule == "lk") {
					str += " and " + $searchs.eq(i).attr("id")
							+ " like '" + tmpvalue.replace(/\*/gi,"%").replace(/\?/gi,"_") + "'";
				}
				if (rule == "eq") {
					str += " and " + $searchs.eq(i).attr("id")
							+ " = '" + tmpvalue + "'";
				}
				if (rule == "le") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " <= '" + tmpvalue + "'";
				}
				//dtle为时间类型时，加上 23:59:59以保证取到选择日期当天的数值
				if (rule == "dtle") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " <= '" + tmpvalue + " 23:59:59'";
				}
				if (rule == "ge") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " >= '" + tmpvalue + "'";
				}	
				if (rule == "lt") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " < '" + tmpvalue + "'";
				}
				if (rule == "gt") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " > '" + tmpvalue + "'";
				}	
				if (rule == "in") {
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " in (" + tmpvalue + ") ";
				}
				if (rule == "daylimit") {
					var now = new Date();
					var dlimit = new Date(now.getTime()-parseInt(tmpvalue)*24*3600*1000).toformat();
					str += " and " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
							+ " >= '" + dlimit + "'";
				}			
				if (rule == "or") {
					or += " or " + $searchs.eq(i).attr("id").replace(/start_|end_/gi,"")
					+ " = '" + tmpvalue + "'";
				}
				if (rule == "order") {
					order += " order by " + tmpvalue;
				}
			}
		}
		if(or!=''){
			or = ' and ('+or.substr(or.indexOf('or')+2)+')';
		}
		return str+or+order;
	}
})(jQuery);
//获得coolie 的值
function getCookie(objName){//获取指定名称的cookie的值
    var arrStr = document.cookie.split("; ");
    for(var i = 0;i < arrStr.length;i ++){
        var temp = arrStr[i].split("=");
        if(temp[0] == objName) return unescape(temp[1]);
    }
    return "";
} 
//添加cookie
function setCookie(objName,objValue,objHours){      
    var str = objName + "=" + escape(objValue);
    if(objHours > 0){                             //为时不设定过期时间，浏览器关闭时cookie自动消失
        var date = new Date();
        var ms = objHours*3600*1000;
        date.setTime(date.getTime() + ms);
        str += "; expires=" + date.toGMTString();
    }
    document.cookie = str;
}
//用正则取cookies函数  
function getCookieReg(name){
     var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); 
	 return "";
}
//删除cookie
function delCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
/* 页面通用方法开始 */
//通用列表删除方法
var listPage = {
	config:{
		listUrl:"",
		delUrl:""
	},
	deleteTr:function(trid,rowid){
		layer.confirm('确认要删除此项？', {icon: 3}, function(index){
		    layer.close(index);
			var loadIndex = layer.load();
		    $.post(listPage.config.delUrl,{id:rowid},function(data){
				if(data.msg="success"){
					layer.msg('删除成功!');
					$("#"+trid).remove();
				}
				else{
					layer.msg('删除失败!', {icon: 5});
				}
				layer.close(loadIndex);
			});	
		});
	},
	gopage:function(rp,page){
		var loadIndex = layer.load();
		$.exAjax({
			url : listPage.config.listUrl,
			data : {param:$(".filter").getFilters(),page:page,rp:rp},
			success : listPage.showlist,
			error : function() {
				layer.msg('获取数据出错!', {icon: 5});
			},
			complete:function(){layer.close(loadIndex);}
		});
	},
	showlist:function(data){}
};
//通用列表查询方法
