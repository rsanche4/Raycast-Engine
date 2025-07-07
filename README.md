# Raycast Engine

Welcome to the **Raycast Engine**! This project is a lightweight and simple 3D rendering engine using basic libraries (with GPU Support!) built around the concept of raycasting, which simulates the way light travels and interacts with objects in a 3D environment.

The Raycast Engine is typically used in first-person perspective games or simulations and can render walls, floors, and ceilings, creating the illusion of a 3D world on a 2D screen. Despite its simplicity, this engine allows for the creation of dynamic and immersive experiences with basic 3D elements like textures, lighting effects, and object placement. There is a LOT to uncover in this engine as there is a big API that allows for all sorts of control, giving the users a lot of creative freedom! Please note, this is classic raycasting, not raytracing. If you are interested in more advanced 3D graphics, I would recommend learning OpenGL etc.

Let me show you a quick demo!

[![Watch the video](https://img.youtube.com/vi/joDJuUwu1GY/0.jpg)](https://youtu.be/joDJuUwu1GY)

Features in Future:
- have sprite info of how high up vertically it should be part of the sprites info itself somehow weather it be the name or something and also of its scale whether we can scale them up or down
- draw skybox completely on the screen if fog has been turned off
- allow for us to not draw anything on wall if its black pixel so leave it as is (Thus giving the impression of an outside etc) and also do so for the floor if it has a black texture dont draw anything in it (thus this lets you see the skybox etc)
- Solid sprites should be good so implement those
- Allow for 128x128 textures too you choose btw 64 or 128
- Translucent sprites too just cuz
- And also allow for increasing and decreasing volume of music or sounds played (particularly useful for distance sound design thing)
- Also you know what add the layer3, the ceiling, so its not the same through out. And add the ceiling the black pixel ability to show a sky type thing. Fine