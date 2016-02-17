/**
 * ��Ҫ������Ա����ҵ��ʵ�ֵĺ����� ��ڲ�����һ��form Element
 */
function validation(formElm) {
	var str_validate = "";
	var flag = false;
	
	//��֤ ��Ʒ����.
	$("#sswpxx table:first tr:gt(0)").each(function(){
		var value = $(this).find("td:eq(3) input:first").val();
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "��Ʒ���� - ����Ϊ��!\n\n";
			flag = true;
			return false;
		}
	});
	
	//��֤ ������
	$("#sswpxx table:first tr:gt(0)").each(function(){
		//alert($(this).find("td:eq(4) span").children().length)
		var value = $(this).find("td:eq(4) input:first").val();
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "������ - ����Ϊ��!\n\n";
			flag = true;
			return false;
		}
	});
	
	//��֤��Ʒ����
	$("#sswpxx table:first tr:gt(0)").each(function(i){
		var value = $(this).find("td:eq(5) input:first").val();
		// �����ж��Ƿ������ֵ�ѡ���, �������һ��.
		if($(this).find("td:eq(4) div").length == 6) {  //��div �ĸ������ж��Ƿ������ֵ�ѡ���.
			value = $(this).find("td:eq(6) input:first").val();
		}
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "��Ʒ���� - ����Ϊ��!\n\n";
			flag = true;
			return false;
		}
	});
	
	//��֤��λ
	$("#sswpxx table:first tr:gt(0)").each(function(){
		var value = $(this).find("td:eq(6) input:first").val();
		if($(this).find("td:eq(4) div").length == 6) {
			value = $(this).find("td:eq(7) input:first").val();
		}
		if (typeof value == 'undefined') return;
		if ($.trim(value).length == 0) {
			str_validate += "��Ʒ��λ - ����Ϊ��\n\n";
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
 * һ�㺯���Ĵ������ ����ķ�����������е�ҳ���Ԫ�� ���������жϣ��Ƿ�����Ҫ������ֶ�����
 */

/**
 * ҳ��������Ĳ���
 */
var oldValue;  //���ڼ�¼ʧȥ����ǰ��AJBH��ֵ.
function ExpandInterface_OnLoad() {
	
	oldValue = getFieldValue("aJBH");
	
	//ʹ���������֮ǰ���ʱ, �������Ƹ�����Ҳ�����仯.
	$("#aJBH").bind("propertychange", function(o){
		if (o.originalEvent.propertyName != "value") return;  //����ֵ�����ı�ֱ�ӷ���.
		if ($.trim(this.value) == "") {
			$("#aJMC, #aB").val("");
		}
	});
	
	// ������дҵ���߼�
	//setDTGL("cJBZT", "and code = 01", "", false);
	var url = window.location.href;
	var readOnly = getUrlPara(url,"ENTITY_READONLY");
	if (readOnly.toUpperCase() == "TRUE" ) {
		$("#needs").show(); //��ʾ�Ǽ��˺͵Ǽ�ʱ��.
		//����ֵ.
		var cyr = DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [getOldValue("cYR")]);
		$("#label__cYR").val(cyr)
		$("img").hide() //�����ֵ�ѡ���.
		$("input[value='����']").hide();  //���ر��水ť.
		$(".cz").hide();       //���ز�����.
		$("#sswpxx table:first tr:gt(1)").each(function(){
			$(this).find("td:gt(1)").each(function(){
				$(this).children().addClass("readonlyTextInput")  //�ı� td ����ʽ.
								  .attr("disabled", "disabled");
			});
		});
	} else {
		$("#aJBH").after("<input id='glajbh' type='button' value='�����������' style='position: absolute; cursor: pointer; height: 21px; width:90px;' onclick='link_case();' />");
	}
	//������ʱ��ʼ�������˵��ֵ�. ���޸�.
	if (!isCreateForCurrForm()) {
		$(".cz").hide();       //���ز�����.
		var filterSql = " or systemid in (select rybh from B_ASJ_RYSAQK where ajbh = '" + getFieldValue("aJBH") +"')";
		setDTGL("cYR",filterSql , "", false);
		setFieldValue("cYR", getOldValue("cYR"));   //��Ϊ�������ֵ��ʼ��ʱδ��������, ������Ҫ��������ֵ.
		$("input[id='_cYR']").val(DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [getOldValue("cYR")]));
		$("input[name^=_cyr_]").each(function(){
			var _oldValue = this.value;
			var name = this.name.substring(1);
			setDTGL(name, filterSql, "", false);
			this.parentNode.onclick = function(){   //ʹѡ�е�input��� ���ݲ����.
				var _val = $.trim($("input[name='_" + name + "']").val()) == "" ? _oldValue : $("input[name='_" + name + "']").val();
				_oldValue = _val;
				if (!$("input[name=" + name + "]").val()) {
					this.firstChild.value = _oldValue;
				}
			}
		});
	} else {
		var rybh = getUrlPara(url, "rybh");   //�ж��Ƿ����԰참������ �����˵Ĳ˵�.
		if (rybh != "error") {
			var ajbh = getUrlPara(url, "ajbh");
			cYR_DTGL(ajbh);
			
			setFieldValue("cYR", rybh);   //��Ϊ�������ֵ��ʼ��ʱδ��������, ������Ҫ��������ֵ.
			$("input[id='_cYR']").val(DBUtil.getValue("select xm from B_ASJ_XYRDJ where rybh = ?", [rybh]));
		}
	}
	
	//У�� ��Ʒ��������Ϊ����.
	$("input[id^=wPSL]").live("blur", function(){
		var value = $(this).val()
		if ($.trim(value).length != "" && !isNumber(value)) {
			alert("���������֣�");
			$(this).select();
		}
	});
	
	//�����һ��ѡ���һ�г�����ʱ����ѡ��ʱ��ʾError������.
	var _value = $("input[id='_cYR']").val();
	$("span[id='_cYR']").live("click", function(){
		if ($(this).children("input").val() == "Error") {
			$(this).children("input").val(_value);
		}
	});
	
}

/**
 * �������ͽ����뿪ʱ��Ĵ��� fieldName - �����¼��ֶ�����
 */
function ExpandInterface_OnDateFocusOut(fieldName) {
	// alert(fieldName);
}

/**
 * �����������ʧȥ����Ĳ���
 */
function ExpandInterface_OnDateYearFocusOut() {
}

/**
 * �����뿪ʱ��Ĵ���
 */

function ExpandInterface_OnBlur() {
	 //��������, �����永����ŵı仯���ı�.
	 if (event.srcElement.name=="aJBH"){
		 if (oldValue != event.srcElement.value) {
			 var ajbh = event.srcElement.value;
			 oldValue = ajbh;
			 if ($.trim(oldValue).length != 0) {
				 // ������Ų��Ǵ��Ӵ��ڻ�ȡ��.
				 if (!$("#aJBH").attr("fromchild")) {
					 var str_ajmc_ab = DBUtil.getValue("SELECT dic_codetodetail('24', ab) || '|' || ajmc FROM b_asj_aj where ajbh = ?", [oldValue]);
					 if (str_ajmc_ab == "Error") {
						 alert("û���ҵ���ذ�����");
						 $("#aJBH").select();
						 $("#aJMC").val("");
						 $("#aB").val("");
						 
						 setFieldValue("cYR", "");  //�������ÿ�.
						 $("input[name^=_cyr_]").each(function(){
							 this.value = "";
						 });
						 $("input[name^=cyr_]").each(function(){
							 this.value = "";
						 });
						 //��̬���˳�����ѡ��.
						 cYR_DTGL(getFieldValue("aJBH"));
						 return;
					 }
					 
					 var arr_ajmc_ab = str_ajmc_ab.split("|");
					 str_ab = arr_ajmc_ab[0];  //����
					 str_ajmc = arr_ajmc_ab[1]; //��������
					 
					 setFieldValue("aB", str_ab);
					 setFieldValue("aJMC", str_ajmc);
					 
					 setFieldValue("cYR", "");  //�������ÿ�.
				 }
				 
				 event.srcElement.removeAttribute("fromchild"); //�Ƴ�����.
				 
				 
				 //��̬���˳�����ѡ��.
				 cYR_DTGL(getFieldValue("aJBH"));
			 }
		 }
	 }
}

/**
 * �����ʱ��Ĵ���
 */
function ExpandInterface_OnClick() {
	// if (event.srcElement.name=="�ֶ�����"){
	// event.srcElement.value = event.srcElement.name; //����
	// ������дҵ���߼�
	// }
}

/**
 * �����ʱ��Ĵ���
 */
function ExpandInterface_OnChange() {
	// if (event.srcElement.name=="�ֶ�����"){
	// event.srcElement.value = event.srcElement.name; //����
	// ������дҵ���߼�
	// }
}

/**
 * Key Press��ʱ��Ĵ���
 */
function ExpandInterface_OnKeyPress() {
	// if (event.srcElement.name=="�ֶ�����"){
	// event.srcElement.value = event.srcElement.name; //����
	// ������дҵ���߼�
	// }
}

/**
 * �ȶ� ��ʱ��Ĵ���
 */
function ExpandInterface_Refer() {
	alert("����дҵ���߼��ڶ�Ӧ��js�ļ�!");
	// if (event.srcElement.parentElement.childNodes.item(0).name=="�ֶ�����"){
	// var ObjectRefer= event.srcElement.parentElement.childNodes.item(0); //����
	// ������дҵ���߼�
	// }
}

/**
 *  ���������Ʒ
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
							     .append("<td><input class='commonTextInput' dataType='string' cnname='��Ʒ����'/></td>")
							     .append("<td><input class='treeselect_zh' name='_cyr_" + (index - 1) + "' maxnumber='2' ext4='1' ext5='1' sql='" + sql + "' ext18='' multiselect='false' ext17='' valuefield='c' cnname='������' checkMethods='checkMaxLength' value='' /><img src='/zfba/images/common/downarrow2.gif' class='fireselect' inputfieldname='_cyr_" + (index - 1) + "'></td>")
							     .append("<td><input id='wPSL_" + (index - 1) + "' class='commonTextInput' dataType='string' maxlength='3' checkMethods='checkMaxLength' style='width: 58px;' cnname='��Ʒ����'/></td>")
							     .append("<td><input class='commonTextInput' dataType='string' style='width: 58px;' cnname='��λ'/></td>")
							     .append("<td><input class='commonTextInput' dataType='string' cnname='����'/></td>")
							     .append("<td style='display: none;'><input class='commonTextInput' dataType='string'/></td>")
							     .append("<td style='display: none;'><input class='commonTextInput' dataType='string'/></td>")
							     .append("<td><a href='javascript:void(0)' onclick='deleteSSWP(this)'>ɾ��</a></td>")
							     .appendTo("#sswpxx table:first"); //����ָ��Ϊfirst.
	
	if (getFieldValue("aJBH")) {
		cYR_DTGL(getFieldValue("aJBH"), "add");
	}
}

/**
 * ɾ��������Ʒ
 * @param element
 */
function deleteSSWP(element) {
	$(element).parent().parent().remove();
}

/**
 * ��ȡ ����ӵ�������Ʒ����Ϣ.
 */
function getAddSSWPInfo(){
	var keys = ['"CWBH"', '"WPMC"', '"CYR"', '"WPSL"', '"WPDW"', '"WPTZ"', '"SYSTEMID"']; //���ڴ��ƴ json ����ļ�
	var values = [];  // ���ڴ��ƴ json ����� ֵ
	var str = "";
	var jsonStr = "";
	$("#sswpxx table:first tr:gt(1)").each(function(){
		$(this).children("td:gt(1):lt(7)").each(function(i){
			var $input = $(this).children("input");
			var val = $input.val();
			// �ֵ�����Ҫȡ code ֵ, �������˵���Ա���.
			if ($input.attr("class") == "treeselect_zh") {
				var name = $input.attr("name").substring(1);
				if(!$("input[name=" + name + "]").val()) {
					val = $(this).nextAll(":last").prev().find("input").val();
				} else {
					val = $("input[name=" + name + "]").val();
				}
			}
			var value = "\"" + val + "\"";  //ƴ�� "xxx" �ĸ�ʽ
			values.push(value);
		});
	});
	
	//����ƴ�� json��ʽ �ַ���.
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
 * ���ؼ��
 */
function goBackSimpleQuery(){
	
	if (window.opener) {
		window.close();
	} else {
		window.location.href = "/zfba/simplequery/queryFromServlet?queryEntityId=B_ASJ_SSCW201511171055";
	}
}
/**
 * �����������
 */
function link_case(old_ajbh){
	if (!$("#glajbh").attr("haschild")){
		var url = "/zfba/simplequery/queryFromServlet?queryEntityId=PCS3500201512090000000008205420&radion_group_query_button=CREATEDTIME >= trunc(sysdate,'month')";
		var subWindow = newWindow(url, "");
		$("#glajbh").attr("haschild", "true");    //��������.
		
		var timer = window.setInterval(function(){ //�ж��Ƿ�����Ӵ���.
			if (subWindow.closed == true) {
				$("#glajbh").removeAttr("haschild");
				window.clearInterval(timer);
			}
		}, 500);
	}
	
}

/**
 * ��������
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
		
		
		$("#aJBH", window.opener.document)[0].focus();  //ʹ���ȡ����, ���Դ���ʧȥ�����¼�.
		window.opener.document.getElementById("aJBH").setAttribute("fromchild", "true");
	}
	
	
	if (window.opener) {
		window.close();  //�����������ڹر�.
	}
	
}
/**
 * ���������ť����
 */
function toAdd() {
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/new.action";
}
/**
 * ����޸İ�ť����
 * @param systemid
 */
function toUpdate(systemid) {
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/load.action?sYSTEMID=" + _systemid + "&queryEntityId=B_ASJ_SSCW201511171055&ObjID1=" + _systemid;
}
/**
 * ���ɾ����ť����
 */
function toDel(systemid, cwzt) {
	if (cwzt != '������') {
		alert("ֻ��ɾ������״̬Ϊ\"������\"�Ĳ���");
		return;
	}
	var _isConfirm = confirm("ȷ���Ƿ�ɾ�������ݣ�");
	
	if (!_isConfirm) {
		return ;
	}
	
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/delete.action?sYSTEMID=" + _systemid + "&queryEntityId=B_ASJ_SSCW201511171055";
}
/**
 * ���鿴��ť����
 * @param systemid
 */
function toView(systemid) {
	var _systemid = systemid;
	window.location.href="/zfba/zfba/sswp/B_ASJ_SSCW/load.action?sYSTEMID=" + _systemid+"&ENTITY_READONLY=TRUE&ObjID1=" + _systemid;
}

/**
 * ��̬���˳�����.
 */
function cYR_DTGL(ajbh, type){
	 var filterSql = " or systemid in (select rybh from B_ASJ_RYSAQK where ajbh = '" + ajbh + "')";
	 if (type != "add") {
		 setDTGL("cYR",filterSql, "", false);
	 }
	 $("input[name^=_cyr_]").each(function(){
		 this.onclick = function() {
			 $(this).siblings("div").remove(); //���Ƴ���ǰ��div. ��Ȼ����bug.
			 var name = this.name.substring(1);
			 setDTGL(name, filterSql, "", false);
		 }
	 });
}

/**
 * ת�永����
 * @param ajbh �������
 * @param wpmc ��Ʒ����
 * @param wpsl ��Ʒ����
 * @param cyr  ������
 * @param wpdw ��Ʒ��λ
 */
function toSacw(ajbh,wpmc,wpsl,cyr_cn,wpdw){
	newWindow(getZfba1Url()+"/mainservlet?actionType=INIT_B_ASJ_WP&AJBH="+ajbh+"&WPMC="+wpmc+"&WPSL="+wpsl+"&WPSZ="+cyr_cn+"&WPDW="+wpdw+"&from=B_ASJ_SSCW");
}