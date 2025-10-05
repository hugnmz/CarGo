# PowerShell script to fix encoding issues in Java files
# This script removes Vietnamese comments and fixes encoding

Write-Host "Fixing encoding issues in Java files..."

# Get all Java files
$javaFiles = Get-ChildItem -Path "src\java" -Recurse -Filter "*.java"

foreach ($file in $javaFiles) {
    Write-Host "Processing: $($file.FullName)"
    
    # Read file content
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    
    # Remove Vietnamese comments (lines starting with // and containing Vietnamese characters)
    $content = $content -replace '//[^\r\n]*[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđĐ][^\r\n]*', '// Comment removed'
    
    # Remove Vietnamese comments in multi-line comments
    $content = $content -replace '/\*[^*]*\*+(?:[^/*][^*]*\*+)*/', '/* Comment removed */'
    
    # Remove Vietnamese characters from single line comments
    $content = $content -replace '//[^\r\n]*[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđĐ][^\r\n]*', '// Comment removed'
    
    # Write back to file with UTF-8 encoding
    $content | Set-Content $file.FullName -Encoding UTF8
}

Write-Host "Encoding fix completed!"
