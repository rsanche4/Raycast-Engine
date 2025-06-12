local event_x = ...
local event_y = select(2, ...)

local score_limit = 1
local gun_pixel_down_animation = 20
local reload_delay = 30

if REAPI:readTempVar("isGame")==1 then
    if REAPI:readTempVar("ammo")==7 then
        REAPI:addUIToScreen("ammo7.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==6 then
        REAPI:addUIToScreen("ammo6.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==5 then
        REAPI:addUIToScreen("ammo5.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==4 then
        REAPI:addUIToScreen("ammo4.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==3 then
        REAPI:addUIToScreen("ammo3.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==2 then
        REAPI:addUIToScreen("ammo2.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readTempVar("ammo")==1 then
        REAPI:addUIToScreen("ammo1.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif (REAPI:readTempVar("ammo")==0 and REAPI:getFrameNumber()%10<5) then
        REAPI:displayText("RELOAD", 104, 124, "font_8px.png")
        if REAPI:readTempVar("playReloadSEonce")==0 then
            REAPI:playSE("reloadnowwarn.wav", false)
        end
        if REAPI:readTempVar("playReloadSEonce")>=10 then
            REAPI:writeTempVar("playReloadSEonce", -1)
        end
        REAPI:writeTempVar("playReloadSEonce", REAPI:readTempVar("playReloadSEonce")+1)
    end

    if (REAPI:getKeyPressed("space") and REAPI:readTempVar("pressSpaceOnce")==1 and REAPI:readTempVar("ammo")>0 and REAPI:readTempVar("pressShiftOnce")==1) then
        REAPI:playSE("pistol.wav", false)
        REAPI:addUIToScreen("gunshot0.png", 0, 0, 255)
        REAPI:writeTempVar("pressSpaceOnce", 0)
        REAPI:writeTempVar("ammo", REAPI:readTempVar("ammo")-1)
        REAPI:writeTempVar("gunshotAnimation", 0)
    elseif (REAPI:readTempVar("gunshotAnimation")==0) then
        REAPI:addUIToScreen("gunshot1.png", 0, 0, 255)
        REAPI:writeTempVar("gunshotAnimation", 1)
    elseif (REAPI:readTempVar("gunshotAnimation")==1) then
        REAPI:addUIToScreen("gunshot2.png", 0, 0, 255)
        REAPI:writeTempVar("gunshotAnimation", -1)
    elseif (REAPI:getKeyReleased("space") and REAPI:readTempVar("pressSpaceOnce")==0) then
        REAPI:addUIToScreen("gunidle.png", 0, 0, 255)
        REAPI:writeTempVar("pressSpaceOnce", 1)
    elseif (REAPI:getKeyPressed("shift") and REAPI:readTempVar("pressShiftOnce")==1 and REAPI:readTempVar("ammo")<7) then
        REAPI:playSE("clippin.wav", false)
        REAPI:writeTempVar("pressShiftOnce", 0)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation, 255)
        REAPI:writeTempVar("ReloadDelay", 0)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")<7 and REAPI:readTempVar("ReloadDelay")==reload_delay) then
        REAPI:writeTempVar("ammo", REAPI:readTempVar("ammo")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2, 255)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")<7 and REAPI:readTempVar("ReloadDelay")<reload_delay) then
        REAPI:writeTempVar("ReloadDelay", REAPI:readTempVar("ReloadDelay")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2, 255)
    elseif (REAPI:readTempVar("pressShiftOnce")==0 and REAPI:readTempVar("ammo")==7) then
        REAPI:writeTempVar("pressShiftOnce", 1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation, 255)
    elseif (REAPI:readTempVar("pressShiftOnce")==1) then
        REAPI:addUIToScreen("gunidle.png", 0, 0, 255)
    end
    
    if (REAPI:readTempVar("health")==0) then
        REAPI:writeTempVar("isGameOver", 1)
        REAPI:writeTempVar("isGame", 0)
    elseif (REAPI:readTempVar("health")<=2) then
        REAPI:addUIToScreen("blood_overlay_3.png", 0, 0, 255)
    elseif (REAPI:readTempVar("health")<=4) then
        REAPI:addUIToScreen("blood_overlay_2.png", 0, 0, 200)
    elseif (REAPI:readTempVar("health")<=6) then
        REAPI:addUIToScreen("blood_overlay_1.png", 0, 0, 140)
    elseif (REAPI:readTempVar("health")<=8) then
        REAPI:addUIToScreen("blood_overlay_0.png", 0, 0, 75)
    end

    local current_health = "health" .. REAPI:readTempVar("health") .. ".png"
    REAPI:addUIToScreen(current_health, 4, 228, 255)
    
    local score_to_show = REAPI:padWithLeadingZeros(REAPI:readTempVar("score"), 8)
    REAPI:displayText(score_to_show, 50, 10, "font_16px.png")
    
    REAPI:addUIToScreen("ui.png", 0, 0, 255)

end

if REAPI:readTempVar("score") >= score_limit then
    if REAPI:readTempVar("playGameOverOnce")==1 then
        REAPI:writeTempVar("playGameOverOnce", 0)
        REAPI:writeTempVar("isGame", 0)
        REAPI:stopBGM()
        REAPI:playBGM("secretending.wav", true)
    end
    
    REAPI:addUIToScreen("secretend.png", 0, 0, 255)
    
    local separ = 10
    local initplace_x = 5
    local initplace = 10
    REAPI:displayText("Phew! That was a lot of zombies", initplace_x, initplace, "font_8px.png")
    REAPI:displayText("DeadShot Girl had to clear out!", initplace_x, initplace + separ, "font_8px.png")
    REAPI:displayText("Just like that, she graduated", initplace_x, initplace + separ*2, "font_8px.png")
    REAPI:displayText("top of her class. Her final", initplace_x, initplace + separ*3, "font_8px.png")
    REAPI:displayText("grade? Ballistics.", initplace_x, initplace + separ*4, "font_8px.png")
    REAPI:displayText("Soon after,", initplace_x, initplace + separ*5, "font_8px.png")
    REAPI:displayText("she took a", initplace_x, initplace + separ*6, "font_8px.png")
    REAPI:displayText("well deserved", initplace_x, initplace + separ*7, "font_8px.png")
    REAPI:displayText("vacation!", initplace_x, initplace + separ*8, "font_8px.png")
    REAPI:displayText("FIN", 5, 240, "font_16px.png")
end

if REAPI:readTempVar("isGameOver")==1 then
    REAPI:stopBGM()
    REAPI:addUIToScreen("blood_overlay_4.png", 0, 0, 255)
    if REAPI:readTempVar("playGameOverOnce")==1 then
        REAPI:playSE("gameover.wav", false)
        REAPI:writeTempVar("playGameOverOnce", 0)
    end
    if REAPI:getFrameNumber()%10<5 then
        REAPI:displayText("R. I. P.", 64, 100, "font_16px.png")
    end
    local score_to_show = "Killcount: " .. REAPI:padWithLeadingZeros(REAPI:readTempVar("score"), 8)
    REAPI:displayText(score_to_show, 52, 128, "font_8px.png")
end

