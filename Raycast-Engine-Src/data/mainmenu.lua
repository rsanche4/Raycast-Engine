local event_x = ...
local event_y = select(2, ...)
if REAPI:readTempVar("isMenu")==1 then
    REAPI:addUIToScreen("menubg.png", 0, 0)
    REAPI:addUIToScreen("girlmenu.png", 0, 0)
    REAPI:addUIToScreen("gametitle.png", 10, 10)
    if REAPI:getFrameNumber()%10<5 then
        REAPI:displayText("Start", 5, 240, "font_16px.png")
    end
end

if (REAPI:getKeyPressed("enter") and REAPI:readTempVar("pressEnterOnce")==1) then
    REAPI:writeTempVar("pressEnterOnce", 0)
end

if (REAPI:getKeyReleased("enter") and REAPI:readTempVar("pressEnterOnce")==0) then
    REAPI:writeTempVar("isIntro", 1)
    REAPI:writeTempVar("isMenu", 0)
    REAPI:writeTempVar("pressEnterOnce", 1)
    REAPI:endScript("mainmenu.lua")
end
