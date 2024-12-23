$jsonFilePath = "app/build/outputs/apk/normalAllarch/debug/output.json"

while (1) {
    Write-Host "Press Any Key to Start..."
    [void][System.Console]::ReadKey($true)
    & clear

    try {
        $jsonContent = Get-Content -Path $jsonFilePath -Raw | ConvertFrom-Json

        $apkFileName = $jsonContent.elements[0].outputFile
        $apkFilePath = Join-Path -Path (Split-Path -Path $jsonFilePath) -ChildPath $apkFileName

        if (!(Test-Path -Path $apkFilePath -PathType Leaf)) {
            Write-Error "APK file dose not exists: $apkFilePath"
            break
        }

        & adb install "$apkFilePath"
        if ($LASTEXITCODE -ne 0) {
            Write-Error "ADB Install Failed with Code: $LASTEXITCODE"
            break
        }

        & adb shell am start -n wang.switchy.hin2n/.activity.MainActivity
    } catch {
        Write-Error "Error: $_"
        break
    }
}