const baseURL_products = 'http://localhost:8189/onlinecart/api/v1/products/product/';
const baseURL_categories = 'http://localhost:8189/onlinecart/api/v1/products/category/';
const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';
const baseURL_auth =    'http://localhost:8189/onlinecart/api/v1/auth/';
const baseURL_orders = 'http://localhost:8189/onlinecart/api/v1/orders/';
const baseURL_paypal = 'http://localhost:8189/onlinecart/api/v1/paypal/';

(function ($localStorage) {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'products/products.html',
                controller: 'productsController'
            })
            .when('/products', {
                  templateUrl: 'products/products.html',
                  controller: 'productsController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/order', {
                  templateUrl: 'order/order.html',
                  controller: 'orderController'
            })
            .when('/contacts', {
                  templateUrl: 'contacts/contacts.html',
                  controller: 'contactsController'
            })
            .when('/order/:orderId', {
                templateUrl: 'order/order.html',
                controller: 'orderController'
            })
            .otherwise({
                redirectTo: '/products'
            })
            ;
    }

    function run($rootScope, $http, $localStorage) {

        const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';

        if ($localStorage.summerUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.summerUser.token;
        }
        if(!$localStorage.cartUuid){
            $http({
                url: baseURL_cart + 'generate',
                method: 'GET',
                params: {}
            }).then(function successCallback(response){
                $localStorage.cartUuid = response.data.value;
            });
       }
       if ($localStorage.cartUuid) {
           $http.defaults.headers.common['CartUuid'] = $localStorage.cartUuid;
       }

    }

})();


angular.module('app').controller('indexController', function ($rootScope, $location, $scope, $http, $localStorage) {

    $scope.loginError = false;
    $scope.username = '';

    $scope.baseURL_products = baseURL_products;

    $scope.pageCounter = 0;
    $scope.totalPage = 0;
    $scope.pagesInView = 3;
    $scope.cartSummary = 0;
    $scope.cartDetail;

    $scope.tryToAuth = function () {
        $http.post(baseURL_auth, $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.marketUser = {username: $scope.user.username, token: response.data.token};
                    $scope.username = $scope.user.username;
                    $scope.loginError=false;
                    if ($scope.user.username) {
                        $scope.user.username = null;
                    }
                    if ($scope.user.password) {
                        $scope.user.password = null;
                    }
                    $http.get(baseURL_cart + 'merge')
                        .then(function successCallback(response) {
                            $scope.loadOrders();
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
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
        $scope.username ='';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.marketUser) {
            return true;
        } else {
            return false;
        }
    };

    $scope.initCart = function(){
        if(!$localStorage.cartUuid){
            $http({
                url: baseURL_cart + 'generate',
                method: 'GET',
                params: {}
            }).then(function(response){
            console.log(response);
                $localStorage.cartUuid = response.data.value;
            });
          }
    }

    $scope.initCart();

    if ($localStorage.marketUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.token;
        $scope.username =$localStorage.marketUser.username;
    }
    if ($localStorage.cartUuid) {
        $http.defaults.headers.common['CartUuid'] = $localStorage.cartUuid;
    }

});