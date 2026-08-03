$Root=Split-Path -Parent $PSScriptRoot;$Run=Join-Path $Root '.dev-run'
Get-ChildItem $Run -Filter *.pid -ErrorAction SilentlyContinue|ForEach-Object{$id=Get-Content $_;Stop-Process -Id $id -ErrorAction SilentlyContinue;Remove-Item $_}
docker compose -f (Join-Path $Root 'docker-compose.yml') stop mysql
