basePath = '';

files = [
  ANGULAR_SCENARIO,
  ANGULAR_SCENARIO_ADAPTER,
  'test/e2e/**/*.js'
];

autoWatch = false;

browsers = ['ChromeCanary'];

singleRun = true;

proxies = {
  '/': 'http://localhost:3501/'
};

junitReporter = {
  outputFile: 'test_out/e2e.xml',
  suite: 'e2e'
};