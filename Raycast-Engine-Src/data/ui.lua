local event_x = ...
local event_y = select(2, ...)
local event_name = select(3, ...)

local score_limit = 99999999
local gun_pixel_down_animation = 20
local reload_delay = 30

if REAPI:readVar("isGame")==1 then
    if REAPI:readVar("ammo")==7 then
        REAPI:addUIToScreen("ammo7.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==6 then
        REAPI:addUIToScreen("ammo6.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==5 then
        REAPI:addUIToScreen("ammo5.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==4 then
        REAPI:addUIToScreen("ammo4.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==3 then
        REAPI:addUIToScreen("ammo3.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==2 then
        REAPI:addUIToScreen("ammo2.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif REAPI:readVar("ammo")==1 then
        REAPI:addUIToScreen("ammo1.png", 187, 4, 255)
        REAPI:addUIToScreen("crosshair.png", 0, 0, 255)
    elseif (REAPI:readVar("ammo")==0 and REAPI:getFrameNumber()%10<5) then
        REAPI:displayText("RELOAD", 104, 124, "font_8px.png")
        if REAPI:readVar("playReloadSEonce")==0 then
            REAPI:playSE("reloadnowwarn.wav", false)
        end
        if REAPI:readVar("playReloadSEonce")>=10 then
            REAPI:writeVar("playReloadSEonce", -1)
        end
        REAPI:writeVar("playReloadSEonce", REAPI:readVar("playReloadSEonce")+1)
    end

    if (REAPI:getKeyPressed("space") and REAPI:readVar("pressSpaceOnce")==1 and REAPI:readVar("ammo")>0 and REAPI:readVar("pressShiftOnce")==1) then
        REAPI:playSE("pistol.wav", false)
        REAPI:addUIToScreen("gunshot0.png", 0, 0, 255)
        REAPI:writeVar("pressSpaceOnce", 0)
        REAPI:writeVar("ammo", REAPI:readVar("ammo")-1)
        REAPI:writeVar("gunshotAnimation", 0)
    elseif (REAPI:readVar("gunshotAnimation")==0) then
        REAPI:addUIToScreen("gunshot1.png", 0, 0, 255)
        REAPI:writeVar("gunshotAnimation", 1)
    elseif (REAPI:readVar("gunshotAnimation")==1) then
        REAPI:addUIToScreen("gunshot2.png", 0, 0, 255)
        REAPI:writeVar("gunshotAnimation", -1)
    elseif (REAPI:getKeyReleased("space") and REAPI:readVar("pressSpaceOnce")==0) then
        REAPI:addUIToScreen("gunidle.png", 0, 0, 255)
        REAPI:writeVar("pressSpaceOnce", 1)
    elseif (REAPI:getKeyPressed("shift") and REAPI:readVar("pressShiftOnce")==1 and REAPI:readVar("ammo")<7) then
        REAPI:playSE("clippin.wav", false)
        REAPI:writeVar("pressShiftOnce", 0)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation, 255)
        REAPI:writeVar("ReloadDelay", 0)
    elseif (REAPI:readVar("pressShiftOnce")==0 and REAPI:readVar("ammo")<7 and REAPI:readVar("ReloadDelay")==reload_delay) then
        REAPI:writeVar("ammo", REAPI:readVar("ammo")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2, 255)
    elseif (REAPI:readVar("pressShiftOnce")==0 and REAPI:readVar("ammo")<7 and REAPI:readVar("ReloadDelay")<reload_delay) then
        REAPI:writeVar("ReloadDelay", REAPI:readVar("ReloadDelay")+1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation*2, 255)
    elseif (REAPI:readVar("pressShiftOnce")==0 and REAPI:readVar("ammo")==7) then
        REAPI:writeVar("pressShiftOnce", 1)
        REAPI:addUIToScreen("gunidle.png", 0, gun_pixel_down_animation, 255)
    elseif (REAPI:readVar("pressShiftOnce")==1) then
        REAPI:addUIToScreen("gunidle.png", 0, 0, 255)
    end
    
    if (REAPI:readVar("health")==0) then
        REAPI:writeVar("isGameOver", 1)
        REAPI:writeVar("isGame", 0)
    elseif (REAPI:readVar("health")<=2) then
        REAPI:addUIToScreen("blood_overlay_3.png", 0, 0, 255)
    elseif (REAPI:readVar("health")<=4) then
        REAPI:addUIToScreen("blood_overlay_2.png", 0, 0, 200)
    elseif (REAPI:readVar("health")<=6) then
        REAPI:addUIToScreen("blood_overlay_1.png", 0, 0, 140)
    elseif (REAPI:readVar("health")<=8) then
        REAPI:addUIToScreen("blood_overlay_0.png", 0, 0, 75)
    end

    local current_health = "health" .. REAPI:readVar("health") .. ".png"
    REAPI:addUIToScreen(current_health, 4, 228, 255)
    
    local score_to_show = REAPI:padWithLeadingZeros(score_limit-REAPI:readVar("score"), 8)
    REAPI:displayText(score_to_show, 50, 10, "font_16px.png")
    
    REAPI:addUIToScreen("ui.png", 0, 0, 255)

end

if REAPI:readVar("score") >= score_limit then
    if REAPI:readVar("playGameOverOnce")==1 then
        REAPI:writeVar("playGameOverOnce", 0)
        REAPI:writeVar("isGame", 0)
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

if REAPI:readVar("isGameOver")==1 then
    REAPI:stopBGM()
    REAPI:addUIToScreen("blood_overlay_4.png", 0, 0, 255)
    if REAPI:readVar("playGameOverOnce")==1 then
        REAPI:playSE("gameover.wav", false)
        REAPI:writeVar("playGameOverOnce", 0)
    end
    if REAPI:getFrameNumber()%10<5 then
        REAPI:displayText("R. I. P.", 64, 100, "font_16px.png")
    end
    local score_to_show = "Killcount: " .. REAPI:padWithLeadingZeros(score_limit-REAPI:readVar("score"), 8)
    REAPI:displayText(score_to_show, 52, 128, "font_8px.png")

    if REAPI:getKeyPressed("enter") then
        REAPI:endScript("director.lua")
        REAPI:addScript("var_init.lua", 0, 1, "var_init.lua")
        REAPI:addScript("intro.lua", 0, 2, "intro.lua")
        REAPI:addScript("mainmenu.lua", 0, 3, "mainmenu.lua")
        REAPI:addScript("director.lua", 0, 4, "director.lua")
        REAPI:addScript("splashscreen.lua", 0, 5, "splashscreen.lua")
    end
end

