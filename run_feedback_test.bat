@echo off
echo ========================================
echo COMPILE AND RUN FEEDBACK TEST
echo ========================================
echo.

REM Tạo thư mục build nếu chưa có
if not exist "build\classes" mkdir build\classes

REM Compile test
echo Compiling TestFeedback.java...
javac -encoding UTF-8 -cp "lib\*;src\java;build\web\WEB-INF\classes" -d build\classes src\java\test\TestFeedback.java src\java\dao\*.java src\java\dao\impl\*.java src\java\model\*.java src\java\service\impl\*.java src\java\mapper\*.java src\java\dto\*.java src\java\util\*.java 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Chạy test
echo Running TestFeedback...
echo.
java -cp "lib\*;build\classes;src\java;src\resources" test.TestFeedback

echo.
echo ========================================
echo TEST COMPLETED
echo ========================================
pause

