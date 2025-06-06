local event_x = ...
local event_y = select(2, ...)

local speed = 0.03
local index = 1
local script_name = "common_female" .. index .. ".lua"
local ent_id = "zombie_" .. index

local animationsWalk = {"sprite_common_female_walk0.png", "sprite_common_female_walk1.png"}

local animation_index = 1
if REAPI:getFrameNumber()%10 < 5 then
    animation_index = 2
end

if (REAPI:readTempVar("health")>0 and REAPI:abs(event_x-REAPI:getPlayerX())<REAPI:get_buffer_dist() and REAPI:abs(event_y-REAPI:getPlayerY())<REAPI:get_buffer_dist()) and REAPI:getFrameNumber()%30 < 1 then
    REAPI:writeTempVar("health", REAPI:readTempVar("health")-1)
    REAPI:addUIToScreen("claweffect.png", 0, 0)
end

if (REAPI:readTempVar("gunshotAnimation")==1) then
    local zx = event_y
    local zy = -event_x
    local px = REAPI:getPlayerY()
    local py = -REAPI:getPlayerX()
    local zvectorx = zx-px
    local zvectory = zy-py
    local pvectorx = REAPI:cos(REAPI:getPlayerDegree()*3.14/180)   
    local pvectory = REAPI:sin(REAPI:getPlayerDegree()*3.14/180)
    local angle_between = REAPI:getAngleBetweenVectors(zvectorx, zvectory, pvectorx, pvectory)
    local hit_threshold_angle = 4
    local hit_threshold_len = 2
    if (angle_between<hit_threshold_angle and REAPI:euclidean_distance(zx, px, zy, py)<hit_threshold_len) then
        REAPI:writeTempVar("score", REAPI:readTempVar("score")+1)
        REAPI:remove_entity(ent_id, script_name)
    end
end

local movexy = REAPI:pathfindToward(ent_id, event_x, event_y, REAPI:getPlayerX(), REAPI:getPlayerY(), speed)
REAPI:edit_entity(ent_id, animationsWalk[animation_index], REAPI:decodeXfromPathString(movexy), REAPI:decodeYfromPathString(movexy))


