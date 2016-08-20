<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String value = request.getHeader("X-Real-IP");
String value2 = request.getHeader("Host");
String value3 = request.getHeader("REMOTE-HOST");
String value4 = request.getHeader("X-Forwarded-For");
System.out.println("--------------> = " + value);
System.out.println("--------------> = " + value2);
System.out.println("--------------> = " + value3);
System.out.println("--------------> = " + value4);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="scripts/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="scripts/json2.js"></script>
	<script type="text/javascript">
		function getReuqest() {
			var xmlHttp;
			try { //Chrome, Firefox, Opera, Safari
				xmlHttp = new XMLHttpRequest();
			} catch(e) {
				try {// Internet Explorer (高版本)
					xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
				} catch(e) {
					try {// Internet Explorer (低版本)
						xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
					} catch(e) {
						alert("your brower not support ajax!");
					}
				}
			}
			return xmlHttp;
		}
		
		var request = getReuqest();
		
		window.onload = function() {
			//$("#slt").change(function() {
			//	alert(1);
			//});
			
			//$("#slt").val("2");
			//$("#slt").change();
			var btn01Ele = document.getElementById("btn01");
			btn01Ele.onclick = function(){
				alert(request)
				//return false;
			}
			var btn02Ele = document.getElementById("btn02");
			btn02Ele.onclick = function() {
				//发送GET请求
				//调用request.open()方法建立一个连接
				//①method: 请求方式
				//②url: 请求的目标地址
				//发送请求参数:
				// + encodeURIComponent(encodeURIComponent("二角"))
				var url = "http://192.168.1.100:8080/ajax/ajaxRequestServlet?name=" + encodeURIComponent("二角");
				request.open("get", url);
				//2.调用 request.send()方法发送请求数据
				request.send(null);
			}
			var btn03Ele = document.getElementById("btn03");
			btn03Ele.onclick = function() {
				//发送POST请求
				//发送请求参数: 将请求参数键值对以参数形式传递给send() 方法
				//设置请求消息头为如下的值
				//Content-Type:application/x-www-form-urlencoded
				var url = "ajaxRequestServlet?name=erjiao";
				var pp = {
						name : "liukai",
						age : "23",
						sex : "男"
				}
				var ppp = {
						name : "erjiao",
						age : "17",
						sex : "女"
				}
				var arr = [];
				arr.push(pp);
				arr.push(ppp);
				alert(JSON.stringify(arr))
				var params = {
						userName : JSON.stringify(arr)
				}
				$.post(url, params, function(data) {
					alert(data);
				})
				//request.open("post", url);
				//request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				//request.send("userName=erjiao");
			}
			var btn04Ele = document.getElementById("btn04");
			btn04Ele.onclick = function() {
				//接收文本格式的响应数据
				request.open("POST", "ajaxResponseText");
				request.send();
				//1.通过onreadystatechange 事件监听 readyState 属性值的变化
				request.onreadystatechange = function(){
					//2.在readystate 属性等于4 并且status 属性等于200时接收相应数据
					if (request.readyState == 4 && request.status == 200) {
						var result = request.responseText;
						alert(result);
					}
				}
			}
			var btn05Ele = document.getElementById("btn05");
			btn05Ele.onclick = function() {
				//返回xml 格式的数据
				request.open("get", "ajaxResponseXML");
				request.send(null);
				request.onreadystatechange = function() {
					if (request.readyState == 4 && request.status == 200) {
						//使用responseXML属性接收xml 格式的数据
						var result = request.responseXML;
						//alert(result);
						var value = result.getElementsByTagName("username")[0].firstChild.nodeValue;
						alert(value);
					}
				}
			}
			var btn06Ele = document.getElementById("btn06");
			btn06Ele.onclick = function() {
				//返回json 格式的数据
				request.open("get", "ajaxResponseJSON");
				request.send(null);
				request.onreadystatechange = function() {
					if (request.readyState == 4 && request.status == 200) {
						//使用responseText属性接收json 格式的数据
						var result = request.responseText;
						//直接接收到的JSON数据是一个字符串的格式, 需要转换为JSON对象的格式.
						result = eval("(" + result + ")");
						alert(result[1].userName);
					}
				}
			}
			var btn07Ele = document.getElementById("btn07");
			btn07Ele.onclick = function() {
				var script = document.createElement("script");
				script.type = "text/javascript";
				script.src = "http://localhost:8080/ajax/ajaxResponseJSONP?callback=handle";
				alert(document.body.firstChild.nodeValue)
				document.body.insertBefore(script, document.body.firstChild);
				
			}
			
			var btn08Ele = document.getElementById("btn08");
			btn08Ele.onclick = function() {
				alert(1)
				$.ajax({
					async : false,
					url : "http://localhost:8080/ajax/ajaxResponseJSONP2?erjiao",
					type : "get",
					dataType : "jsonp",
					jsonp : "callback",
					jsonpCallback : "erjiao",
					success : function(data) {
						alert(data.actionErrors)
						alert(data.result);
					},
					error : function() {
						alert("传送失败!");
					}
				});
			}
		};
		
		function handle(data) {
			alert(data.result);
		}
	</script>
  </head>
  
  <body>
    <center>
		<button id="btn01">获取XMLHttpRequest对象</button><br />
		<button id="btn02">发送GET请求</button><br />
		<button id="btn03">发送POST请求</button><br />
		<button id="btn04">接收【文本】格式的响应数据</button><br />
		<button id="btn05">接收【XML】格式的响应数据</button><br />
		<button id="btn06">接收【JSON】格式的响应数据</button><br />
		<button id="btn07">发送并接收【JSONP】格式的响应数据</button><br />
		<button id="btn08">发送并接收【JSONP】格式的响应数据(Ajax)</button><br />
		
		<select id="slt">
			<option value="0">0</option>
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
		</select>
		
		<br /><br />
		
		<form action="AjaxRequestServlet" method="post">
			<input type="text" name="userName" /><input type="submit" />
		</form>
	</center>
  </body>
</html>
