$(document).ready(function(){
   $('.btn_menu').on('click', function(){
        console.log("!!");
      $('.blog-aside').addClass('action');
      $('.overlay').fadeIn();
   });
   $('.overlay').on('click', function(){
      $('.blog-aside').removeClass('action');
      $('.overlay').fadeOut();
   });
   $('.btn_home_menu').on('click', function(){
      $('.navbar_menu').toggleClass('active');
   });
});
