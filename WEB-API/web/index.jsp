<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="RestaurantConnected" method="POST">
            <input type="number" name="restaurantId" placeholder="restaurant id"><br><br>
            <input type="text" name="restaurantName" placeholder="restaurant Name"><br><br>
            <input type="number" name="addharNumber" placeholder="addhar Number"><br><br>
            <input type="text" name="address" placeholder="address"><br><br>
            <input type="time" name ="openTime" placeholder="open Time"><br><br>
            <input type="time" name ="closeTime" placeholder="close Time"><br><br>
            <button type="submit">Submit</button> 
        </form>
        <a href="sidenavPage.jsp">sidenav</a>
    </body>
    
</html>
