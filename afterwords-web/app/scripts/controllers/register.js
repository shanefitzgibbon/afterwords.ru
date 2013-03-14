'use strict';

afterwords.controller('RegisterCtrl', function($scope, $rootScope, $http, dialog, $log) {
  $scope.registration = {};
  $scope.registration.firstName = '';
  $scope.registration.lastName = '';
  $scope.registration.email = '';
  $scope.registration.password = '';
  $scope.registration.confirm = '';

  $scope.registration.register = function() {
    dialog.close();
  };

  $scope.registration.cancel = function() {
    dialog.close();
  }

  $scope.switch = function() {
    dialog.close();
    $rootScope.openLoginDialog();
  }

});