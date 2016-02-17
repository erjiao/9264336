/**
 * 需要开发人员根据业务实现的函数。 入口参数是一个form Element
 */
function validation(formElm) {
	var str_validate = "";
	var flag = false;
	
	//验证 物品名称.
	$("#sswpxx table:first tr:gt(0)").each(function(){
		var value = $(this).find("td:eq(3) input:first").val();
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "物品名称 - 不能为空!\n\n";
			flag = true;
			return false;
		}
	});
	
	//验证 持有人
	$("#sswpxx table:first tr:gt(0)").each(function(){
		//alert($(this).find("td:eq(4) span").children().length)
		var value = $(this).find("td:eq(4) input:first").val();
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "持有人 - 不能为空!\n\n";
			flag = true;
			return false;
		}
	});
	
	//验证物品数量
	$("#sswpxx table:first tr:gt(0)").each(function(i){
		var value = $(this).find("td:eq(5) input:first").val();
		// 用于判断是否点击了字典选择框, 点击会多出一列.
		if($(this).find("td:eq(4) div").length == 6) {  //以div 的个数来判断是否点击了字典选择框.
			value = $(this).find("td:eq(6) input:first").val();
		}
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "物品数量 - 不能为空!\n\n";
			flag = true;
			return false;
		}
	});
	
	//验证单位
	$("#sswpxx table:first tr:gt(0)").each(function(){
		var value = $(this).find("td:eq(6) input:first").val();
		if($(this).find("td:eq(4) div").length == 6) {
			value = $(this).find("td:eq(7) input:first").val();
		}
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "物品单位 - 不能为空\n\n";
			flag = true;
			return false;
		}
	});
	
	if (flag) {
		alert(str_validate);
		return false;
	}
	
	$("#addsscwInfo").val(getAddSSWPInfo());
	return true;
}

/**
 * 一般函数的处理规则 下面的方法是针对所有的页面的元素 必须首先判断，是否是需要处理的字段名称
 */

/**
 * 页面加载完后的操作
 */
var oldValue;  //用于记录失去焦点前的AJBH的值.
function ExpandInterface_OnLoad() {
	
	oldValue = getFieldValue("aJBH");
	
	//使当案件编号之前清空时, 案件名称跟案别也发生变化.
	$("#aJBH").bind("propertychange", function(o){
		if (o.originalEvent.propertyName != "value") return;  //不是值发生改变直接返回.
		if ($.trim(this.value) == "") {
			$("#aJMC, #aB").val("");
		}
	});
	
	// 这里填写业务逻辑
	//setDTGL("cJBZT", "and code = 01", "", false);
	var url = window.location.href;
	var readOnly = getUrlPara(url,"ENTITY_READONLY");
	if (readOnly.toUpperCase() == "TRUE" ) {
		$("#needs").show(); //显示登记人和登记时间.
		//设置值.
		var cyr = DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [getOldValue("cYR")]);
		$("#label__cYR").val(cyr)
		$("img").hide() //隐藏字典选择框.
		$("input[value='保存']").hide();  //隐藏保存按钮.
		$(".cz").hide();       //隐藏操作列.
		$("#sswpxx table:first tr:gt(1)").each(function(){
			$(this).find("td:gt(1)").each(function(){
				$(this).children().addClass("readonlyTextInput")  //改变 td 的样式.
								  .attr("disabled", "disabled");
			});
		});
	} else {
		$("#aJBH").after("<input id='glajbh' type='button' value='关联案件编号' style='position: absolute; cursor: pointer; height: 21px; width:90px;' onclick='link_case();' />");
	}
	//当载入时初始化持有人的字典. 即修改.
	if (!isCreateForCurrForm()) {
		$(".cz").hide();       //隐藏操作列.
		var filterSql = " or systemid in (select rybh from B_ASJ_RYSAQK where ajbh = '" + getFieldValue("aJBH") +"')";
		setDTGL("cYR",filterSql , "", false);
		setFieldValue("cYR", getOldValue("cYR"));   //因为持有人字典初始化时未加载数据, 所以需要重新设上值.
		$("input[id='_cYR']").val(DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [getOldValue("cYR")]));
		$("input[name^=_cyr_]").each(function(){
			var _oldValue = this.value;
			var name = this.name.substring(1);
			setDTGL(name, filterSql, "", false);
			this.parentNode.onclick = function(){   //使选中的input框的 内容不清空.
				var _val = $.trim($("input[name='_" + name + "']").val()) == "" ? _oldValue : $("input[name='_" + name + "']").val();
				_oldValue = _val;
				if (!$("input[name=" + name + "]").val()) {
					this.firstChild.value = _oldValue;
				}
			}
		});
	} else {
		var rybh = getUrlPara(url, "rybh");   //判断是否来自办案主界面 嫌疑人的菜单.
		if (rybh != "error") {
			var ajbh = getUrlPara(url, "ajbh");
			cYR_DTGL(ajbh);
			
			setFieldValue("cYR", rybh);   //因为持有人字典初始化时未加载数据, 所以需要重新设上值.
			$("input[id='_cYR']").val(DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [rybh]));
		}
	}
	
	//校验 物品数量必须为数字.
	$("input[id^=wPSL]").live("blur", function(){
		var value = $(this).val()
		if ($.trim(value).length != "" && !isNumber(value)) {
			alert("请输入数字！");
			$(this).select();
		}
	});
	
	//解决第一次选择第一行持有人时重新选择时显示Error的问题.
	var _value = $("input[id='_cYR']").val();
	$("span[id='_cYR']").live("click", function(){
		if ($(this).children("input").val() == "Error") {
			$(this).children("input").val(_value);
		}
	});
	
}

