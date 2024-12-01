@echo off

:: Read the location from project_location.txt
set /p PROJECT_LOCATION=<../project_location.txt

:: Open worlds_data.json
start notepad "%PROJECT_LOCATION%/worlds_data.json"

:: Iterate through Python files and start them accordingly
for %%f in (*.py) do (
    if "%%f"=="world_editor.py" (
        start python %%f
    ) else (
        if not "%%f"=="event_editor.py" start pythonw %%f
    )
)