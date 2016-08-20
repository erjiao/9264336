<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="scripts/jquery-1.7.2.js"></script>
<script type="text/javascript">
	$(function(){
		$("button").click(function(){
			//测试$.post()方法
			//url:发送请求地址。
			//data:待发送 Key/value 参数。
			//callback:发送成功时回调函数。
			//type:返回内容格式，xml, html, script, json, text, _default。
			$.post("ajaxjQueryServlet",{name : "erjiao"}, function(data){
				alert(data)
				alert(data.name + "," + data.password);
			}, "json");
		})
	});
</script>
</head>
<body>
	
	<center>
		<button>$.post()方法解析JSON数据</button>
	</center>
	
</body>
</html>