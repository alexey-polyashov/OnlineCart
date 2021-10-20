angular.module('app').controller('productsController', function ($scope, $http, $localStorage) {

    $scope.loadCategories = function(){
        $http({
            url: baseURL_categories,
            method: 'GET'
        }).then(function(response){
            $scope.categories = response.data;
        });
    };

    $scope.loadProducts = function(){
        $http({
            url: baseURL_products,
            method: 'GET',
            params: {
            'page': $scope.pageCounter,
            'recordsOnPage': 6,
            'minprice': $scope.filter!=null ? $scope.filter.minPrice : null,
            'maxprice': $scope.filter!=null ? $scope.filter.maxPrice : null,
            'title': $scope.filter!=null ? $scope.filter.titleLike : null,
            'category': $scope.filter!=null ? $scope.filter.category : null}
        }).then(function(response){
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
            $scope.cartSummary = response.data;
        })
    }

    $scope.clicPrevPage = function () {
        if($scope.pageCounter>0){
            $scope.pageCounter -= 1;
            $scope.loadProducts();
        }
        return false;
    };

    $scope.clicNextPage = function () {
        if($scope.pageCounter<($scope.totalPage-1)){
            $scope.pageCounter += 1;
            $scope.loadProducts();
        }
        return false;
    };

    $scope.goToPage = function (page) {
        if(page>=0 && page<$scope.totalPage){
            $scope.pageCounter = page;
            $scope.loadProducts();
        }
        return false;
    };

    $scope.deleteProduct = function(productId){
        $http({
            url: baseURL_products + 'delete/' + productId,
            method: 'GET',
            params: {}
        }).then(function(response){
            $scope.loadProducts();
        });
    };

    $scope.addToCart = function(productId){
        $http({
            url: baseURL_cart,
            method: 'POST',
            params: {'id': productId}
        }).then(function(response){
            $scope.loadProducts();
        });
        return false;
    };

    $scope.loadCategories();
    $scope.loadProducts();

});