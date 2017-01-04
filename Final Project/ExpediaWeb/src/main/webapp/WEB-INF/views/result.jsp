<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Expedia Travel | Machine Learning</title>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,700italic,800italic,400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Pinyon+Script'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Quicksand:400,700'
	rel='stylesheet' type='text/css'>
<link href="resources/css/bootstrap.css" rel='stylesheet'
	type='text/css' />
<link href="resources/css/style.css" rel="stylesheet" type="text/css"
	media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="resources/js/jquery.min.js"></script>
</head>
<body>
	<!--header starts-->
	<div class="header">
		<div class="top-header">
			<div class="container">
				<div class="logo">
					<a href="index.html"><img src="resources/images/logo.jpg" /></a>
					<div class="clearfix"></div>
				</div>
				<span class="menu"> </span>
				<div class="m-clear"></div>
				<div class="top-menu">
					<ul>
						<li><a class="scroll" href="index.jsp">START</a></li>
						<li class="active"><a href="facilities.html" class="scroll">FACILITIES</a></li>
						<li><a class="scroll" href="restaurant.html">RESTAURANT</a></li>
						<li><a class="scroll" href="conference.html">CONFERENCE</a></li>
						<li><a class="scroll" href="booking.html">BOOKING</a></li>
						<li><a class="scroll" href="contact.html">REVIEW</a></li>
						<div class="clearfix"></div>
					</ul>
					<script>
						$("span.menu").click(function() {
							$(".top-menu ul").slideToggle(200);
						});
					</script>
				</div>
			</div>
		</div>
	</div>
	<!---->
	<div class="main_bg">
		<div class="container">
			<div class="main">
				<div class="details">

					<!-- <h3>Recommended hotel cluster for the user is: <c:out value="${output}"></c:out></h3> -->
					<c:if test="${error == 'no_value'}">
		  					Arrival date is empty. Please enter the value!
		  			</c:if>

					<c:if test="${empty error}">
						<c:forEach var="hotel" items="${hotelList}" varStatus="counter" step="2">
							<div class="row">
								<div class="col-sm-6">
									<img class="card-img-top" src="resources/images/hotel.jpg"
										alt="Hotel Image">
									<div class="card-block">
										<h4 class="card-title">${hotelList[counter.index].name}</h4>
										<p class="card-text">${hotel.city}</p>
									</div>
									<ul class="list-group list-group-flush">
										<li class="list-group-item">Hotel ID: ${hotelList[counter.index].id}</li>
										<li class="list-group-item">Hotel Rating: ${hotelList[counter.index].rating}</li>
										<li class="list-group-item">Review Score: ${hotelList[counter.index].reviewScore}</li>
									</ul>
								</div>
							
								<c:if test="${!counter.last}">
								<div class="col-sm-6">
									<img class="card-img-top" src="resources/images/hotel.jpg"
										alt="Hotel Image">
									<div class="card-block">
										<h4 class="card-title">${hotelList[counter.index+1].name}</h4>
										<p class="card-text">${hotel.city}</p>
									</div>
									<ul class="list-group list-group-flush">
										<li class="list-group-item">Hotel ID: ${hotelList[counter.index+1].id}</li>
										<li class="list-group-item">Hotel Rating: ${hotelList[counter.index+1].rating}</li>
										<li class="list-group-item">Review Score: ${hotelList[counter.index+1].reviewScore}</li>
									</ul>
								</div>
							</c:if>
							<br />
							</div>
						</c:forEach>

						
					</c:if>

					<div class="det_text">
						<p class="para">There are many variations of passages of Lorem
							Ipsum available, but the majority have suffered alteration in
							some form, by injected humour, or randomised words which don't
							look even slightly believable. If you are going to use a passage
							of Lorem Ipsum, you need to be sure there isn't anything
							embarrassing hidden in the middle of text. All the Lorem Ipsum
							generators on the Internet tend to repeat predefined chunks as
							necessary, making this the first true generator on the Internet.
							It uses a dictionary of over 200 Latin words, combined with a
							handful of model sentence structures, to generate Lorem Ipsum
							which looks reasonable.</p>
						<p class="para">Contrary to popular belief, Lorem Ipsum is not
							simply random text. It has roots in a piece of classical Latin
							literature from 45 BC, making it over 2000 years old. Richard
							McClintock, a Latin professor at Hampden-Sydney College in
							Virginia, looked up one of the more obscure Latin words,
							consectetur, from a Lorem Ipsum passage, and going through the
							cites of the word in classical literature, discovered the
							undoubtable source. Lorem Ipsum comes from sections 1.10.32 and
							1.10.33 of "de Finibus Bonorum et Malorum" (The Extremes of Good
							and Evil) by Cicero, written in 45 BC. This book is a treatise on
							the theory of ethics</p>
						<div class="read_more">
							<a href="details.html">read more</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!---->
	<div class="fotter">
		<div class="container">
			<h3>143 City Located Hotels World Wide</h3>
			<h4>"Hospitality, Quality & Good Locations. We only provide you
				with the best hotels" - John Deo</h4>
			<i class="man"></i>
		</div>
	</div>
	<!---->
	<div class="fotter-info">
		<div class="container">
			<div class="col-md-5 details">
				<div class="hotel-info">
					<h4>ABOUT THIS HOTEL</h4>
					<p>Suspendisse erat mi, tincidunt sit amet massa quis, commodo
						fermentum diam. Sed nec dui nec nunc tempor interdum.</p>
					<p>Ut vulputate augue urna, ut porta dolor imperdiet a.
						Vestibulum nec leo eu magna aliquam ornare.</p>
				</div>
				<div class="news">
					<h4>LATEST NEWS</h4>
					<h5>Grand Hotel Joins DeluxelHotels</h5>
					<a href="#">15 AUG</a>
					<h5>Happy Chirstmas To Everyone</h5>
					<a href="#">15 AUG</a>
					<h5>Best Places To Visit 2014</h5>
					<a href="#">15 AUG</a>
					<h5>Various Offers</h5>
					<a href="#">15 AUG</a>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="col-md-7 details">
				<div class="join">
					<h4>JOIN DELUXEHOTELS</h4>
					<p>Cum sociis natoque penatibus et magnis dis parturient
						montes, nascetur ridiculus mus. Phasellus vestibulum blandit
						egestas. Nam id lectus vel orci luctus consectetur eget id elit.
						In tortor odio, pellentesque eu arcu sit amet, lacinia euismod
						nisi. Aliquam sodales tristique mauris ac fermentum. Donec vel
						varius ipsum. Pellentesque vitae mollis massa.</p>
					<p>There is no costs or whatsoever so sign up today!</p>
					<a href="#">READ MORE</a>
				</div>
				<div class="member">
					<h4>MEMBERS AREA</h4>
					<form>
						<p>Username</p>
						<input type="text" placeholder="" required />
						<p>Password</p>
						<input type="password" placeholder="" required /> <input
							type="submit" value="LOGIN" />
					</form>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
		</div>
		<h6>
			Template by <a href="http://w3layouts.com/">W3layouts 
		</h6>
	</div>
	<!---->

</body>
</html>