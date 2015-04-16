var app = angular.module('acm');

app.factory('challengeFactory', ['$http', function($http) {
	var o = {
		challenges: [],
		openChallenges: [],
		challenge: {}
	};
	o.getAll = function() {
		return $http.get('/challenges/getall').success(function(data) {
			angular.copy(data, o.challenges);
		});
	};
	o.getOpen = function() {
		return $http.get('/challenges/getopen').success(function(data) {
			angular.copy(data, o.openChallenges);
		});
	};
	return o;
}]);

app.controller('ChallengesCtrl', [
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