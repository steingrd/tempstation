var fControllers = angular.module('fControllers', []);

fControllers.controller('StartController', ['$scope', function($scope) {
	}]);


fControllers.controller('BrewController', ['$scope', '$routeParams', 

	function($scope, $routeParams) {
		console.log($routeParams.id);
		$scope.brew = $routeParams.id;
	}]);


fControllers.controller('ListBrewsController', ['$scope', '$http', 

	function($scope, $http) {

		$scope.refresh = function() {
			$http.get('/brews').success(function(data) {
				$scope.brews = data;
			});
		};

		$scope.brews = [];
		$scope.refresh();

	}]);