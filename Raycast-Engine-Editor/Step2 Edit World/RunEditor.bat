@echo off
for %%f in (*.py) do (
    if not "%%f"=="event_editor.py" start pythonw %%f
)