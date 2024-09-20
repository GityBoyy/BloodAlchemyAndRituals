@echo off
setlocal
for /f "tokens=2 delims=:." %%x in ('chcp') do set _codepage=%%x
chcp 65001>nul
cd D:\Programming\Mods-2\BloodAlchemyAndRituals\run
"C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot\bin\java.exe" @D:\Programming\Mods-2\BloodAlchemyAndRituals\build\moddev\serverRunClasspath.txt @D:\Programming\Mods-2\BloodAlchemyAndRituals\build\moddev\serverRunVmArgs.txt -Dfml.modFolders=bloodalchemyandrituals%%%%D:\Programming\Mods-2\BloodAlchemyAndRituals\build\classes\java\main;bloodalchemyandrituals%%%%D:\Programming\Mods-2\BloodAlchemyAndRituals\build\resources\main net.neoforged.devlaunch.Main @D:\Programming\Mods-2\BloodAlchemyAndRituals\build\moddev\serverRunProgramArgs.txt
if not ERRORLEVEL 0 (  echo Minecraft failed with exit code %ERRORLEVEL%  pause)
chcp %_codepage%>nul
endlocal