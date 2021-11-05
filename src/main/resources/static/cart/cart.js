
angular.module('app').controller('cartController', function ($scope, $http, $localStorage) {

    $scope.cartDetail = null;
    $scope.cartSummary =0;
    $scope.cartNotEmpty = false;
    $scope.ordersListNotEmpty = false;

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

        angular.element(document.querySelector("#cart_message_block")).text("").removeClass("text-danger");
        angular.element(document.querySelector("#phone")).removeClass("border-danger");
        angular.element(document.querySelector("#addressPostcode")).removeClass("border-danger");
        angular.element(document.querySelector("#addressArea1")).removeClass("border-danger");
        angular.element(document.querySelector("#addressArea2")).removeClass("border-danger");
        angular.element(document.querySelector("#addressLine1")).removeClass("border-danger");
        angular.element(document.querySelector("#addressLine2")).removeClass("border-danger");
        angular.element(document.querySelector("#addressCountrycode")).removeClass("border-danger");

        var elements = document.querySelectorAll('.errmessage');
        for(let elem of elements){
            elem.remove();
        }

        console.log($scope.neworder);
        var emptyOrderData = ["addressCountrycode"];
        $http.post(baseURL_orders + 'createorder', $scope.neworder ? $scope.neworder : '{}')
        .then(function successCallback(response){
            console.log(response);
            $scope.cartDetail = null;
            $scope.cartSummary =0;
            $scope.cartNotEmpty = $scope.cartSummary>0;
            angular.element(document.querySelector("#cart_message_block")).text("Заказ оформлен").addClass("text-success");
        }, function errorCallback(response) {
                console.log(response.data);
                if(response.data){
                    if(Array.isArray(response.data)){
                        var fieldsArr = response.data;
                        angular.element(document.querySelector("#cart_message_block"))
                            .text("Заполните обязательные поля")
                            .addClass("text-danger");
                        fieldsArr.forEach(function(errField, i){
                            let curField = document.querySelector("#" + errField.fieldName);
                            angular.element(curField)
                                    .addClass("border-danger");
                            curField.insertAdjacentHTML("beforebegin", '<span class = "errmessage text-danger">' + errField.message + '</span>');
                        });
                    } else {
                        angular.element(document.querySelector("#cart_message_block"))
                            .text("Ошибка: " + response.data)
                            .addClass("text-danger");
                    }
                }
           }
        );
    }

    $scope.initCart();
    if ($localStorage.cartUuid) {
        $http.defaults.headers.common['CartUuid'] = $localStorage.cartUuid;
    }

    $scope.loadCart();

});