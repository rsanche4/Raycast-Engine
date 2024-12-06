local event_x = ...
local event_y = select(2, ...)

local selected = REAPI:readTempVar("selected")
if selected then
    if selected == "1" then
        REAPI:addUIToScreen("menu_selector.png", 327, 383)
    elseif selected == "2" then
        REAPI:addUIToScreen("menu_selector.png", 327, 435 - 12)
    end
else
    REAPI:writeTempVar("selected", "1")
end

if REAPI:getKeyPressed() == "up_arrow" then
    if  REAPI:readTempVar("keypressedGate") == "0" then
        REAPI:playBGM("select.WAV", false)
        REAPI:writeTempVar("keypressedGate", "1")
    end
    REAPI:writeTempVar("selected", "1")
end

if REAPI:getKeyPressed() == "down_arrow" then
    if  REAPI:readTempVar("keypressedGate") == "1" then
        REAPI:playBGM("select.WAV", false)
        REAPI:writeTempVar("keypressedGate", "0")
    end
    REAPI:writeTempVar("selected", "2")
end
