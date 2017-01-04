<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<title>hotel-fortune bootstrap Design website | Home :: w3layouts</title>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Pinyon+Script' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Quicksand:400,700' rel='stylesheet' type='text/css'>
<link href="resources/css/bootstrap.css" rel='stylesheet' type='text/css'/>
<link href="resources/css/style.css" rel="stylesheet" type="text/css" media="all"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="resources/js/jquery.min.js"></script>
</head>
<body>
<!--header starts-->
<div class="header">
	 <div class="top-header">
		 <div class="container">
			 <div class="logo">
				 <a href="index.html"><img src="resources/images/logo.jpg"/></a>
			 <div class="clearfix"></div>
			 </div>
			 <span class="menu"> </span>
			 <div class="m-clear"></div>
			 <div class="top-menu">
				<ul>
					 <li><a class="scroll" href="index.html">START</a></li>
					 <li><a class="scroll" href="facilities.html">FACILITIES</a></li>
					 <li><a class="scroll" href="restaurant.html">RESTAURANT</a></li>
					 <li class="active"><a href="conference.html">CONFERENCE</a></li>
					 <li><a class="scroll" href="booking.html">BOOKING</a></li>
					 <li><a class="scroll" href="contact.html">CONTACT US</a></li>
						<div class="clearfix"></div>
				 </ul>
				 <script>
					$("span.menu").click(function(){
						$(".top-menu ul").slideToggle(200);
					});
				</script>
			 </div>
		  </div>
	  </div>
</div>
<!---->
 <div class="confer">
	<script src="resources/js/responsiveslides.min.js"></script>
	 <script>
    // You can also use "$(window).load(function() {"
    $(function () {
      $("#conference-slider").responsiveSlides({
      	auto: true,
        manualControls: '#slider3-pager',
      });
    });
  </script>
  <div class="container">
    <h2>FORTUNE<span>CONFERENCE</span></h2>
    <div class="conference-slider">
	    <form action="ranking.htm" method="post">
		<c:forEach var="hotel" items="${hotelList}" varStatus="counter" step="2">
			<div class="row">
				<div class="col-sm-6">
					<img class="card-img-top" src="resources/images/hotel.jpg"
						alt="Hotel Image">
					<div class="card-block">
						<h3 class="card-title">${hotelList[counter.index].name}</h4>
						<p class="card-text">${hotel.city}</p>
					</div>
					<ul class="list-group list-group-flush">
						<li class="list-group-item">Hotel ID:
							${hotelList[counter.index].id}</li>
						<li class="list-group-item">Hotel Rating:
							${hotelList[counter.index].rating}</li>
						<li class="list-group-item">Review Score: ${hotelList[counter.index].reviewScore}</li>
					</ul>
				</div>
				<c:if test="${!counter.last}">
				<div class="col-sm-6">
					<img class="card-img-top" src="resources/images/hotel.jpg" alt="Hotel Image">
					<div class="card-block">
						<h3 class="card-title">${hotelList[counter.index+1].name}</h4>
						<p class="card-text">${hotel.city}</p>
					</div>
					<ul class="list-group list-group-flush">
						<li class="list-group-item">Hotel ID:
							${hotelList[counter.index+1].id}</li>
						<li class="list-group-item">Hotel Rating:
							${hotelList[counter.index+1].rating}</li>
						<li class="list-group-item">Review Score: ${hotelList[counter.index+1].reviewScore}</li>
					</ul>
				</div>
				</c:if>
			</div>		
		<input type="hidden" value= ${hotel.destinationID} name="destinationID"/>
		
		</c:forEach>
		<input type="submit" class="btn btn-primary" value="Predict Search Order" />
	</form>
    </div> 
	<h4>Conference Halls in Fortune Hotels</h4>
     <p>A big announcement, a new idea or a special occasion demands a venue to match. A name your guest will be delighted to see on the invitation - a place to impress, inspire and linger in the memory. It's a tall order. Allow us to make things a little easier for you with our collection of luxury London hotels.</p>
	 <p>Each Deluxe Hotels in World has its own unique personality. So you can give your event that unique atmosphere and feel. Between them all, Deluxe Hotels the  offer a huge range of room sizes and layouts that can be configured to your precise requirements. Whether you're arranging a board meeting or banquet, product launch or wedding, you've come to the right place.</p>
	 <p>The Charing Cross Hotel, has 9 meeting rooms, the largest of which can accommodate up to 150 people.</p>
	 <p>The Deluxe Hotel, near Marble Arch in London has 26 meeting rooms, the largest of which can accommodate up to 400 people. The Deluxe Hotel adjoining One Whitehall Place have 13 meeting rooms, the largest of which can accommodate up to 340 people.The Tower Hotel, near Tower Bridge in London has 19 meeting rooms, the largest of which can accommodate up to 600 people.</p>
	 <div class="sponcers">
		<div class="sponcers-head">
		<h5>Featured Sponsors</h5>
		</div>
	 </div>
	 <ul id="flexiselDemo1">
		<li>
			<div class="biseller-column">
			<img src="resources/images/s1.png" alt="">
			</div>
		</li>
		<li>
			<div class="biseller-column">
			<img src="resources/images/s2.png" alt="">
			</div>
		</li>
		<li>
			<div class="biseller-column">
			<img src="resources/images/s3.png" alt="">
			</div>
		</li>
		<li>
			<div class="biseller-column">
			<img src="resources/images/s2.png" alt="">
			</div>
		</li>
		<li>
			<div class="biseller-column">
			<img src="resources/images/s1.png" alt="">
			</div>
		</li>
		<li>
			<div class="biseller-column">
			<img src="resources/images/s3.png" alt="">
			</div>
		</li>
	</ul>
	<script type="text/javascript">
	 $(window).load(function() {
		$("#flexiselDemo1").flexisel({
			visibleItems: 3,
			animationSpeed: 1000,
			autoPlay: true,
			autoPlaySpeed: 3000,    		
			pauseOnHover: true,
			enableResponsiveBreakpoints: true,
			responsiveBreakpoints: { 
				portrait: { 
					changePoint:480,
					visibleItems: 1
				}, 
				landscape: { 
					changePoint:640,
					visibleItems: 2
				},
				tablet: { 
					changePoint:768,
					visibleItems: 3
				}
			}
		});
		
	});
   </script>
   <script type="text/javascript" src="js/jquery.flexisel.js"></script>		
	 </div>
 </div>
 
 </div>
 <!---->
