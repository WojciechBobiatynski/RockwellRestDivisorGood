/* keyboard menu navigation - begin */
  menu = $j('#menu');
  current = menu.filter('.current');
  var siblings = 0;
  var parent = 0;
  var open = 0;
  var upmenu = true;
  if (!current.length) current = menu.children('.home');

  function menuNavNavigate(direction) {

    upmenu = (current.parents('ul').length - 1)?false:true;

    if (direction=='left' || direction=='up') {

      if (!upmenu && (direction=='left' || direction=='right')) 
        if (!current.find('li').length) current = current.parents('.hover');

      if (current.prev().length == 0)
          menuNavShow(current.siblings().last());
      else 
          menuNavShow(current.prev());
    }

    if (direction=='right' || direction=='down') {

      if (current.hasClass('hover') && upmenu && direction=='down') {
        menuNavShow(current.find('ul > li').first());
      }

      console.log(upmenu);

      if (!upmenu && (direction=='left' || direction=='right')) 
        if (!current.find('li').length) current = current.parents('.hover');

      if (current.next().length == 0) {
        menuNavShow(current.siblings().first());
      } else {
        menuNavShow(current.next());
      }
    }

  };

  function menuNavInit() {
    menuActive = true;
    menuNavShow(current);
  }

  function menuNavShow(item) {
    menu.find('li').removeClass('current').removeClass('hover');
    item.addClass('current').addClass('hover');
    item.parents('li').addClass('current').addClass('hover');
    if (item.children('ul').length) item.addClass('hover');
    current = item;

  }

  $j(document).keyup(function(e) {

      switch(e.which) {
          case 37: // left arrow
            if (menuActive) menuNavNavigate('left');
          break;

          case 38: // up arrow
            if (menuActive) menuNavNavigate('up');
          break;

          case 39: // right arrow
            if (menuActive) menuNavNavigate('right');
          break;

          case 40: // down arrow
            if (menuActive) menuNavNavigate('down');
          break;

          case 77: // m - menu
            menuNavInit();
          break;

          default: return; // exit this handler for other keys
      }
      e.preventDefault(); // prevent the default action (scroll / move caret)
  });

  //menu.removeClass('.current');

/* keyboard menu navigation - end */