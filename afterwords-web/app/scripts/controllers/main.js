'use strict';

afterwords.controller('MainCtrl', function($scope) {
  function Document(){
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
  }
  $scope.awdoc = new Document();
  function Process(){
    //public vars
    this.currentStep = 'step1'
    //public functions
    this.cancel = function(){
      this.currentStep ='step1'
    }
  }
  $scope.process = new Process();
});
