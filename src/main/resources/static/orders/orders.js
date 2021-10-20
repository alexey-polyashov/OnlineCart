angular.module('app').controller('ordersController', function ($scope, $http, $localStorage, $location) {

    $scope.ordersListNotEmpty = false;

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

    $scope.showorder = function(orderId){
        var orderHead = angular.element(document.querySelector("#order_id_"+orderId));
        orderHead.toggleClass("selected_order");
        if(orderHead.hasClass("selected_order")){
            $http({
                url: baseURL_orders + "orderdetail/"+orderId,
                method: 'GET'
            }).then(function (response) {
                console.log(response);
                var details = '<tr class="order_detail_' + orderId + '"><td colspan = "5" class="selected_order_cell">'+
                '<table class="table"><thead><tr class="selected_order_details_header"><td>№п/п</td><td>Наименование</td><td>Цена</td><td>Количество</td><td>Стоимость</td></tr></thead>'+
                '<tbody>';
                for(var ind = 0 ; ind < response.data.items.length; ind++){
                    details = details+'<tr class="selected_order_details">'+
                    '<td>'+(ind+1)+'</td>'+
                    '<td>'+response.data.items[ind].title+'</td>'+
                    '<td>'+response.data.items[ind].price+'</td>'+
                    '<td>'+response.data.items[ind].quantity+'</td>'+
                    '<td>'+response.data.items[ind].totalPrice+'</td>'+
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

    $scope.pay = function (orderId) {
        $location.path('/order/' + orderId);
    }

    const searchString = new URLSearchParams(window.location.search);

    $scope.loadOrders();

});