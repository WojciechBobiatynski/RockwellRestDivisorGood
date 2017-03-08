/*
 * //cdn.sodexo.pl/gryf/js/init.js
 * version: 0.1
 * 
 * Copyright (c) 2015 Sodexo Poland
 *
*/


var menu, current, menuBack = null;

/* automagic hover class */
(function($j){
$j.fn.hoverClass = function() {
    $j(this).hover(function(){$j(this).toggleClass('hover')},function(){$j(this).toggleClass('hover')});
}
})(jQuery);

var sxo = new function() {

  var menuActive = false;

  this.html = null;
  this.development = null;

  this.ready = function() {
  
    this.html = $j('html');

    $j('body > nav a[href="#"]').click(function(){return false});
    $j('body > footer a[rel*=external]').click(function(){ window.open(this.href); return false; });

    $j('input,textarea,select,button').each(function(){
      
      $j(this).focus(function(){
        $j(this).addClass('focus');
        if ($j(this).parent().hasClass('tooltip')) $j(this).parent().addClass('tooltip-focus');
      }).blur(function(){
        $j(this).removeClass('focus');
        if ($j(this).parent().hasClass('tooltip')) $j(this).parent().removeClass('tooltip-focus');
      }).hoverClass();

      /* input required */
      if ($j(this).hasClass('required')) {
        var label = $j(this).prev('.label');
        $j(label).html( '<em class="required">*</em> ' + $j(label).html() );
      }

      /* input tooltip */
      var msg = $j(this).data('msg');
      if ((typeof msg !== 'undefined') && (msg.length)) {
        $j(this).wrap('<div class="tooltip"></div>').parent().css({height:$j(this).outerHeight()}).append('<a><em>?</em></a>').find('a').mouseenter(function(e) {
          $j('body').append('<div id="tooltip">'+msg+'</div>');
          var tooltip = $j('#tooltip');
          var mousex = e.pageX - ($j(tooltip).outerWidth()/2);
          var mousey = e.pageY - 67 - $j(tooltip).outerHeight();
          $j(tooltip).css({ top: mousey, left: mousex })
        }).mouseleave(function(){
          $j('#tooltip').remove();
        });
      }

    });

    /* back button */
    if (sessionStorage.getItem('menuBack') !== null) {
      menuBack = sessionStorage.getItem('menuBack');
      sessionStorage.removeItem('menuBack');
    }
    if (menuBack == null) {
      breadcrumbs = $j('#breadcrumbs ul li');
      var menuBack = '';
      if ($j(breadcrumbs).length > 1) menuBack = $j(breadcrumbs).find('a').last().attr('href');
    }
    if (menuBack.length) $j('#menu').prepend('<li class="back"><a href="'+menuBack+'">&lt;</a></li>');

    /* routingPath js & css */
    if (routingPath!='') head.load(contextPath + resourcesUrl + routingPath + '.js', contextPath + resourcesUrl + routingPath + '.css');

    /* keyboard plugin*/
    head.load(cdn+'gryf/js/keyboard.js');

    /* ie<9 */
    if (this.html.hasClass('lt-ie9')) head.load(cdnJs('ie9'));

  }

}

head(function() {
  $j = jQuery.noConflict();
  $j(document).ready(function(){
    sxo.ready();
  });
});