var module = angular.module('restApi', ['ngMaterial']);
module.controller('appController', function($scope, $templateCache, $http, $mdDialog, $rootScope) {
	var scope = $scope;
	var root = $rootScope;
	$scope.subHeaderText = "Listagem dos arquivos que foram carregados"
	$scope.listUrl = 'rest/filehandler/listUploads';
	$scope.uploadUrl = 'rest/filehandler/upload';
	$scope.uploadReportUrl = 'rest/filehandler/uploadReport';
	$scope.getFilePrefix = 'rest/filehandler/getfile/';
	
	$scope.listUploads = function() {
		$http({
			method: 'GET',
			url: window.location.href + $scope.listUrl
		}).then(function(response) {
			if(response.data && response.data.length > 0) {
				$scope.uploadList = response.data;
				$scope.subHeaderText = "Foram encontrados " + response.data.length + " arquivos carregados:";
			} else if(response.data.length == 0) {
				$scope.subHeaderText = "Nenhum registro encontrado, tente carregar um arquivo na aba Upload!";
			}
			$scope.showListLoading = false;
		}, function(response) {
			$scope.subHeaderText = "Algo deu errado, por favor tente novamente!";
			$scope.showListLoading = false;
		})
	}
	
	$scope.getValue = function(text) {
		scope.userName = text;
	}

	$scope.uploadFile = function() {
		if($scope.userName && $scope.fileLoaded) {
			$scope.showUploadLoading = true
			var filesUploaded = 0;
			$scope.recursiveUpload(0);
			$scope.uploadStart = new Date().getTime();
		} else {
			var popup = $mdDialog.alert()
		      .title("Oops...")
		      .content("Verifique se o identificador de usuario foi setado no canto superior direito da tela e seu arquivo upado")
		      .ok("Ok");

		    $mdDialog.show(popup);
		}
	}
	
	$scope.downloadFile = function(link) {
		window.location.href = link;
	}
	
	$scope.uploadReport = function(status, successFunction) {
		var upload = {
			chunkFileNumber: $scope.fileLoaded.length,
			fileName: $scope.fileLoadedName,
			uploadTime: $scope.timeToUpload,
			finishedUpload: status
		}
		$http.post($scope.uploadReportUrl, upload)
		.success(function(data) {
			if(successFunction) {
				successFunction();
			}
		});
	}
	
	root.$watch('finishedUpload', function(newValue, oldValue) {
		if(newValue && newValue.status) {
			$scope.uploadEnd = new Date().getTime();
			$scope.timeToUpload = $scope.uploadEnd - $scope.uploadStart;
			$scope.uploadReport(newValue.status, $scope.clearInfo);
		}
	});
	
	$scope.clearInfo = function() {
		$scope.uploadStart = undefined;
		$scope.uploadEnd = undefined;
		$scope.timeToUpload = undefined;
		$scope.fileLoaded = undefined;
		$scope.fileLoadedName = undefined;
		$scope.showUploadLoading = false;
		document.getElementById('uploadHidden').value = '';
	}
	
	$scope.recursiveUpload = function(index) {
		var fd = new FormData();
		fd.append('fileName', $scope.fileLoadedName);
		fd.append('filePartName', $scope.fileLoadedName + "part" + index);
		fd.append('userId', $scope.userName);
		fd.append('file', $scope.fileLoaded[index]);
		fd.append('data', 'string');
		$http.post($scope.uploadUrl, fd, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		}).success(function(data) {
			if(index < $scope.fileLoaded.length - 1) {
				$scope.uploadReport(false);
				$scope.recursiveUpload(index + 1)
			} else {
				root.finishedUpload = {status: true};
			}
		}).error(function() {
			root.finishedUpload = {status: true};
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

	   while (chunk < chunks) {
	       var offset = chunk*chunkSize;
	       $scope.fileLoaded.push(f.slice(offset,offset+chunkSize))
	       chunk++;
	   }
	   
	   $scope.$apply();
    }
	
});