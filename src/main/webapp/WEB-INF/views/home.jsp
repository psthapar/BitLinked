<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Corporate</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"/>
    <link rel='stylesheet prefetch'
        href='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/components/icon.min.css'>

    <link rel="stylesheet" href="css/style.css">
    <!-- REFERANCE -->
    <!-- http://preview.themeforest.net/item/mestro/full_screen_preview/3404242 -->
    <style>
	#showcase{
	    color: white;
	    height: 60vh;
	    background-image: url("../img/home_bgpic.jpg");
	    background-position: center;
	    background-size: cover;
	    background-repeat: no-repeat;
	    background-attachment: fixed;
	}    
    </style>
	  <style type="text/css">
	    body > .ui.container {
	      margin-top: 3em;
	    }
	    iframe {
	      border: none;
	      width: calc(100% + 2em);
	      margin: 0em -1em;
	      height: 300px;
	    }
	    iframe html {
	      overflow: hidden;
	    }
	    iframe body {
	      padding: 0em;
	    }
	
	    .ui.container > h1 {
	      font-size: 3em;
	      text-align: center;
	      font-weight: normal;
	    }
	    .ui.container > h2.dividing.header {
	      font-size: 2em;
	      font-weight: normal;
	      margin: 4em 0em 3em;
	    }
	
	
	    .ui.table {
	      table-layout: fixed;
	    }
	  </style>
    
</head>

<body>
    <header id="showcase">
<!--         <div class="ui stackable inverted secondary menu" id="menu"> -->
<!--             <div class="ui container"> -->
<!--                 <div class="item"> -->
<!--                     <img src="img/bitlinked.png" alt="Logo"> -->
<!--                 </div> -->
<!--                 <div class=" menu right"> -->
<!--                     <a class="item"><b>Home</b></a> -->
<!--                     <a href="#" class="item">About</a> -->
<!--                     <a href="testapi" class="item">Test APIs</a> -->
<!--                     <a href="recruiter" class="item">Recruiter</a> -->
<!--                     <a href="#" class="item">Seekers</a> -->
<!--                     <a href="#" class="item">Contact</a> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->

	<div class="ui attached stackable menu">
	  <div class="ui container">
	       <div class="item">
	           <img src="img/bitlinked_logo.png" alt="Logo">
	       </div>
	  
		  <div class=" menu right">
				<a class="item">
				  <i class="home icon"></i> Home
				</a>
				<a href="recruiter" class="item">
				  <i class="grid layout icon"></i> Recruiter
				</a>
				<a href="seeker" class="item">
				  <i class="mail icon"></i> Seeker
				</a>
				<div class="ui simple dropdown item">
				  More
				  <i class="dropdown icon"></i>
				  <div class="menu">
<!-- 					<a class="item"><i class="edit icon"></i> Edit Profile</a> -->
<!-- 					<a class="item"><i class="globe icon"></i> Choose Language</a> -->
					<a href="testapi" class="item"><i class="settings icon"></i> Test APIs</a>
				  </div>
				</div>
			</div>
	  </div>
	</div>
        <div class="ui container">
            <div class="ui two column grid padded ">
                <div class="row hom-txt">
                    <div class="column">
                        <h1>The Best Choice For <b class="ui green header">Creative</b> Studio </h1>
                        <p>Creative Studio cannot be responsible to proof nor approve your work for any typographic
                            errors,
                            omissions or
                            mistakes. While we will bring to your attention any</p>
                        <button class="circular ui positive basic button">Start Now</button>

                    </div>
                </div>
            </div>
        </div>
    </header>

    <div class="ui container">
        <div>
            <h1 class="ui center aligned header">Explore Features</h1>
            <p>Passionate about Perfection Keep away from people who try Make your teams more productive to belittle
                your ambitions.</p>
        </div>
        <div class="ui two column grid">
            <div class="row" style="margin-top: 50px;">
                <div class="column">
                    <div class="ui segment">
                        <div class="ui center aligned icon header">
                            <i class="green life ring outline icon"></i>
                            <h4 class="ui header">Unlimited Features</h4>
                        </div>
                        <p class="center aligned description">Keep away from people who try to belittle your ambitions.
                            Small people always do that but the
                            really Keep away from people great.</p>
                    </div>
                </div>
                <div class="column">
                    <div class="ui segment">
                        <div class="ui center aligned icon header">
                            <i class="green life ring outline icon"></i>
                            <h4 class="ui header">Unlimited Features</h4>
                        </div>
                        <p class="center aligned description">Keep away from people who try to belittle your ambitions.
                            Small people always do that but the
                            really Keep away from people great.</p>
                    </div>
                </div>
            </div>
           
        </div>
    </div>


    <script src="js/jsquery.min.js"></script>
    <script src="js/semantic.min.js"></script>
    <script src="js/main.js"></script>
</body>

</html>