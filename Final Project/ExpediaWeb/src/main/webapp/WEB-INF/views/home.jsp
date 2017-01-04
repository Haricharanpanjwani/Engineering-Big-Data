<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h1>Hello world!</h1>

	<P>The time on the server is ${serverTime}.</P>

	<form action="ranking.htm" method="post">
		<c:forEach var="hotel" items="${hotelList}">
			<div class="row">
				<div class="col-sm-6">
					<img class="card-img-top" src="resources/images/logo.jpg"
						alt="Hotel Image">
					<div class="card-block">
						<h4 class="card-title">${hotel.name}</h4>
						<p class="card-text">${hotel.city}</p>
					</div>
					<ul class="list-group list-group-flush">
						<li class="list-group-item">Hotel ID:
							${hotel.id}</li>
						<li class="list-group-item">Hotel Rating:
							${hotel.rating}</li>
						<li class="list-group-item">Review Score: ${hotel.reviewScore}</li>
					</ul>
				</div>
			</div>		
		<input type="hidden" value= ${hotel.destinationID} name="destinationID"/>
		</c:forEach>
		<input type="submit" value="Predict Search Order" />
	</form>
</body>
</html>
