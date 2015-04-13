var app = angular.module('acm');

app.factory('rosterFactory', ['$http', function($http) {
	var o = {
		roster: []
	};
	o.getAll = function() {
		return $http.get('/profiles').success(function(data) {
			angular.copy(data, o.roster);
		});
	};
	return o;
}]);

app.controller('RosterCtrl', [
	'$scope',
	'rosterFactory',
	function($scope, rosterFactory) {
		rosterFactory.getAll();
		$scope.roster = rosterFactory.roster;
	}
]);