<div class="fotter">
	 <div class="container">
		 <h3>143 City Located Hotels World Wide</h3>
		 <h4>"Hospitality, Quality & Good Locations. We only provide you with the best hotels" - John Deo</h4>
		 <i class="man"></i>
	 </div>
</div>
<!---->
<div class="fotter-info">
	  <div class="container">
	      <div class="col-md-5 details">
			 <div class="hotel-info">
				 <h4>ABOUT THIS HOTEL</h4>
				 <p>Suspendisse erat mi, tincidunt sit amet massa quis, commodo fermentum diam. Sed nec dui nec nunc tempor interdum.</p>
				 <p>Ut vulputate augue urna, ut porta dolor imperdiet a. Vestibulum nec leo eu magna aliquam ornare.</p>
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
				 <p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus vestibulum blandit egestas. 
				 Nam id lectus vel orci luctus consectetur eget id elit. In tortor odio, pellentesque eu arcu sit amet, lacinia euismod nisi. Aliquam sodales tristique mauris ac fermentum.
				 Donec vel varius ipsum. Pellentesque vitae mollis massa. </p>
				 <p>There is no costs or whatsoever so sign up today!</p>
				 <a href="#">READ MORE</a>
			 </div>
			 <div class="member">
			 <h4>MEMBERS AREA</h4>
			 <form>
			 <p>Username</p>
			 <input type="text" placeholder="" required/>
			 <p>Password</p>
			 <input type="password" placeholder="" required/>		
			 <input type="submit" value="LOGIN"/>
			 </form>
			 </div>
			 <div class="clearfix"></div>
		 </div>
		 <div class="clearfix"></div>
	  </div>	
		<h6>Template by <a href="http://w3layouts.com/">W3layouts</h6>	
</div>
<!---->

</body>
</html>