'use strict';

afterwords.controller('RootCtrl', function($scope, $rootScope, $window, $http, $dialog){

  $rootScope.login = {};
  $rootScope.login.email = '';
  $rootScope.login.password = '';
  $rootScope.login.user = null;

  $scope.updateLang = function(language) {
    $.i18n.setLng(language, function(){
//      $scope.$apply();
      $window.location.reload(); //this event should be rare so a reload is ok??
    });
  }

  $rootScope.openLoginDialog = function(){
    var d = $dialog.dialog();
    d.open('views/login.html', 'LoginCtrl');
  };

  $rootScope.openRegistrationDialog = function(){
    var d = $dialog.dialog();
    d.open('views/register.html', 'RegisterCtrl');
  };

  $rootScope.login.disconnect = function() {
    $rootScope.login.email = '';
    $rootScope.login.password = '';
    $rootScope.login.user = null;
  };

  $rootScope.$watch('login.email + login.password', function() {
    if ($rootScope.login.email && $rootScope.login.password){
      $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($rootScope.login.email + ':' + $rootScope.login.password);
    }
    else {
      delete $http.defaults.headers.common['Authorization'];
    }
  });

  $scope.$on('event:auth-loginRequired', function(event, response) {
    if (response.status === 401){
        var message = response.data;
        if (message) $rootScope.login.alerts = [{type: 'error', msg: message}];
    }
    $scope.openLoginDialog();
  });

  $scope.$on('event:auth-loginConfirmed', function() {
      $rootScope.login.alerts = [];
  });


});

