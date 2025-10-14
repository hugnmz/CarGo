Write-Host "=== CHAY TEST CART SERVICE ===" -ForegroundColor Green

# Di chuyen den thu muc project
Set-Location "D:\Project_PRJ\CarGo"

Write-Host "Dang compile..." -ForegroundColor Yellow
javac -cp "lib\*;src\java" -d out src\java\test\CartServiceTest.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compile that bai!" -ForegroundColor Red
    Read-Host "Nhan Enter de thoat"
    exit 1
}

Write-Host "✓ Compile thanh cong!" -ForegroundColor Green

Write-Host "Dang chay test..." -ForegroundColor Yellow
java -cp "lib\*;out" test.CartServiceTest

Write-Host ""
Write-Host "=== KET THUC TEST ===" -ForegroundColor Green
Read-Host "Nhan Enter de thoat"

