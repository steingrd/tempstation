var controllers = angular.module('controllers', []);

controllers.controller('StartPageController', ['$scope', function($scope) {
}]);

controllers.controller('BrewsPageController', ['$scope', '$http', function($scope, $http) {

	$scope.refresh = function() {
		
		$http.get('/brews').success(function(data) {
			console.log("FOO");
			console.log(JSON.stringify(data));
			$scope.brews = data;
		});
		
	};

	$scope.brews = [];
	$scope.refresh();

}]);

controllers.controller('BrewDetailsController', ['$scope', '$http', function($scope, $http) {

	$scope.brews = [
		{
			"key": "testbrew",
			"lastUpdated": "2014-04-17T16:16:42"
		}
	];

}]);