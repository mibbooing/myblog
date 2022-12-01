$(document).ready(function(){
   $('#btn_menu').on('click', function(){
      $('.area-aside').addClass('action');
      $('.overlay').fadeIn();
   });
   $('.overlay').on('click', function(){
      $('.area-aside').removeClass('action');
      $('.overlay').fadeOut();
   });
   $('.btn_home_menu').on('click', function(){
        console.log("!!");
      $('.navbar_menu').toggleClass('active');
   });
});
