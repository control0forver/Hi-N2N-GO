$rootDir = "app"

$jsonFiles = Get-ChildItem -Path $rootDir -Recurse -Filter "output.json" -File | ForEach-Object { $_.FullName }

if ($jsonFiles.Length -eq 0)
{
    Write-Host 'No any "output.json" found.'
}

for ($i = 0; $i -lt $jsonFiles.Length; $i++) {
    Write-Host "$( [char]9679 ) $( $i + 1 ). $( $jsonFiles[$i] )"
}

while (1)
{
    Write-Host ("Enter the index of json file(1-{0}):" -f $jsonFiles.Count)
    $userInput = Read-Host

    if (($userInput -lt 1) -or ($userInput -gt $jsonFiles.Count))
    {
        Write-Host "Invalid input, please try again!"
    }
    else
    {
        break
    }
}

$jsonFilePath = $jsonFiles[$userInput - 1]

while (1)
{
    Write-Host "Press Any Key to Start..."
    [void][System.Console]::ReadKey($true)
    & clear

    try
    {
        $jsonContent = Get-Content -Path $jsonFilePath -Raw | ConvertFrom-Json

        $apkFileName = $jsonContent.elements[0].outputFile
        $apkFilePath = Join-Path -Path (Split-Path -Path $jsonFilePath) -ChildPath $apkFileName

        if (!(Test-Path -Path $apkFilePath -PathType Leaf))
        {
            Write-Error "APK file dose not exists: $apkFilePath"
            break
        }

        & adb install "$apkFilePath"
        if ($LASTEXITCODE -ne 0)
        {
            Write-Error "ADB Install Failed with Code: $LASTEXITCODE"
            break
        }

        & adb shell am start -n wang.switchy.hin2n/.activity.MainActivity
    }
    catch
    {
        Write-Error "Error: $_"
        break
    }
}