<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
<form method="post" action="/create-user">
    <input type="text" placeholder="name" name="name"/>
    <br/>
    <input type="email" placeholder="email" name="email"/>
    <br/>
    <input type="submit" value="Login"/>
</form>
</body>
</html>