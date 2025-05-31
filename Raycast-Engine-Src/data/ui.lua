local event_x = ...
local event_y = select(2, ...)

local gun_pixel_down_animation = 20
local reload_delay = 30

if REAPI:readTempVar("isGame")==1 then
    if REAPI:readTempVar("ammo")==7 then
        REAPI:addUIToScreen("ammo7.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==6 then
        REAPI:addUIToScreen("ammo6.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==5 then
        REAPI:addUIToScreen("ammo5.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==4 then
        REAPI:addUIToScreen("ammo4.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==3 then
        REAPI:addUIToScreen("ammo3.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==2 then
        REAPI:addUIToScreen("ammo2.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif REAPI:readTempVar("ammo")==1 then
        REAPI:addUIToScreen("ammo1.png", 187, 4)
        REAPI:addUIToScreen("crosshair.png", 0, 0)
    elseif (REAPI:readTempVar("ammo")==0 and REAPI:getFrameNumber()%10<5) then
        REAPI:displayText("RELOAD", 104, 124, "font_8px.png")
    end

    if (REAPI:getKeyPressed("space") and REAPI:readTempVar("pressSpaceOnce")==1 and REAPI:readTempVar("ammo")>0 and REAPI:readTempVar("pressShiftOnce")==1) then
        REAPI:addUIToScreen("gunshot0.png", 0, 0)
        REAPI:writeTempVar("pressSpaceOnce", 0)
        REAPI:writeTempVar("ammo", REAPI:readTempVar("ammo")-1)
        REAPI:writeTempVar("gunshotAnimation", 0)
    elseif (REAPI:readTempVar("gunshotAnimation")==0) then
        REAPI:addUIToScreen("gunshot1.png", 0, 0)
        REAPI:writeTempVar("gunshotAnimation", 1)
    elseif (REAPI:readTempVar("gunshotAnimation")==1) then
        REAPI:addUIToScreen("gunshot2.png", 0, 0)
        REAPI:writeTempVar("gunshotAnimation", -1)
    elseif (REAPI:getKeyReleased("space") and REAPI:readTempVar("pressSpaceOnce")==0) then
        REAPI:addUIToScreen("gunidle.png", 0, 0)
        REAPI:writeTempVar("pressSpaceOnce", 1)
    elseif (REAPI:getKeyPressed("shift") and REAPI:readTempVar("pressShiftOnce")==1 and REAPI:readTempVar("ammo")<7) then
        REAPI:writeTempVar("pressShiftOnce", 0)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation)
        REAPI:writeTempVar("ReloadDelay", 0)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")<7 and REAPI:readTempVar("ReloadDelay")==reload_delay) then
        REAPI:writeTempVar("ammo", REAPI:readTempVar("ammo")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")<7 and REAPI:readTempVar("ReloadDelay")<reload_delay) then
        REAPI:writeTempVar("ReloadDelay", REAPI:readTempVar("ReloadDelay")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")==7) then
        REAPI:writeTempVar("pressShiftOnce", 1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation)
    elseif (REAPI:readTempVar("pressShiftOnce")==1) then
        REAPI:addUIToScreen("gunidle.png", 0, 0)
    end

    REAPI:addUIToScreen("health100.png", 4, 228)
    REAPI:addUIToScreen("ui.png", 0, 0)
    REAPI:displayText("00000000", 50, 10, "font_16px.png")
end




        