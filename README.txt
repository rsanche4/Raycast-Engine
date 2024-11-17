Work in Progress.

TODO:
- multiple-direction sprite. So when u look from an angle its different
- multiplayer modding
- Readme Docs


Anatomy of a script:

// Conditions
Every script starts by checking if a variable in the world json is true or something of the sort. this will tell it whether to run or not. If we want to set it to like the player touches it or something of the sort, we have varibales that have the position of the player. We will update them everytime. The event also is saved up in world json so no worries cuz we also know where we are. We will variables saved up in the json, all of them the entire game engine will have all its core variables saved up in there.

// Movement
Does this sprite, event move. or is it still. Here, what u can do is simply update the variables of where its at in the map in worlds json. According to some internal logic. So easy to move.

// Actual Commands
Now here you have access to the entire LUA language. This means that now you can execute all you want. You can access all the variables in world json, and store variables in world json.
