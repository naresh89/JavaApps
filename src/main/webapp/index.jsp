<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<body>
	<h2>Welcome to HipChat Integration Demo</h2>
	<h3>Post notifications to room : Rlpochipchat</h3>
	
	<button id="build" type="button">Post build updates to Hipchat</button>
	
	<button id="service" type="button">Post service subscription to Hipchat Updater::</button>
	
	<button id="jenkinsAuthTest" type="button">Jenkins Auth Test</button>
	<br>
	<br>
	<div><label>Hipchat post notification statuss: </label><label id="divmsg" style="display:none"></label></div>

<script type="text/javascript">
  
  $('#build').click(function(){
	  
	  $('#divmsg').empty();
	  var msg = 'Build is updated successfully';
	  
	  var contextPath='<%=request.getContextPath()%>';
	  
	  $.get(contextPath+'/rest/hipChat/notify/'+msg,function(succcmsg){
		  //alert(succcmsg);
		  
		  $('#divmsg').attr('style','font-weight:bold').html(succcmsg);
		  
	  });
	  
  });
  
$('#service').click(function(){
	$('#divmsg').empty();
	  var msg = 'Service subscription is updated';
	  
	  var contextPath='<%=request.getContextPath()%>';
	  
	  $.get(contextPath+'/rest/hipChat/notify/'+msg,function(succcmsg){
		  //alert(succcmsg);
		  
		  $('#divmsg').attr('style','font-weight:bold').html(succcmsg);
		  
	  });
	  
  });
  
  $('#jenkinsAuthTest').click(function(){
	  
	  var logfileurl = 'http://52.27.46.167:8080/job/Catalyst_Get/339/logText/progressiveText?start=0';

      $.ajax({
          type: 'GET',
          url: logfileurl,
          dataType: 'json',
          beforeSend: function (xhr) {
              xhr.setRequestHeader('Authorization', make_base_auth('admin', 'admin@RL123'));
          },
          success: function (data) {
              alert('success');
          }
      });

      function make_base_auth(user, password) {
          var tok = user + ':' + password;
          var hash = btoa(tok);
          return 'Basic ' + hash;
      }
      
  });
  
</script>


</body>
</html>
