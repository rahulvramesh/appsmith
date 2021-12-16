const path = require("path");
const Perf = require("./perf.js");
const dsl = require("./dsl/simple-typing").dsl;
var fs = require("fs");

// Set the perf directory as APP_ROOT on the global level
global.APP_ROOT = path.resolve(__dirname);

// Create the directory
const dir = `${APP_ROOT}/traces/reports`;
if (!fs.existsSync(dir)) {
  fs.mkdirSync(dir, { recursive: true });
}

process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

async function testTyping() {
  const perf = new Perf({ ignoreHTTPSErrors: true }); // @todo Remove it after initial testing
  await perf.launch();
  const page = perf.getPage();
  await perf.loadDSL(dsl);

  const selector = "input.bp3-input"; // Input selector
  await page.waitForSelector(selector);
  const input = await page.$(selector);

  await perf.startTrace("Edit input");
  await page.type(selector, "Hello Appsmith");
  await perf.stopTrace();

  await perf.startTrace("Clear input");
  await input.click({ clickCount: 3 });
  await input.press("Backspace");
  await perf.stopTrace();

  await perf.startTrace("Edit input again");
  await page.type(selector, "Howdy satish");
  await perf.stopTrace();

  await perf.generateReport();
  await perf.close();
}

async function runTests() {
  await testTyping();
  await testTyping();
  await testTyping();
  await testTyping();
  await testTyping();
}
runTests();
