local event_x = ...
local event_y = select(2, ...)

REAPI:writeTempVar("isIntro", 0)
REAPI:writeTempVar("isMenu", 0)
REAPI:writeTempVar("SplashScreen", 1)
REAPI:writeTempVar("isGame", 0)
REAPI:writeTempVar("isGameOver", 0)

REAPI:writeTempVar("pressEnterOnce", 1)
REAPI:writeTempVar("pressSpaceOnce", 1)
REAPI:writeTempVar("pressShiftOnce", 1)
REAPI:writeTempVar("ammo", 7)
REAPI:writeTempVar("health", 10)
REAPI:writeTempVar("score", 0)
REAPI:writeTempVar("splashScreenTime", -40)
REAPI:writeTempVar("menuFadeIn", 0)
REAPI:writeTempVar("introFadeIn", 0)
REAPI:writeTempVar("playReloadSEonce", 0)
REAPI:writeTempVar("playGameOverOnce", 1)

REAPI:playBGM("menuBGM.wav", true)
REAPI:endScript("var_init.lua")

