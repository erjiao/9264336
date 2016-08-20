<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/" />
<link rel="stylesheet" type="text/css" href="style/chat.css"/>
<script type="text/javascript" src="scripts/jquery-1.7.2.js"></script>
<script type="text/javascript" src="scripts/dateFormate.js"></script>
<script type="text/javascript">
	
$(function(){
	var finalMessageId = 0;
	//声明函数: 询问服务器端是否存在新的聊天记录
	askForNew();
	function askForNew(){
		$.post("servletAsk", {finalMessageId : finalMessageId}, function(hasNew){
			if (hasNew == "true") {
				getNew();
			}
		}, "text");
		
		//注意: 一定要使用函数的引用, 不能加().
		setTimeout(askForNew, 1000);
	}
	
	function getNew(){
		$showMessage = $("#showMessage");
		$.post("servletGetNew", {finalMessageId : finalMessageId}, function(data){
			for (var i = 0; i < data.length; i++) {
				var messageId = data[i].messageId;
				finalMessageId = messageId;
				var message = data[i].message;
				var messageTime = data[i].messageTime;
				
				
				messageTime = new Date(messageTime).Format("yyyy年MM月dd日 hh:mm:ss");
				
				var htmlStr = "<div>" + messageTime + " " + message + "</div>";
				
				$showMessage.append(htmlStr);
				
				//获取#showMessage 对应DOM 对象, 通过srcollTop 属性设置滚动条的显示位置
				$showMessage[0].scrollTop = 100000000;
			}
			
		}, "json");
	}
	
	//给多行文本框绑定键盘按下事件
	$("#sendMessage").keypress(function(event){
		//在用户按下回车键时,发送聊天信息
		//通过事件的keyCode 属性值获取当前按下的键的对应的ASSII码
		if (event.keyCode == 13) {
			//获取聊天内容
			var message = $.trim(this.value);
			//使用Ajax技术将聊天信息发送到服务器端
			$.post("sendMessage", {"message" : message});
			
			//情况文本域.
			this.value = "";
		}
	});
});
	
</script>
</head>
<body>
	
	<img alt="" src="image/panda.jpg" />
	
	<div id="showMessage"></div>
	<textarea id="sendMessage"></textarea>
	
	
</body>
</html>