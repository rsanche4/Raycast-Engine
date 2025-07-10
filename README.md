# Raycast Engine

Welcome to the **Raycast Engine**! This project is a lightweight and simple 3D rendering engine using basic libraries (with GPU Support!) built around the concept of raycasting, which simulates the way light travels and interacts with objects in a 3D environment.

The Raycast Engine is typically used in first-person perspective games or simulations and can render walls, floors, and ceilings, creating the illusion of a 3D world on a 2D screen. Despite its simplicity, this engine allows for the creation of dynamic and immersive experiences with basic 3D elements like textures, lighting effects, and object placement. There is a LOT to uncover in this engine as there is a big API that allows for all sorts of control, giving the users a lot of creative freedom! Please note, this is classic raycasting, not raytracing. If you are interested in more advanced 3D graphics, I would recommend learning OpenGL etc.

Let me show you a quick demo!

[![Watch the video](https://img.youtube.com/vi/joDJuUwu1GY/0.jpg)](https://youtu.be/joDJuUwu1GY)

Features in Future:
- Maybe with the help of AI and your own knowledge try to make it so you can have blocks and ceilings that extend higher universally. Textures can be changed to you could have multiple levels of textures etc. Only 2 levels should be enough but if we figure that out we can have many basically. So we can even stack them up even it should be fun.
- You could do planes i guess, useful for fences and things like that, but also make them so that they can be big vertical and drawn like sprites thing, transparent with collision and also make them so they can be higher than universal ceiling height so u can do some cool structures with it
- have sprite info of how high up vertically it should be part of the sprites info itself somehow weather it be the name or something and also of its scale whether we can scale them up or down
- draw skybox completely on the screen if fog has been turned off
- allow for us to not draw anything on wall if its black pixel so leave it as is (Thus giving the impression of an outside etc) and also do so for the floor if it has a black texture dont draw anything in it (thus this lets you see the skybox etc)
- Solid sprites should be good so implement those
- Allow for 128x128 textures too you choose btw 64 or 128 per map even if you would like
- Translucent sprites too just cuz
- And also allow for increasing and decreasing volume of music or sounds played (particularly useful for distance sound design thing)
- Also you know what add the layer3, the ceiling, so its not the same through out. And add the ceiling the black pixel ability to show a sky type thing. Fine
- Change the file structures so it makes more sense and it's a bit easier to follow
- Oh improve pathfinding so its A* or close to it
- Allow for Saving and Loading files
- Rewrite if possible on C++ for speedup and stability
- Rewrite editor in JS CSS HTML for stability as front end 
- Publish DSG.exe
- Remake Lola in this engine and give it a nicer ending and better storyline about acceptance