/**
 * 日期类型焦点离开时候的处理 fieldName - 产生事件字段名称
 */
function ExpandInterface_OnDateFocusOut(fieldName) {
	// alert(fieldName);
}

/**
 * 日期输入框年失去焦点的操作
 */
function ExpandInterface_OnDateYearFocusOut() {
}

/**
 * 焦点离开时候的处理
 */

function ExpandInterface_OnBlur() {
	 //案件名称, 案别随案件编号的变化而改变.
	 if (event.srcElement.name=="aJBH"){
		 if (oldValue != event.srcElement.value) {
			 var ajbh = event.srcElement.value;
			 oldValue = ajbh;
			 if ($.trim(oldValue).length != 0) {
				 // 案件编号不是从子窗口获取的.
				 if (!$("#aJBH").attr("fromchild")) {
					 var str_ajmc_ab = DBUtil.getValue("SELECT dic_codetodetail('24', ab) || '|' || ajmc FROM b_asj_aj where ajbh = ?", [oldValue]);
					 if (str_ajmc_ab == "Error") {
						 alert("没有找到相关案件！");
						 $("#aJBH").select();
						 $("#aJMC").val("");
						 $("#aB").val("");
						 
						 setFieldValue("cYR", "");  //持有人置空.
						 $("input[name^=_cyr_]").each(function(){
							 this.value = "";
						 });
						 $("input[name^=cyr_]").each(function(){
							 this.value = "";
						 });
						 //动态过滤持有人选择.
						 cYR_DTGL(getFieldValue("aJBH"));
						 return;
					 }
					 
					 var arr_ajmc_ab = str_ajmc_ab.split("|");
					 str_ab = arr_ajmc_ab[0];  //案别
					 str_ajmc = arr_ajmc_ab[1]; //案件名称
					 
					 setFieldValue("aB", str_ab);
					 setFieldValue("aJMC", str_ajmc);
					 
					 setFieldValue("cYR", "");  //持有人置空.
				 }
				 
				 event.srcElement.removeAttribute("fromchild"); //移除属性.
				 
				 
				 //动态过滤持有人选择.
				 cYR_DTGL(getFieldValue("aJBH"));
			 }
		 }
	 }
}

/**
 * 点击的时候的处理
 */
function ExpandInterface_OnClick() {
	// if (event.srcElement.name=="字段名称"){
	// event.srcElement.value = event.srcElement.name; //例子
	// 这里填写业务逻辑
	// }
}

/**
 * 点击的时候的处理
 */
function ExpandInterface_OnChange() {
	// if (event.srcElement.name=="字段名称"){
	// event.srcElement.value = event.srcElement.name; //例子
	// 这里填写业务逻辑
	// }
}

/**
 * Key Press的时候的处理
 */
