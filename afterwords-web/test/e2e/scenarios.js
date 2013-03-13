'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('afterwords app', function() {

//  beforeEach(function() {
//    browser().navigateTo('../../app/index.html');
//  });


//  xit('should automatically redirect to /view1 when location hash/fragment is empty', function() {
//    expect(browser().location().url()).toBe("/");
//  });

  describe('main', function() {
    it('should update the word count when text is entered into the textarea', function() {
      browser().navigateTo('/index.html');
      input('awdoc.text').enter('Here is some test text.');
      expect(element('#word-count-value').text()).toEqual('5');
      //pause();
    });

    it('should enable the continue button when the "agree to terms and conditions" box is checked', function(){
      browser().navigateTo('/index.html');
      expect(element('#continue').attr('disabled')).toEqual('disabled');
      input('awdoc.agreeTerms').check();
      expect(element('#continue').attr('disabled')).not().toBeDefined();
    });

    it('should enable the cancel button when the "agree to terms and conditions" box is checked', function(){
      browser().navigateTo('/index.html');
      expect(element('#cancel').attr('disabled')).toEqual('disabled');
      input('awdoc.agreeTerms').check();
      expect(element('#cancel').attr('disabled')).not().toBeDefined();
    });

    it('should transition to the "step 2 panel" when the continue button is pressed', function(){
      browser().navigateTo('index.html');
      input('awdoc.text').enter('Here is some test text.');
      input('awdoc.agreeTerms').check();
      element('#continue').click();
      expect(element('span#word_count').count()).toBe(1);
      expect(element('span#word_count').text()).toBe('5');
      expect(element('span#quote_cost').count()).toBe(1);
      expect(element('span#quote_cost').text()).toBe('250');
//      expect(element('#demo-original-text').text()).toBe('Here is some test text.');
    });

    it('should update the cost when the "Add analysis of mistakes" option is selected', function(){
      browser().navigateTo('index.html');
      input('awdoc.text').enter('Here is some test text.');
      input('awdoc.agreeTerms').check();
      element('#continue').click();
      expect(element('span#quote_cost').text()).toBe('250');
      input('awdoc.includeAnalysis').check();
      expect(element('span#quote_cost').text()).toBe('950');
    });

    describe('when there is NOT a logged in user', function(){
      iit('should display the login form when the "Confirm Order" button is clicked', function(){
        browser().navigateTo('index.html');
        expect(element('.nav a#sign_in').text()).toBe('Sign in');
        input('awdoc.text').enter('Here is some test text.');
        input('awdoc.agreeTerms').check();
        element('#continue').click();
        expect(element('span#quote_cost').text()).toBe('250');
        element('#continue').click();
        pause();
        expect(element('form#login').count()).toBe(1);
      });
    });

    describe('when there IS a logged in user', function() {

    });


  });

});
