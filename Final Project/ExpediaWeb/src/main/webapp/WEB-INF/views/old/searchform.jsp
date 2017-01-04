<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <!--  <link rel="stylesheet" href="/resources/demos/style.css">-->
  <script>
  $(function() {
	    $( "#checkindate" ).datepicker({
	      defaultDate: "+2",
	      changeMonth: true,
	      numberOfMonths: 1,
	      minDate: +2, 
	      onClose: function( selectedDate ) {
	    	  var date2 = $('#checkindate').datepicker('getDate');
	          date2.setDate(date2.getDate() + 1);
	        $( "#checkoutdate" ).datepicker("option", "minDate", date2);
	      }
	    });
	    $( "#checkoutdate" ).datepicker({
	      defaultDate: +3,
	      changeMonth: true,
	      numberOfMonths: 1,
	      onClose: function( selectedDate ) {
	         }
	    });
	  });
  </script>
  
<form id="searchform" action="searchresult.htm" method="post">
		<table>
		<tr><td>Search for Hotel/City:</td><td> <input type="text" name="searchterm" value='${requestScope.searchTerm }' onkeypress="return acceptRegExOnly(event, /^[a-zA-Z0-9 ]$/);"/></td></tr>
		<fmt:formatDate value="${requestScope.checkinDate}" var="checkinDate" type="date" pattern="MM/dd/yyyy" />
		<fmt:formatDate value="${requestScope.checkoutDate}" var="checkoutDate" type="date" pattern="MM/dd/yyyy" />
		
		<tr><td>Check-in Date:</td><td> <input type="text" name="checkindate" value="${checkinDate }" id="checkindate" onselect="checkoutdate()"/></td></tr>
		<tr><td>Check-out Date:</td><td> <input type="text" name="checkoutdate" value="${checkoutDate }" id="checkoutdate"/></td></tr>
		
		<tr><td>Number of Rooms:</td><td><input type="text" name="rooms" value="2" id="checkindate" onselect="checkoutdate()"/></td></tr>		
		
		<tr><td>Adults:</td><td><input type="text" name="adults" value="2" id="checkindate" onselect="checkoutdate()"/></td></tr>
		<tr><td>Children:</td><td><input type="text" name="childs" value="2" id="checkindate" onselect="checkoutdate()"/></td></tr>
		
		<tr><td colspan="2"><input type="submit" value="Search" name="search"/></td></tr>
		</table>
		
		
</form>

	<c:if test="${!empty requestScope.errormsg}">
    	<p style="color:red">${requestScope.errormsg}</p>
    </c:if>