function ExpandInterface_OnKeyPress() {
	// if (event.srcElement.name=="字段名称"){
	// event.srcElement.value = event.srcElement.name; //例子
	// 这里填写业务逻辑
	// }
}

/**
 * 比对 的时候的处理
 */
function ExpandInterface_Refer() {
	alert("请填写业务逻辑在对应的js文件!");
	// if (event.srcElement.parentElement.childNodes.item(0).name=="字段名称"){
	// var ObjectRefer= event.srcElement.parentElement.childNodes.item(0); //例子
	// 这里填写业务逻辑
	// }
}

/**
 *  添加随身物品
 * @param element
 */
function addSSWP(element) {
	var index = 
		parseInt($(element).parent().parent().siblings(":last").find("td:eq(1)").text()) + 1;
	
	if (isNaN(index))
		index = 2;
	var sql = "SELECT RYBH AS code, xm AS detail, null AS spell FROM B_ASJ_XYRDJ where 1 = 0";
	
	$("<tr align='center'></tr>").append("<td style='display: none;'></td>")
							     .append("<td>" + index + "</td>")
							     .append("<td><input class='readonlyTextInput' dataType='string' style='width: 184px;' readonly='readonly'/></td>")
							     .append("<td><input class='commonTextInput' dataType='string' cnname='物品名称'/></td>")
							     .append("<td><input class='treeselect_zh' name='_cyr_" + (index - 1) + "' maxnumber='2' ext4='1' ext5='1' sql='" + sql + "' ext18='' multiselect='false' ext17='' valuefield='c' cnname='持有人' checkMethods='checkMaxLength' value='' /><img src='/zfba/images/common/downarrow2.gif' class='fireselect' inputfieldname='_cyr_" + (index - 1) + "'></td>")
							     .append("<td><input id='wPSL_" + (index - 1) + "' class='commonTextInput' dataType='string' maxlength='3' checkMethods='checkMaxLength' style='width: 58px;' cnname='物品数量'/></td>")
							     .append("<td><input class='commonTextInput' dataType='string' style='width: 58px;' cnname='单位'/></td>")
							     .append("<td><input class='commonTextInput' dataType='string' cnname='特征'/></td>")
							     .append("<td style='display: none;'><input class='commonTextInput' dataType='string'/></td>")
							     .append("<td style='display: none;'><input class='commonTextInput' dataType='string'/></td>")
							     .append("<td><a href='javascript:void(0)' onclick='deleteSSWP(this)'>删除</a></td>")
							     .appendTo("#sswpxx table:first"); //必须指定为first.
	
	if (getFieldValue("aJBH")) {
		cYR_DTGL(getFieldValue("aJBH"), "add");
	}
}

/**
 * 删除随身物品
 * @param element
 */
function deleteSSWP(element) {
	$(element).parent().parent().remove();
}

/**
 * 获取 新添加的随身物品的信息.
 */
function getAddSSWPInfo(){
	var keys = ['"CWBH"', '"WPMC"', '"CYR"', '"WPSL"', '"WPDW"', '"WPTZ"', '"SYSTEMID"']; //用于存放拼 json 数组的键
	var values = [];  // 用于存放拼 json 数组的 值
	var str = "";
	var jsonStr = "";
	$("#sswpxx table:first tr:gt(1)").each(function(){
		$(this).children("td:gt(1):lt(7)").each(function(i){
			var $input = $(this).children("input");
			var val = $input.val();
			// 字典项需要取 code 值, 即持有人的人员编号.
			if ($input.attr("class") == "treeselect_zh") {
				var name = $input.attr("name").substring(1);
				if(!$("input[name=" + name + "]").val()) {
					val = $(this).nextAll(":last").prev().find("input").val();
				} else {
					val = $("input[name=" + name + "]").val();
				}
			}
			var value = "\"" + val + "\"";  //拼成 "xxx" 的格式
			values.push(value);
		});
	});
	
	//遍历拼接 json格式 字符串.
	$.each(values, function(i){
		str += keys[i%7] + ":" + values[i] + ",";
		if (i && (i + 1) % 7 == 0) {
			jsonStr += "{" + str.substring(0, str.length - 1) + "},";
			str = "";
		}
		num = i;
	});
	
	jsonStr = "[" + jsonStr.substring(0, jsonStr.length - 1) + "]";
	return jsonStr;
}

