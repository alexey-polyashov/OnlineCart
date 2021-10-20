angular.module('app', ['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {

   $scope.makePayment = function(sum){

         $http({
             url: 'http://localhost:8189/onlinecart/paypal/make/payment',
             method: 'POST',
             params: {'sum':sum}
         }).then(function(response){
                document.location.href = response.data.redirect_url;
            }
         );

    }

});