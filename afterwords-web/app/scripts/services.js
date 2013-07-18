'use strict';

// devcode: !production
//var RESOURCE_SERVER_URL = 'http://localhost\\:9000';
//var SERVER_URL = 'http://localhost:9000';
// endcode

// devcode: production
var RESOURCE_SERVER_URL = 'http://afterwords.cloudfoundry.com';
var SERVER_URL = 'http://afterwords.cloudfoundry.com';
//endcode

afterwords.factory('jobService', function($resource, $log){

  this.submitJob = function(text, success) {
    var JobResource = $resource(RESOURCE_SERVER_URL + '/api/jobs/:jobId', {});
    var newJob = new JobResource();
    newJob.text = text;
    newJob.$save(function(serverJob, responseHeaders){
      $log.info('received job back from server');
      $log.log('data: ' + serverJob);
      $log.log('responseHeaders: ' + responseHeaders);
      if(success){
        success(serverJob, responseHeaders);
      }
    });
  };

  this.pendingJobs = function() {
    var DataSource = $resource(RESOURCE_SERVER_URL + '/api/jobs/pending');
    return DataSource.query();
  }

  this.completedJobs = function() {
    var DataSource = $resource(RESOURCE_SERVER_URL + '/api/jobs/completed');
    return DataSource.query();
  }

  return this;
});

afterwords.factory('userService', function($http, $resource, authService, $log){

  //attempt to get the user information from the server.
  //On success , notify the authService that login is confirmed
  this.connect = function(successFn, errorFn) {
    var config = {"withCredentials": true};
    $http.get(SERVER_URL + '/api/login', config).success(function(data, status) {
      // despite its name, this callback is triggered even in case of an error, so we have to take care
      if (status < 200 || status >= 300){
        $log.error("Error getting user from server. Server status code: " + status);
        errorFn(data, status);
        return;
      }
      authService.loginConfirmed();
      successFn(data, status);
    })
    .error(function(data, status){
        $log.error("Error getting user from server. Server status code: " + status);
        errorFn(data, status);
    });
  };

  this.register = function(registration, successFn, errorFn) {
    //turn off credentials for registration
    var config = {"withCredentials": false};
    var newRegistration = {};
    newRegistration.firstName = registration.firstName;
    newRegistration.lastName = registration.lastName;
    newRegistration.email = registration.email;
    newRegistration.password = registration.password;
    $http.post(SERVER_URL + '/api/users/register', newRegistration, config)
      .success(function(data, status) {
        $log.log('user service - register() returned data: ' + data);
        $log.log('id: ' + data.id);
        if(successFn) successFn(data);
      })
      .error(function(data, status) {
        $log.error('user service - register() returned ERROR with data: ' + data);
        $log.error('status: ' + status);
        if (errorFn) errorFn(data, status);
      });
  };

  return this;
});


//$rootScope.login.connect = function() {
//  var config = {"withCredentials": true};
//  $http.get('http://localhost:9000/api/users/' + $rootScope.login.email, config).success(function(data, status) {
//    // despite its name, this callback is triggered even in case of an error, so we have to take care
//    if (status < 200 || status >= 300){
//      dialog.close();
//      $log.error("Error getting user from server. Server status code: " + status);
//      return;
//    }
//    $rootScope.login.user = data;
//    authService.loginConfirmed();
//    dialog.close();
//  });
//};

