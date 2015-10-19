/*
	use:仿百度帖吧的翻页前台代码
	用法--
		$(".pagefooter").page(
			{total:250,		//总条数
			currentpage:15,		//当前页码
			gopage:gopage}	//点击标签后的实现函数，需要外面存在该函数
							//如：
							//	var gopage=function(pagesize,currentpage){
							//		alert(pagesize+"---"+currentpage);
							//	}
							//传入两个参数pagesize(每页显示条数)，currentpage(当前页码)
			
		);
	version:1.0.2
	author:wangchao
*/
;(function($){
	$.fn.page = function (options) {
	return this.each(function () {
		var t = this;
		var defaults = {
			pagesize: 10,
			currentpage: 1,
			cpageclass:"nowpage",
			cjumpclass:"jumppage",
			firstshow: true,
			firstname: "首页",
			prename:"上一页",
			lastname:"末页",
			aftername:"下一页",
			jumpname:"确定",
			li:true,
			jumpshow:true,
			sizeselectshow:true
		};	
		var p = $.extend(defaults,options);
		var htm ="";
		var lip="";
		var lipact="";
		var lia="";
		if(p.li){
			lip="<li>";
			lipact='<li class="active">';
			lia="</li>";
		}
		var g={
			gopage:function(currentpage){						
				if(p.gopage == undefined || typeof(p.gopage)!="function")
					return false;
				else p.gopage(p.pagesize,currentpage);
			}
		};
		if(p.total == undefined || p.total == "0" ){
			$(this).empty();
			return false;			
		}
		p.total = parseInt(p.total);
		p.currentpage = parseInt(p.currentpage);
		var pagesum = Math.ceil(p.total/p.pagesize);
		if(p.currentpage != 1){
			if(p.firstshow) htm += lip+'<a href="javascript:;" style="cursor:pointer" class="finalfirstpage">'+p.firstname+'</a>'+lia;
			htm += lip+'<a href="javascript:;" style="cursor:pointer" class="finalprepage">'+p.prename+'</a>'+lia;
		}
		if(pagesum > 1 && pagesum <11){
			for(var i=1;i<=pagesum;i++){
				if(i==p.currentpage) htm += lipact+'<span class="finalcurrentpagenum '+p.cpageclass+'">'+i+'</span>'+lia;
				else htm += lip+'<a href="javascript:;" style="cursor:pointer" class="pagenum">'+i+'</a>'+lia;				
			}
		}
		if(pagesum >10 ){
			var bottom = 1;
			var top = 10;
			if(p.currentpage>5&&pagesum-p.currentpage>5){
				bottom = p.currentpage - 4;
				top = p.currentpage + 5;
			}
			else if(pagesum-p.currentpage<6){
				bottom = pagesum - 9;
				top = pagesum;				
			}
			for(var i=bottom;i<=top;i++){
				if(i==p.currentpage) htm += lipact+'<span class="finalcurrentpagenum '+p.cpageclass+'">'+i+'</span>'+lia;
				else htm += lip+'<a href="javascript:;" style="cursor:pointer" class="pagenum">'+i+'</a>'+lia;				
			}
		}
		if(p.currentpage != pagesum){
			htm += lip+'<a href="javascript:;" style="cursor:pointer" class="finalafterpage">'+p.aftername+'</a>'+lia;			
			if(p.firstshow)htm += lip+'<a href="javascript:;" style="cursor:pointer" class="finallastpage">'+p.lastname+'</a>'+lia;
		}
		if(p.jumpshow&&pagesum>5){
			htm += lip+'<span class="'+p.cjumpclass+'">共<b class="totalpagenum">'+pagesum+
			'</b>页，跳到<input style="width:40px" maxlength="5" class="finaljumppagenum">页</span>'+lia;
			htm += lip+'<a href="javascript:;" style="cursor:pointer" class="finaljumppage">'+p.jumpname+'</a>'+lia;
		}
		if(p.sizeselectshow){
			htm += lip+'<select style="width:80px" class="finalsizeselect form-control"><option value="5">5</option><option value="10">10</option><option value="20">20</option>'
			  +'<option value="50">50</option><option value="100">100</option><option value="500">500</option></select>'+lia;
			$(".finalsizeselect",t).val(p.pagesize);
		}
		$(this).empty();
		$(this).append(htm);
		if(p.firstshow) $(".finalfirstpage",t).click(function(){g.gopage(1);});
		$(".finalprepage",t).click(function(){g.gopage(p.currentpage-1);});
		if(p.firstshow) $(".finallastpage",t).click(function(){g.gopage(pagesum);});
		if(p.jumpshow&&pagesum>5) $(".finaljumppage",t).click(function(){
			var pagein = $(".finaljumppagenum",t).val();
			if(/^\d+$/.test(pagein)){
				if(pagein>pagesum)pagein=pagesum;
				if(pagein<1)pagein=1;
				g.gopage(pagein);
			}
			else{
				$(".finaljumppagenum",t).val("");
			}
		});
		$(".finalafterpage",t).click(function(){g.gopage(p.currentpage+1);});

		if(p.sizeselectshow){
			$(".finalsizeselect",t).change(function(){
				p.pagesize = $(this).val();
				g.gopage(1);
			});
			$(".finalsizeselect",t).val(p.pagesize);
		}
		for(var i=0;i<$(".pagenum",t).length;i++){
			var count = $(".pagenum",t).eq(i).html();
			$(".pagenum",t).eq(i).bind("click", {k:count}, function(e){g.gopage(e.data.k)});
		}
	});	
	};
  })(jQuery)