@echo off
echo ========================================
echo    CARGO CONTRACT DEBUG TESTS
echo ========================================

echo.
echo 1. Running SimpleContractTest...
cd /d "%~dp0"
java -cp "src\java;lib\*;build\classes" test.SimpleContractTest

echo.
echo 2. Running SimpleDatabaseTest...
java -cp "src\java;lib\*;build\classes" test.SimpleDatabaseTest

echo.
echo 3. Running ContractMapperTest...
java -cp "src\java;lib\*;build\classes" test.ContractMapperTest

echo.
echo 4. Running ContractDITest...
java -cp "src\java;lib\*;build\classes" test.ContractDITest

echo.
echo 5. Running ContractMappingResultTest...
java -cp "src\java;lib\*;build\classes" test.ContractMappingResultTest

echo.
echo ========================================
echo    ALL TESTS COMPLETED
echo ========================================
pause
