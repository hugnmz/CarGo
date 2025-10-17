Write-Host "=== THEM DU LIEU TEST VAO DATABASE (FIXED) ===" -ForegroundColor Green

# Di chuyen den thu muc project
Set-Location "D:\Project_PRJ\CarGo"

Write-Host "Dang compile..." -ForegroundColor Yellow
javac -cp "lib\*;src\java" -d out src\java\test\InsertTestData.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compile that bai!" -ForegroundColor Red
    Read-Host "Nhan Enter de thoat"
    exit 1
}

Write-Host "✓ Compile thanh cong!" -ForegroundColor Green

Write-Host "Dang them du lieu test..." -ForegroundColor Yellow
java -cp "lib\*;out" test.InsertTestData

Write-Host ""
Write-Host "=== HOAN THANH THEM DU LIEU ===" -ForegroundColor Green
Read-Host "Nhan Enter de thoat"

