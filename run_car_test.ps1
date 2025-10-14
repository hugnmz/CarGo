# Test Car Loading
Write-Host "=== RUNNING CAR LOADING TEST ===" -ForegroundColor Green

# Compile test
Write-Host "Compiling test..." -ForegroundColor Yellow
javac -cp "lib\*;src\java" -d out\production\CarGo src\java\test\CarLoadingTest.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation failed" -ForegroundColor Red
    exit 1
}

# Run test
Write-Host "Running test..." -ForegroundColor Yellow
java -cp "lib\*;out\production\CarGo" test.CarLoadingTest

Write-Host "=== TEST COMPLETED ===" -ForegroundColor Green

