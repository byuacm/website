angular.module('acm', ['ui.router', 'ngCookies', 'ui.bootstrap'])
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
			controller: 'ChallengesCtrl'
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
  function($scope, $state, $window, $cookies){
	function checkCookie() {
		return false;
	}

	var loggedIn = checkCookie();
	$scope.isActive = function(state) {
		return state === $state.current.name;
	};
	$scope.isLoggedIn = function() {
		return loggedIn;
	};
	$scope.logIn = function() {
		// check for cookie first
		var window = $window.open(
			'https://cas.byu.edu/cas/login?service=http://localhost:9000/login/cas',
			'targetWindow',
			'toolbar=no,' +
			'location=no,' +
			'status=no,' +
			'menubar=no,' +
			'scrollbars=yes,' +
			'resizeable=yes,' +
			'width=600,' +
			'height=650');

		window.onbeforeunload = function(event) {

		};
		loggedIn = true;
	};
	$scope.register = function() {
		// check for cookie
		var window = $window.open(
			'https://cas.byu.edu/cas/login?service=http://localhost:9000/register/cas',
			'targetWindow',
			'toolbar=no,' +
			'location=no,' +
			'status=no,' +
			'menubar=no,' +
			'scrollbars=yes,' +
			'resizeable=yes,' +
			'width=600,' +
			'height=650');

		window.onbeforeunload = function(event) {

		};
		loggedIn = true;
	};
	$scope.checkIn = function() {
		// Check in to the current meeting - only if there is one
	};
	$scope.logOut = function() {
		// check for NO cookie
		var window = $window.open('https://cas.byu.edu/cas/logout');

		window.onbeforeunload = function(event) {

		};
		loggedIn = false;
	};
  }
])
.factory('challengeFactory', ['$http', function($http) {
	var o = {
		challenges: [],
		openChallenges: [],
		challenge: {}
	};
	o.getAll = function() {
		return $http.get('/challenges').success(function(data) {
			angular.copy(data, o.challenges);
		});
	};
	o.getOpen = function() {
		return $http.get('/challenges/open').success(function(data) {
			angular.copy(data, o.openChallenges);
		});
	};
	return o;
}])
.controller('ChallengesCtrl', [
	'$scope',
	'challengeFactory',
	function($scope, challengeFactory) {
		challengeFactory.getAll();
		$scope.challenges = challengeFactory.challenges;

		challengeFactory.getOpen();
		var open = challengeFactory.openChallenges;

		$scope.isChallengeDisabled = function(id) {
			for (var i = 0; i < open.length; i++) {
				if (open[i].id == id) {
					return false;
				}
			}
			return true;
		}
	}
]);