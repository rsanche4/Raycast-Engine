local event_x = ...
local event_y = select(2, ...)
REAPI:playBGM("Title Screen.wav", true)
REAPI:writeTempVar("isMenu", "1")
REAPI:writeTempVar("keypressedGate", "1")
REAPI:endScript(event_x, event_y)