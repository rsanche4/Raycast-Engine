local event_x = ...
local event_y = select(2, ...)

REAPI:writeTempVar("isIntro", 0)
REAPI:writeTempVar("isMenu", 1)
REAPI:writeTempVar("isGame", 0)
REAPI:writeTempVar("isGameOver", 0)

REAPI:writeTempVar("pressEnterOnce", 1)
REAPI:writeTempVar("pressSpaceOnce", 1)
REAPI:writeTempVar("pressShiftOnce", 1)
REAPI:writeTempVar("ammo", 7)

REAPI:endScript("var_init.lua")

