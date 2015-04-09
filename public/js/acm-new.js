angular.module('acm', ['ui.router'])
.config([
	'$stateProvider',
	'$urlRouterProvider',
	function($stateProvider, $urlRouterProvider) {
		$stateProvider.state('home', {
			url: '/',
			templateUrl: '/html/home.html',
			//controller: 'HomeCtrl'
		})
		.state('challenges', {
			url: '/challenges',
			templateUrl: '/html/challenges.html',
			//controller: 'ChallengesCtrl'
		})
		.state('partner', {
			url: '/partner',
			templateUrl: '/html/partner.html',
			//controller: 'PartnerCtrl'
		})
		.state('roster', {
			url: '/roster',
			templateUrl: '/html/roster.html',
			//controller: 'RosterCtrl'
		});
		$urlRouterProvider.otherwise('/');
	}
])
.controller('MainCtrl', [
  '$scope',
  '$state',
  '$window',
  function($scope, $state, $window){
	$scope.loggedIn = false; // just for now 
	$scope.isActive = function(state) {
		return state === $state.current.name;
	};
	$scope.isLoggedIn = function() {
		return $scope.loggedIn; // here we need to check for the cookie
	};
	$scope.logIn = function() {
		// check for cookie first
		var window = $window.open('https://cas.byu.edu/cas/login?service=http://localhost:9000/cas/login');
		$scope.loggedIn = true;
	};
	$scope.register = function() {
		// check for cookie
		var window = $window.open('https://cas.byu.edu/cas/login?service=http://localhost:9000/cas/register');
		$scope.loggedIn = true;
	};
	$scope.checkIn = function() {
		// Check in to the current meeting - only if there is one
	};
	$scope.logOut = function() {
		// check for NO cookie
		var window = $window.open('https://cas.byu.edu/cas/logout');
		$scope.loggedIn = false;
	};
  }
]);