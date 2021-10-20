angular.module('app', ['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {

    const baseURL_products = 'http://localhost:8189/onlinecart/api/v1/products/product/';
    const baseURL_cart  = 'http://localhost:8189/onlinecart/api/v1/cart/';
    const baseURL_auth =    'http://localhost:8189/onlinecart/api/v1/auth/';
    const baseURL_orders = 'http://localhost:8189/onlinecart/api/v1/orders/';
    const baseURL_paypalController = 'http://localhost:8189/onlinecart/paypal/';

    $scope.ordersListNotEmpty = false;

    $scope.loginError = false;
    $scope.username = '';

    $scope.loadOrders = function () {
        if (!$scope.isUserLoggedIn()) {
            return;
        }
        $http({
            url: baseURL_orders,
            method: 'GET'
        }).then(function (response) {
            $scope.orders = response.data;
            $scope.ordersListNotEmpty = response.data.length>0;
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
        $scope.loadOrders();
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
        $scope.loadOrders();
    }

    $scope.showorder = function(orderId){
        var orderHead = angular.element(document.querySelector("#order_id_"+orderId));
        orderHead.toggleClass("selected_order");
        if(orderHead.hasClass("selected_order")){
            $http({
                url: baseURL_orders + "orderdetail/"+orderId,
                method: 'GET'
            }).then(function (response) {
                var details = '<tr class="order_detail_' + orderId + '"><td colspan = "5" class="selected_order_cell">'+
                '<table class="table"><thead><tr class="selected_order_details_header"><td>№п/п</td><td>Наименование</td><td>Цена</td><td>Количество</td><td>Стоимость</td></tr></thead>'+
                '<tbody>';
                for(var ind = 0 ; ind < response.data.length; ind++){
                    details = details+'<tr class="selected_order_details">'+
                    '<td>'+(ind+1)+'</td>'+
                    '<td>'+response.data[ind].product.title+'</td>'+
                    '<td>'+response.data[ind].price+'</td>'+
                    '<td>'+response.data[ind].quantity+'</td>'+
                    '<td>'+response.data[ind].totalPrice+'</td>'+
                    '</tr>';
                }
                details = details + '</tbody></td></tr>';
                orderHead = orderHead.after(details);
            });

        }else{
            var stringNodes = document.querySelectorAll('.order_detail_'+orderId);
            for(var ind=0; ind < stringNodes.length; ind++){
                stringNodes[ind].remove();
            }
        }
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

    $scope.confirmPay = function(orderId, paymentId, payerId){
    console.log("confirmPay:paymentId " + paymentId);
    console.log("confirmPay:payerId " + payerId);
       $http({
            url: baseURL_paypalController + 'complete/payment',
            method: 'POST',
            params: {'orderId':orderId, 'paymentId':paymentId, 'payerId':payerId}
        }).then(function(response){
            $scope.loadOrders();
        });
    }

    $scope.pay = function(orderId){
       $http({
            url: baseURL_paypalController + 'make/payorder',
            method: 'POST',
            params: {'orderId':orderId}
        }).then(function(response){
            document.location.href = response.data.redirect_url;
        });
    }

    const searchString = new URLSearchParams(window.location.search);

    const paymentId = searchString.get('paymentId');
    const payerId = searchString.get('PayerID');
    const orderId = searchString.get('orderId');

    if(paymentId!=null && payerId!=null){
        $scope.confirmPay(orderId, paymentId, payerId);
    }

    $scope.loadOrders();

});