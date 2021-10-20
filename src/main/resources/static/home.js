angular.module('app', ['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {

    const baseURL_products = 'http://localhost:8189/onlinecart/api/v1/products/product/';
    const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';
    const baseURL_auth =    'http://localhost:8189/onlinecart/api/v1/auth/';
    const baseURL_orders = 'http://localhost:8189/onlinecart/api/v1/orders/';

    $scope.loginError = false;
    $scope.username = '';

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
                    $scope.loadOrders();
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

    if ($localStorage.marketUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.token;
        $scope.username =$localStorage.marketUser.username;
    }

});