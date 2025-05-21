local event_x = ...
local event_y = select(2, ...)
REAPI:writeTempVar("isMenu", "1")
REAPI:writeTempVar("keypressedGate", "1")
REAPI:writeTempVar("shootOnceFlag", 1)
REAPI:writeTempVar("reloadVar", 0)
REAPI:writeTempVar("reloadOnceFlag", 1)
REAPI:endScript("title_screen_bgm.lua")
