'use strict';

afterwords.controller('MainCtrl', function($scope, $resource, jobService, $location, $log) {
  function Document(){
    //private vars
    var wordsPerPage = 300;
    var costPerPage = 250;
    var costIncludeAnalysis = 700;
    //public vars
    this.text = '';
    this.agreeTerms = false;
    this.includeAnalysis = false;
    this.serverId = '';
    //public functions
    this.wordCount = function(){
      if (typeof this.text == 'undefined') return 0;
      if (this.text.length == 0) return 0;
      var regex = /\s+/gi;
      var count = this.text.trim().replace(regex, ' ').split(' ').length;
      return count
    }
    this.pageCount = function(){
      return Math.ceil(this.wordCount() / wordsPerPage);
    }
    this.cost = function(){
      var documentCost = this.pageCount() * costPerPage;
      var additionalCosts = this.includeAnalysis ? (costIncludeAnalysis * this.pageCount()) : 0;
      return documentCost + additionalCosts;
    }
    this.submit = function(){
      if (this.agreeTerms && this.wordCount() > 0){
        jobService.submitJob(this.text, function(createdJob){
          $log.info('job service returned: ' + createdJob.id);
          $scope.awdoc.serverId = createdJob.id;
          $scope.process.currentStep = 'step3'
        });
      }
    }

  }
  $scope.awdoc = new Document();
  function Process(){
    //public vars
    this.currentStep = 'step1';

    //public functions
    this.cancel = function(){
      this.currentStep = 'step1';

    }
  }
  $scope.process = new Process();

//  $scope.$watch('process.currentStep', function(step){
//    $location.search({'step' : step});
//  });

});
