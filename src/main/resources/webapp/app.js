var app = angular.module('app', [
		'ngRoute',
		'controllers'
	]);

app.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider
			.when('/', {
				templateUrl: 'partials/index.html',
				controller: 'StartPageController'
			})
			
			.when('/brews', {
				templateUrl: 'partials/brews.html',
				controller: 'BrewsPageController'
			})

			.when('/brews/:brewId', {
				templateUrl: 'partials/brewDetails.html',
				controller: 'BrewDetailsController'
			})

			.otherwise({
				redirectTo: '/'
			});
	}]);
