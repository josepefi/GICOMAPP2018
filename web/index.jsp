<%-- 
    Document   : index
    Created on : 9/09/2016, 11:34:12 AM
    Author     : Jose Peralta
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <Meta name = "google-signin-client_id" content = "31373340811-n16nnhsuh8rn5gdp0bie814gqb1tfid4.apps.googleusercontent.com">

        <title>CIGOM</title>

        <link href="login/css/bootstrap.css" rel="stylesheet">
        <link href="login/css/style.css" rel="stylesheet">
        <link rel="shortcut icon" type="image/x-icon" href="login/img/icono.png" />
        <link href='http://fonts.googleapis.com/css?family=Fjalla+One' rel='stylesheet' type='text/css'>

        <script type="text/javascript" src="login/js/jquery.min.js"></script>
        <script type="text/javascript" src="login/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="login/js/jssor.js"></script>
        <script type="text/javascript" src="login/js/jssor.slider.js"></script>

        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <script>
            function onSignIn(googleUser) {
                // Useful data for your client-side scripts:
                var profile = googleUser.getBasicProfile();
                console.log("ID: " + profile.getId()); // Don't send this directly to your server!
                 console.log('Full Name: ' + profile.getName());
                 console.log('Given Name: ' + profile.getGivenName());
                 console.log('Family Name: ' + profile.getFamilyName());
                 console.log("Image URL: " + profile.getImageUrl());
                 console.log("Email: " + profile.getEmail());
                //alert("Email:" + profile.getEmail());
                // The ID token you need to pass to your backend:
                // var id_token = googleUser.getAuthResponse().id_token;
                // console.log("ID Token: " + id_token);

                var email = profile.getEmail();
                $.post('log', {email: email});
            }
            ;

        </script>

        <!--Jssor Slider-->
        <script>
            jQuery(document).ready(function ($) {
                //Reference http://www.jssor.com/development/slider-with-slideshow-jquery.html
                //Reference http://www.jssor.com/development/tool-slideshow-transition-viewer.html

                var _SlideshowTransitions = [
                    //Fade in R
                    {$Duration: 1200, x: -0.3, $During: {$Left: [0.3, 0.7]}, $Easing: {$Left: $JssorEasing$.$EaseInCubic, $Opacity: $JssorEasing$.$EaseLinear}, $Opacity: 2}
                    //Fade out L
                    , {$Duration: 1200, x: 0.3, $SlideOut: true, $Easing: {$Left: $JssorEasing$.$EaseInCubic, $Opacity: $JssorEasing$.$EaseLinear}, $Opacity: 2}
                ];

                var options = {
                    $AutoPlay: true, //[Optional] Whether to auto play, to enable slideshow, this option must be set to true, default value is false
                    $AutoPlaySteps: 1, //[Optional] Steps to go for each navigation request (this options applys only when slideshow disabled), the default value is 1
                    $AutoPlayInterval: 4000, //[Optional] Interval (in milliseconds) to go for next slide since the previous stopped if the slider is auto playing, default value is 6000
                    $PauseOnHover: 1, //[Optional] Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1 pause for desktop, 2 pause for touch device, 3 pause for desktop and touch device, 4 freeze for desktop, 8 freeze for touch device, 12 freeze for desktop and touch device, default value is 1

                    $ArrowKeyNavigation: true, //[Optional] Allows keyboard (arrow key) navigation or not, default value is false
                    $SlideDuration: 500, //[Optional] Specifies default duration (swipe) for slide in milliseconds, default value is 500
                    $MinDragOffsetToSlide: 20, //[Optional] Minimum drag offset to trigger slide , default value is 20
                    //$SlideWidth: 1500,                                 //[Optional] Width of every slide in pixels, default value is width of 'slides' container
                    //$SlideHeight: 600,                                //[Optional] Height of every slide in pixels, default value is height of 'slides' container
                    $SlideSpacing: 0, //[Optional] Space between each slide in pixels, default value is 0
                    $DisplayPieces: 1, //[Optional] Number of pieces to display (the slideshow would be disabled if the value is set to greater than 1), the default value is 1
                    $ParkingPosition: 0, //[Optional] The offset position to park slide (this options applys only when slideshow disabled), default value is 0.
                    $UISearchMode: 1, //[Optional] The way (0 parellel, 1 recursive, default value is 1) to search UI components (slides container, loading screen, navigator container, arrow navigator container, thumbnail navigator container etc).
                    $PlayOrientation: 1, //[Optional] Orientation to play slide (for auto play, navigation), 1 horizental, 2 vertical, 5 horizental reverse, 6 vertical reverse, default value is 1
                    $DragOrientation: 3, //[Optional] Orientation to drag slide, 0 no drag, 1 horizental, 2 vertical, 3 either, default value is 1 (Note that the $DragOrientation should be the same as $PlayOrientation when $DisplayPieces is greater than 1, or parking position is not 0)

                    $SlideshowOptions: {//[Optional] Options to specify and enable slideshow or not
                        $Class: $JssorSlideshowRunner$, //[Required] Class to create instance of slideshow
                        $Transitions: _SlideshowTransitions, //[Required] An array of slideshow transitions to play slideshow
                        $TransitionsOrder: 1, //[Optional] The way to choose transition to play slide, 1 Sequence, 0 Random
                        $ShowLink: true                                    //[Optional] Whether to bring slide link on top of the slider when slideshow is running, default value is false
                    },
                    $BulletNavigatorOptions: {//[Optional] Options to specify and enable navigator or not
                        $Class: $JssorBulletNavigator$, //[Required] Class to create navigator instance
                        $ChanceToShow: 2, //[Required] 0 Never, 1 Mouse Over, 2 Always
                        $Lanes: 1, //[Optional] Specify lanes to arrange items, default value is 1
                        $SpacingX: 10, //[Optional] Horizontal space between each item in pixel, default value is 0
                        $SpacingY: 10                                    //[Optional] Vertical space between each item in pixel, default value is 0
                    },
                    $ArrowNavigatorOptions: {
                        $Class: $JssorArrowNavigator$, //[Requried] Class to create arrow navigator instance
                        $ChanceToShow: 2, //[Required] 0 Never, 1 Mouse Over, 2 Always
                        $AutoCenter: 2                                 //[Optional] Auto center navigator in parent container, 0 None, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
                    },
                    $ThumbnailNavigatorOptions: {
                        $Class: $JssorThumbnailNavigator$, //[Required] Class to create thumbnail navigator instance
                        $ChanceToShow: 2, //[Required] 0 Never, 1 Mouse Over, 2 Always
                        $ActionMode: 0, //[Optional] 0 None, 1 act by click, 2 act by mouse hover, 3 both, default value is 1
                        $DisableDrag: true                              //[Optional] Disable drag or not, default value is false
                    }
                };

                var jssor_sliderb = new $JssorSlider$("sliderb_container", options);
                //responsive code begin
                //you can remove responsive code if you don't want the slider scales while window resizes
                function ScaleSlider() {
                    var parentWidth = jssor_sliderb.$Elmt.parentNode.clientWidth;
                    if (parentWidth)
                        jssor_sliderb.$ScaleWidth(Math.min(parentWidth, 1500));
                    else
                        window.setTimeout(ScaleSlider, 30);
                }
                ScaleSlider();

                $(window).bind("load", ScaleSlider);
                $(window).bind("resize", ScaleSlider);
                $(window).bind("orientationchange", ScaleSlider);
                //responsive code end
            });
        </script>
        <!--/Jssor Slider-->
    </head>

    <body style="background:#EDEDED;">

        <!--MENU-->
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">

            <div class="container">

                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> 
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>  
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                    <ul class="nav navbar-nav">

                        <li class="navbar-brand"><a href="index.html" id="enlaceblanco">SISTEMA INTEGRAL LINEA 4</a></li>
                    </ul>
                </div>

            </div>	

        </nav>													
        <!--/MENU-->

        <!--LOGO-->

        <div class="containerwide" style="margin-top: 51px; " >
            <div class="container">
                <div class="row clearfix">
                    <div class="col-md-12 column">
                        <div class="col-md-3" style="margin-top: 10px;"><img src="login/img/logoC.png" width="90%"   style="padding-bottom: 5px;"></div>
                        <div class="col-md-3" style="margin-top: 10px;"><h2 style="padding-top: 15px; font-family: 'Fjalla One', sans-serif;">CONSORCIO <br/>DE INVESTIGACIÓN <br/>DEL  <b>GOLFO DE </b> MÉXICO</h2>
                        </div>

                        <div class="col-md-4" style="margin-top: 10px;">
                            <div class="login-panel panel panel-default" style="background: #EDEDED;">
                                <!-- <div class="panel-heading">
                                    <center><h3 class="panel-title">Login de acceso</h3></center>
                                 </div>-->
                                <div class="panel-body">
                                    <form role="form" method="post" action="log">
                                        <fieldset>
                                            <label>ACCESO AL SISTEMA</label>
                                            <div class="form-group">
                                                <input class="form-control" autofocus title="ejemplo@cigom.org" type="text" name="usuario" placeholder="E-mail" pattern="[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@cigom.org"  required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" title="Campo vacio" type="password" name="password" value="" placeholder="Password" required autofocus>
                                            </div>

                                            <label>
                                                <button class="btn btn-primary" type="submit">Entrar</button>
                                            </label>
                                            <%
                                                if (request.getAttribute("msg") != null) {
                                            %>
                                            <br>
                                            <center>  <h5 style="color:#EC1010">
                                                    <b>"<%= request.getAttribute("msg")%>"</b>
                                                </h5> </center>
                                                <%
                                                    }
                                                %>  
                                            <!-- Change this to a button or input when using this as a form -->

                                        </fieldset>
                                    </form>
                                </div>
                            </div>
                        </div>
                        
                    <!--
                        <div class="col-md-2" style="margin-top: 10px;">

                            <p><br>Inicia sesión en tu cuenta @cigom.org y acepta los permisos que se te solicitan.</p>

                        </div>
                        <div class="g-signin2" data-onsuccess="onSignIn" ></div>
                        <br>
                        <a href="#" onclick="signOut();">Salir</a>-->
                        <script>
                            function signOut() {
                               var auth2 = gapi.auth2.getAuthInstance();
                                auth2.signOut().then(function () {
                                    //console.log('User signed out.');
                                    alert("ha terminado la sesión en la aplicación");
                                     document.location.href="https://accounts.google.com/logout?continue=http://google.com";
                                });
                                //auth2.disconnect();
                            }
                        </script>
                    </div>
                </div>


                <div class="row clearfix">
                    <div class="col-md-12">
                        <!-- BANNER -->

                        <div id="sliderb_container" style="position: relative; width: 1000px; height: 400px; overflow: hidden;">



                            <!-- Slides Container -->
                            <div u="slides" style="cursor: move; position: absolute; left: 0px; top: 0px; width: 1000px; height: 400px;
                                 overflow: hidden;">
                                <div>
                                    <img u=image src="login/img/slider/s3.jpg" />

                                </div>
                                <div>
                                    <img u=image src="login/img/slider/s2.jpg" />

                                </div>
                                <div>
                                    <img u=image src="login/img/slider/banner12.jpg" />

                                </div>
                            </div>
                        </div>
                        <!-- /BANNER -->
                    </div>

                    <div class="col-md-12" style="padding-top: 100px;">
                        <footer style="background: #E6E6E6; border-radius: 2px; border-top: 3px solid #2274ba; box-shadow: 0 1px 2px #666; padding: 10px 10px 10px; background-color: #E1E1E1; position:absolute;bottom: 0; width:100%; ">

                            <ul style="color:#000;">
                                <li style="float: left; font-size: 12px; margin-right: 20px; list-style:none;">SITIOS RELACIONADOS:</li>
                                <li style="float: left; font-size: 12px; margin-right: 20px; list-style:none;"><a href="http://www.ibt.unam.mx/" target="_blank" style="text-decoration:underline;">Instituto de Biotecnología</a> (IBT)</li>
                                <li style="float: left; font-size: 12px; margin-right: 20px; list-style:none;"><a href="http://www.cigom.info/" target="_blank" style="text-decoration:underline;">CIGOM INFO</a> (CIGOM)</li>
                                <li style="float: left; font-size: 12px; margin-right: 20px; list-style:none;"><a href="http://l4.cigom.info/login"  target="_blank" style="text-decoration:underline;">LINEA DE ACCIÓN 2 y 4</a> (L2 Y L4) </li>
                                <!--<li><a href="http://www.flickr.com/photos/vanort/4300971167/" target="_blank">barcelona sky</a> (CC BY-SA 2.0) </li>-->

                            </ul>
                            <br>

                        </footer>

                    </div>
                </div>				
            </div>
        </div>
        <!--/LOGO-->

    </body>
</html>