/**
 * 返回简表
 */
function goBackSimpleQuery(){
	
	if (window.opener) {
		window.close();
	} else {
		window.location.href = "/zfba/simplequery/queryFromServlet?queryEntityId=B_ASJ_SSCW201511171055";
	}
}
/**
 * 关联案件编号
 */
function link_case(old_ajbh){
	if (!$("#glajbh").attr("haschild")){
		var url = "/zfba/simplequery/queryFromServlet?queryEntityId=PCS3500201512090000000008205420&radion_group_query_button=CREATEDTIME >= trunc(sysdate,'month')";
		var subWindow = newWindow(url, "");
		$("#glajbh").attr("haschild", "true");    //设置属性.
		
		var timer = window.setInterval(function(){ //判断是否存在子窗口.
			if (subWindow.closed == true) {
				$("#glajbh").removeAttr("haschild");
				window.clearInterval(timer);
			}
		}, 500);
	}
	
}

/**
 * 关联案件
 * @param ajbh
 * @param ajmc
 * @param ab
 */
function toLinked(ajbh, ajmc, ab){
	var old_ajbh = $("#aJBH", window.opener.document).val();
	if (old_ajbh != ajbh) {
		$("#aJBH", window.opener.document).val(ajbh);
		$("#aJMC", window.opener.document).val(ajmc);
		$("#aB", window.opener.document).val(ab);
		$("input[id='_cYR']", window.opener.document).val("");
		$("input[id='cYR']", window.opener.document).val("");
		
		
		$("#aJBH", window.opener.document)[0].focus();  //使其获取焦点, 可以触发失去焦点事件.
		window.opener.document.getElementById("aJBH").setAttribute("fromchild", "true");
	}
	
	
	if (window.opener) {
		window.close();  //关联案件窗口关闭.
	}
	
}
/**
 * 简表新增按钮调用
 */
function toAdd() {
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/new.action";
}
/**
 * 简表修改按钮调用
 * @param systemid
 */
function toUpdate(systemid) {
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/load.action?sYSTEMID=" + _systemid + "&queryEntityId=B_ASJ_SSCW201511171055&ObjID1=" + _systemid;
}
/**
 * 简表删除按钮调用
 */
function toDel(systemid, cwzt) {
	if (cwzt != '待保管') {
		alert("只能删除财物状态为\"待保管\"的财物");
		return;
	}
	var _isConfirm = confirm("确定是否删除该数据？");
	
	if (!_isConfirm) {
		return ;
	}
	
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/delete.action?sYSTEMID=" + _systemid + "&queryEntityId=B_ASJ_SSCW201511171055";
}
/**
 * 简表查看按钮调用
 * @param systemid
 */
function toView(systemid) {
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/load.action?sYSTEMID=" + _systemid+"&ENTITY_READONLY=TRUE&ObjID1=" + _systemid;
}

/**
 * 动态过滤持有人.
 */
function cYR_DTGL(ajbh, type){
	 var filterSql = " or systemid in (select rybh from B_ASJ_RYSAQK where ajbh = '" + ajbh + "')";
	 if (type != "add") {
		 setDTGL("cYR",filterSql, "", false);
	 }
	 $("input[name^=_cyr_]").each(function(){
		 this.onclick = function() {
			 $(this).siblings("div").remove(); //先移除以前的div. 不然会有bug.
			 var name = this.name.substring(1);
			 setDTGL(name, filterSql, "", false);
		 }
	 });
}

/**
 * 转涉案财物
 * @param ajbh 案件编号
 * @param wpmc 物品名称
 * @param wpsl 物品数量
 * @param cyr  持有人
 * @param wpdw 物品单位
 */
function toSacw(ajbh,wpmc,wpsl,cyr_cn,wpdw){
	newWindow(getZfba1Url()+"/mainservlet?actionType=INIT_B_ASJ_WP&AJBH="+ajbh+"&WPMC="+wpmc+"&WPSL="+wpsl+"&WPSZ="+cyr_cn+"&WPDW="+wpdw+"&from=B_ASJ_SSCW");
}