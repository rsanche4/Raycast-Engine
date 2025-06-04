local event_x = ...
local event_y = select(2, ...)

local separ = 10
local initplace_x = 10
local initplace = 30

if REAPI:readTempVar("isIntro") == 1 then
    REAPI:addUIToScreen("menuDark.png", 0, 0)
    REAPI:displayText("She was supposed to be in", initplace_x, initplace, "font_8px.png")
    REAPI:displayText("algebra class. Instead, she", initplace_x, initplace + separ, "font_8px.png")
    REAPI:displayText("was hiding behind the back", initplace_x, initplace + separ*2, "font_8px.png")
    REAPI:displayText("stairs, watching gun videos", initplace_x, initplace + separ*3, "font_8px.png")
    REAPI:displayText("and carving bullet calibers", initplace_x, initplace + separ*4, "font_8px.png")
    REAPI:displayText("into her notebook. Teachers", initplace_x, initplace + separ*5, "font_8px.png")
    REAPI:displayText("said it was a phase...", initplace_x, initplace + separ*6, "font_8px.png")
    REAPI:displayText("and a problem.", initplace_x, initplace + separ*7, "font_8px.png")
    REAPI:displayText("Then the sky cracked open,", initplace_x, initplace + separ*8, "font_8px.png")
    REAPI:displayText("the dead started walking, and", initplace_x, initplace + separ*9, "font_8px.png")
    REAPI:displayText("suddenly her weird obsession", initplace_x, initplace + separ*10, "font_8px.png")
    REAPI:displayText("with recoil patterns didn't", initplace_x, initplace + separ*11, "font_8px.png")
    REAPI:displayText("seem so weird anymore. Now,", initplace_x, initplace + separ*12, "font_8px.png")
    REAPI:displayText("the only math problem she", initplace_x, initplace + separ*13, "font_8px.png")
    REAPI:displayText("needs to solve is subtracting", initplace_x, initplace + separ*14, "font_8px.png")
    REAPI:displayText("the undead from existence.", initplace_x, initplace + separ*15, "font_8px.png")
    REAPI:displayText("And her beloved magnum sure", initplace_x, initplace + separ*16, "font_8px.png")
    REAPI:displayText("as hell has the solution.", initplace_x, initplace + separ*17, "font_8px.png")
    if REAPI:getFrameNumber()%20<15 then
        REAPI:displayText("Press Start to Begin", initplace_x, initplace + separ*19, "font_8px.png")
    end
end

if (REAPI:getKeyPressed("enter") and REAPI:readTempVar("pressEnterOnce")==1 and REAPI:readTempVar("isIntro")==1) then
    REAPI:writeTempVar("isIntro", 0)
    REAPI:writeTempVar("isGame", 1)
    REAPI:setPlayerX(250)
    REAPI:setPlayerY(250)
    REAPI:setPlayerDirection(1, 0, 0, -.66)
    REAPI:endScript("intro.lua")
end
