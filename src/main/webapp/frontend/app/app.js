var module = angular.module('restApi', ['ngMaterial']);
module.controller('appController', function($scope, $templateCache, $http, $mdDialog, $rootScope) {
	var root = $rootScope;
	$scope.subHeaderText = "Listagem dos arquivos que foram carregados"
	$scope.listUrl = 'rest/service/listUploads';
	$scope.uploadUrl = 'rest/service/upload';
	$scope.listUploads = function() {
		$scope.showListLoading = true;
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
			$scope.showListLoading = false;
		}, function(response) {
			$scope.subHeaderText = "Algo deu errado, por favor tente novamente!";
			$scope.showListLoading = false;
		})}

	$scope.uploadFile = function() {
		var filesUploaded = 0;
		$scope.recursiveUpload(0);
		alert('comecou')
		//TODO:set entity to PROCESSING 
	}
	
	root.$watch('finishedUpload', function(newValue, oldValue) {
		if(newValue && newValue.status == 'sucess') {
			alert('acabou');
			//TODO: set entity to DONE or FAILED with time
		} else
	});
	
	$scope.recursiveUpload = function(index) {
		var fd = new FormData();
		fd.append('fileName', $scope.fileLoadedName);
		fd.append('file', $scope.fileLoaded[index]);
		fd.append('data', 'string');
		$http.post($scope.uploadUrl, fd, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		}).success(function(data) {
			if(index < $scope.fileLoaded.length - 1) {
				$scope.recursiveUpload(index + 1)
			} else {
				root.finishedUpload = {status: 'DONE'};
			}
		}).error(function() {
			root.finishedUpload = {status: 'FAILED'};
		});
	}
	
	$scope.cleanList = function() {
		$scope.uploadList = undefined;
		$scope.subHeaderText = "Listagem dos arquivos que foram carregados";
	}
	
	$scope.handleFileSelect = function(file) {
	   var files = file.files;
	   var f = files[0];
	   $scope.fileLoaded = [];
	   $scope.fileLoadedName = f.name;
	   
	   var chunkSize = 1024 * 1024;
	   var fileSize = f.size;
	   var chunks = Math.ceil(f.size/chunkSize,chunkSize);
	   var chunk = 0;

	   while (chunk <= chunks) {
	       var offset = chunk*chunkSize;
	       $scope.fileLoaded.push(f.slice(offset,offset+chunkSize))
	       chunk++;
	   }
    }
	
});