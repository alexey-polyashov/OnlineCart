angular.module('app', ['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {

    const baseURL_products = 'http://localhost:8189/onlinecart/api/v1/products/product/';
    const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';
    const baseURL_auth =    'http://localhost:8189/onlinecart/api/v1/auth/';
    const baseURL_orders = 'http://localhost:8189/onlinecart/api/v1/orders/';

    $scope.cartDetail = null;
    $scope.cartSummary =0;
    $scope.cartNotEmpty = false;
    $scope.ordersListNotEmpty = false;

    $scope.loginError = false;
    $scope.username = '';

    $scope.loadCart = function(){
        $http({
            url: baseURL_cart,
            method: 'GET',
            params: {}
        }).then(function(response){
            console.log(response);
            $scope.cartDetail = response.data;
        });

        $http({
            url: baseURL_cart + 'getsummary/',
            method: 'GET',
            params: {}
        }).then(function(response){
            console.log(response);
            $scope.cartSummary = response.data;
            $scope.cartNotEmpty = $scope.cartSummary>0;
        });
    };

    $scope.deleteItem = function(productId){
         $http({
             url: baseURL_cart + 'delete',
             method: 'POST',
             params: {'id':productId, 'quantity':0}
         }).then(function(response){
             $scope.loadCart();
         });
    }

    $scope.decreaseItem = function(productId){
         $http({
             url: baseURL_cart + 'delete',
             method: 'POST',
             params: {'id':productId, 'quantity':1}
         }).then(function(response){
             $scope.loadCart();
         });
    }

    $scope.addItem = function(productId){
        $http({
            url: baseURL_cart,
            method: 'POST',
            params: {'id': productId}
        }).then(function(response){
            $scope.loadCart();
        });
    }

    $scope.clearCart = function(){
        $http({
            url: baseURL_cart + 'clear',
            method: 'GET',
            params: {}
        }).then(function(response){
            $scope.cartDetail = null;
            $scope.cartSummary =0;
            $scope.cartNotEmpty = $scope.cartSummary>0;
        });
    }

    $scope.createOrder = function(){
        var mes = angular.element(document.querySelector("#cart_message_block"));
        mes.text("");
        mes.removeClass("text-danger");
        var fld = angular.element(document.querySelector("#phoneField"));
        fld.removeClass("border-danger");
        fld = angular.element(document.querySelector("#destinationField"));
        fld.removeClass("border-danger");

        $http({
            url: baseURL_orders + 'createorder',
            method: 'POST',
            params: {
                phone: $scope.phone,
                address: $scope.address
            }
        }).then(function successCallback(response){
            $scope.cartDetail = null;
            $scope.cartSummary =0;
            $scope.cartNotEmpty = $scope.cartSummary>0;
            mes.text("Заказ оформлен");
            mes.addClass("text-success");
        }, function errorCallback(response) {
                var ind = 0;
                if(Array.isArray(response.data)){
                    var mes_txt = "";
                    for(ind = 0; ind < response.data.length; ind ++){
                        mes_txt += response.data[ind] + "; ";
                        var fld = null;
                        if(response.data[ind].indexOf("ТЕЛЕФОН")>=0){
                            fld = angular.element(document.querySelector("#phoneField"));
                        }else if(response.data[ind].indexOf("АДРЕС")>=0){
                            fld = angular.element(document.querySelector("#destinationField"));
                        }
                        if(fld!=null){
                            fld.addClass("border-danger");
                        }
                    }
                    mes.text(mes_txt);
                    mes.addClass("text-danger");
                }
           }
        );
    }

   $scope.tryToAuth = function () {
        $http.post(baseURL_auth, $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.marketUser = {username: $scope.user.username, token: response.data.token};
                    $scope.username = $scope.user.username;
                    $scope.loginError=false;
                    $scope.user.username = null;
                    $scope.user.password = null;
                    $http.get(baseURL_cart + 'merge').
                    then(function successCallback(response){
                        $scope.loadCart();
                    });

                }else{
                    $scope.loginError=true;
                }
            }, function errorCallback(response) {
                $scope.loginError=true;
                $scope.username='';
            });
    };

    $scope.clearUser = function () {
        delete $localStorage.marketUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        $scope.username ='';
        $scope.loadCart();
        if ($scope.user.username!=null) {
            $scope.user.username = null;
        }
        if ($scope.user.password!=null) {
            $scope.user.password = null;
        }
    };


    $scope.isUserLoggedIn = function () {
        if ($localStorage.marketUser) {
            return true;
        } else {
            return false;
        }
    };

    if ($localStorage.marketUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.token;
        $scope.username =$localStorage.marketUser.username;
    }

    $scope.initCart = function(){
        if(!$localStorage.cartUuid){
            $http({
                url: baseURL_cart + 'generate',
                method: 'GET',
                params: {}
            }).then(function(response){
                $localStorage.cartUuid = response.data.value;
            });
       }
    }

    $scope.initCart();
    if ($localStorage.cartUuid) {
        $http.defaults.headers.common['CartUuid'] = $localStorage.cartUuid;
    }

    $scope.loadCart();


});