'use strict';

var SERVER_URL = 'http://localhost\\:9000';

afterwords.factory('jobService', function($resource, $log){

  this.submitJob = function(text, success) {
    var JobResource = $resource(SERVER_URL + '/api/jobs/:jobId', {});
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

  return this;
});

