local event_x = ...
local event_y = select(2, ...)

local selected = REAPI:readTempVar("selected")
if selected then
    if selected == "1" then
        REAPI:addUIToScreen("menu_selector.png", 20, 20)
    elseif selected == "2" then
        REAPI:addUIToScreen("menu_selector.png", 20, 40)
    end
else
    REAPI:writeTempVar("selected", "1")
end

if REAPI:getKeyPressed() == "up_arrow" then
    if  REAPI:readTempVar("keypressedGate") == "0" then
        REAPI:playSE("select.WAV", false)
        REAPI:writeTempVar("keypressedGate", "1")
    end
    REAPI:writeTempVar("selected", "1")
end

if REAPI:getKeyPressed() == "down_arrow" then
    if  REAPI:readTempVar("keypressedGate") == "1" then
        REAPI:playSE("select.WAV", false)
        REAPI:writeTempVar("keypressedGate", "0")
    end
    REAPI:writeTempVar("selected", "2")
end

if REAPI:getKeyPressed() == "enter" then
    if selected == "1" then
        if REAPI:readTempVar("isMenu", "1") then
            REAPI:writeTempVar("isMenu", "0")
            REAPI:playSE("menu_enter.WAV", false)
            REAPI:playBGM("outside.wav", true)
            REAPI:endScript(249,251)
            REAPI:endScript(event_x, event_y)
        end
    else
        REAPI:systemExit()
    end
end