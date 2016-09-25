var module = angular.module('restApi', ['ngMaterial']);
module.controller('appController', function($scope, $templateCache, $http) {
	$scope.subHeaderText = "Listagem dos arquivos que foram carregados"
	$scope.listUrl = 'rest/service/listUploads';
	$scope.listUploads = function() {
		$http({
			method: 'GET',
			url: window.location.href + $scope.listUrl
		}).then(function(response) {
			if(response.data && response.data.length > 0) {
				$scope.uploadList = response.data;
				$scope.subHeaderText = "Foram encontrados " + response.data.length + " arquivos carregados:";
			} else if(response.data.length == 0) {
				$scope.subHeaderText = "Não foram encontrados registros, tente carregar um arquivo na aba Upload!";
			}
		}, function(response) {
			alert('fail')
		})}

	$scope.uploadFile = function() {
		alert('upload')
	}
	
	$scope.cleanList = function() {
		$scope.uploadList = undefined;
		$scope.subHeaderText = "Listagem dos arquivos que foram carregados";
	}
});