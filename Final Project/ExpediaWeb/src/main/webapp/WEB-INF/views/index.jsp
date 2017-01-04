<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Expedia Travel</title>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,700italic,800italic,400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Pinyon+Script'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Quicksand:400,700'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<link href="resources/css/bootstrap.css" rel='stylesheet'
	type='text/css' />
<link href="resources/css/style.css" rel="stylesheet" type="text/css"
	media="all" />

<script src="resources/js/jquery.min.js"></script>
<script>
	$(function() {
		$("#checkindate").datepicker({
			defaultDate : "+2",
			changeMonth : true,
			numberOfMonths : 1,
			minDate : +2,
			onClose : function(selectedDate) {
				var date2 = $('#checkindate').datepicker('getDate');
				date2.setDate(date2.getDate() + 1);
				$("#checkoutdate").datepicker("option", "minDate", date2);
			}
		});
		$("#checkoutdate").datepicker({
			defaultDate : +3,
			changeMonth : true,
			numberOfMonths : 1,
			onClose : function(selectedDate) {
			}
		});
	});
</script>
</head>
<body>
	<!--header starts-->
	<div class="header">
		<div class="top-header">
			<div class="container">
				<div class="logo">
					<a href="index.html"><img src="resources/images/logo.jpg" /></a>
				</div>
				<span class="menu"> </span>
				<div class="m-clear"></div>
				<div class="top-menu">
					<ul>
						<li class="active"><a href="index.jsp">HOME</a></li>
						<li><a class="scroll" href="facilities.html">FACILITIES</a></li>
						<li><a class="scroll" href="restaurant.jsp">RESTAURANT</a></li>
						<li><a class="scroll" href="conference.jsp">CONFERENCE</a></li>
						<li><a class="scroll" href="booking.jsp">BOOKING</a></li>
						<li><a class="scroll" href="review.jsp">REVIEW</a></li>
					</ul>
					<script>
						$("span.menu").click(function() {
							$(".top-menu ul").slideToggle(200);
						});
					</script>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="banner">
			<div class="banner-info text-center">
				<h3>
					<label>Hello,</label> You've Reached
				</h3>
				<h1>EXPEDIA TRAVEL</h1>
				<span></span>
				<ul>
					<li><a class="scroll" href="#">Vacations</a><i class="line"></i></li>
					<li><a class="scroll" href="#">Hotels</a><i class="line2"></i></li>
					<li><a class="scroll" href="#">Flights</a></li>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
	</div>

	<!---strat-date-piker---->
	<link rel="stylesheet" href="resources/css/jquery-ui.css" />
	<script src="resources/js/jquery-ui.js"></script>
	<script>
		$(function() {
			$("#datepicker,#datepicker1").datepicker();
		});
	</script>
	<!---/End-date-piker---->

	<link type="text/css" rel="stylesheet" href="resources/css/JFFormStyle-1.css" />
	<script type="text/javascript" src="resources/js/JFCore.js"></script>
	<script type="text/javascript" src="resources/js/JFForms.js"></script>
	<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
	<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
	<!-- Set here the key for your domain in order to hide the watermark on the web server -->
	<script type="text/javascript">
		(function() {
			JC.init({
				domainKey : ''
			});
		})();
	</script>

	<form action="search.htm" method="POST">
		<div class="online_reservation">
			<div class="b_room">
				<div class="booking_room">
					<div class="reservation">
						<ul>
							<li class="span1_of_3 left">
								<h5>Arrival</h5>
								<div class="book_date">
									<form>
										<input type="text" name="checkindate" id="checkindate"
											onselect="checkoutdate()" />
									</form>
								</div>
							</li>
							<li class="span1_of_3 left">
								<h5>Depature</h5>
								<div class="book_date">
									<form>
										<input type="text" name="checkoutdate" id="checkoutdate" />
									</form>
								</div>
							</li>
							<li class="span1_of_3 left">
								<h5>City</h5>
								<div class="book_date">
									<select name="city">
										<option value="2"> New York</option>
										<option value="3"> Barcelona</option>
										<option value="7"> Mumbai</option>
										<option value="9"> Amsterdam</option>
										<option value="10"> Rome</option>
										<option value="13"> Tuscany</option>
										<option value="14"> Dubai</option>
										<option value="15"> Paris</option>
										<option value="16"> London</option>
										<option value="18"> Berlin</option>
									</select>
								</div>
							</li>							
							<li class="span1_of_2 left">
								<h5>ADULT</h5>
								<div class="adult_count">
									<select name="adults" id="num_of_adults">
										<option name="1">1</option>
										<option name="2">2</option>
										<option name="3">3</option>
										<option name="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
									</select>
								</div>
							</li>
							<li class="span1_of_2 left">
								<h5>KIDS</h5>
								<div class="kids_count">
									<select name="kids" id="num_of_kids">
										<option name="1">1</option>
										<option name="2">2</option>
										<option name="3">3</option>
										<option name="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
									</select>
								</div>
							</li>
							<li class="span1_of_2 left">
								<h5>FUNCTION</h5> <select name="model" id="model">
									<option value="cluster">Cluster</option>
									<option value="search">Search</option>
							</select>
							</li>
							<li class="span1_of_2 left">
								<div class="date_btn">
									<input type="submit" value="Search" />
								</div>
							</li>
							<div class="clearfix"></div>
						</ul>
					</div>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</form>

	</div>
	<!---->
	<div class="package text-center">
		<div class="container">
			<h3>Book A Package</h3>
			<p>Sed euismod sem id consequat rutrum. Ut convallis lorem a orci
				mollis, eu vulputate libero aliquet. Praesent egestas nisi sed purus
				tincidunt faucibus. Aliquam lobortis orci lacus, sed faucibus augue
				dapibus vitae. Ut vitae mi sapien. Phasellus a eros justo. Curabitur
				odio massa, tincidunt nec nibh sit amet</p>
			<!-- requried-jsfiles-for owl -->
			<link href="resources/css/owl.carousel.css" rel="stylesheet">
			<script src="resources/js/owl.carousel.js"></script>
			<script>
				$(document).ready(function() {
					$("#owl-demo").owlCarousel({
						items : 1,
						lazyLoad : true,
						autoPlay : true,
						navigation : true,
						navigationText : false,
						pagination : false,
					});
				});
			</script>
			<!-- //requried-jsfiles-for owl -->
			<div id="owl-demo" class="owl-carousel">
				<div class="item text-center image-grid">
					<ul>
						<li><img src="resources/images/1.jpg" alt=""></li>
						<li><img src="resources/images/2.jpg" alt=""></li>
						<li><img src="resources/images/3.jpg" alt=""></li>
					</ul>
				</div>
				<div class="item text-center image-grid">
					<ul>
						<li><img src="resources/images/3.jpg" alt=""></li>
						<li><img src="resources/images/4.jpg" alt=""></li>
						<li><img src="resources/images/5.jpg" alt=""></li>
					</ul>
				</div>
				<div class="item text-center image-grid">
					<ul>
						<li><img src="resources/images/6.jpg" alt=""></li>
						<li><img src="resources/images/2.jpg" alt=""></li>
						<li><img src="resources/images/8.jpg" alt=""></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!---->
	<!---->
	<div class="rooms text-center">
		<div class="container">
			<h3>Our Room Types</h3>
			<div class="room-grids">
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic1.jpg" alt="" />
					<h4>Standard Double Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic2.jpg" alt="" />
					<h4>Supperior Double Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic3.jpg" alt="" />
					<h4>Family Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="clearfix"></div>
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic4.jpg" alt="" />
					<h4>Standard Single Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic5.jpg" alt="" />
					<h4>Supperior Single Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="col-md-4 room-sec">
					<img src="resources/images/pic6.jpg" alt="" />
					<h4>VIP Room</h4>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Praesent scelerisque lectus vitae dui sollicitudin commodo.</p>
					<div class="items">
						<li><a href="#"><span class="img1"> </span></a></li>
						<li><a href="#"><span class="img2"> </span></a></li>
						<li><a href="#"><span class="img3"> </span></a></li>
						<li><a href="#"><span class="img4"> </span></a></li>
						<li><a href="#"><span class="img5"> </span></a></li>
						<li><a href="#"><span class="img6"> </span></a></li>
					</div>
				</div>
				<div class="clearfix"></div>
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
					<form action="contact.htm" method="post">
						<p>Username</p>
						<input type="text" placeholder="" required />
						<p>Password</p>
						<input type="password" placeholder="" required /> 
						<input type="submit" value="LOGIN" />
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