local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isMenu")==1 then
    REAPI:addUIToScreen("black.png", 0, 0, 255)
    if REAPI:readTempVar("menuFadeIn") < 255 then
        REAPI:writeTempVar("menuFadeIn", REAPI:readTempVar("menuFadeIn")+1)
    end
    local fadein = REAPI:readTempVar("menuFadeIn")
    REAPI:addUIToScreen("menubg.png", 0, 0, fadein)
    REAPI:addUIToScreen("girlmenu.png", fadein-255, 0, fadein)
    REAPI:addUIToScreen("gametitle.png", 10, 265-fadein, fadein)
    
    if REAPI:getFrameNumber()%10<5 then
        REAPI:displayText("Start", 260-REAPI:readTempVar("menuFadeIn"), 240, "font_16px.png")
    end

    if (REAPI:getKeyPressed("enter") and REAPI:readTempVar("pressEnterOnce")==1) then
        REAPI:writeTempVar("pressEnterOnce", 0)
    end

    if (REAPI:getKeyReleased("enter") and REAPI:readTempVar("pressEnterOnce")==0) then
        REAPI:writeTempVar("isIntro", 1)
        REAPI:writeTempVar("isMenu", 0)
        REAPI:writeTempVar("pressEnterOnce", 1)
        REAPI:stopBGM()
        REAPI:playBGM("prologue.wav", true)
        REAPI:playSE("start.wav", false)
        REAPI:endScript("mainmenu.lua")
    end

end

