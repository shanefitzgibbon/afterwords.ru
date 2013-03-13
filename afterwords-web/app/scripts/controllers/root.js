'use strict';

afterwords.controller('RootCtrl', function($scope, $window, $http, $dialog){
  $scope.updateLang = function(language) {
    $.i18n.setLng(language, function(){
//      $scope.$apply();
      $window.location.reload(); //this event should be rare so a reload is ok??
    });
  }

  $scope.openLoginDialog = function(){
    var d = $dialog.dialog();
    d.open('views/login.html', 'LoginCtrl');
  };

  $scope.$on('event:auth-loginRequired', function() {
    $scope.openLoginDialog();
  });

  $scope.$on('event:auth-loginConfirmed', function() {

  });


});

