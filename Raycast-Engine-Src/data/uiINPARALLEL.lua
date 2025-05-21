local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isMenu")=="0" then
    if (REAPI:getKeyPressed("space") and REAPI:readTempVar("shootOnceFlag") > 0) then
        REAPI:playSE("gunshot.wav", false)
        REAPI:addUIToScreen("gunshoot.png", 0, 0)
        REAPI:writeTempVar("shootOnceFlag", 0)
    elseif (REAPI:readTempVar("reloadVar")>0) or (REAPI:getKeyPressed("shift") and REAPI:readTempVar("reloadOnceFlag")==1) then
        delaytime = 100
        REAPI:writeTempVar("reloadOnceFlag", 0)
        if REAPI:readTempVar("reloadVar")==0 then
            REAPI:addUIToScreen("reload0.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 1)
            REAPI:playSE("reload.wav", false)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==1 then
            REAPI:addUIToScreen("reload1.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 2)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==2 then
            REAPI:addUIToScreen("reload2.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 3)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==3 then
            REAPI:addUIToScreen("reload3.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 4)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==4 then
            REAPI:addUIToScreen("reload3.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 5)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==5 then
            REAPI:addUIToScreen("reload2.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 6)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==6 then
            REAPI:addUIToScreen("reload1.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 7)
            REAPI:delay(delaytime)
        elseif REAPI:readTempVar("reloadVar")==7 then
            REAPI:addUIToScreen("reload0.png", 0, 0)
            REAPI:writeTempVar("reloadVar", 0)
            REAPI:writeTempVar("reloadOnceFlag", 1)
            REAPI:delay(delaytime)
        end
    else
        REAPI:addUIToScreen("gun.png", 0, 0)
        if REAPI:getKeyReleased("space") then
            REAPI:writeTempVar("shootOnceFlag", 1)
        end
    end

    REAPI:addUIToScreen("vignette.png", 0, 0)
end
