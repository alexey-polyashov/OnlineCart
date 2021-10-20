angular.module('app').controller('orderController', function ($scope, $http, $localStorage, $routeParams) {

    $scope.orderNotPaid = true;

    $scope.loadOrder = function () {
        $http({
            url: baseURL_orders + "orderdetail/" + $routeParams.orderId,
            method: 'GET'
        }).then(function (response) {
            $scope.order = response.data;
            $scope.orderNotPaid = response.data.status == "PLACED";
            $scope.renderPaymentButtons();
        });
    }
    if ($localStorage.marketUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.token;
        $scope.username =$localStorage.marketUser.username;
    }
    $scope.renderPaymentButtons = function() {
        paypal.Buttons({
            createOrder: function(data, actions) {
                return fetch(baseURL_paypal + 'create/' + $scope.order.id, {
                    method: 'post',
                    headers: {
                        'content-type': 'application/json',
                        'Authorization': 'Bearer ' + $localStorage.marketUser.token
                    }
                }).then(function(response) {
                    return response.text();
                });
            },

            onApprove: function(data, actions) {
                return fetch(baseURL_paypal + 'capture/' + data.orderID, {
                    method: 'post',
                    headers: {
                        'content-type': 'application/json',
                        'Authorization': 'Bearer ' + $localStorage.marketUser.token
                    },
                    body: JSON.stringify({
                        orderID: data.orderID
                    })
                }).then(function successCallback(response) {
                        var orderStatus = angular.element(document.querySelector("#orderStatus"));
                        orderStatus.removeClass("bg-danger");
                        orderStatus.addClass("bg-success");
                        $scope.orderNotPaid = false;
                        orderStatus.text("Оплачен");
                        var payPalButtons = angular.element(document.querySelector("#paypal-buttons"));
                        payPalButtons.remove();
                    });
            },

            onCancel: function (data) {
                console.log("Order canceled: " + data);
                alert("Заказ отменен");
            },

            onError: function (err) {
                console.log(err);
                alert(err.token);
            }
        }).render('#paypal-buttons');
    }

    $scope.loadOrder();
});