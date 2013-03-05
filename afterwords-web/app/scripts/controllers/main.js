'use strict';

afterwords.controller('MainCtrl', function($scope, $resource) {
  function Document(){
    //private vars
    var wordsPerPage = 300;
    var costPerPage = 200;
    //public vars
    this.text = ''
    this.agreeTerms = false;
    //public functions
    this.wordCount = function(){
      if (this.text == undefined || this.text.length == 0) return 0;
      var regex = /\s+/gi;
      var count = this.text.trim().replace(regex, ' ').split(' ').length;
      return count
    }
    this.pageCount = function(){
      return Math.ceil(this.wordCount() / wordsPerPage);
    }
    this.cost = function(){
      return this.pageCount() * costPerPage;
    }
    this.submit = function(){
      if (this.agreeTerms && this.wordCount() > 0){
        var Job = $resource('http://localhost\\:9000/api/jobs/:jobId', {});
        var newJob = new Job();
        newJob.text = this.text;
        newJob.$save();
      }
    }

  }
  $scope.awdoc = new Document();
  function Process(){
    //public vars
    this.currentStep = 'step1';
    this.addParsingErrors = false;
    //public functions
    this.cancel = function(){
      this.currentStep = 'step1';
      this.addParsingErrors = false;
    }
  }
  $scope.process = new Process();
});
