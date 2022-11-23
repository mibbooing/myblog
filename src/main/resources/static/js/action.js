    $('#btn_menu').on('click', function(){
       $('.area-aside').addClass('action');
       $('.overlay').fadeIn();
    });
    $('.overlay').on('click', function(){
       $('.area-aside').removeClass('action');
       $('.overlay').fadeOut();
    });
