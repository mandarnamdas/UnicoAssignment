<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Restful WS Application</title>
<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
</head>
<body>

	<a href="javascript:$('#pushToQueue').show();">Push to Queue</a>
	<a href="rest/show">Show Queue Data</a>
	<div id="pushToQueue" style="display: none">
		<form action="rest/push" method="post">
			<input type="text" name="param1"> 
			<input type="text" name="param2"> 
			<input type="submit" value="Submit" />
		</form>
	</div>
</body>
</html>