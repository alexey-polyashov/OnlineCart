
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
        angular.element(document.querySelector("#phoneField")).removeClass("border-danger");
        angular.element(document.querySelector("#addressPostcode")).removeClass("border-danger");
        angular.element(document.querySelector("#addressArea1")).removeClass("border-danger");
        angular.element(document.querySelector("#addressArea2")).removeClass("border-danger");
        angular.element(document.querySelector("#addressLine1")).removeClass("border-danger");
        angular.element(document.querySelector("#addressLine2")).removeClass("border-danger");
        angular.element(document.querySelector("#addressCountrycode")).removeClass("border-danger");

        $http({
            url: baseURL_orders + 'createorder',
            method: 'POST',
            params: {
                phone: $scope.phone,
                addressPostcode: $scope.address_postcode,
                addressCountrycode: $scope.address_countrycode,
                addressArea1: $scope.address_area1,
                addressArea2: $scope.address_area2,
                addressLine1: $scope.address_line1,
                addressLine2: $scope.address_line2
            }
        }).then(function successCallback(response){
            console.log(response);
            $scope.cartDetail = null;
            $scope.cartSummary =0;
            $scope.cartNotEmpty = $scope.cartSummary>0;
            angular.element(document.querySelector("#cart_message_block")).text("Заказ оформлен").addClass("text-success");
        }, function errorCallback(response) {
                console.log(response);
                if(response.data && response.data.message){
                    angular.element(document.querySelector("#cart_message_block"))
                        .text(response.data.message)
                        .addClass("text-danger");
                }
                if(Array.isArray(response.data.fieldErrors)){
                        if(response.data.fieldErrors.indexOf("phoneField")>=0){
                            angular.element(document.querySelector("#phoneField")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressArea1")>=0){
                            angular.element(document.querySelector("#addressArea1")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressArea2")>=0){
                            angular.element(document.querySelector("#addressArea2")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressLine1")>=0){
                            angular.element(document.querySelector("#addressLine1")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressLine2")>=0){
                            angular.element(document.querySelector("#addressLine2")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressCountrycode")>=0){
                            angular.element(document.querySelector("#addressCountrycode")).addClass("border-danger");
                        }
                        if(response.data.fieldErrors.indexOf("addressPostcode")>=0){
                            angular.element(document.querySelector("#addressPostcode")).addClass("border-danger");
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