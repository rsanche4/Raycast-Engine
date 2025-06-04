local event_x = ...
local event_y = select(2, ...)

local speed = 0.01
local offset_y = 0
local offset_x = 0
if REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_1" then
    speed = 0.011
    offset_x = 0.01
    offset_y = 0
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_2" then
    speed = 0.012
    offset_x = 0.02
    offset_y = 1
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_3" then
    speed = 0.013
    offset_x = 0.03
    offset_y = 1
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_4" then
    speed = 0.014
    offset_x = 0.04
    offset_y = 1
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_5" then
    speed = 0.015
    offset_x = 1
    offset_y = 0.01
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_6" then
    speed = 0.016
    offset_x = 1
    offset_y = 0.02
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_7" then
    speed = 0.017
    offset_x = 1
    offset_y = 0.03
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_8" then
    speed = 0.018
    offset_x = 1
    offset_y = 0.04
elseif REAPI:get_entity_id_by_position(event_x, event_y)=="zombie_9" then
    speed = 0.019
    offset_x = 0.05
    offset_y = 0.05
end

local animationsWalk = {"sprite_common_female_walk0.png", "sprite_common_female_walk1.png"}

local animation_index = 1
if REAPI:getFrameNumber()%10 < 5 then
    animation_index = 2
end

local movexy = REAPI:pathfindToward(REAPI:get_entity_id_by_position(event_x, event_y), event_x, event_y, REAPI:getPlayerX()+offset_x, REAPI:getPlayerY()+offset_y, speed*2)

REAPI:edit_entity(REAPI:get_entity_id_by_position(event_x, event_y), animationsWalk[animation_index], REAPI:decodeXfromPathString(movexy), REAPI:decodeYfromPathString(movexy))