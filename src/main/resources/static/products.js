angular.module('app', ['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {

    const baseURL_products = 'http://localhost:8189/onlinecart/api/v1/products/product/';
    const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';
    const baseURL_auth =    'http://localhost:8189/onlinecart/api/v1/auth/';
    const baseURL_orders = 'http://localhost:8189/onlinecart/api/v1/orders/';


    $scope.pageCounter = 0;
    $scope.totalPage = 0;
    $scope.pagesInView = 3;
    $scope.cartSummary = 0;
    $scope.cartDetail;

    $scope.loginError = false;
    $scope.username = '';

    $scope.loadProducts = function(){
        $http({
            url: baseURL_products,
            method: 'GET',
            params: {
            'page': $scope.pageCounter,
            'recordsOnPage': 6,
            'minprice': $scope.filter!=null ? $scope.filter.minPrice : null,
            'maxprice': $scope.filter!=null ? $scope.filter.maxPrice : null,
            'title': $scope.filter!=null ? $scope.filter.titleLike : null}
        }).then(function(response){
            console.log(response);
            showProducts(response.data);
        });
    };

    function showProducts(response_data){
        $scope.products = response_data.content;
        $scope.totalPage = response_data.totalPages;
        let minPageNumber = Math.floor($scope.pageCounter / $scope.pagesInView) * $scope.pagesInView;
        $scope.viewedPageNumbers = [];
        $scope.pageNumbers = [];
        for(let i=0; i<$scope.pagesInView && i+minPageNumber < $scope.totalPage; i++){
            $scope.viewedPageNumbers[i] = (minPageNumber+i)+1;
            $scope.pageNumbers[i] = (minPageNumber+i);
        };
        $http({
            url: baseURL_cart + 'getsummary',
            method: 'GET',
            params: {}
        }).then(function(response){
            console.log(response);
            $scope.cartSummary = response.data;
        })
    }

    $scope.clicPrevPage = function () {
        if($scope.pageCounter>0){
            $scope.pageCounter -= 1;
            $scope.loadProducts();
        }
    };

    $scope.clicNextPage = function () {
        if($scope.pageCounter<($scope.totalPage-1)){
            $scope.pageCounter += 1;
            $scope.loadProducts();
        }
    };

    $scope.goToPage = function (page) {
        if(page>=0 && page<$scope.totalPage){
            $scope.pageCounter = page;
            $scope.loadProducts();
        }
    };

    $scope.deleteProduct = function(productId){
        $http({
            url: baseURL_products + 'delete/' + productId,
            method: 'GET',
            params: {}
        }).then(function(response){
            $scope.loadProducts();
        });
    }

    $scope.addToCart = function(productId){
        $http({
            url: baseURL_cart,
            method: 'POST',
            params: {'id': productId}
        }).then(function(response){
            $scope.loadProducts();
        });
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
                    $scope.loadOrders();

                    $http.get(baseURL_cart + 'merge');

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

    $scope.loadProducts();

});