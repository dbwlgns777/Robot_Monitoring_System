$Root=Split-Path -Parent $PSScriptRoot; $Run=Join-Path $Root '.dev-run'; New-Item -ItemType Directory -Force $Run|Out-Null
docker compose -f (Join-Path $Root 'docker-compose.yml') up -d mysql
$items=@(@('backend','.\gradlew.bat',':backend:bootRun'),@('device','.\gradlew.bat',':device-server:bootRun'),@('frontend','npm.cmd','--prefix frontend run dev'))
foreach($i in $items){$p=Start-Process -FilePath $i[1] -ArgumentList $i[2] -WorkingDirectory $Root -RedirectStandardOutput (Join-Path $Run "$($i[0]).log") -RedirectStandardError (Join-Path $Run "$($i[0]).error.log") -PassThru;$p.Id|Set-Content (Join-Path $Run "$($i[0]).pid")}
Write-Host 'Frontend http://localhost:5173 | Backend http://localhost:8080 | Device http://localhost:8081'
