echo "Run system tests with RestAssured... Check reports at ./system-test/build/reports/tests/test/index.html"

cd system-test
gradlew clean test
